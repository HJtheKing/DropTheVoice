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
        <v-col cols="12" sm="8" md="7">
          <v-tabs v-model="activeTab" centered background-color="primary" class="mb-4">
            <v-tab value="pick" @click="storageStore.changeTab(activeTab); userStore.resetPickNotification()">
    <v-icon v-if="userStore.hasNewPickNotifications" class="notification-icon">mdi-new-box</v-icon>
    줍한 음성 목록
  </v-tab>
  <v-tab value="liked" @click="storageStore.changeTab(activeTab); userStore.resetLikeNotification()">
    <v-icon v-if="userStore.hasNewLikeNotifications" class="notification-icon">mdi-new-box</v-icon>
    좋아요 음성 목록
  </v-tab>
  <v-tab value="spread" @click="storageStore.changeTab(activeTab); userStore.resetSpreadNotification()">
    <v-icon v-if="userStore.hasNewSpreadNotifications" class="notification-icon">mdi-new-box</v-icon>
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
      <v-row justify="center" v-if="storageStore.isFetching">
        <v-progress-circular indeterminate color="primary"></v-progress-circular>
      </v-row>

      <!-- 더 이상 데이터가 없을 때 표시할 메시지 -->
      <v-row justify="center" v-if="!storageStore.hasMoreVoices && !storageStore.isFetching">
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

const storageStore = useStorageStore();
const userStore = useUserStore();
const router = useRouter();

const activeTab = ref('pick');

const pickVoices = computed(() => storageStore.pickVoices);
const likedVoices = computed(() => storageStore.likedVoices);
const spreadVoices = computed(() => storageStore.spreadVoices);

const filteredVoices = computed(() => {
  if (activeTab.value === 'pick') {
    return pickVoices.value;
  } else if (activeTab.value === 'liked') {
    return likedVoices.value;
  } else if (activeTab.value === 'spread') {
    return spreadVoices.value;
  }
  return false;
});

const navigateToDetail = (id) => {
  router.push({ name: 'audioplayer', params: { id } });
};

const handleScroll = () => {
  const bottomOfWindow = window.innerHeight + window.scrollY >= document.documentElement.offsetHeight - 10;
  if (bottomOfWindow && !storageStore.isFetching && storageStore.hasMoreVoices) {
    storageStore.loadMoreVoices();
  }
};

onMounted(() => {
  userStore.tryAutoLogin();
  window.addEventListener('scroll', handleScroll);

  if (storageStore.activeTab === 'pick') {
    storageStore.reloadPickVoices();
  } else if (storageStore.activeTab === 'liked') {
    storageStore.reloadLikedVoices();
  } else if (storageStore.activeTab === 'spread') {
    storageStore.reloadSpreadVoices();
  }
});
</script>

<style scoped>
.notification-icon{
  animation:pulse 0.5s infinite;
  font-size:20px;
  color:yellow;
  position:absolute;
  top:-1px;
  right:-5px;
}
@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}
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
