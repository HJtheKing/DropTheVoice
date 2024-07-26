<template>
  <v-container class="timer-container" :class="{ hidden: !isRecording }" fluid>
    <v-row justify="center">
        <div class="d-flex justify-center align-center">
          <v-icon color="red" class="mr-2">mdi-record-circle</v-icon>
          <span class="timer-text">[녹음 중] {{ timer.toFixed(2) }} 초</span>
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
  color: red;
  white-space: nowrap;
}
</style>
