// store/record.js
import { ref } from 'vue';
import { defineStore } from 'pinia';

export const useRecordStore = defineStore('record', () => {
  const isRecording = ref(false);
  const isPlaying = ref(false);
  const recordingAnalyser = ref(null);
  const playbackAnalyser = ref(null);
  const audioUrl = ref('');
  const audioContext = ref(null);

  function toggleRecordingState() {
    isRecording.value = !isRecording.value;
  }

  function setRecordingAnalyser(analyser) {
    recordingAnalyser.value = analyser;
  }

  function setPlaybackAnalyser(analyser) {
    playbackAnalyser.value = analyser;
  }

  function setPlayingState(state) {
    isPlaying.value = state;
  }

  function setAudioUrl(url) {
    audioUrl.value = url;
  }

  function setAudioContext(context) {
    audioContext.value = context;
  }

  return {
    isRecording,
    isPlaying,
    recordingAnalyser,
    playbackAnalyser,
    audioUrl,
    audioContext,
    toggleRecordingState,
    setRecordingAnalyser,
    setPlaybackAnalyser,
    setPlayingState,
    setAudioUrl,
    setAudioContext,
  };
});
