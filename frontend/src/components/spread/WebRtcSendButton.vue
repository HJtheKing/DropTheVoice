<template>
  <v-btn id="sendFileBtn" @click="triggerFileInput" class="action-button" color="grey-darken-3" rounded large>
    <div class="button-content">
      <img src="@/assets/upload-icon.png" alt="업로드 아이콘" class="icon" />
      <span>목록에서 가져오기</span>
    </div>
  </v-btn>
  <input id="file-upload" type="file" @change="uploadFile" ref="fileInput" />
</template>

<script setup>
import { useStore } from 'vuex';
import { useUserStore } from '@/store/user';

import { ref, onMounted, watch } from 'vue';
const fileInput = ref(null);

const store = useStore();

const triggerFileInput = () => {
  fileInput.value.click();
};


const uploadFile = (event) => {
  const files = event.target.files;
  if (files && files.length > 0) {
    const file = files[0];
    store.dispatch('sendFile',file);
    //여기서 stompClient를 직접 받는게 아니라,
    //index.js 내부에 파일을 전달해주자.
    // 파일 처리 로직을 여기에 추가하세요
  } else {
    console.error('파일 선택 오류: 파일이 선택되지 않았습니다.');
  }
  fileInput.value.value = ""; // 추가된 부분
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
  display: none;
  /* 숨겨진 파일 입력 요소 */
}
</style>
