<template>
  <div>
    <div style="display: flex; flex-direction: column; align-items: center;">
          <h1>보관함</h1>
    </div>
    <div class="tabs">
      <button @click="switchTab('all')" :class="{ active: activeTab === 'all' }">줍한 음성 목록</button>
      <button @click="switchTab('liked')" :class="{ active: activeTab === 'liked' }">좋아요 음성 목록</button>
    </div>
    <div v-if="activeTab === 'all'" class="voice-list">
      <div v-for="voice in allVoices" :key="voice.id" class="voice-item">
        <img :src="voice.image" alt="voice image" class="voice-image" />
        <div class="voice-title">{{ voice.title }}</div>
      </div>
    </div>
    <div v-if="activeTab === 'liked'" class="voice-list">
      <div v-for="voice in likedVoices" :key="voice.id" class="voice-item">
        <img :src="voice.image" alt="voice image" class="voice-image" />
        <div class="voice-title">{{ voice.title }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useVoiceStore } from '@/store/storage';

const activeTab = ref('all');
const voiceStore = useVoiceStore();

const allVoices = computed(() => voiceStore.allVoices);
const likedVoices = computed(() => voiceStore.likedVoices);

function switchTab(tab) {
  activeTab.value = tab;
}

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
