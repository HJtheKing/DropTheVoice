<template>
  <v-app id="inspire">
    <router-view />
    <MenuBar />
  </v-app>
</template>

<script setup>
import MenuBar from '@/components/MenuBar.vue';
</script>