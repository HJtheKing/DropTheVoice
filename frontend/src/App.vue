<template>
  <v-app id="inspire">
    <div class="main-content">
      <router-view />
      <MyHeader />
    </div>
    <MenuBar />
  </v-app>
</template>

<script setup>
import MenuBar from '@/components/MenuBar.vue';
import { useStore } from 'vuex';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from './store/user';

import { onMounted } from 'vue';
import MyHeader from './components/MyHeader.vue';

const store = useStore();
const route = useRoute();
const router = useRouter();
const userStore = useUserStore();

onMounted(async () => {
  await router.isReady();
  // 로그인 이후에 웹소켓 연결
  if (!store.getters.isConnected && route.name !== 'login') {
    store.dispatch('connectWebSocket');
  }
  userStore.initializeSSE(); // SSE 연결 초기화
});
</script>

<style scoped>
.main-content {
  padding-top: 64px; /* MenuBar의 높이만큼 여백 추가 */
}

.floating-back-button {
  position: fixed;
  top: 20px;
  left: 20px;
  z-index: 1000;
  background-color: #000000;
  color: white;
}
</style>
