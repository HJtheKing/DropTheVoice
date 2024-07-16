<template>
  <div class="record-button-container">
    <div :class="['record-button', { recording: isRecording }]" @click="toggleRecording">
    </div>
    <audio v-if="audioUrl && !isRecording" :src="audioUrl" controls></audio>
  </div>
  
  <div class="save-button-container">
      <button  class="save-button" v-if="audioUrl && !isRecording">저장</button>
    </div>
</template>

<script setup>
import { useRecordStore } from '@/stores/record'
import { storeToRefs } from 'pinia'

const recordStore = useRecordStore()
const { isRecording, audioUrl } = storeToRefs(recordStore)

let mediaRecorder = null
let audioChunks = []

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
  mediaRecorder = new MediaRecorder(stream)
  
  mediaRecorder.ondataavailable = event => {
    audioChunks.push(event.data)
  }
  
  mediaRecorder.onstop = () => {
    const audioBlob = new Blob(audioChunks, { type: 'audio/wav' })
    audioUrl.value = URL.createObjectURL(audioBlob)
    audioChunks = []
  }
  
  mediaRecorder.start()
  isRecording.value = true
}

const stopRecording = () => {
  console.log('Stop Recording')  // 콘솔 로그 확인
  mediaRecorder.stop()
  isRecording.value = false
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
</style>
