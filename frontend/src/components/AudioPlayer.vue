<template>
  <v-container class="audio-player">
    <audio ref="audio" controls @timeupdate="updateTime" @loadedmetadata="updateDuration" @ended="resetPlayer" @error="handleError">
      <source :src="src" type="audio/mp3">
      Your browser does not support the audio element.
    </audio>
    <v-container class="controls" justify="center">
      <v-row justify="center" class="time">
        {{ formattedCurrentTime }} / {{ formattedDuration }}
      </v-row>
      <v-row justify="center" class="bar-row">
        <input
          class="play-bar"
          type="range"
          :min="0"
          :max="duration"
          step="0.01"
          v-model="currentTime"
          @input="seekAudio"
        >
      </v-row>
    
      <v-container justify="center" class="btn-row">
        <v-row justify="space-between">
          <v-btn class="player-btn" @click="goToStart">
            <v-icon>mdi-skip-backward</v-icon>
          </v-btn>
          <v-btn class="player-btn" @click="rewindAudio">
            <v-icon>mdi-rewind</v-icon>
          </v-btn>
          <v-btn class="player-btn" @click="togglePlay">
            <v-icon>{{ isPlaying ? 'mdi-pause' : 'mdi-play' }}</v-icon>
          </v-btn>
          <v-btn class="player-btn" @click="fastForwardAudio">
            <v-icon>mdi-fast-forward</v-icon>
          </v-btn>
          <v-btn class="player-btn" @click="goToEnd">
            <v-icon>mdi-skip-forward</v-icon>
          </v-btn>
        </v-row>
      </v-container>
    </v-container>
  </v-container>
</template>

<script setup>
import { ref, watch, onMounted, computed } from 'vue';

const props = defineProps({
  src: {
    type: String,
    required: true,
  },
});

const audio = ref(null);
const currentTime = ref(0);
const duration = ref(0);
const isPlaying = ref(false);

onMounted(() => {
  if (props.src && audio.value) {
    audio.value.src = props.src;
    audio.value.load();
    audio.value.play().then(() => {
      isPlaying.value = true;
    }).catch(error => console.error("Play Error", error));
  }
});

const togglePlay = () => {
  if (audio.value.paused) {
    audio.value.play().then(() => {
      isPlaying.value = true;
    }).catch(error => console.error("Playback error: ", error));
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

const resetPlayer = () => {
  isPlaying.value = false;
  currentTime.value = 0;
};

const goToStart = () => {
  audio.value.currentTime = 0;
  audio.value.pause();
  isPlaying.value = false;
};

const goToEnd = () => {
  audio.value.currentTime = audio.value.duration;
};

const rewindAudio = () => {
  audio.value.currentTime = Math.max(0, audio.value.currentTime - 5);
};

const fastForwardAudio = () => {
  audio.value.currentTime = Math.min(audio.value.duration, audio.value.currentTime + 5);
};

const handleError = (event) => {
  console.error('Audio playback error:', event);
};

const formattedCurrentTime = computed(() => formatTime(currentTime.value));
const formattedDuration = computed(() => formatTime(duration.value));

const formatTime = (seconds) => {
  const minutes = Math.floor(seconds / 60);
  const secs = Math.floor(seconds % 60);
  return `${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
};
</script>

<style scoped>
.audio-player {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.time {
  margin-bottom: 10px;
  align-items: center;
}

.bar-row {
  margin-bottom: 30px;
}

.btn-row {
  width: 100%;
  max-width: 500px;
}

.player-btn {
  margin-right: 10px;
  padding: 10px 20px;
  background-color: black;
  color: white;
  font-weight: bold;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.player-btn:hover {
  color: #874d4d;
}

.play-bar {
  width: 100%;
  max-width: 500px;
  accent-color: #ffffff;
}
audio{
  display: none;
}
</style>
