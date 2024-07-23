<template>
    <div class="record-audio-container">
      <button @click="toggleRecording">{{ isRecording ? 'Stop Recording' : 'Start Recording' }}</button>
      <audio v-if="audioUrl" :src="audioUrl" controls></audio>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  
  const isRecording = ref(false)
  const audioUrl = ref('')
  let mediaRecorder = null
  let audioChunks = []
  
  const startRecording = async () => {
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
    mediaRecorder.stop()
    isRecording.value = false
  }
  
  const toggleRecording = () => {
    if (isRecording.value) {
      stopRecording()
    } else {
      startRecording()
    }
  }
  </script>
  
  <style scoped>
  .record-audio-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px;
  }
  
  button {
    padding: 10px 20px;
    font-size: 16px;
    cursor: pointer;
  }
  </style>
  
