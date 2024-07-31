<template>
  <v-container class="timer-container" fluid>
    <v-row justify="center" :class="{ hidden: !isRecording }">
        <div class="d-flex justify-center align-center">
          <v-icon color="red" class="mr-2">mdi-record-circle</v-icon>
          <span class="timer-text">{{ formatTime(timer) }}</span>
        </div>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useRecordStore } from '@/store/record';
import { onBeforeRouteLeave } from 'vue-router'
import { storeToRefs } from 'pinia';

const recordStore = useRecordStore();
const { isRecording } = storeToRefs(recordStore)

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

const formatTime = (time) => {
  const minutes = Math.floor(time / 60);
  const seconds = Math.floor(time % 60);
  return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
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
  margin-bottom: 20px;
}

.hidden {
  visibility: hidden;
}

.timer-text {
  font-size: 16px;
  color: #fff;
  white-space: nowrap;
}
</style>
