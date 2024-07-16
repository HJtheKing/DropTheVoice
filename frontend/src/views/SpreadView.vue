<template>
  <div class="home-container">
    <header>
      <h1>뿌리기</h1>
    </header>
    <div class="tab-container">
      <button :class="{ active: activeTab === 'pokemon' }" @click="setTab('pokemon')">포켓몬</button>
      <button :class="{ active: activeTab === 'virus' }" @click="setTab('virus')">바이러스</button>
    </div>
    <div class="button-container">
      <RecordButton v-if="activeTab === 'pokemon'" />
      <UploadButton v-if="activeTab === 'pokemon'" />

      <RecordButton v-if="activeTab === 'virus'" />
      <UploadButton v-if="activeTab === 'virus'" />
    </div>
  </div>
</template>

<script setup>
import { useSpreadStore } from '@/stores/spread.js'
import RecordButton from '@/Components/spread/RecordButton.vue'
import UploadButton from '@/Components/spread/UploadButton.vue'
import { storeToRefs } from 'pinia'

const spreadStore = useSpreadStore()
const { activeTab } = storeToRefs(spreadStore)
const { setTab } = spreadStore
</script>

<style scoped>
.home-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #1e1e1e;
  height: 100vh;
  color: white;
  padding: 20px;
}

header {
  margin-top: 20px;
}

h1 {
  font-size: 24px;
  font-weight: bold;
}

.tab-container {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
  background-color: #333;
  padding: 5px;
  border-radius: 10px;
}

.tab-container button {
  background: none;
  border: none;
  color: #999;
  font-size: 16px;
  cursor: pointer;
  padding: 10px 20px;
  border-radius: 10px;
}

.tab-container button.active {
  color: #1e1e1e;
  background-color: white;
  font-weight: bold;
  border: 2px solid white;
}

.tab-container button:not(.active) {
  color: white;
  border: 2px solid transparent;
}

.button-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  margin-top: 50px;
}

@media (min-width: 600px) {
  .button-container {
    flex-direction: row;
  }
}

.nav-item {
  color: #999;
  text-decoration: none;
  font-size: 14px;
}

.nav-item.active {
  color: #bb86fc;
}
</style>
