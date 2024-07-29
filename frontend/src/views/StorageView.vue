<template>
    <v-container fluid>
    <v-row justify="center">
      <v-col cols="12" class="text-center">
        <h1>보관함</h1>
      </v-col>
    </v-row>

    <v-row justify="center">
      <v-col cols="12" sm="8" md="6" >
        <v-row justify="center">
        <v-tabs v-model="activeTab" centered background-color="primary" class="mb-4">
          <v-tab v-for="tab in tabs" :key="tab.value" :value="tab.value">{{ tab.label }}</v-tab>
        </v-tabs>
        </v-row>
      </v-col>
    </v-row>

    <v-row justify="center">
      <v-col cols="12" sm="8" md="6">
        <v-row>
          <v-col v-for="voice in filteredVoices" :key="voice.id" cols="12" class="mb-2">
            <v-card>
              <v-row>
                <v-col cols="auto">
                  <v-img :src="voice.image" alt="voice image" class="voice-image"></v-img>
                </v-col>
                <v-col>
                  <v-card-title>{{ voice.title }}</v-card-title>
                </v-col>
              </v-row>
            </v-card>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useStorageStore } from '@/store/storage';

const activeTab = ref('all');
const voiceStore = useStorageStore();

const allVoices = computed(() => voiceStore.allVoices);
const likedVoices = computed(() => voiceStore.likedVoices);


const filteredVoices = computed(() => {
  return activeTab.value === 'all' ? allVoices.value : likedVoices.value;
});

const tabs = [
  { value: 'all', label: '줍한 음성 목록' },
  { value: 'liked', label: '좋아요 음성 목록' },
];

onMounted(async () => {
  await voiceStore.fetchAllVoices();
  await voiceStore.fetchLikedVoices();
});
</script>

<style>
body {
  background-color: #000;
  color: #fff;
}

.tabs {
  display: flex;
  justify-content: center; /* 가운데 정렬 */
  margin: 20px 0;
}


button {
  background-color: #444;
  color: #fff;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  margin: 0 2px; /* 버튼 간격을 좁힙니다. */
}

button.active {
  background-color: #ff0;
  color: #000;
}

.voice-list {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.voice-item {
  display: flex;
  align-items: center;
  background-color: #aaa;
  width: 80%;
  margin: 10px 0;
  padding: 10px;
}

.voice-image {
  width: 50px;
  height: 50px;
  margin-right: 20px;
}

.voice-title {
  flex: 1;
}
</style>
