<template>
  <v-app id="inspire">
    <div class="main-content">
      <router-view />
    </div>
    <MenuBar />
  </v-app>
</template>

<script setup>
import MenuBar from '@/components/MenuBar.vue';

import { useStore } from 'vuex';
import { useRoute, useRouter  } from 'vue-router';

import { onMounted } from 'vue';

const store = useStore();
const route = useRoute();
const router = useRouter();

onMounted(async () => {
  await router.isReady();
  // 로그인 이후에 웹소켓 연결
  if (!store.getters.isConnected && route.name !== 'login') {
    store.dispatch('connectWebSocket');
  }
});

</script>

<style scoped>
.main-content {
  padding-bottom: 50px; /* MenuBar의 높이만큼 여백 추가 */
}
</style>
