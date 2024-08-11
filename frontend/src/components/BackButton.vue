<template>
  <v-btn icon @click="goBack">
    <v-icon>mdi-arrow-left</v-icon>
  </v-btn>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const goBack = () => {
  router.back();
};
</script>

<style scoped>
</style>
