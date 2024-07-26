<template>
  <div class="audio-player">
    <audio ref="audio" @timeupdate="updateTime" @loadedmetadata="updateDuration" @ended="resetPlayer" @error="handleError">
      <source :src="audioSrc" type="audio/wav">
      Your browser does not support the audio element.
    </audio>
    <div class="controls">
      <button class="play-btn" @click="togglePlay">
        <v-icon>{{ isPlaying ? 'mdi-pause' : 'mdi-play' }}</v-icon>
      </button>
      <input class="play-bar" type="range" min="0" :max="duration" step="0.01" v-model="currentTime" @input="seekAudio">
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
      audio.value.onloadedmetadata = () => {
        duration.value = audio.value.duration;
      };
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
  background-color: #f3b549;
  color: #000;
  font-weight: bold;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
button:hover {
  background-color: #f3b549;
  color: #000;
  font-weight: bold;
}
input {
      overflow: hidden;
      width: 80px;
      accent-color: #8B92DF;
}
</style>
