<script setup>
import { defineProps } from 'vue';

const props = defineProps({
    item: Object
});
</script>

<template>
  <v-card outlined class="custom-card" @click="$emit('click')">
    <v-img :src="item.imageUrl" class="custom-image" aspect-ratio="1.7" />
    <v-card-title class="custom-title">
      {{ item.title }}
    </v-card-title>
    <div class="custom-text">
    <v-icon color="blue" class="mr-2">
      mdi-thumb-up
    </v-icon>
    <span class="mr-10">{{item.heartCount}}</span>
    <v-icon color="blue" class="mr-2">
      mdi-headphones
    </v-icon>
    <span class="mr-10">{{ item.listenCount }}</span>
    </div>
  </v-card>
</template>

<style scoped>
.custom-card {
  padding: 10px;
  border-radius: 10px;
  background-color: black;
  color: #fff;
}

.custom-card:hover{
    transform: scale(1.02);
}

.custom-image {
  border: 2px solid gray;
  border-radius: 5px;
}

.custom-title {
  text-align: center;
  margin-top: 10px;
}

.custom-text{
  text-align: center;
}
</style>
