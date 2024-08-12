<template>
  <WebRtcSendButton/>
  <v-app class="bg-black">
    <v-container class="login-container" max-width="600px">
      <v-form @submit.prevent="login">
        <v-card class="dark-card">
          <v-card-title class="text-left">
            <h1>로그인</h1>
          </v-card-title>
          <v-card-text>
            <v-text-field
            v-model="id"
            label="아이디"
            placeholder="아이디"
            outlined
            class="white-placeholder"
            ></v-text-field>
            <v-text-field
              v-model="pw"
              label="비밀번호"
              placeholder="비밀번호"
              type="password"
              outlined
              class="white-placeholder"
            ></v-text-field>
            <v-checkbox
              v-model="rememberMe"
              label="아이디 저장"
              hide-details
              class="white-placeholder"
            ></v-checkbox>
            <v-btn block color="primary" type="submit" class="mt-3">
              로그인
            </v-btn>
            <div class="social-login">
              <v-btn block color="#1ec800" @click="naverLogin" class="social-button">
                <img src="@/assets/images/icon-naver.png" alt="Naver Login" />
                네이버로 로그인
              </v-btn>
              <v-btn block color="#fee500" @click="kakaoLogin" class="social-button">
                <img src="@/assets/images/icon-kakao.png" alt="Kakao Login" />
                카카오로 로그인
              </v-btn>
            </div>
          </v-card-text>
          <v-card-actions class="footer-links">
            <v-row justify="center" no-gutters>
              <v-col class="text-center" cols="auto">
                <a href="#">아이디 찾기</a>
              </v-col>
              <v-col class="text-center" cols="auto">
                <span>|</span>
              </v-col>
              <v-col class="text-center" cols="auto">
                <a href="#">비밀번호 찾기</a>
              </v-col>
              <v-col class="text-center" cols="auto">
                <span>|</span>
              </v-col>
              <v-col class="text-center" cols="auto">
                <RouterLink :to="{ name: 'signup' }">회원가입</RouterLink>
              </v-col>
            </v-row>
          </v-card-actions>
        </v-card>
      </v-form>
    </v-container>
  </v-app>
</template>
<script setup>
import { ref } from "vue";
import { useUserStore } from "@/store/user";
import WebRtcSendButton from "@/components/spread/WebRtcSendButton.vue";

const store = useUserStore();
const id = ref("");
const pw = ref("");
const rememberMe = ref(false);

const login = () => {``
  store.userLogin(id.value, pw.value);
};

const naverLogin = () => {
  // Handle Naver login
};

const kakaoLogin = () => {
  // Handle Kakao login
};
</script>
<style scoped>
.login-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.v-btn {
  margin-top: 10px;
  color: white;
}

.social-login .v-btn {
  display: flex;
  align-items: center;
  justify-content: center;
}

.social-login img {
  margin-right: 5px;
  width: 20px;
  height: 20px;
}

.footer-links a {
  text-decoration: none;
  color: #fff;
  margin: 0 5px;
}

.dark-card {
  background-color: #1e1e1e;
  color: #fff;
}

.white-placeholder .v-input__control .v-input__slot::before {
  border-color: #fff;
}

.white-placeholder .v-input__control .v-input__slot::after {
  border-color: #fff;
}

.v-card-title,
.v-card-text {
  color: #fff;
}

.v-card-actions .v-row {
  margin: 0;
  padding: 0;
}
</style>
