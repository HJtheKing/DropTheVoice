<template>
  <div class="canvas-container">
    <canvas ref="canvasRef" width="500" height="500"></canvas>
  </div>
  <div class="record-button-container">
    <div :class="['record-button', { recording: isRecording }]" @click="toggleRecording"></div>
  </div>
  <v-container class="audio-container">
    <audio-player ref="audioPlayer"></audio-player>
  </v-container>
  <div class="save-button-container">
    <button class="save-button" v-if="audioUrl && !isRecording" @click="saveRecording">저장</button>
  </div>
</template>

<script setup>
import { useRecordStore } from '@/store/record';
import { storeToRefs } from 'pinia';
import { ref, onMounted, onUnmounted } from 'vue';
import { MediaRecorder, register } from 'extendable-media-recorder';
import { connect } from 'extendable-media-recorder-wav-encoder';
import * as lamejs from '@breezystack/lamejs';
import AudioPlayer from "@/components/AudioPlayer.vue";

// Pinia 스토어를 사용하여 전역 상태 관리
const recordStore = useRecordStore();
const { isRecording, audioUrl } = storeToRefs(recordStore); // 반응형 상태 참조

// 녹음된 오디오 청크를 저장하는 배열
const audioChunks = [];
let mediaRecorder = null; // MediaRecorder 객체를 저장할 변수
let audioBlob = null; // 녹음된 오디오 데이터를 저장할 Blob

const audioPlayer = ref(null); // 오디오 플레이어를 참조하는 ref

// Web Audio API 관련 변수
let audioContext = null; // AudioContext 객체를 저장할 변수
let analyser = null; // 오디오 분석기 (AnalyserNode) 객체를 저장할 변수
let dataArray = null; // 오디오 데이터 배열을 저장할 변수
let bufferLength = null; // 분석할 데이터의 크기를 저장할 변수
let drawInterval = null; // 주기적으로 그리기 작업을 수행하기 위한 interval ID를 저장할 변수
let stream = null; // MediaStream 객체를 저장할 변수 (오디오 입력 스트림)

// 인코더 등록 상태 추적
let isEncoderRegistered = false;

// canvas 요소에 대한 ref
const canvasRef = ref(null);

onMounted(() => {
  window.addEventListener('resize', resizeCanvas);
  resizeCanvas();
});

onUnmounted(() => {
  window.removeEventListener('resize', resizeCanvas);
  clearInterval(drawInterval);
});

const resizeCanvas = () => {
  const canvas = canvasRef.value;
  if (canvas) {
    const container = canvas.parentElement;
    canvas.width = container.clientWidth;
    canvas.height = container.clientHeight;
  }
};

// 녹음 시작 및 중지 토글 메서드
const toggleRecording = () => {
  if (isRecording.value) {
    stopRecording();
  } else {
    startRecording();
  }
};

// 녹음을 시작하는 메서드
const startRecording = async () => {
  try {
    console.log("Started Recording")
    // 기존 스트림과 오디오 컨텍스트 종료
    if (stream) {
      stream.getTracks().forEach(track => track.stop());
      stream = null;
    }
    if (audioContext) {
      audioContext.close();
      audioContext = null;
    }

    // 오디오 플레이어 초기화
    if (audioPlayer.value) {
      audioPlayer.value.loadAudio('');
    }

    // 인코더가 등록되지 않은 경우에만 등록
    if (!isEncoderRegistered) {
      await register(await connect());
      isEncoderRegistered = true; // 등록 상태 설정
    }

    // 오디오 입력 스트림 가져오기
    stream = await navigator.mediaDevices.getUserMedia({ audio: true });

    // Web Audio API 설정
    audioContext = new AudioContext();
    const source = audioContext.createMediaStreamSource(stream);
    analyser = audioContext.createAnalyser();
    analyser.fftSize = 2048;
    bufferLength = analyser.frequencyBinCount;
    dataArray = new Uint8Array(bufferLength);

    source.connect(analyser);

    // MediaRecorder 설정 및 시작
    mediaRecorder = new MediaRecorder(stream, { mimeType: 'audio/wav' });
    mediaRecorder.ondataavailable = event => {
      audioChunks.push(event.data);
    };

    mediaRecorder.onstop = () => {
      audioBlob = new Blob(audioChunks, { type: 'audio/wav' });
      audioUrl.value = URL.createObjectURL(audioBlob);

      if (audioPlayer.value) {
        audioPlayer.value.loadAudio(audioUrl.value);
      }

      audioChunks.length = 0;
      clearInterval(drawInterval);
      if (audioContext) {
        audioContext.close();
        audioContext = null;
      }

      // 스트림의 모든 트랙을 종료
      if (stream) {
        stream.getTracks().forEach(track => track.stop());
        stream = null;
      }
    };

    // 파형 그리기 시작
    startDrawing(); // 파형 그리기 함수 먼저 호출

    mediaRecorder.start(); // 녹음 시작
    isRecording.value = true;

  } catch (error) {
    console.error('Error starting recording:', error);
  }
};

// 녹음을 중지하는 메서드
const stopRecording = () => {
  if (mediaRecorder && mediaRecorder.state !== 'inactive') {
    mediaRecorder.stop(); // 녹음 중지
    console.log("Stopped recording");
  }
  isRecording.value = false;
};

// 오디오 파형을 그리는 메서드
const startDrawing = () => {
  console.log("Started drawing")

  const canvas = document.querySelector('canvas');
  if (!canvas) return;

  const canvasCtx = canvas.getContext('2d');
  if (!canvasCtx) return;

  drawInterval = setInterval(() => {
    if (!analyser) return;

    analyser.getByteTimeDomainData(dataArray); // 오디오 데이터 가져오기

    canvasCtx.clearRect(0, 0, canvas.width, canvas.height); // 캔버스 초기화
    canvasCtx.lineWidth = 3;
    canvasCtx.strokeStyle = 'rgb(0, 0, 0)';

    canvasCtx.beginPath();
    // 파형 폭 계산하기
    const sliceWidth = (canvas.width * 1.0) / (bufferLength / 8);
    let x = 0;

    // 오디오 데이터를 기반으로 파형 그리기
    for (let i = 0; i < bufferLength; i += 8) {
      const v = (dataArray[i] / 128.0);
      const y = (v * canvas.height / 2);

      if (i === 0) {
        canvasCtx.moveTo(x, y);
      } else {
        canvasCtx.lineTo(x, y);
      }
      x += sliceWidth;
    }

    canvasCtx.lineTo(canvas.width, canvas.height / 2);
    canvasCtx.stroke();
  }, 66);
};

// 녹음을 저장하고 MP3로 변환하는 메서드
const saveRecording = async () => {
  if (!audioBlob) return;

  const mp3Blob = await convertWavToMp3(audioBlob);
  const url = URL.createObjectURL(mp3Blob);

  // MP3 다운로드
  const downloadLink = document.createElement('a');
  downloadLink.href = url;
  downloadLink.download = `dropthevoice_음성녹음_${Date.now()}.mp3`;
  downloadLink.click();
  console.log("Started downloading");
};

// WAV 파일을 MP3로 변환하는 메서드
async function convertWavToMp3(wavBlob) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = function () {
      const arrayBuffer = this.result;
      const wavHeader = lamejs.WavHeader.readHeader(new DataView(arrayBuffer));
      // 모노 채널로 설정 (1 채널)
      const wavSamples = new Int16Array(arrayBuffer, wavHeader.dataOffset, wavHeader.dataLen / 2);
      
      // 모노로 인코딩 설정
      const channels = 1; // 모노
      const sampleRate = wavHeader.sampleRate; // 원본 샘플링 레이트 사용
      const bitRate = 128; // 비트 전송률 128kbps

      // MP3 인코더 초기화
      const mp3Encoder = new lamejs.Mp3Encoder(channels, sampleRate, bitRate);
      const mp3Data = [];

      // 샘플 블록 크기를 더 크게 설정
      const sampleBlockSize = 1152 * 4; // 기본값의 4배 크기
      
      let mp3Buffer;
      for (let i = 0; i < wavSamples.length; i += sampleBlockSize) {
        // 샘플 블록 크기를 사용하여 버퍼 인코딩
        mp3Buffer = mp3Encoder.encodeBuffer(wavSamples.subarray(i, i + sampleBlockSize));
        if (mp3Buffer.length > 0) {
          mp3Data.push(new Int8Array(mp3Buffer));
        }
      }

      // 인코딩 완료 후 남은 데이터 플러시
      mp3Buffer = mp3Encoder.flush();
      if (mp3Buffer.length > 0) {
        mp3Data.push(new Int8Array(mp3Buffer));
      }

      // 최종 MP3 Blob 생성
      const mp3Blob = new Blob(mp3Data, { type: 'audio/mp3' });
      resolve(mp3Blob);
      console.log("Successfully converted to mp3");
    };

    reader.onerror = reject;
    reader.readAsArrayBuffer(wavBlob);
  });
}
</script>


<style scoped>
.black-background {
  background-color: #000;
  color: #fff;
}
.audio-play {
  display: flex;
  flex-direction: column;
  align-items: center;
}

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
  background-color: #000;
  margin-bottom: 20px; /* 오디오 플레이어와 저장 버튼 사이의 간격 */
}


.save-button-container {
  width: 100%;
  display: flex;
  justify-content: center;
  padding-bottom: 80px;
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
