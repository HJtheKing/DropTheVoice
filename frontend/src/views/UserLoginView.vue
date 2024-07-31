<template>
  <v-container class="login-container" max-width="400px">
    <v-form>
      <v-card>
        <v-card-title class="text-left">
          <h2>로그인</h2>
        </v-card-title>
        <v-card-text>
          <v-text-field
            v-model="id"
            label="아이디"
            placeholder="아이디"
            outlined
          ></v-text-field>
          <v-text-field
            v-model="pw"
            label="비밀번호"
            placeholder="비밀번호"
            type="password"
            outlined
          ></v-text-field>
          <v-checkbox
            v-model="rememberMe"
            label="아이디 저장"
            hide-details
          ></v-checkbox>
          <v-btn block color="primary" @click="login">
            로그인
          </v-btn>
          <div class="social-login">
            <v-btn block color="#1ec800" @click="naverLogin">
              <img src="@/assets/images/icon-naver.png" alt="Naver Login" />
              네이버로 로그인
            </v-btn>
            <v-btn block color="#fee500" @click="kakaoLogin">
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
</template>

<script setup>
import { ref } from "vue";
import { useUserStore } from "@/store/user";

const store = useUserStore();
const id = ref("");
const pw = ref("");
const rememberMe = ref(false);

const login = () => {
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
  max-width: 400px;
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
  color: black;
  margin: 0 5px;
}
</style>
