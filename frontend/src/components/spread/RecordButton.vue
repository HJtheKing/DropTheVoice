<template>
  <v-btn class="action-button" @click="handleClick" color="primary" rounded large>
    <div class="button-content">
      <img src="@/assets/microphone-icon.png" alt="녹음 아이콘" class="icon" />
      <span>실시간 녹음하기</span>
    </div>
  </v-btn>
</template>

<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();

const handleClick = () => {
  router.push('record');
};
</script>

<style scoped>
.action-button {
  background-color: #333;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 16px;
  width: 200px;
  height: 200px;
  cursor: pointer;
  text-align: center;
}

.button-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.icon {
  width: 50px;
  height: 50px;
  margin-bottom: 10px;
}
</style>
