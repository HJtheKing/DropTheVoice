<template>
  <v-app class="bg-black">
    <v-main>
      <v-container class="custom-container">
        <v-row justify="center" class="py-4">
          <h1>음성 듣기</h1>
        </v-row>
        <v-row justify="center">
          <v-img :src="voice.imageUrl" height="300px" class="rounded-md" contain></v-img>
        </v-row>
        <v-row justify="center" class="py-4">
          <h3>{{ voice.title }}</h3>
              <!-- <v-icon color="blue" class="mr-2">mdi-thumb-up</v-icon>
              <span class="mr-10">{{ item.heartCount }}</span>
              <v-icon color="blue" class="mr-2">mdi-headphones</v-icon>
              <span>{{ item.listenCount }}</span> -->
        </v-row>

        <v-row>
          <v-container class="audio-play">
            <AudioPlayer v-if="voice.savePath" :src="voice.savePath"/>
            <v-icon
              v-else icon="mdi-check-circle"
              :size="large"
            ></v-icon>
          </v-container>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import AudioPlayer from '@/components/AudioPlayer.vue';

const route = useRoute();
const voice = ref({});

onMounted(async () => {
  const voiceId = route.params.id;
  try {
    const res = await axios.get(`${import.meta.env.VITE_BASE_URL}/api-voice/best-voice/${voiceId}`);
    voice.value = res.data;
    console.log('Voice data: ', voice.value)
  } catch (error) {
    console.error(`Error fetching voice detail for ID ${voiceId}:`, error);
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
</style>
