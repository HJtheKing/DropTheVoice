<template>
  <v-container fluid class="home-container">
    <v-row justify="center">
      <v-col cols="12" class="text-center">
        <h1>실시간 녹음하기</h1>
      </v-col>
    </v-row>
    <v-row justify="center">
      <v-col cols="12" sm="8" md="6">
        <CurrentTime />
      </v-col>
    </v-row>
    <v-row justify="center" class="recording-container">
        <MyTimer />
        <MyRecordButton />
    </v-row>
  </v-container>
</template>

<script setup>
import CurrentTime from '@/components/record/CurrentTime.vue'
import MyTimer from '@/components/record/MyTimer.vue'
import MyRecordButton from '@/components/record/MyRecordButton.vue'
</script>

<style scoped>
.home-container {
  color: rgb(255, 255, 255);
  padding: 20px;
  min-height: 100vh;
}

.recording-container {
  margin: 20px 0;
}
</style>
