<template>
  <div class="audio-player">
    <audio ref="audio" @timeupdate="updateTime" @loadedmetadata="updateDuration" @ended="resetPlayer" @error="handleError">
      <source :src="audioSrc" type="audio/mpeg">
      Your browser does not support the audio element.
    </audio>
    <div class="controls">
      <button @click="togglePlay">{{ isPlaying ? '멈춤' : '재생' }}</button>
      <input type="range" min="0" :max="duration" step="0.01" v-model="currentTime" @input="seekAudio">
    </div>
    <div class="time">
      {{ formatTime(currentTime) }} / {{ formatTime(duration) }}
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';

export default {
  setup() {
    const audio = ref(null);
    const audioSrc = ref(null);
    const isPlaying = ref(false);
    const currentTime = ref(0);
    const duration = ref(0);

    const togglePlay = () => {
      if (audio.value.paused) {
        audio.value.play();
        isPlaying.value = true;
      } else {
        audio.value.pause();
        isPlaying.value = false;
      }
    };

    const updateTime = () => {
      currentTime.value = audio.value.currentTime;
    };

    const updateDuration = () => {
      duration.value = audio.value.duration;
    };

    const seekAudio = () => {
      audio.value.currentTime = currentTime.value;
    };

    const formatTime = (seconds) => {
      const minutes = Math.floor(seconds / 60);
      const secs = Math.floor(seconds % 60);
      return `${minutes}:${secs < 10 ? '0' : ''}${secs}`;
    };

    const resetPlayer = () => {
      isPlaying.value = false;
      currentTime.value = 0;
    };

    const loadAudio = (audioUrl) => {
      audioSrc.value = audioUrl;
      audio.value.load();
    };

    const handleError = (event) => {
      console.error('Audio playback error:', event);
    };

    return {
      audio,
      audioSrc,
      isPlaying,
      currentTime,
      duration,
      togglePlay,
      updateTime,
      updateDuration,
      seekAudio,
      formatTime,
      resetPlayer,
      loadAudio,
      handleError
    };
  }
};
</script>

<style scoped>
.audio-player {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.controls {
  display: flex;
  align-items: center;
}
.time {
  margin-top: 10px;
}
button {
  margin-right: 10px;
  padding: 10px 20px;
  background-color: #007BFF;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
button:hover {
  background-color: #0056b3;
}
</style>
