<template>
  <div class="current-time">
    {{ currentTime }}
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const currentTime = ref(new Date().toLocaleString('ko-KR', { month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric' }))

const updateTime = () => {
  currentTime.value = new Date().toLocaleString('ko-KR', { month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric' })
}

onMounted(() => {
  const intervalId = setInterval(updateTime, 60000)  // 매 분마다 업데이트
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
