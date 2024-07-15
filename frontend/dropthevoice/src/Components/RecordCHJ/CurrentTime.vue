<template>
    <div class="current-time">
      {{ currentTime }}
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, onUnmounted } from 'vue'
  
  const currentTime = ref(new Date().toLocaleTimeString())
  
  const updateTime = () => {
    currentTime.value = new Date().toLocaleTimeString()
  }
  
  onMounted(() => {
    const intervalId = setInterval(updateTime, 1000)
    
    onUnmounted(() => {
      clearInterval(intervalId)
    })
  })
  </script>
  
  <style scoped>
  .current-time {
    font-size: 24px;
    color: white;
    background-color: #333;
    padding: 10px;
    border-radius: 5px;
  }
  </style>
  