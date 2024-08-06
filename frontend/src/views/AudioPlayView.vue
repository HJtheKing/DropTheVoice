<template>
  <v-app class="bg-black">
    <v-main>
      <v-container class="custom-container">
        <v-row justify="center" class="py-4">
          <h2>{{ selectedItem.title }}</h2>
        </v-row>
        <v-row>
          <v-img :src="selectedItem.imgURL" height="300px" contain></v-img>
        </v-row>
        <v-row justify="center" class="py-4">
          <h2>{{ selectedItem.title }}</h2>
        </v-row>
        <v-row justify="center" class="py-4">
          <h3>사연자: {{ selectedItem.author }}</h3>
        </v-row>
        <v-row>
          <v-container class="audio-play">
            <audio-player ref="audioPlayer"></audio-player>
            <v-btn class="load-btn" @click="playSampleAudio">오디오 불러오기</v-btn>
          </v-container>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue';
import { useSeletedTrackStore } from '@/store/playList.js';
import AudioPlayer from '@/components/AudioPlayer.vue';
import audioFile from '@/assets/tracks/진격 (Zinkyeok) - Rusty Ground.webm';

const store = useSeletedTrackStore();
const selectedItem = computed(() => store.getSelectedItem);

const audioPlayer = ref(null);

function playSampleAudio() {
  if (audioPlayer.value) {
    audioPlayer.value.loadAudio(audioFile);
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
