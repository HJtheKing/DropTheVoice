<script setup>
import { defineProps } from 'vue';

const props = defineProps({
    item: Object
});
</script>

<template>
  <v-card outlined class="custom-card">
    <v-img :src="item.imageUrl" class="custom-image" aspect-ratio="1.7" />
    <v-card-title class="custom-title">
      {{ item.title }}
    </v-card-title>
  </v-card>
</template>

<style scoped>
.custom-card {
  padding: 10px;
  border-radius: 10px;
  background-color: black;
}

.custom-image {
  border: 2px solid gray;
  border-radius: 5px;
}

.custom-title {
  color: white;
  text-align: center;
  margin-top: 10px;
}
</style>
