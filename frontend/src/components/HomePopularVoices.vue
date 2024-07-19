<script setup>
import {ref, onMounted} from 'vue';
import axios from 'axios';

onMounted(() => {
  fetchBestVoices(); // <div>
})

const voices = ref([]);
const voiceDetail = ref();

async function fetchVoiceDetail(voiceId) {
  try {
    await axios.get(`http://localhost:8080/api-voice/best-voice/${voiceId}`)
    .then((res)=>{
      voiceDetail.value = res.data;
      console.log(res.data);
    });
  } catch (error) {
    console.error(`Error fetching voice detail for ID ${voiceId}:`, error);
  }
}

async function fetchBestVoices() {
  try {
    await axios.get('http://localhost:8080/api-voice/best-voice')
                                .then((res) =>{
                                  voices.value = res.data;
                                });
  } catch (error) {
    console.error('Error fetching best voices:', error);
  }
}

function getDetail(id){
  fetchVoiceDetail(id);
}
</script>

<template>
  <div>
    <v-row>
      <v-col>
        <h2>오늘의 인기 음성</h2>
      </v-col>
      <v-col class="text-right">
        <v-btn class="more-button" text>더보기</v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-card class="mb-4 list-items" elevation="2" v-for="(item, index) in voices" :key="index">
          <v-row no-gutters @click="getDetail(item.id)">
            <v-col cols="4">
              <v-img :src="item.imageUrl" height="100px" contain></v-img>
            </v-col>
            <v-col cols="8">
              <v-card-title>
                <div class="content">
                  <h3>{{ item.title }}</h3>
                  <p>{{ item.listenCount }} Listeners</p>
                  <v-avatar size="36">
                    <img :src="item.imageUrl" class="avatar-img" />
                  </v-avatar>
                  <span class="author-name">{{ item.userName }}</span>
                </div>
              </v-card-title>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<style scoped>
.more-button {
  background-color: #f3b549;
  color: #000;
  font-weight: bold;
}
.list-items {
  background-color: #000;
}
.author-name {
  color: #fff;
  font-weight: bold;
}
.avatar-img {
  object-fit: cover;
  width: 100%;
  height: 100%;
}
h2 {
  color: #fff;
  font-weight: bold;
}
h3 {
  color: #fff;
  margin: 0;
}
p {
  color: #ccc;
  margin: 0;
}
</style>
