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

const latitude = ref(0);
const longitude = ref(0);
onMounted(() => {
  
  if (!store.getters.isConnected) {
    store.dispatch('connectWebSocket');
  }
});

function sendMessage() {
    if ("geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                latitude.value = position.coords.latitude;
                longitude.value = position.coords.longitude;
                console.log(latitude.value);
                store.dispatch('sendMessage',{number:userStore.loginUserId,x:latitude.value,y:longitude.value});
            },
            (error) => {
                alert("위치 정보를 가져오는데 실패했습니다: " + error.message);
            },
            {
                enableHighAccuracy: true,
                timeout: 5000,
                maximumAge: 0
            }
        );
    } else {
        alert("이 브라우저에서는 위치 정보 서비스를 지원하지 않습니다.");
    }
}
</script>

<style scoped>
.main-content {
  padding-bottom: 50px; /* MenuBar의 높이만큼 여백 추가 */
}
</style>
