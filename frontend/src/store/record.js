import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useRecordStore = defineStore('record', () => {
  const isRecording = ref(false);
  const audioUrl = ref(null);
  const audioBlob = ref(null);
  const activeBars = ref(0);
  const analyser = ref(null); // 오디오 분석기
  const dataArray = ref(null); // 오디오 데이터 배열
  const bufferLength = ref(0); // 분석할 데이터 크기
  const stream = ref(null); // MediaStream 객체 저장
  const javascriptNode = ref(null);
  const isPlaying = ref(false);
  let audioContext = null;

  return {
    isRecording,
    audioUrl,
    audioBlob,
    activeBars,
    analyser,
    dataArray,
    bufferLength,
    stream,
    javascriptNode,
    isPlaying,
    audioContext
  };
});
