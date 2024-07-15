<template>
  <!-- <header>
    <div class="wrapper">

      <nav>
        <RouterLink to="/">Home</RouterLink>
      </nav>
    </div>
  </header> -->
  <v-app id="inspire">
    <!-- <RouterView /> -->
    <router-view />
    <MenuBar />
  </v-app>
</template>

<script setup>
// import { RouterLink, RouterView } from 'vue-router'
import MenuBar from '@/components/MenuBar.vue';
</script>

<!-- <style scoped></style> -->

