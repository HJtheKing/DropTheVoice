<template>
  <div class="visualizer">
    <div class="bars">
      <div v-for="n in 20" :key="n" class="bar" :class="{ active: n <= activeBars }"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  isRecording: Boolean,
  recordingAnalyser: Object,
  isPlaying: Boolean,
  playbackAnalyser: Object,
});

const activeBars = ref(0);
let analyser = null;
let dataArray = new Uint8Array();

const updateAnalyser = () => {
  if (props.isRecording && props.recordingAnalyser) {
    analyser = props.recordingAnalyser;
  } else if (props.isPlaying && props.playbackAnalyser) {
    analyser = props.playbackAnalyser;
  } else {
    analyser = null;
  }

  if (analyser) {
    dataArray = new Uint8Array(analyser.frequencyBinCount);
    startDrawing();
  }
};

const startDrawing = () => {
  if (!analyser) return;

  const draw = () => {
    if (!analyser) return;

    analyser.getByteFrequencyData(dataArray);
    const average = dataArray.reduce((a, b) => a + b, 0) / dataArray.length;
    activeBars.value = Math.floor(average / 4);

    requestAnimationFrame(draw);
  };

  draw();
};

watch(() => props.isRecording, updateAnalyser);
watch(() => props.isPlaying, updateAnalyser);
watch(() => props.recordingAnalyser, updateAnalyser);
watch(() => props.playbackAnalyser, updateAnalyser);
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
  background-color: #5d5c5c; /* Gray color for all bars */
}

.bar.active {
  background-color: #8B92DF;
}
</style>