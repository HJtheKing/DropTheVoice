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
        <button :class="{ active: activeTab === 'pokemon' }" @click="setTab('pokemon')">음성 두기</button>
        <button :class="{ active: activeTab === 'virus' }" @click="setTab('virus')">음성 전파</button>
      </div>

      <MapComponent v-if="activeTab === 'pokemon'"/>
      <div class="bottom-button-container">

        <v-container class="bg-black">
          <v-row no-gutters class="align-content-start">
            <v-col cols="6" class="d-flex justify-center ">
              <v-btn router-link class="v-alert--density-comfortable" size="x-large" prepend-icon="mdi-microphone"
                     variant="tonal" @click="navigateTo('record')">
                녹음하기
              </v-btn>
            </v-col>
            <v-col cols="6" class="d-flex justify-center ">
              <v-btn class="v-alert--density-comfortable" size="x-large" prepend-icon="mdi-upload-box" variant="tonal"
                     @click="navigateTo('upload-voice')">
                업로드
              </v-btn>
              </v-col>
          </v-row>
        </v-container>
      </div>
    </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
import { useSpreadStore } from '@/store/spread.js'
import { storeToRefs } from 'pinia'
import MapComponent from "@/components/spread/MapComponent.vue";
import { useRouter } from 'vue-router';

const router = useRouter();

const spreadStore = useSpreadStore()
const { activeTab } = storeToRefs(spreadStore)
const { setTab } = spreadStore

function navigateTo(routeName) {
  localStorage.setItem('voiceType', activeTab.value)
  router.push({ name: routeName });
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
