<template>
    <v-bottom-navigation
      v-model="activeNav"
      grow
      class="fixed-bottom bottom-navy"
    >
      <v-btn value="홈" @click="navigateTo('home')">
        <v-icon>mdi-home</v-icon>
        <span>홈</span>
      </v-btn>
      <v-btn value="음성찾기" @click="navigateTo('finder')">
        <v-icon>mdi-compass</v-icon>
        <span router-link :to="{ name: 'finder' }">
          음성 줍기
        </span>
      </v-btn>
      <v-btn value="보관함" @click="navigateTo('storage')">
        <v-icon>mdi-folder</v-icon>
        <span router-link :to="{ name: 'storage'}">보관함</span>
      </v-btn>
      <v-btn value="알림함">
        <v-icon>mdi-bell</v-icon>
        <span>알림함</span>
      </v-btn>
      <v-btn value="더보기">
        <v-icon>mdi-view-grid</v-icon>
        <span>더보기</span>
      </v-btn>
    </v-bottom-navigation>
  </template>

  <script>
  export default {
    data() {
      return {
        activeNav: 'HOME'
      };
    },
    methods: {
      navigateTo(routeName) {
        if (this.$route.name === routeName) {
        // 이미 해당 경로에 있는 경우 스크롤을 맨 위로 이동
        // window.scrollTo({ top: 0, behavior: 'smooth' });
        } else {
        this.$router.push({ name: routeName });
        // window.scrollTo({ top: 0, behavior: 'smooth' });
        }
      },
    },
  };
  </script>

  <style scoped>
  .bottom-navy {
    background-color: #252836;
    color: #808191;
  }
  </style>
  
