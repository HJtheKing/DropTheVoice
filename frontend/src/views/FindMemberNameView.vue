<template>
  <v-dialog v-model="dialog" persistent max-width="500px">
    <v-card>
      <v-card-title>
        <span class="headline">회원 아이디 조회</span>
      </v-card-title>
      <v-card-text>
        <v-form ref="memberNameSearchForm">
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
        <v-btn color="blue darken-1" text @click="changePassword">
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
const memberEmail = ref('');

const validateEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

const changePassword = () => {
  if (!memberEmail.value) {
    alert('이메일 주소를 입력해주세요.');
    return;
  }

  if (!validateEmail(memberEmail.value)) {
    alert('유효한 이메일 주소가 아닙니다.');
    return;
  }

  // 이메일을 백엔드로 전송하여 아이디 조회
  axios.post(`${import.meta.env.VITE_BASE_URL}/api-member/findMemberId`, {
    memberEmail: memberEmail.value,
  })
    .then((response) => {
      const memberName = response.data.memberName; // 서버에서 memberId를 반환한다고 가정
      alert('회원님의 아이디는 ' + memberName + ' 입니다');
      dialog.value = false; // 모달 닫기
      memberEmail.value = ''; // 입력 필드 초기화
    })
    .catch((error) => {
      alert('아이디 조회에 실패했습니다: ' + error.message);
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
