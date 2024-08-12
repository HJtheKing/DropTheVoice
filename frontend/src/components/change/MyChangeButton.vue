<template>
  <v-container class="py-8">
    <v-row justify="center" align="center" class="fill-height">
      <v-col cols="6" xs="12" class="d-flex justify-center">
        <v-card class="main-button" elevation="2" @click="recordStore.uploadFile()">
          <v-card-text class="text-center align-center">
            <v-icon class="main-button-icon">mdi-send</v-icon>
            <div class="main-button-text">원본 업로드</div>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="6" xs="12" class="d-flex justify-center">
        <v-card class="main-button" elevation="2" @click="tst()">
          <v-card-text v-if="member" class="text-center align-center">
            <v-icon class="main-button-icon">mdi-headphones</v-icon>
            <div class="main-button-text">음성변조</div>
            <div class="small-text">남은횟수 : {{ member.remainChangeCount }}</div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { useUserStore } from '@/store/user';
import { useRecordStore } from '@/store/record';
import { computed , onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const userStore = useUserStore();
const member = computed(() => userStore.user);

const recordStore = useRecordStore();

function tst() {
  if (!recordStore.audioBlob) return;
  navigateTo('upload-voice');
}

function navigateTo(routeName) {
  router.push({ name: routeName });
}

onMounted(() =>{
  userStore.tryAutoLogin();
})
</script>

<style scoped>
.main-button {
  height: calc(30vw - 24px);
  max-height: 200px;
  width: calc(30vw - 24px);
  max-width: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  background-color: #3a3a3a;
  color: #fff;
}

.main-button-icon {
  font-size: 100px;
  margin-bottom: 8px;
}

.main-button-text {
  color: #fff;
  font-weight: bold;
  font-size: 24px;
}

.text-center {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
}

@media (max-width: 600px) {
  .main-button {
    height: calc(50vw - 16px);
    max-height: 160px;
    width: calc(50vw - 16px);
    max-width: 160px;
  }

  .main-button-icon {
    font-size: 60px;
    margin-bottom: 6px;
  }

  .main-button-text {
    font-size: 18px;
  }
}

@media (min-width: 601px) and (max-width: 960px) {
  .main-button {
    height: calc(40vw - 20px);
    max-height: 180px;
    width: calc(40vw - 20px);
    max-width: 180px;
  }

  .main-button-icon {
    font-size: 80px;
    margin-bottom: 7px;
  }

  .main-button-text {
    font-size: 20px;
  }
}

.small-text {
    font-size: 14px;
}
</style>
