<template>
  <v-container fluid>
    <v-row justify="center" class="my-4">
      <!-- WaveFormDisplay는 RecordView에서 직접 사용됨 -->
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
        <v-btn class="load-btn" @click="playSampleAudio">오디오 불러오기</v-btn>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref } from 'vue';
import { useRecordStore } from '@/store/record';
import { MediaRecorder, register } from 'extendable-media-recorder';
import { connect } from 'extendable-media-recorder-wav-encoder';
import AudioPlayer from "@/components/AudioPlayer.vue";

const emit = defineEmits(['audioRecorded']);

const recordStore = useRecordStore();
const { isRecording, recordingAnalyser, toggleRecordingState, setAudioUrl, audioContext, setRecordingAnalyser } = recordStore;

const audioChunks = [];
let mediaRecorder = null;
const audioBlob = ref(null);
const audioPlayer = ref(null);
let analyser = null;
let stream = null;

const playSampleAudio = () => {
  if (audioPlayer.value) {
    audioPlayer.value.loadAudio('@/assets/tracks/진격 (Zinkyeok) - Rusty Ground.webm');
  }
};

const getAudioBlob = () => {
  return audioBlob.value;
};

// expose 메서드를 사용하여 외부에서 사용할 수 있도록 설정
defineExpose({
  getAudioBlob
});

const toggleRecording = () => {
  if (isRecording.value) {
    stopRecording();
  } else {
    startRecording();
  }
};

const startRecording = async () => {
  try {
    if (stream) {
      stream.getTracks().forEach(track => track.stop());
      stream = null;
    }

    if (audioContext.value.state === 'suspended') {
      await audioContext.value.resume();
    }

    await register(await connect());

    stream = await navigator.mediaDevices.getUserMedia({ audio: true });

    const source = audioContext.value.createMediaStreamSource(stream);
    analyser = audioContext.value.createAnalyser();
    analyser.fftSize = 2048;

    source.connect(analyser);
    setRecordingAnalyser(analyser);

    mediaRecorder = new MediaRecorder(stream, { mimeType: 'audio/wav' });
    mediaRecorder.ondataavailable = event => {
      audioChunks.push(event.data);
    };

    mediaRecorder.onstop = () => {
      audioBlob.value = new Blob(audioChunks, { type: 'audio/wav' });
      const url = URL.createObjectURL(audioBlob.value);
      setAudioUrl(url);
      emit('audioRecorded', url);

      audioChunks.length = 0;

      if (stream) {
        stream.getTracks().forEach(track => track.stop());
        stream = null;
      }
    };

    mediaRecorder.start();
    toggleRecordingState();
    
  } catch (error) {
    console.error('Error starting recording:', error);
  }
};

const stopRecording = () => {
  if (mediaRecorder && mediaRecorder.state !== 'inactive') {
    mediaRecorder.stop();
  }
  toggleRecordingState();
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
