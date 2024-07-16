
<template>
  <v-app class="black-background">
    <v-main>
      <v-container class="custom-container">
        <v-row justify="center" class="py-4">
          <h1 class="title">Drop The Voice</h1>
        </v-row>
        <HomeSpreadOrCatch />
        <HomePopularVoices :popularVoices="popularVoices" />
      </v-container>
    </v-main>
  </v-app>
</template>
<script>
import HomeSpreadOrCatch from '@/components/HomeSpreadOrCatch.vue';
import HomePopularVoices from '@/components/HomePopularVoices.vue';

export default {
  components: {
    HomeSpreadOrCatch,
    HomePopularVoices,
  },
  data() {
    return {
      popularVoices: [
        {
          title: '멀캠 14층에 들리는 소문',
          listeners: '78.4K',
          author: '김싸피',
          image: './src/assets/images/thumb1.webp',
          avatar: './src/assets/images/avatar1.webp',
        },
        {
          title: '우리 사장 편집증인 것 같다...',
          listeners: '23.5K',
          author: 'Courier',
          image: './src/assets/images/thumb2.jpg',
          avatar: './src/assets/images/avatar2.webp',
        },
        {
          title: '본인 술김에 약혼함.wav',
          listeners: '20.5K',
          author: 'Dovah',
          image: './src/assets/images/thumb3.jpg',
          avatar: './src/assets/images/avatar3.jpg',
        },
      ],
    };
  },
};
</script>

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

