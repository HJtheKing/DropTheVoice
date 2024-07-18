<script setup>
import HomeSpreadOrCatch from '@/components/HomeSpreadOrCatch.vue';
import HomePopularVoices from '@/components/HomePopularVoices.vue';

import axios from 'axios';
import { ref, onMounted, watch } from 'vue';
import { useStore } from 'vuex';

const store = useStore();

const latitude = ref(0);
const longitude = ref(0);

onMounted(() => {
  if (!store.getters.isConnected) {
    store.dispatch('connectWebSocket').then(sendMessage());
  }
});

function sendMessage() {
    if ("geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                latitude.value = position.coords.latitude;
                longitude.value = position.coords.longitude;
                console.log(latitude.value);
                store.dispatch('sendMessage',{name:"김병관",x:latitude.value,y:longitude.value});
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

<template>
  <v-app class="black-background">
    <v-main>
      <v-container class="custom-container">
        <v-row justify="center" class="py-4">
          <h1 class="title">Drop The Voice</h1>
        </v-row>
        <button @click="sendMessage()">hih</button>
        <HomeSpreadOrCatch />
        <HomePopularVoices />
      </v-container>
    </v-main>
  </v-app>
</template>

<style>
.custom-container {
  max-width: 800px;
  margin: 0 auto;
  padding-bottom: 80px;
}

.black-background {
  background-color: #000;
  color: #fff;
}

.title {
  color: #fff;
  font-weight: bold;
  font-size: 24px;
}
</style>
