<template>
  <v-container fluid>
    <v-row justify="center" class="my-4">
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
  </v-container>
</template>

<script setup>
import { ref, watch } from 'vue';
import { useRecordStore } from '@/store/record';
import { storeToRefs } from 'pinia';
import { MediaRecorder, register } from 'extendable-media-recorder';
import { connect } from 'extendable-media-recorder-wav-encoder';

// 녹음 데이터를 반환하는 메서드
function getAudioBlob() {
  return recordStore.audioBlob.value;
}

// 메서드를 외부에서 사용할 수 있도록 설정
defineExpose({
  getAudioBlob
});

const recordStore = useRecordStore();
const { isRecording, audioBlob, analyser, dataArray, bufferLength, stream, javascriptNode } = storeToRefs(recordStore); // 반응형 상태 참조

// 초기화
watch([analyser, dataArray, bufferLength, stream, javascriptNode], (newValues) => {
  if (!analyser.value) analyser.value = null;
  if (!dataArray.value) dataArray.value = new Uint8Array(0);
  if (!bufferLength.value) bufferLength.value = 0;
  if (!stream.value) stream.value = null;
  if (!javascriptNode.value) javascriptNode.value = null;
});

// Web Audio API 관련 변수
let audioContext = null; // AudioContext 객체를 저장할 변수
const audioChunks = [];
let mediaRecorder = null; // MediaRecorder 객체를 저장할 변수

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
    if (stream.value) {
      stream.value.getTracks().forEach(track => track.stop());
      stream.value = null;
    }
    if (audioContext) {
      audioContext.close();
      audioContext = new (window.AudioContext || window.webkitAudioContext)();
    }

    // // 오디오 플레이어 초기화
    // if (audioPlayer.value) {
    //   audioPlayer.value.loadAudio('');
    //   console.log("오디오플레이어 초기화")
    // } else {
    //   console.log("오디오 플레이어 초기화 필요없음")
    // }

    // 인코더가 등록되지 않은 경우에만 등록
    if (!isEncoderRegistered) {
      await register(await connect());
      isEncoderRegistered = true; // 등록 상태 설정
    }

    // 오디오 입력 스트림 가져오기
    stream.value = await navigator.mediaDevices.getUserMedia({ audio: true });

    // Web Audio API 설정
    audioContext = new AudioContext();
    const source = audioContext.createMediaStreamSource(stream.value);
    analyser.value = audioContext.createAnalyser();
    analyser.value.fftSize = 2048;
    bufferLength.value = analyser.value.frequencyBinCount;
    dataArray.value = new Uint8Array(bufferLength.value);

    javascriptNode.value = audioContext.createScriptProcessor(2048, 1, 1);

    analyser.value.smoothingTimeConstant = 0.3;
    analyser.value.fftSize = 2048;

    source.connect(analyser.value);
    analyser.value.connect(javascriptNode.value);
    javascriptNode.value.connect(audioContext.destination);

    // MediaRecorder 설정 및 시작
    mediaRecorder = new MediaRecorder(stream.value, { mimeType: 'audio/wav' });
    mediaRecorder.ondataavailable = event => {
      audioChunks.push(event.data);
    };

    mediaRecorder.onstop = () => {
      audioBlob.value = new Blob(audioChunks, { type: 'audio/wav' });
      // audioUrl.value = URL.createObjectURL(audioBlob.value);
      const Url = URL.createObjectURL(audioBlob.value);
      recordStore.loadAudio(Url); // 녹음 종료 시 URL을 스토어에 저장


      audioChunks.length = 0;
      if (audioContext) {
        audioContext.close();
        audioContext = null;
      }

      // 스트림의 모든 트랙을 종료
      if (stream.value) {
        stream.value.getTracks().forEach(track => track.stop());
        stream.value = null;
      }
    };

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
  margin-bottom: 20px;
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
  background-color: #5d5c5c;
}

.bar.active {
  background-color: #8B92DF;
}
</style>