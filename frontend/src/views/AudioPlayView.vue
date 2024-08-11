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
        </v-row>

        <!-- 좋아요 및 ListenCount 섹션 -->
        <v-row class="actions">
          <v-icon
            :color="isLiked ? 'blue' : 'white'"
            class="mr-2 like-button"
            @click="toggleLike"
          >
            mdi-thumb-up
          </v-icon>
          <span>{{ likeCount }} Likes</span>
          <v-icon color="blue" class="ml-6 mr-2">mdi-headphones</v-icon>
          <span>{{ voice.listenCount }} Listens</span>
        </v-row>

        <!-- 오디오 플레이어 섹션 -->
        <v-row>
          <v-container class="audio-play">
            <v-progress-circular v-if="isLoading" indeterminate color="primary"></v-progress-circular>
            <AudioPlayer v-else :src="audioSrc" />
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
import AudioPlayer from '@/components/AudioPlayer.vue';

const route = useRoute();
const voice = ref({});
const likeCount = ref(0);
const isLiked = ref(false);
const isLoading = ref(true);

// 오디오 소스 결정
const audioSrc = computed(() => {
  return voice.value.processedPath ? voice.value.processedPath : voice.value.savePath;
});
onMounted(async () => {
  const voiceId = route.params.id;
  try {
    const token = sessionStorage.getItem('access-token');
    const headers = token ? { Authorization: `Bearer ${token}` } : {};

    const res = await axios.get(`${import.meta.env.VITE_BASE_URL}/api-voice/best-voice/${voiceId}`, { headers });
    
    voice.value = res.data;
    likeCount.value = voice.value.heartCount;
    isLiked.value = voice.value.isLiked; 
    isLoading.value = false; 
  } catch (error) {
    console.error(`Error ${voiceId}:`, error);
    isLoading.value = false; 
  }
});
const toggleLike = async () => {
  const token = sessionStorage.getItem('access-token');
  if (!token) {
    console.error('JWT 토큰이 없습니다.');
    return;
  }

  try {
    const url = `${import.meta.env.VITE_BASE_URL}/api-voice/${voice.value.voiceId}/like`;

    const response = await axios.post(url, null, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (response.status === 200 && response.data) {
      isLiked.value = response.data.isLiked;
      likeCount.value = response.data.likeCount;
    } else {
      console.error('서버에서 응답이 제대로 오지 않았습니다.');
    }
  } catch (error) {
    console.error('Error like:', error);
  }
};
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

.actions {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin-top: 20px;
  margin-left: 20px;
  color: #fff;
}

.like-button {
  cursor: pointer;
  transition: color 0.3s ease;
}

.like-button:hover {
  color: #1976d2;
}

.ml-6 {
  margin-left: 24px;
}
</style>
