<template>
  <v-container class="current-time" fluid>
    <v-row justify="center">
      <v-col cols="12" sm="6" md="4">
        <v-card>
          <v-card-text>
            <span>{{ currentTime }}</span>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const currentTime = ref(new Date().toLocaleString('ko-KR', { month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric' }))

const updateTime = () => {
  currentTime.value = new Date().toLocaleString('ko-KR', { month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric' })
}

onMounted(() => {
  const intervalId = setInterval(updateTime, 60000)  // 매 분마다 업데이트
  onUnmounted(() => {
    clearInterval(intervalId)
  })
})
</script>

<style scoped>
.current-time {
  font-size: 24px;
  color: #1e90ff;
  padding: 10px;
  border-radius: 5px;
  text-align: center;
}

</style>
