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
import { useUserStore } from '@/store/user';

import { ref, onMounted, watch } from 'vue';

console.log("---a-----");
const store = useStore();
const userStore = useUserStore();

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
</style>
