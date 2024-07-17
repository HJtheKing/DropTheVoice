<template>
  <div class="timer-container" v-if="isRecording">
      <span>[녹음 중] </span>
      <span>{{ timer.toFixed(2) }} 초</span>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useRecordStore } from '@/stores/record';
import { onBeforeRouteLeave } from 'vue-router'
import { storeToRefs } from 'pinia';

const recordStore = useRecordStore();
const {isRecording} = storeToRefs(recordStore)

const timer = ref(0.00)
let intervalId = null

const startTimer = () => {
  timer.value = 0.00
  intervalId = setInterval(() => {
    timer.value += 0.01
  }, 10)
}

const stopTimer = () => {
  clearInterval(intervalId)
  timer.value = 0.00
}

onMounted(() => {
  watch(isRecording, (newVal) => {
    if (newVal) {
      startTimer()
    } else {
      stopTimer()
    }
  })
})

onBeforeRouteLeave(() => {
  stopTimer()
})

onUnmounted(() => {
  clearInterval(intervalId)
})
</script>

<style scoped>
.timer-container {
  font-size: 16px;
  color: red;
  position: absolute;
  top: -10px;
  left: 50%;
  transform: translateX(-50%);
  white-space: nowrap; /* 텍스트가 줄 바꿈 없이 한 줄로 나오도록 설정 */
}
</style>
