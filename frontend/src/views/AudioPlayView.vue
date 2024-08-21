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
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import AudioPlayer from '@/components/AudioPlayer.vue';
import { getVoice } from '@/utils/rtc';

const route = useRoute();
const voice = ref({});
const likeCount = ref(0);
const isLiked = ref(false);
const isLoading = ref(true);
const audioSrc = ref(null);
const voiceType = ref(null);

let lat;
let lon;

onMounted(async () => {
  const voiceId = route.params.id;
  try {
    const voiceFromIdxDB = await getVoice(Number(voiceId));
    const token = sessionStorage.getItem('access-token');
    const headers = token ? { Authorization: `Bearer ${token}` } : {};
    const res = await axios.get(`${import.meta.env.VITE_BASE_URL}/api-voice/best-voice/${voiceId}`, { headers });

    voice.value = res.data;
    likeCount.value = voice.value.heartCount;
    isLiked.value = voice.value.liked; 
    isLoading.value = false;
    voiceType.value = voice.value.voiceType;

    console.log(voiceType)

    if(voiceFromIdxDB){
      const url = URL.createObjectURL(voiceFromIdxDB);
      audioSrc.value = url;
    } else {
      audioSrc.value = voice.value.processedPath ? voice.value.processedPath : voice.value.savePath;
    }
    console.log(audioSrc.value)
  } catch (error) {
    console.error(`Error ${voiceId}:`, error);
    isLoading.value = false; 
  }
});
const toggleLike = () => {
  console.log('toggleLike activated')
  
  const token = sessionStorage.getItem('access-token');
  const headers = token ? { Authorization: `Bearer ${token}` } : {};
  if (!token) {
    console.error('JWT 토큰이 없습니다.');
    return;
  }
  
  try {
    const url = `${import.meta.env.VITE_BASE_URL}/api-voice/${voice.value.voiceId}/like`;
    console.log(url)
    
    const response = async () => { // async 추가
      if (voiceType.value === 'virus') { // 비교 연산자 수정

        await getGeo();

        const formData = new FormData();
        formData.append("latitude", lat);
        formData.append("longitude", lon);

        try {
          const result = await axios.post(url, formData, { headers });
          return result; // 결과 반환
        } catch (error) {
          console.error("Error occurred while sending the request:", error);
        }
      } else {
        console.log('not virus!!!!!!!');
        try {
          const result = await axios.post(url, null, { headers });
          return result; // 결과 반환
        } catch (error) {
          console.error("Error occurred while sending the request:", error);
        }
      }
    };  

  // 함수 호출
  response().then(result => {
    // 결과를 사용
    console.log("Response:", result);
    isLiked.value = result.data.isLiked;
    likeCount.value = result.data.likeCount;
  }).catch(error => {
    // 에러 처리
    console.error("Error:", error);
  });

  }
  catch (error) {
    console.error('Error like:', error);
  }
};

function getGeo() {
    return new Promise((resolve, reject) => {
        if ("geolocation" in navigator) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    lat = position.coords.latitude;
                    lon = position.coords.longitude;  
                    resolve();
                },
                (error) => {
                    console.log("위치 정보를 가져오는데 실패했습니다: " + error.message);
                    reject(new Error("위치 정보를 가져오는데 실패했습니다: " + error.message));

                },
                {
                    enableHighAccuracy: true,
                    timeout: 5000,
                    maximumAge: 0
                }
            );
        } else {
            alert("이 브라우저에서는 위치 정보 서비스를 지원하지 않습니다.");
            reject(new Error("위치 정보를 가져오는데 실패했습니다: "));
        }
    })
}
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
