<template>
  <v-app class="bg-black">
    <v-main>
      <v-container class="custom-container">
        <v-row justify="center" class="py-4">
          <h2>{{ voice.title }}</h2>
        </v-row>
        <v-row justify="center">
          <v-img :src="voice.imgURL" height="300px" contain></v-img>
        </v-row>
        <v-row justify="center" class="py-4">
          <h3>사연자: {{ voice.voiceId }}</h3>
        </v-row>
        <v-row>
          <v-container class="audio-play">
            <audio-player ref="audioPlayer" :src="voice.audioUrl"></audio-player>
            <v-btn class="load-btn" @click="playSampleAudio">오디오 불러오기</v-btn>
          </v-container>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>
<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import { useSeletedTrackStore } from '@/store/playList.js';
import AudioPlayer from '@/components/AudioPlayer.vue';

const route = useRoute();
const voice = ref({});
const audioPlayer = ref(null);

onMounted(async () => {
  const voiceId = route.params.id;
  try {
    const res = await axios.get(`${import.meta.env.VITE_BASE_URL}/api-voice/${voiceId}`);
    voice.value = res.data;
    console.log(voice.value)
  } catch (error) {
    console.error(`Error fetching voice detail for ID ${voiceId}:`, error);
  }
});

const store = useSeletedTrackStore();
const selectedItem = computed(() => store.getSelectedItem);

function playSampleAudio() {
  if (audioPlayer.value) {
    audioPlayer.value.loadAudio(voice.value.audioUrl);
  }
}

onMounted(() => {
  if (!selectedItem.value.title) {
    console.log('선택된 아이템이 없습니다.');
  }
});
</script>
<style scoped>
.custom-container {
  max-width: 800px;
  margin: 0 auto;
  padding-bottom: 80px;
}
.audio-play {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.load-btn {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #f3b549;
  color: #000;
  font-weight: bold;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
.load-btn:hover {
  background-color: #cd9e01;
}
</style>
