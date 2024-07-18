<template>
    <button class="action-button" @click="uploadFile">
      <div class="button-content">
        <img src="@/assets/change.png" alt="오리지널 아이콘" />
        <span>에밀리로 목소리 변조하기</span>
        <span>남은 횟수 : {{ props.remain }}</span>
      </div>
    </button>
  </template>
  
  <script setup>
  const uploadFile = () => {
    console.log('목록에서 가져오기');
    // 파일 업로드 로직 추가
  };

  const props = defineProps({
	remain: Number
})
  console.log(props);

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
  