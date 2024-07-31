<template>
  <v-container class="signup-container" max-width="400px">
    <v-form>
      <v-card>
        <v-card-title class="text-left">
          <h2>회원가입</h2>
        </v-card-title>
        <v-card-text>
          <p>가입을 통해 더 다양한 서비스를 만나보세요!</p>
          <v-row class="input-group ma-1">
            <v-col cols="9">
              <v-text-field
                v-model="userId"
                :rules="[rules.required, rules.min(6), rules.max(20)]"
                placeholder="아이디 입력 (6 ~ 20자)"
                outlined
              ></v-text-field>
            </v-col>
            <v-col cols="3" class="d-flex align-center">
              <v-btn class="check-button" @click="checkDuplicate" height="40px">중복 확인</v-btn>
            </v-col>
          </v-row>
          <v-text-field
            v-model="password"
            :rules="[rules.required, rules.min(8), rules.max(20), rules.password]"
            placeholder="비밀번호 입력 (특수문자 포함 8~20자)"
            type="password"
            outlined
          ></v-text-field>
          <v-text-field
            v-model="confirmPassword"
            :rules="[rules.required, rules.matchPassword]"
            placeholder="비밀번호 재입력"
            type="password"
            outlined
          ></v-text-field>
          <v-row class="input-group ma-1">
            <v-col cols="6">
              <v-text-field
                v-model="email"
                :rules="[rules.required, rules.email]"
                placeholder="이메일 주소"
                outlined
              ></v-text-field>
            </v-col>
            <v-col cols="1" class="d-flex align-center justify-center">
              <span>@</span>
            </v-col>
            <v-col cols="5">
              <v-select
                v-model="emailDomain"
                :items="emailDomains"
                outlined
              ></v-select>
            </v-col>
          </v-row>
          <v-text-field
            v-model="phone"
            :rules="[rules.required, rules.phone]"
            placeholder="휴대폰 번호 입력('-' 제외 11자리 입력)"
            outlined
          ></v-text-field>
          <v-btn block color="primary" @click="submitForm">가입하기</v-btn>
        </v-card-text>
      </v-card>
    </v-form>
  </v-container>
</template>

<script setup>
import { ref } from 'vue';

const userId = ref("");
const password = ref("");
const confirmPassword = ref("");
const email = ref("");
const emailDomain = ref("naver.com");
const phone = ref("");

const emailDomains = ["naver.com", "gmail.com", "daum.net", "직접 입력"];

const rules = {
  required: (value) => !!value || "필수 입력 항목입니다.",
  min: (min) => (value) => value.length >= min || `${min}자 이상 입력하세요.`,
  max: (max) => (value) => value.length <= max || `${max}자 이하로 입력하세요.`,
  email: (value) => /.+@.+\..+/.test(value) || "유효한 이메일 주소를 입력하세요.",
  phone: (value) => /^\d{11}$/.test(value) || "휴대폰 번호를 정확히 입력하세요.",
  password: (value) => /(?=.*\W)/.test(value) || "특수문자를 포함해야 합니다.",
  matchPassword: (value) => value === password.value || "비밀번호가 일치하지 않습니다.",
};

const checkDuplicate = () => {
  // 아이디 중복 확인 로직
  alert("아이디 중복 확인");
};

const submitForm = () => {
  // 가입하기 버튼 클릭 시 폼 제출 로직
  alert("가입하기");
};
</script>

<style scoped>
.signup-container {
  max-width: 500px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.v-btn {
  margin-top: 10px;
  color: white;
}

.input-group {
  margin-bottom: 10px;
}

.check-button {
  background-color: #8a2be2;
  color: white;
  font-size: 14px;
}

input::placeholder {
  font-size: 14px;
}

select {
  font-size: 14px;
}
</style>
