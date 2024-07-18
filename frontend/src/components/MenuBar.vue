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
      <v-btn value="음성찾기">
        <v-icon>mdi-compass</v-icon>
        <span>음성찾기</span>
      </v-btn>
      <v-btn value="보관함">
        <v-icon>mdi-folder</v-icon>
        <span>보관함</span>
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
        this.$router.push({ name: routeName });
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
  