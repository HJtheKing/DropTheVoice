<template>
  <v-app class="black-background">
    <v-main>
      <v-container class="custom-container">
        <v-row justify="space-between" class="upper-content py-4">
          <h1 class="title">실시간 녹음하기</h1>
          <v-icon>mdi-upload</v-icon>
        </v-row>
        <v-row justify="center">
          <v-col cols="12" sm="8" md="6">
            <CurrentTime />
          </v-col>
        </v-row>
        <MyTimer />
        <MyRecordButton />
      </v-container>
    </v-main>
  </v-app>`
</template>

<script setup>
import CurrentTime from '@/components/record/CurrentTime.vue'
import MyTimer from '@/components/record/MyTimer.vue'
import MyRecordButton from '@/components/record/MyRecordButton.vue'
</script>

<style scoped>
.custom-container {
  max-width: 800px;
  margin: 0 auto;
  padding-bottom: 80px;
}

.upper-content {
  margin: 10px;
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
</style>
