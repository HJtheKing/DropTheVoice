<template>
  <v-container fluid>
    <v-row justify="center">
      <v-col cols="12" class="text-center">
        <h1>보관함</h1>
      </v-col>
    </v-row>

    <v-row justify="center">
      <v-col cols="12" sm="8" md="6">
        <v-row justify="center">
          <v-tabs v-model="activeTab" centered background-color="primary" class="mb-4" @click="store.changeTab(activeTab)">
            <v-tab value="all">줍한 음성 목록</v-tab>
            <v-tab value="liked">좋아요 음성 목록</v-tab>
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

        <!-- 로딩 애니메이션 -->
        <v-row justify="center" v-if="store.isFetching">
          <v-progress-circular indeterminate color="primary"></v-progress-circular>
        </v-row>

        <!-- 더 이상 데이터가 없을 때 표시할 메시지 -->
        <v-row justify="center" v-if="!store.hasMoreVoices && !store.isFetching">
          <p>더 이상 불러올 데이터가 없습니다.</p>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useStorageStore } from '@/store/storage';

const store = useStorageStore();

const activeTab = ref('all');

const allVoices = computed(() => store.allVoices);
const likedVoices = computed(() => store.likedVoices);

const filteredVoices = computed(() => {
  return activeTab.value === 'all' ? allVoices.value : likedVoices.value;
});

const handleScroll = () => {
  const bottomOfWindow = window.innerHeight + window.scrollY >= document.documentElement.offsetHeight - 10;
  if (bottomOfWindow && !store.isFetching && store.hasMoreVoices) {
    store.loadMoreVoices();
  }
};

onMounted(() => {
  // store.fetchAllVoices(); // 초기에는 'all' 탭의 첫 페이지 데이터를 불러옴
  window.addEventListener('scroll', handleScroll);

  // 데이터 초기화 및 다시 로드
  if (store.activeTab === 'all') {
    store.reloadAllVoices();
  } else if (store.activeTab === 'liked') {
    store.reloadLikedVoices();
  }
});
</script>
