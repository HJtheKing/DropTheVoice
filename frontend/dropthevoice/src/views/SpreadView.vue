<template>
    <div class="home-container">
      <header>
        <h1>뿌리기</h1>
      </header>
      <div class="tab-container">
      <button :class="{ active: activeTab === 'spread' }" @click="activeTab = 'spread'">확산하기</button>
      <button :class="{ active: activeTab === 'layEggs' }" @click="activeTab = 'layEggs'">알 낳기</button>
    </div>
    <div class="button-container">
      <RecordButton v-if="activeTab === 'spread'" />
      <UploadButton v-if="activeTab === 'layEggs'" />
    </div>
      <div>
        <navBarCHJ />
      </div>
    </div>
  </template>
  
  <script setup>
  import RecordButton from '@/components/spread/RecordButton.vue'
  import UploadButton from '@/components/spread/UploadButton.vue'
  import navBarCHJ from '@/components/navBarCHJ/navBarCHJ.vue'
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

  .tab-container button.active {
  color: #1e1e1e;
  background-color: white;
  font-weight: bold;
  }
  </style>
  