<template>
  <div class="record-audio">
    <button @click="startRecording" :disabled="isRecording">Start Recording</button>
    <button @click="stopRecording" :disabled="!isRecording">Stop Recording</button>
    <audio ref="audio" controls></audio>
    <a ref="downloadLink" :href="downloadUrl" :download="downloadFilename" style="display: none;">Download</a>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { FFmpeg } from '@ffmpeg/ffmpeg';

// 반응형 상태 및 참조 생성
const audio = ref(null);
const isRecording = ref(false);
const mediaRecorder = ref(null);
const audioChunks = ref([]);
const downloadLink = ref(null);
const downloadUrl = ref('');
const downloadFilename = ref('');

// FFmpeg 인스턴스 생성
const ffmpeg = new FFmpeg({ log: true });

const startRecording = async () => {
  const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
  mediaRecorder.value = new MediaRecorder(stream);
  mediaRecorder.value.ondataavailable = (event) => {
    audioChunks.value.push(event.data);
  };
  mediaRecorder.value.start();
  isRecording.value = true;
};

const stopRecording = () => {
  mediaRecorder.value.stop();
  isRecording.value = false;

  mediaRecorder.value.onstop = async () => {
    // 녹음된 데이터를 WAV 형식으로 저장
    const audioBlob = new Blob(audioChunks.value, { type: 'audio/wav' });
    // WAV 파일을 MP3로 변환 (필터 적용)
    const mp3Blob = await convertToMP3WithFilters(audioBlob);
    // 변환된 MP3 파일을 오디오 요소에 할당
    audio.value.src = URL.createObjectURL(mp3Blob);

    // 다운로드 링크 생성
    const timestamp = Date.now();
    downloadFilename.value = `recorded_voice_${timestamp}.mp3`;
    downloadUrl.value = URL.createObjectURL(mp3Blob);
    downloadLink.value.style.display = 'block';
    downloadLink.value.click();
    downloadLink.value.style.display = 'none';
  };
};

const convertToMP3WithFilters = async (audioBlob) => {
  await ffmpeg.load();

  // 오디오 Blob을 ArrayBuffer로 변환
  const arrayBuffer = await audioBlob.arrayBuffer();

  // ArrayBuffer를 Uint8Array로 변환하여 ffmpeg FS에 파일로 작성
  ffmpeg.FS('writeFile', 'input.wav', new Uint8Array(arrayBuffer));

  // 소음 제거 및 목소리 강조 필터 적용하여 파일 변환 실행
  await ffmpeg.run('-i', 'input.wav', '-af', 'anlmdn=s=0.01:b=0.9,treble=g=5', 'output.mp3');

  // 변환된 MP3 파일을 읽어들임
  const data = ffmpeg.FS('readFile', 'output.mp3');

  // Blob으로 변환
  return new Blob([data.buffer], { type: 'audio/mpeg' });
};

// FFmpeg 인스턴스 초기화
ffmpeg.load().then(() => {
  console.log('FFmpeg loaded');
}).catch((error) => {
  console.error('Error loading FFmpeg:', error);
});
</script>

<style scoped>
.record-audio {
  display: flex;
  flex-direction: column;
  align-items: center;
}
button {
  margin: 10px;
  padding: 10px 20px;
  background-color: #007BFF;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
button:disabled {
  background-color: #ccc;
}
audio {
  margin-top: 20px;
}
</style>
