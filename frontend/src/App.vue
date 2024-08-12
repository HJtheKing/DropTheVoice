<template>
  <v-app id="inspire">
    <div class="main-content">
      <router-view />
    </div>
    <MenuBar />
    <v-btn icon class="floating-back-button" @click="goBack">
      <v-icon>mdi-arrow-left</v-icon>
    </v-btn>
  </v-app>
</template>

<script setup>
import MenuBar from '@/components/MenuBar.vue';
import { useRouter } from 'vue-router';

import { useStore } from 'vuex';
import { useUserStore } from '@/store/user';
import { useStorage } from '@vueuse/core';

import { onMounted } from 'vue';

const store = useStore();
const userStore = useUserStore();
const router = useRouter();

const goBack = () => {
  router.back();
};

onMounted(() => {
  if (!store.getters.isConnected) {
    store.dispatch('connectWebSocket');
  }
});
</script>

<style scoped>
.main-content {
  padding-bottom: 50px; /* MenuBar의 높이만큼 여백 추가 */
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
