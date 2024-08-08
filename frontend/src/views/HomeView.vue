<script setup>
import HomeSpreadOrCatch from '@/components/HomeSpreadOrCatch.vue';
import HomePopularVoices from '@/components/HomePopularVoices.vue';

import axios from 'axios';
import { ref, onMounted, watch } from 'vue';
import {createStore} from '@/store/index.js'
import { useStore } from 'vuex';
import { useUserStore } from '@/store/user';

// const store = useStore();
const store= createStore();
console.log(store);

const userStore = useUserStore();

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

<template>
  <v-app class="black-background">
    <v-main>
      <v-container class="custom-container">
        <v-row justify="center" class="py-4">
          <v-col cols="12" class="text-center">
          <h1 class="title">Drop The Voice</h1>
          </v-col>
        </v-row>
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


@media (min-width: 601px) and (max-width: 960px) {
  .title {
    font-size: 22px;
  }
  .custom-container {
    padding-left: 20px;
    padding-right: 20px;
  }
}

@media (min-width: 961px) {
  .title {
    font-size: 24px;
  }
  .custom-container {
    padding-left: 30px;
    padding-right: 30px;
  }
}
</style>
