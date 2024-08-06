<template>
  <v-app class="bg-black">
    <v-main>
    <v-container class="custom-container">
      <v-row justify="center" class="py-4">
        <v-col cols="12" class="text-center">
        <h1 class="title">뿌리기</h1>
        </v-col>
      </v-row>

      <div class="tab-container">
        <button :class="{ active: activeTab === 'pokemon' }" @click="setTab('pokemon')">포켓몬</button>
        <button :class="{ active: activeTab === 'virus' }" @click="setTab('virus')">바이러스</button>
      </div>

      <MapComponent v-if="activeTab === 'pokemon'"/>
      <div class="bottom-button-container">

        <v-container class="bg-black">
          <v-row no-gutters class="align-content-start">
            <v-col cols="6" class="d-flex justify-center ">
              <v-btn router-link class="v-alert--density-comfortable" size="x-large" prepend-icon="mdi-microphone"
                     variant="tonal" @click="router.push({name: 'record'})">
                녹음하기
              </v-btn>
            </v-col>
            <v-col cols="6" class="d-flex justify-center ">
              <v-btn class="v-alert--density-comfortable" size="x-large" prepend-icon="mdi-upload-box" variant="tonal"
                     @click="triggerFileInput">
                업로드
              </v-btn>
              <v-file-input class="d-none" @change="handleFileChange" ref="fileInput"/>
              </v-col>
          </v-row>
        </v-container>
        
        <div v-if="selectedFile" class="file-info-container">
          <p>파일 이름: {{ selectedFile.name }}</p>
          <v-text-field v-model="title" label="Title" class="mt-2" />
          <v-select
            v-model="selectedPitch"
            :items="pitchOptions"
            label="음성변조 선택"
            class="mt-4"
          ></v-select>
          <v-btn class="v-alert--density-comfortable" size="large" variant="tonal" @click="uploadFile">
            {{ uploadStatus === '업로드 중...' ? uploadStatus : '업로드 하기' }}
          </v-btn>
        </div>

        <!-- Upload Status -->
        <div v-if="uploadStatus && uploadStatus !== '업로드 중...'" class="upload-status-container">
          <p>{{ uploadStatus }}</p>
        </div>

        <!-- Video Player -->
        <div v-if="videoUrl" class="video-container">
          <video controls :src="videoUrl" width="600" class="mt-4"></video>
        </div>
      </div>
    </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
import { ref } from 'vue';
import { useSpreadStore } from '@/store/spread.js'
import { storeToRefs } from 'pinia'
import MapComponent from "@/components/spread/MapComponent.vue";
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();

const spreadStore = useSpreadStore()
const { activeTab } = storeToRefs(spreadStore)
const { setTab } = spreadStore
const fileInput = ref(null);
const selectedFile = ref(null);
const title = ref('');
const selectedPitch = ref(null);
const pitchOptions = ref(['높게','원본' ,'낮게']);
const videoUrl = ref(null);
const uploadStatus = ref('');

const triggerFileInput = () => {
  fileInput.value.click();
}

const handleFileChange = (event) => {
  selectedFile.value = event.target.files[0];
}

const uploadFile = async () => {
  if (selectedFile.value && title.value && selectedPitch.value !== null) {
    const formData = new FormData();
    formData.append('audioFile', selectedFile.value);
    formData.append('title', title.value);

    let pitchShift = 0;
    if (selectedPitch.value === '높게') {
      pitchShift = 3;
    } else if (selectedPitch.value === '낮게') {
      pitchShift = -3;
    }
    formData.append('pitchShift', pitchShift);

    uploadStatus.value = '업로드 중...';

    try {
      const response = await axios.post('http://localhost:8080/api-spread/spread', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      console.log('File uploaded successfully:', response.data);
      videoUrl.value = response.data.videoUrl || response.data.processedPath;
      uploadStatus.value = '업로드 성공';
    } catch (error) {
      uploadStatus.value = '업로드 실패';
    }
  } else {
    uploadStatus.value = '모든 빈칸을 채워주세요.';
  }
}
</script>

<style scoped>
header {
  margin-top: 20px;
}

h1 {
  font-size: 24px;
  font-weight: bold;
}

.tab-container {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
  background-color: #333;
  padding: 5px;
  border-radius: 10px;
}

.tab-container button {
  background: none;
  border: none;
  color: #999;
  font-size: 16px;
  cursor: pointer;
  padding: 10px 20px;
  border-radius: 10px;
}

.tab-container button.active {
  color: #1e1e1e;
  background-color: white;
  font-weight: bold;
  border: 2px solid white;
}

.tab-container button:not(.active) {
  color: white;
  border: 2px solid transparent;
}

.button-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  margin-top: 50px;
}

.bottom-button-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  margin-top: 50px;
}

.file-info-container {
  margin-top: 20px;
  text-align: center;
}

.upload-status-container {
  margin-top: 20px;
  text-align: center;
  font-size: 16px;
  color: white;
}

.video-container {
  margin-top: 20px;
  text-align: center;
}

@media (min-width: 601px) and (max-width: 960px) {
  .title {
    font-size: 22px;
  }
  .custom-container {
    padding-left: 20px;
    padding-right: 20px;
  }
}

@media (min-width: 961px) {
  .title {
    font-size: 24px;
  }
  .custom-container {
    padding-left: 30px;
    padding-right: 30px;
  }
}
</style>
