<template>
    <div class="visualizer">
        <div class="bars">
            <div v-for="n in 20" :key="n" class="bar" :class="{ active: n <= activeBars }"></div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';

let analyser = null; // 오디오 분석기 (AnalyserNode) 객체를 저장할 변수
let dataArray = null; // 오디오 데이터 배열을 저장할 변수

// expose 메서드를 사용하여 외부에서 사용할 수 있도록 설정
defineExpose({
  startDrawing
});

// 오디오 파형을 그리는 메서드
const startDrawing = () => {
  console.log("Started drawing")

  if (!analyser) return;

  javascriptNode.onaudioprocess = () => {
    if (isRecording.value) {
      dataArray = new Uint8Array(analyser.frequencyBinCount);
      analyser.getByteFrequencyData(dataArray);
      const average = dataArray.reduce((a, b) => a + b, 0) / dataArray.length;
      activeBars.value = Math.floor(average / 4);
    }
  };
};
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