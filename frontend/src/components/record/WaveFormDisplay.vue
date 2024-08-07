<template>
  <div class="visualizer">
    <div class="bars">
      <div v-for="n in 20" :key="n" class="bar" :class="{ active: n <= activeBars }"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRecordStore } from '@/store/record';
import { storeToRefs } from 'pinia';

const recordStore = useRecordStore();
const { isPlaying, analyser, dataArray, isRecording, activeBars, javascriptNode } = storeToRefs(recordStore);

const startDrawing = () => {
  console.log("Started drawing");

  if (!analyser.value) return;

  if (javascriptNode.value) {
    javascriptNode.value.onaudioprocess = () => {
      if (isRecording.value || isPlaying.value) {
        dataArray.value = new Uint8Array(analyser.value.frequencyBinCount);
        analyser.value.getByteFrequencyData(dataArray.value);
        const average = dataArray.value.reduce((a, b) => a + b, 0) / dataArray.value.length;
        activeBars.value = Math.floor(average / 4);
      }
    };
  } else {
    console.error("javascriptNode is not initialized");
  }
};

onMounted(() => {
  watch([isRecording, isPlaying, javascriptNode], ([newRecording, newPlaying, newJavascriptNode]) => {
    if ((newRecording || newPlaying) && newJavascriptNode) {
      startDrawing();
    }
  });
});
</script>

<style scoped>
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
  background-color: #5d5c5c;
}

.bar.active {
  background-color: #8B92DF;
}
</style>
