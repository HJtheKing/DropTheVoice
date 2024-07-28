<template>
  <div class="current-time">
    <span>{{ currentTime }}</span>
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
  color: #1e90ff;
  /* background-color: #333; */
  padding: 10px;
  border-radius: 5px;
}

/* .current-time {
  font-size: 24px;
  display: flex;
  justify-content: space-around;
  width: 200px;
  margin-bottom: 20px;
} */

</style>
