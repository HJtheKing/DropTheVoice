<template>
  <v-dialog v-model="dialog" persistent max-width="500px">
    <v-card>
      <v-card-title>
        <span class="headline">비밀번호 변경</span>
      </v-card-title>
      <v-card-text>
        <v-form ref="passwordChangeForm">
          <v-text-field
            v-model="currentPassword"
            label="기존 비밀번호"
            type="password"
            required
          ></v-text-field>
          <v-text-field
            v-model="newPassword"
            label="새 비밀번호"
            type="password"
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
          변경
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref } from 'vue';
import { useUserStore } from "@/store/user";

const userStore = useUserStore();
const dialog = ref(false);
const currentPassword = ref('');
const newPassword = ref('');

const validatePassword = (password) => {
  const passwordRegex = /^(?=.*[!@#$%^&*(),.?":{}|<>])[A-Za-z\d!@#$%^&*(),.?":{}|<>]{8,20}$/;
  return passwordRegex.test(password);
};

const changePassword = () => {
  if (!currentPassword.value || !newPassword.value) {
    alert('모든 필드를 입력해주세요.');
    return;
  }

  if (!validatePassword(newPassword.value)) {
    alert('새 비밀번호는 특수문자를 포함하여 8~20자여야 합니다.');
    return;
  }

  userStore.changePassword(currentPassword.value, newPassword.value)
    .then(() => {
      alert('비밀번호가 변경되었습니다.');
      dialog.value = false;
      currentPassword.value = '';
      newPassword.value = '';
    }).catch((error) => {
      alert('비밀번호 변경에 실패했습니다: ' + error.message);
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
