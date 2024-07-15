<template>
    <v-row justify="center" class="py-8">
      <v-col cols="6" class="d-flex justify-center">
        <v-card class="main-button" elevation="2" @click="navigateTo('example1')">
          <v-card-text class="text-center align-center">
            <v-icon class="main-button-icon">mdi-send</v-icon>
            <div class="main-button-text">뿌리기</div>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="6" class="d-flex justify-center">
        <v-card class="main-button" elevation="2" @click="navigateTo('example2')">
          <v-card-text class="text-center align-center">
            <v-icon class="main-button-icon">mdi-headphones</v-icon>
            <div class="main-button-text">주워듣기</div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </template>

<script>
export default {
  methods: {
    navigateTo(routeName) {
      this.$router.push({ name: routeName });
    },
  },
};
</script>
  
  <style scoped>
  .main-button {
    height: calc(50vw - 24px);
    max-height: 344px;
    width: calc(50vw - 24px);
    max-width: 344px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    background-color: #3a3a3a;
    color: #fff;
  }
  .main-button-icon {
    font-size: 160px;
    margin-bottom: 16px;
  }
  .main-button-text {
    color: #fff;
    font-weight: bold;
    font-size: 36px;
  }
  .text-center {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    width: 100%;
  }
  </style>
  