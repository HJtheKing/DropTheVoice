<template>
  <v-app class="bg-black">
    <v-container class="signup-container" max-width="600px">
      <v-form ref="form" v-model="isFormValid" @submit.prevent="submitForm">
        <v-card class="dark-card">
          <v-card-title class="text-left">
            <h1>회원가입</h1>
          </v-card-title>
          <v-card-text>
            <p>가입을 통해 다양한 서비스를 만나보세요!</p>
            <v-row class="input-group">
              <v-col cols="12" sm="9">
                <v-text-field
                  v-model="userId"
                  :rules="[rules.required, rules.min(6), rules.max(20)]"
                  placeholder="아이디 입력 (6 ~ 20자)"
                  outlined
                  class="white-placeholder"
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="3" class="d-flex align-center">
                <v-btn class="check-button" @click="checkDuplicate" height="40px">중복 확인</v-btn>
              </v-col>
            </v-row>
            <v-text-field
              v-model="password"
              :rules="[rules.required, rules.min(8), rules.max(20), rules.password]"
              placeholder="비밀번호 입력 (특수문자 포함 8~20자)"
              type="password"
              outlined
              class="mb-3 white-placeholder"
            ></v-text-field>
            <v-text-field
              v-model="confirmPassword"
              :rules="[rules.required, rules.matchPassword]"
              placeholder="비밀번호 재입력"
              type="password"
              outlined
              class="mb-3 white-placeholder"
            ></v-text-field>
            <v-row class="input-group">
              <v-col cols="12" sm="6">
                <v-text-field
                  v-model="email"
                  :rules="[rules.required, rules.email]"
                  placeholder="이메일 주소"
                  outlined
                  class="white-placeholder"
                ></v-text-field>
              </v-col>
              <v-col cols="1" class="d-flex align-center justify-center">
                <span>@</span>
              </v-col>
              <v-col cols="12" sm="5">
                <v-select
                  v-model="emailDomain"
                  :items="emailDomains"
                  outlined
                ></v-select>
              </v-col>
            </v-row>
            <v-text-field
              v-if="emailDomain === '직접 입력'"
              v-model="customEmailDomain"
              :rules="[rules.required, rules.customEmail]"
              placeholder="직접 입력"
              outlined
              class="mb-3 white-placeholder"
            ></v-text-field>
            <v-text-field
              v-model="phone"
              :rules="[rules.required, rules.phone]"
              placeholder="휴대폰 번호 입력('-' 제외 11자리 입력)"
              outlined
              class="mb-3 white-placeholder"
            ></v-text-field>
            <v-btn block color="primary" type="submit" class="mt-3">가입하기</v-btn>
          </v-card-text>
        </v-card>
      </v-form>
    </v-container>
  </v-app>
</template>

<script setup>
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

// 폼 입력 값 및 상태 관리
const userId = ref("");
const password = ref("");
const confirmPassword = ref("");
const email = ref("");
const emailDomain = ref("naver.com");
const customEmailDomain = ref("");
const phone = ref("");
const form = ref(null);
const router = useRouter();
const isFormValid = ref(false);  // 폼 유효성 상태를 관리하는 변수 추가

// 이메일 도메인 선택지
const emailDomains = ["naver.com", "gmail.com", "daum.net", "직접 입력"];

// 유효성 검사 규칙
const rules = {
  required: (value) => !!value || "필수 입력 항목입니다.",
  min: (min) => (value) => value.length >= min || `${min}자 이상 입력하세요.`,
  max: (max) => (value) => value.length <= max || `${max}자 이하로 입력하세요.`,
  customEmail: (value) => /.+\..+/.test(value) || "유효한 도메인을 입력하세요.",
  phone: (value) => /^\d{11}$/.test(value) || "휴대폰 번호를 정확히 입력하세요.",
  password: (value) => /(?=.*\W)/.test(value) || "특수문자를 포함해야 합니다.",
  matchPassword: (value) => value === password.value || "비밀번호가 일치하지 않습니다.",
};

// 아이디 중복 확인 상태
const duplicateCheck = ref(false);

// 아이디 중복 확인 함수
const checkDuplicate = async () => {
  if (!userId.value) {
    alert('아이디를 입력해주세요.');
    return;
  }
  try {
    const response = await axios.post('http://localhost:8080/api-member/check-duplicate', { memberName: userId.value });
    if (response.data === "fail") {
      alert('아이디가 이미 존재합니다.');
    } else if (response.data === "success" && userId.value.length >= 6) {
      duplicateCheck.value = true;
      alert('사용 가능한 아이디입니다.');
    } else {
      alert('유효한 아이디가 아닙니다.');
    }
  } catch (error) {
    alert('아이디가 이미 존재합니다.');
  }
};

// 폼 제출 함수
const submitForm = async () => {
  // 폼의 모든 입력 값이 유효한지 확인
  if (isFormValid.value && duplicateCheck.value) {
    try {
      const response = await axios.post('http://localhost:8080/api-member/register', {
        memberEmail: `${email.value}@${emailDomain.value === '직접 입력' ? customEmailDomain.value : emailDomain.value}`,
        memberName: userId.value,
        memberPassword: password.value,
        profileImgUrl: '',
      });
      if (response.data) {
        alert('가입이 완료되었습니다.');
        router.push('login');
      } else {
        alert('가입에 실패했습니다.');
      }
    } catch (error) {
      alert('가입 중 오류가 발생했습니다.');
    }
  } else if (!isFormValid.value) {
    alert('입력된 정보를 확인해주세요.');
  } else {
    alert('아이디 중복을 확인해주세요.');
  }
};

// 아이디가 변경되면 중복 확인 상태를 초기화
watch(userId, () => {
  duplicateCheck.value = false;
});
</script>


<style scoped>

.signup-container {
  max-width: 600px;
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
  color: #b0b0b0;
}

select {
  font-size: 14px;
  color: #fff;
}

.white-placeholder .v-input__control .v-input__slot::before {
  border-color: #fff;
}

.white-placeholder .v-input__control .v-input__slot::after {
  border-color: #fff;
}

.dark-card {
  background-color: #1e1e1e;
  color: #fff;
}

.v-card-title,
.v-card-text {
  color: #fff;
}

@media (max-width: 600px) {
  .signup-container {
    padding: 10px;
  }

  .check-button {
    width: 100%;
  }
}
</style>
