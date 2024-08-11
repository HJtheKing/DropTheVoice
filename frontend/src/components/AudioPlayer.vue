<template>
  <v-container class="audio-player">
    <audio ref="audio" @timeupdate="updateTime" @loadedmetadata="updateDuration" @ended="resetPlayer" @error="handleError">
      <source :src="audioUrl" type="audio/wav">
      Your browser does not support the audio element.
    </audio>
    <v-container class="controls" justify="center">
      <v-row justify="center" class="time">
        {{ formatTime(currentTime) }} / {{ formatTime(duration) }}
      </v-row>
      <v-row justify="center" class="bar-row">
        <input class="play-bar" type="range" min="0" :max="duration" step="0.01" v-model="currentTime" @input="seekAudio">
      </v-row> 
      <v-container justify="center" class="btn-row">
        <v-row justify="space-between">
          <v-btn class="player-btn" @click="goToStart">
            <v-icon>mdi-skip-backward</v-icon>
          </v-btn>
          <v-btn class="player-btn" @mousedown="handleMouseDown('rewind')" @mouseup="handleMouseUp('rewind')">
            <v-icon>mdi-rewind</v-icon>
          </v-btn>
          <v-btn class="player-btn" @click="togglePlay">
            <v-icon>{{ isPlaying ? 'mdi-pause' : 'mdi-play' }}</v-icon>
          </v-btn>
          <v-btn class="player-btn" @mousedown="handleMouseDown('forward')" @mouseup="handleMouseUp('forward')">
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
import { ref, watch, defineExpose } from 'vue';
import { useRecordStore } from '@/store/record';
import { storeToRefs } from 'pinia';

const recordStore = useRecordStore();
const { isPlaying, audioUrl } = storeToRefs(recordStore);

const audio = ref(null);
const currentTime = ref(0);
const duration = ref(0);

const rewindInterval = ref(null);
const longPressTimeout = ref(null);
const longPressThreshold = 300; // 밀리초
const mouseDownTime = ref(0);

watch(audioUrl, (newUrl) => {
  if (newUrl && audio.value) {
    audio.value.src = newUrl;
    audio.value.load();
    audio.value.onloadedmetadata = () => {
      duration.value = audio.value.duration;
    };
  }
});

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
  return `${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
};

const resetPlayer = () => {
  isPlaying.value = false;
  currentTime.value = 0;
};

const loadAudio = (audioUrl) => {
  audio.value.src = audioUrl;
  audio.value.load();
  audio.value.onloadedmetadata = () => {
    duration.value = audio.value.duration;
  };  
};

defineExpose({
  loadAudio
});

const handleError = (event) => {
  console.error('Audio playback error:', event);
};

const goToStart = () => {
  audio.value.currentTime = 0;
  audio.value.pause();
  isPlaying.value = false;
};

const goToEnd = () => {
  audio.value.currentTime = audio.value.duration;
};

const handleMouseDown = (action) => {
  mouseDownTime.value = Date.now();
  longPressTimeout.value = setTimeout(() => {
    if (action === 'rewind') {
      rewindAudio();
    } else if (action === 'forward') {
      fastForwardAudio();
    }
  }, longPressThreshold);
};

const handleMouseUp = (action) => {
  const clickDuration = Date.now() - mouseDownTime.value;
  if (clickDuration < longPressThreshold) {
    if (action === 'rewind') {
      jumpBackwardAudio();
    } else if (action === 'forward') {
      jumpForwardAudio();
    }
    clearTimeout(longPressTimeout.value);
    longPressTimeout.value = null;
  }
  
  stopRewind();
  resetPlaybackRate();
};

const resetPlaybackRate = () => {
  audio.value.playbackRate = 1;
};

const rewindAudio = () => {
  rewindInterval.value = setInterval(() => {
    if (audio.value.currentTime > 0) {
      audio.value.currentTime -= 0.2;
    } else {
      clearInterval(rewindInterval.value);
    }
  }, 100);
};

const fastForwardAudio = () => {
  rewindInterval.value = setInterval(() => {
    if (audio.value.currentTime < audio.value.duration) {
      audio.value.currentTime += 0.2;
    } else {
      clearInterval(rewindInterval.value);
    }
  }, 100);
};

const jumpForwardAudio = () => {
  if (audio.value.currentTime + 3 < audio.value.duration) {
    audio.value.currentTime += 3;
  } else {
    audio.value.currentTime = audio.value.duration;
  }
};

const jumpBackwardAudio = () => {
  if (audio.value.currentTime - 3 > 0) {
    audio.value.currentTime -= 3;
  } else {
    audio.value.currentTime = 0;
  }
};

const stopRewind = () => {
  clearInterval(rewindInterval.value);
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
  background-color: #f3b549;
  color: #000;
  font-weight: bold;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
.player-btn:hover {
  background-color: #f3b549;
  color: #000;
  font-weight: bold;
}

.play-bar {
  width: 100%;
  max-width: 500px;
  accent-color: #8B92DF;
}
</style>
