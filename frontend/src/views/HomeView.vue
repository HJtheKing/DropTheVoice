<script setup>
import HomeSpreadOrCatch from '@/components/HomeSpreadOrCatch.vue';
import HomePopularVoices from '@/components/HomePopularVoices.vue';

</script>

<template>

  <v-app class="black-background">
    <v-main>
      <v-container class="custom-container">
        <v-row justify="center" class="py-4">
          <v-col cols="12" class="text-center">
          <h1 class="title">Drop The Voice V08142059</h1>
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
