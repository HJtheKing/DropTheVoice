<template>
    <button class="action-button" @click="startRecording">
      <div class="button-content">
        <img src="@/assets/microphone-icon.png" alt="녹음 아이콘" />
        <span>실시간 녹음하기</span>
      </div>
    </button>
  </template>
  
  <script setup>
  const startRecording = () => {
    console.log('실시간 녹음하기');
    // 녹음 시작 로직 추가
  };
  </script>
  
  <style scoped>
  .action-button {
    background-color: #333;
    border: none;
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
  
  .action-button img {
    width: 50px;
    height: 50px;
    margin-bottom: 10px;
  }
  </style>
  