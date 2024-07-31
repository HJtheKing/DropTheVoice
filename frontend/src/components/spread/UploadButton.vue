<template>
    <v-btn class="action-button" @click="triggerFileInput" color="primary" rounded large>
    <div class="button-content">
      <img src="@/assets/upload-icon.png" alt="업로드 아이콘" class="icon" />
      <span>목록에서 가져오기</span>
    </div>
  </v-btn>
  <input id="file-upload" type="file" @change="uploadFile" ref="fileInput" />
</template>

<script setup>
import { ref } from 'vue';
const fileInput = ref(null);

const triggerFileInput = () => {
  fileInput.value.click();
};

const uploadFile = (event) => {
  const files = event.target.files;
  if (files && files.length > 0) {
    const file = files[0];
    console.log('파일 선택됨:', file);
    // 파일 처리 로직을 여기에 추가하세요
  } else {
    console.error('파일 선택 오류: 파일이 선택되지 않았습니다.');
  }
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

input[type="file"] {
  display: none; /* 숨겨진 파일 입력 요소 */
}
</style>
