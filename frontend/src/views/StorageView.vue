<template>
  <v-app class="bg-black">
    <v-container class="custom-container">
      <!-- 제목 섹션 -->
      <v-row justify="center">
        <v-col cols="12" class="text-center">
          <h1>보관함</h1>
        </v-col>
      </v-row>
      
      <v-row justify="center">
        <v-col cols="12" sm="8" md="6">
          <v-tabs v-model="activeTab" centered background-color="primary" class="mb-4">
  <v-tab value="all" @click="store.changeTab(activeTab)">
    줍한 음성 목록
  </v-tab>
  <v-tab value="liked" @click="store.changeTab(activeTab)">
    좋아요 음성 목록
  </v-tab>
  <v-tab value="alarm">
    전파된 음성 목록
  </v-tab>
</v-tabs>
        </v-col>
      </v-row>

      <!-- 음성 목록 섹션 -->
      <v-row justify="center">
        <v-col cols="12">
          <v-card
            v-for="voice in filteredVoices"
            :key="voice.voiceId"
            class="mb-4 list-items"
            elevation="2"
            @click="navigateToDetail(voice.voiceId)"
          >
            <v-row no-gutters class="d-flex align-center">
              <v-col cols="4">
                <v-img
                  :src="voice.imageUrl"
                  height="100px"
                  width="100%"
                  contain
                  class="rounded-left rounded-md"
                ></v-img>
              </v-col>
              <v-col cols="8">
                <v-card-title class="py-3">
                  <div class="content">
                    <h3 class="title mb-2">{{ voice.title }}</h3>
                    <div class="d-flex align-center mb-2">
                      <v-icon color="blue" class="mr-2">mdi-thumb-up</v-icon>
                      <span class="mr-10">{{ voice.heartCount }}</span>
                      <v-icon color="blue" class="mr-2">mdi-headphones</v-icon>
                      <span>{{ voice.listenCount }}</span>
                    </div>
                  </div>
                </v-card-title>
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
        <v-alert type="info">모두 다 발견되었어요 🌈</v-alert>
      </v-row>
    </v-container>
  </v-app>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useStorageStore } from '@/store/storage';
import { useUserStore } from "@/store/user";

const store = useStorageStore();
const userStore = useUserStore();
const router = useRouter();

const activeTab = ref('all');

const allVoices = computed(() => store.allVoices);
const likedVoices = computed(() => store.likedVoices);

const filteredVoices = computed(() => {
  return activeTab.value === 'all' ? allVoices.value : likedVoices.value;
});

const navigateToDetail = (id) => {
  router.push({ name: 'audioplayer', params: { id } });
};

const handleScroll = () => {
  const bottomOfWindow = window.innerHeight + window.scrollY >= document.documentElement.offsetHeight - 10;
  if (bottomOfWindow && !store.isFetching && store.hasMoreVoices) {
    store.loadMoreVoices();
  }
};


onMounted(() => {
  userStore.tryAutoLogin();
  window.addEventListener('scroll', handleScroll);

  if (store.activeTab === 'all') {
    store.reloadAllVoices();
  } else if (store.activeTab === 'liked') {
    store.reloadLikedVoices();
  }
});
</script>

<style scoped>
.custom-container {
  max-width: 800px;
  margin: 0 auto;
  padding-bottom: 80px;
  padding-left: 15px;
  padding-right: 15px;
}

.bg-black {
  background-color: #000;
  color: #fff;
}

.list-items {
  background-color: #000;
  color: #fff;
  overflow: hidden;
}

.list-items:hover {
  transform: scale(1.02);
  cursor: pointer;
}

h1, h3, p {
  color: #fff;
  margin: 0;
}
</style>
