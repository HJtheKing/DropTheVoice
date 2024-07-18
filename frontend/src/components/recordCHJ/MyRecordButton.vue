<template>
  <div class="canvas-container">
    <canvas ref="canvas" width="500" height="500"></canvas>
  </div>
  <div class="record-button-container">
    <div :class="['record-button', { recording: isRecording }]" @click="toggleRecording"></div>
  </div>
  <div class="audio-container">
    <audio v-if="audioUrl && !isRecording" :src="audioUrl" controls></audio>
  </div>
  
  <div class="save-button-container">
      <button  class="save-button" v-if="audioUrl && !isRecording">저장</button>
    </div>
</template>

<script setup>
import { useRecordStore } from '@/stores/record'
import { storeToRefs } from 'pinia'
import { ref, onMounted, onUnmounted } from 'vue'

const recordStore = useRecordStore()
const { isRecording, audioUrl } = storeToRefs(recordStore)

// MediaRecorder 객체를 저장할 변수입니다.
let mediaRecorder = null;
// 오디오 데이터를 저장할 배열입니다.
let audioChunks = [];
// Web Audio API의 AudioContext 객체를 저장할 변수입니다.
let audioContext = null;
// Web Audio API의 AnalyserNode 객체를 저장할 변수입니다.
let analyser = null;
// 오디오 데이터를 저장할 배열입니다.
let dataArray = null;
// 분석할 데이터의 크기를 저장할 변수입니다.
let bufferLength = null;
// 주기적으로 그리기 작업을 수행하기 위한 interval ID를 저장할 변수입니다.
let drawInterval = null;  

const canvasRef = ref(null)

const resizeCanvas = () => {
  const canvas = canvasRef.value
  if (canvas) {
    const container = canvas.parentElement
    canvas.width = container.clientWidth
    canvas.height = container.clientHeight
  }
}

onMounted(() => {
  window.addEventListener('resize', resizeCanvas)
  resizeCanvas()
})

onUnmounted(() => {
  window.removeEventListener('resize', resizeCanvas)
})

const toggleRecording = () => {
  console.log('Toggle Recording Clicked')  // 콘솔 로그 확인
  if (isRecording.value) {
    stopRecording()
  } else {
    startRecording()
  }
}

const startRecording = async () => {
  console.log('Start Recording')  // 콘솔 로그 확인
  const stream = await navigator.mediaDevices.getUserMedia({ audio: true })

  // Web Audio API 설정
  audioContext = new (window.AudioContext)();
  // 스트림을 AudioContext의 소스로 만듭니다.
  const source = audioContext.createMediaStreamSource(stream);
  // audiocontext의 analyser 생성 및 할당
  analyser = audioContext.createAnalyser();
  // fft 크기 설정
  analyser.fftSize = 2048;
  // anlyser를 soucrce에 연결
  source.connect(analyser);

  bufferLength = analyser.frequencyBinCount;

  dataArray = new Uint8Array(bufferLength);

  mediaRecorder = new MediaRecorder(stream)
  
  mediaRecorder.ondataavailable = event => {
    audioChunks.push(event.data)
  }
  
  mediaRecorder.onstop = () => {
    const audioBlob = new Blob(audioChunks, { type: 'audio/wav' })
    audioUrl.value = URL.createObjectURL(audioBlob)
    audioChunks = []
    clearInterval(drawInterval);
    if(audioContext){
      audioContext.close();
    }
  }
  
  mediaRecorder.start()
  isRecording.value = true

  
  startDrawing();
}

const stopRecording = () => {
  console.log('Stop Recording')  // 콘솔 로그 확인
  mediaRecorder.stop()
  isRecording.value = false

}

const startDrawing = () => {
  console.log('start drawing')
  const canvas = document.querySelector('canvas');
  const canvasCtx = canvas.getContext('2d');

  drawInterval = setInterval(() => {
    if(!analyser) return;

    analyser.getByteTimeDomainData(dataArray);

    canvasCtx.fillStyle = 'rgb(255, 255, 255)';
    canvasCtx.fillRect(0, 0, canvas.width, canvas.height);

    canvasCtx.lineWidth = 3;
    canvasCtx.strokeStyle = 'rgb(0, 0, 0)'

    canvasCtx.beginPath();
    // 파형을 그릴 폭을 계산합니다.
    const sliceWidth = (canvas.width * 1.0) / (bufferLength / 8);
    let x = 0;

    // 데이터를 사용하여 파형을 그립니다.
    for (let i = 0; i < bufferLength; i += 8) {
      const v = (dataArray[i])/ 128.0;
      const y = (v * canvas.height) / 2;

      if (i === 0) {
        canvasCtx.moveTo(x, y);
      } else {
        canvasCtx.lineTo(x, y);
      }
      x += sliceWidth;
    }

    // 파형 그리기를 마무리합니다.
    canvasCtx.lineTo(canvas.width, canvas.height / 2);
    canvasCtx.stroke();
  }, 66)

  
}
</script>

<style scoped>
.record-button-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  position: relative;
  margin-top: 1cm;
  margin-bottom: 20px; /* 녹음 버튼과 오디오 플레이어 사이의 간격 */
}

canvas {
  border: 1px solid black;
}

.record-button {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  border: 3px solid white;
  background-color: red;
  cursor: pointer;
  position: relative;
  transition: background-color 0.3s ease;
}

.record-button::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-color: rgba(255, 0, 0, 0.5);
  transform: translate(-50%, -50%) scale(1);
  transition: opacity 0.3s ease, transform 0.3s ease;
  opacity: 0;
  z-index: -1;
}

.recording::before {
  opacity: 1;
  transform: translate(-50%, -50%) scale(1.0);
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% {
    transform: translate(-50%, -50%) scale(1.0);
    opacity: 1;
  }
  100% {
    transform: translate(-50%, -50%) scale(1.3);
    opacity: 0;
  }
}

.record-button:hover {
  background-color: darkred;
}



.audio-container {
  width: 100%;
  display: flex;
  justify-content: center;
  margin-bottom: 20px; /* 오디오 플레이어와 저장 버튼 사이의 간격 */
}


.save-button-container {
  width: 100%;
  display: flex;
  justify-content: center;
}

.save-button {
  padding: 10px 20px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.save-button:hover {
  background-color: #2980b9;
}


.canvas-container {
  width: 100%;
  height: 100%;
  max-width: 300px;
  max-height: 300px;
  display: flex;
  justify-content: center;
  align-items: center;
}

canvas {
  border: 1px solid black;
  border-radius: 50%;
  width: 100%;
  height: 100%;
  max-width: 500px;
  max-height: 500px;
}


</style>
