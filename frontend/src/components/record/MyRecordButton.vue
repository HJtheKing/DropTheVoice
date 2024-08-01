<template>
  <v-container fluid>
    <!-- 오디오 관련 -->
    <v-row justify="center" class="my-4">
        <div class="visualizer">
          <div class="bars">
            <div v-for="n in 20" :key="n" class="bar" :class="{ active: n <= activeBars }"></div>
          </div>
        </div>
    </v-row>
    <v-row justify="center" class="my-4">
      <v-col cols="12" class="d-flex justify-center align-center">
        <v-btn
          class="record-button"
          :class="{ recording: isRecording }"
          @click="toggleRecording"
          color="red"
          rounded
          large
        >
          <v-icon>mdi-microphone-outline</v-icon>
        </v-btn>
      </v-col>
    </v-row>
    <v-row justify="center" class="my-4">
        <audio-player ref="audioPlayer"></audio-player>
    </v-row>
    <v-row justify="center">
        <!-- <v-btn v-if="audioUrl && !isRecording" @click="saveRecord" color="blue" class="save-button">
          저장
        </v-btn> -->
        <v-btn class="load-btn" @click="playSampleAudio">오디오 불러오기</v-btn>
    </v-row>
  </v-container>
</template>


<script setup>
import { ref,} from 'vue';
import { useRecordStore } from '@/store/record';
import { storeToRefs } from 'pinia';
import { MediaRecorder, register } from 'extendable-media-recorder';
import { connect } from 'extendable-media-recorder-wav-encoder';
import AudioPlayer from "@/components/AudioPlayer.vue";

// 테스트용 import, load
import audioFile from '@/assets/tracks/진격 (Zinkyeok) - Rusty Ground.webm';
function playSampleAudio() {
  if (audioPlayer.value) {
    audioPlayer.value.loadAudio(audioFile);
  }
}

// 녹음 데이터를 반환하는 메서드
function getAudioBlob() {
  return audioBlob.value;
}

function getAnalyser() {
  return analyser;
}

function getDataArray() {
  return dataArray;
}

// expose 메서드를 사용하여 외부에서 사용할 수 있도록 설정
defineExpose({
  getAudioBlob,
  getAnalyser,
  getDataArray
});

const recordStore = useRecordStore();
const { isRecording, audioUrl } = storeToRefs(recordStore); // 반응형 상태 참조

// 녹음된 오디오 청크를 저장하는 배열
const audioChunks = [];
let mediaRecorder = null; // MediaRecorder 객체를 저장할 변수
const audioBlob = ref(null); // 녹음된 오디오 데이터를 저장할 Blob

const audioPlayer = ref(null); // 오디오 플레이어를 참조하는 ref

// Web Audio API 관련 변수
const activeBars = ref(0);
let audioContext = null; // AudioContext 객체를 저장할 변수
let analyser = null; // 오디오 분석기 (AnalyserNode) 객체를 저장할 변수
let dataArray = null; // 오디오 데이터 배열을 저장할 변수
let bufferLength = null; // 분석할 데이터의 크기를 저장할 변수
let stream = null; // MediaStream 객체를 저장할 변수 (오디오 입력 스트림)

let javascriptNode = null;

// 인코더 등록 상태 추적
let isEncoderRegistered = false;


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

    javascriptNode = audioContext.createScriptProcessor(2048, 1, 1);

    analyser.smoothingTimeConstant = 0.3;
    analyser.fftSize = 2048;

    source.connect(analyser);
    analyser.connect(javascriptNode);
    javascriptNode.connect(audioContext.destination);

    // MediaRecorder 설정 및 시작
    mediaRecorder = new MediaRecorder(stream, { mimeType: 'audio/wav' });
    mediaRecorder.ondataavailable = event => {
      audioChunks.push(event.data);
    };

    mediaRecorder.onstop = () => {
      audioBlob.value = new Blob(audioChunks, { type: 'audio/wav' });
      audioUrl.value = URL.createObjectURL(audioBlob.value);

      if (audioPlayer.value) {
        audioPlayer.value.loadAudio(audioUrl.value);
        console.log("Loaded recorded audio to player")
      } else {
        console.log("Failed")
      }

      audioChunks.length = 0;
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
}

// 오디오 파형을 그리는 메서드
const startDrawing = () => {
  console.log("Started drawing")

  if (!analyser) return;

  javascriptNode.onaudioprocess = () => {
    if (isRecording.value) {
      dataArray = new Uint8Array(analyser.frequencyBinCount);
      analyser.getByteFrequencyData(dataArray);
      const average = dataArray.reduce((a, b) => a + b, 0) / dataArray.length;
      activeBars.value = Math.floor(average / 4);
    }
  };
};
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

.visualizer {
  display: flex;
  justify-content: center;
  margin: 20px 0;
}

.bars {
  display: flex;
  gap: 4px;
}

.bar {
  width: 8px;
  height: 20px;
  background-color: #5d5c5c; /* Gray color for all bars */
}

.bar.active {
  background-color: #8B92DF;
}



</style>
