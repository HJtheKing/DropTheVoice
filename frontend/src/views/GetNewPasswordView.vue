<template>
  <v-dialog v-model="dialog" persistent max-width="500px">
    <v-card>
      <v-card-title>
        <span class="headline">회원 아이디 조회</span>
      </v-card-title>
      <v-card-text>
        <v-form ref="memberNameSearchForm">
          <v-text-field
            v-model="memberName"
            label="회원 아이디"
            type="name"
            required
          ></v-text-field>
          <v-text-field
            v-model="memberEmail"
            label="이메일 주소"
            type="email"
            required
          ></v-text-field>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text @click="dialog = false">
          취소
        </v-btn>
        <v-btn color="blue darken-1" text @click="getNewPassword">
          조회
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref } from 'vue';
import axios from "axios";

const dialog = ref(false);
const memberName = ref('');
const memberEmail = ref('');

const validateEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

const getNewPassword = () => {
  if (!memberEmail.value || !memberName.value) {
    alert('모든 필드를 입력해주세요.');
    return;
  }

  if (!validateEmail(memberEmail.value)) {
    alert('유효한 이메일 주소가 아닙니다.');
    return;
  }

  // 이메일을 백엔드로 전송하여 아이디 조회
  axios.post(`${import.meta.env.VITE_BASE_URL}/api-member/getNewPassword`, {
    memberName: memberName.value,
    memberEmail: memberEmail.value,
  })
    .then(() => {
      alert('해당 이메일 주소로 임시 비밀번호를 발송하였습니다.');
      dialog.value = false; // 모달 닫기
      memberEmail.value = ''; // 입력 필드 초기화
    })
    .catch(() => {
      alert('회원 조회에 실패했습니다');
    });
};


const openDialog = () => {
  dialog.value = true;
};

const closeDialog = () => {
  dialog.value = false;
};

defineExpose({
  openDialog,
  closeDialog,
});
</script>

<style scoped>
.headline {
  font-size: 20px;
  font-weight: bold;
}
</style>
