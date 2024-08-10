<template>
  <v-app class="bg-black">
    <v-main>
      <v-container class="custom-container" v-if="!uploadInProgress">
        <v-row justify="center" class="py-4">
          <v-col cols="12" class="text-center">
            <h1 class="title">음성 뿌리기</h1>
          </v-col>
        </v-row>

        <div class="file-info-container">
          <v-text-field v-model="title" label="제목" class="mt-2" />
          <v-file-input
            label="파일 선택"
            v-model="selectedFile"
            prepend-icon="mdi-upload"
            :show-size="true"
            @change="handleFileChange"
          ></v-file-input>

          <v-row justify="center" class="py-4">
            <v-col cols="12" class="text-center">
              <v-btn
                v-if="!showPitchControls && member"
                class="v-alert--density-comfortable"
                size="large"
                variant="tonal"
                @click="togglePitchControls"
              >
                음성 변조 남은 횟수 : {{ member.remainChangeCount }}
              </v-btn>
            </v-col>
          </v-row>

          <div v-if="showPitchControls">
            <v-row justify="center" class="py-4">
              <v-col cols="12" class="text-center">
                <p class="text-subtitle-1">Pitch</p>
              </v-col>
            </v-row>
            <v-slider
            :disabled="member.remainChangeCount === 0"
              v-model="pitch"
              :min="-12"
              :max="12"
              :step="0.1"
              thumb-label="always"
              class="mt-4"
              color="primary"
            ></v-slider>
            <v-row justify="center" class="py-4">
              <v-col cols="12" class="text-center">
                <v-btn
                  class="v-alert--density-comfortable"
                  size="large"
                  variant="tonal"
                  @click="togglePitchControls"
                >
                  원본 업로드로 돌아가기
                </v-btn>
              </v-col>
            </v-row>
          </div>

          <v-row justify="center" class="py-4">
            <v-col cols="12" class="text-center">
              <v-btn
                v-if="!showPitchControls"
                class="v-alert--density-comfortable"
                size="large"
                variant="tonal"
                @click="uploadFile('original')"
              >
                원본 업로드
              </v-btn>
              <v-btn
                v-if="showPitchControls"
                class="v-alert--density-comfortable"
                size="large"
                variant="tonal"
                @click="uploadFile('modified')"
              >
                음성 변조 업로드
              </v-btn>
            </v-col>
          </v-row>
        </div>
      </v-container>

      <v-container v-else class="upload-status-container">
        <div class="center-content">
          <h1 class="title">{{ uploadStatus }}</h1>
          <v-progress-circular
            v-if="uploadStatus === '업로드 중...'"
            :size="128"
            :width="12"
            indeterminate
            color="dark-blue"
          ></v-progress-circular>
          <v-icon
            v-else-if="uploadStatus === '업로드 성공'"
            icon="mdi-check-circle"
            :size="128"
          ></v-icon>
          <v-icon
            v-else-if="uploadStatus === '업로드 실패'"
            icon="mdi-alert-circle"
            :size="128"
          ></v-icon>
          <v-btn
            v-if="uploadStatus === '업로드 성공' || uploadStatus === '업로드 실패'"
            color="white"
            text-color="black"
            class="mt-4 font-weight-bold"
            @click="$router.push('/')"
          >
            홈으로 돌아가기
          </v-btn>
        </div>
      </v-container>

      <div v-if="audioUrl" class="video-container">
        <video controls :src="audioUrl" width="600" class="mt-4"></video>
      </div>
    </v-main>
  </v-app>
</template>
<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue';
import { useUserStore } from '@/store/user';
import { useSpreadStore } from '@/store/spread';
import axios from 'axios';

const userStore = useUserStore();
const spreadStore = useSpreadStore();
const member = computed(() => userStore.user);

const title = ref('');
const selectedFile = ref(null);
const pitch = ref(0);
const audioUrl = ref(null);
const uploadStatus = ref('');
const showPitchControls = ref(false);
const uploadInProgress = ref(false);

const memberId = computed(() => userStore.loginUserId);

const latitude = ref(0);
const longitude = ref(0);

const mp3Blob = ref(null);
onMounted(async () => {
  const base64Data = localStorage.getItem('recordData');
  if (base64Data) {
  mp3Blob.value = base64ToBlob(base64Data);
  }
  if (mp3Blob.value) {
    selectedFile.value = new File([mp3Blob.value], 'recorded-audio.mp3', { type: 'audio/mp3' });
  }
});

function base64ToBlob(base64) {
  const [prefix, base64Data] = base64.split(',');

  const contentType = prefix.match(/:(.*?);/)[1];
  const byteCharacters = atob(base64Data);
  const byteNumbers = new Array(byteCharacters.length);

  for (let i = 0; i < byteCharacters.length; i++) {
    byteNumbers[i] = byteCharacters.charCodeAt(i);
  }

  const byteArray = new Uint8Array(byteNumbers);
  return new Blob([byteArray], { type: contentType });
}

onBeforeUnmount(() => {
  localStorage.removeItem('recordData');
  localStorage.removeItem('voiceType')
});

function showPosition(position) {
  latitude.value = position.coords.latitude;
  longitude.value = position.coords.longitude;
  console.log(`Latitude: ${latitude.value} | Longitude: ${longitude.value}`);
}

function showError(error) {
  switch (error.code) {
    case error.PERMISSION_DENIED:
      console.log('User denied the request for Geolocation.');
      break;
    case error.POSITION_UNAVAILABLE:
      console.log('Location information is unavailable.');
      break;
    case error.TIMEOUT:
      console.log('The request to get user location timed out.');
      break;
    case error.UNKNOWN_ERROR:
      console.log('An unknown error occurred.');
      break;
  }
}

function getLocation() {
  return new Promise((resolve, reject) => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(resolve, reject);
    } else {
      reject(new Error('Geolocation is not supported by this browser.'));
    }
  });
}

const handleFileChange = (event) => {
  selectedFile.value = event.target.files[0];
  console.log(selectedFile.value);
}

const togglePitchControls = () => {
  showPitchControls.value = !showPitchControls.value;
}

const uploadFile = async (type) => {
  try {
    const position = await getLocation();
    showPosition(position);
  } catch (error) {
    showError(error);
  }

  if (selectedFile.value && title.value) {
    const formData = new FormData();
    formData.append('audioFile', selectedFile.value);
    formData.append('memberId', memberId.value);
    formData.append('title', title.value);
    formData.append('voiceType', localStorage.getItem('voiceType'));
    formData.append('latitude', latitude.value);
    formData.append('longitude', longitude.value);
    formData.append('pitchShift', pitch.value);

    uploadStatus.value = '업로드 중...';
    uploadInProgress.value = true;

    try {
      const response = await axios.post(`${import.meta.env.VITE_BASE_URL}/api-upload/upload`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      audioUrl.value = response.data.videoUrl || response.data.processedPath;
      uploadStatus.value = '업로드 성공';
    } catch (error) {
      uploadStatus.value = '업로드 실패';
    } finally {
      localStorage.removeItem('recordData');
}
  } else {
    alert ('제목 혹은 파일은 선택해 주세요.');
  }
}
</script>
<style scoped>
.title {
  font-size: 24px;
  font-weight: bold;
}
.custom-container {
  max-width: 800px;
  margin: auto;
}

.file-info-container,
.video-container {
  margin-top: 20px;
  text-align: center;
}

.upload-status-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  text-align: center;
}

.center-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
</style>
