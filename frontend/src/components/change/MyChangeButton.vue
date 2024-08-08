<template>
  <v-container class="py-8">
    <v-row justify="center" align="center" class="fill-height">
      <v-col cols="6" xs="12" class="d-flex justify-center">
        <v-card class="main-button" elevation="2" @click="recordStore.uploadFile()">
          <v-card-text class="text-center align-center">
            <v-icon class="main-button-icon">mdi-send</v-icon>
            <div class="main-button-text">원본 업로드</div>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="6" xs="12" class="d-flex justify-center">
        <v-card class="main-button" elevation="2" @click="tst()">
          <v-card-text v-if="member" class="text-center align-center">
            <v-icon class="main-button-icon">mdi-headphones</v-icon>
            <div class="main-button-text">음성변조</div>
            <div class="small-text">남은횟수 : {{ member.remainChangeCount }}</div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { useUserStore } from '@/store/user';
import { useRecordStore } from '@/store/record';
import { computed , onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const userStore = useUserStore();
const member = computed(() => userStore.user);

const recordStore = useRecordStore();

function tst(){
  console.log(member.value.remainChangeCount);
}

function navigateTo(routeName) {
  router.push({ name: routeName });
}

onMounted(() =>{
  userStore.tryAutoLogin();
})

// const memberId = computed(() => userStore.loginUserId);
// const locationMessage = ref('');
// const latitude = ref(0);
// const longitude = ref(0);

// function showPosition(position) {
//   latitude.value = position.coords.latitude;
//   longitude.value = position.coords.longitude;
//   locationMessage.value = `Latitude: ${latitude.value} | Longitude: ${longitude.value}`;
//   console.log(locationMessage.value);
// }

// function showError(error) {
//   switch(error.code) {
//     case error.PERMISSION_DENIED:
//       locationMessage.value = 'User denied the request for Geolocation.';
//       break;
//     case error.POSITION_UNAVAILABLE:
//       locationMessage.value = 'Location information is unavailable.';
//       break;
//     case error.TIMEOUT:
//       locationMessage.value = 'The request to get user location timed out.';
//       break;
//     case error.UNKNOWN_ERROR:
//       locationMessage.value = 'An unknown error occurred.';
//       break;
//   }
// };

// function getLocation() {
//   return new Promise((resolve, reject) => {
//     if (navigator.geolocation) {
//       navigator.geolocation.getCurrentPosition(resolve, reject);
//     } else {
//       reject(new Error('Geolocation is not supported by this browser.'));
//     }
//   });
// }

// const uploadFile = async () => {
//   try {
//     // 위치 정보 가져오기
//     const position = await getLocation();
//     showPosition(position);
//   } catch (error) {
//     showError(error);
//   }

//   if (selectedFile.value && title.value && selectedPitch.value !== null) {
//     const formData = new FormData();
//     formData.append('audioFile', selectedFile.value);
//     formData.append('memberId', memberId.value);
//     formData.append('title', title.value);
//     formData.append('voiceType', spreadStore.activeTab);
//     formData.append('latitude', latitude.value);
//     formData.append('longitude', longitude.value);

//     let pitchShift = 0;
//     if (selectedPitch.value === '높게') {
//       pitchShift = 3;
//     } else if (selectedPitch.value === '낮게') {
//       pitchShift = -3;
//     }
//     formData.append('pitchShift', pitchShift);

//     uploadStatus.value = '업로드 중...';

//     try { 
//       console.log(latitude.value)
//       const response = await axios.post('http://localhost:8080/api-upload/upload', formData, {
//         headers: {
//           'Content-Type': 'multipart/form-data'
//         }
//       });
//       console.log('File uploaded successfully:', response.data);
//       videoUrl.value = response.data.videoUrl || response.data.processedPath;
//       uploadStatus.value = '업로드 성공';
//     } catch (error) {
//       uploadStatus.value = '업로드 실패';
//     }
//   } else {
//     uploadStatus.value = '모든 빈칸을 채워주세요.';
//   }
// }
</script>

<style scoped>
.main-button {
  height: calc(30vw - 24px);
  max-height: 200px;
  width: calc(30vw - 24px);
  max-width: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  background-color: #3a3a3a;
  color: #fff;
}

.main-button-icon {
  font-size: 100px;
  margin-bottom: 8px;
}

.main-button-text {
  color: #fff;
  font-weight: bold;
  font-size: 24px;
}

.text-center {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
}

@media (max-width: 600px) {
  .main-button {
    height: calc(50vw - 16px);
    max-height: 160px;
    width: calc(50vw - 16px);
    max-width: 160px;
  }

  .main-button-icon {
    font-size: 60px;
    margin-bottom: 6px;
  }

  .main-button-text {
    font-size: 18px;
  }
}

@media (min-width: 601px) and (max-width: 960px) {
  .main-button {
    height: calc(40vw - 20px);
    max-height: 180px;
    width: calc(40vw - 20px);
    max-width: 180px;
  }

  .main-button-icon {
    font-size: 80px;
    margin-bottom: 7px;
  }

  .main-button-text {
    font-size: 20px;
  }
}

.small-text {
    font-size: 14px;
}
</style>
