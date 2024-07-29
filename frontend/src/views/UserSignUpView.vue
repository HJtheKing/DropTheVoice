<template>
  <div class="signup-container">
    <h2>회원가입</h2>
    <p>가입을 통해 더 다양한 서비스를 만나보세요!</p>
    <form>
      <div class="input-group">
        <input type="text" placeholder="아이디 입력 (6 ~ 20자)"/>
        <button type="button" class="check-button">중복 확인</button>
      </div>
      <div class="input-group">
        <input type="password" placeholder="비밀번호 입력 (특수문자 포함 8~20자)" />
      </div>
      <div class="input-group">
        <input type="password" placeholder="비밀번호 재입력" />
      </div>
      <div class="input-group">
        <input type="email" placeholder="이메일 주소" />
        <span>@</span>
        <select>
          <option value="naver.com">naver.com</option>
          <option value="gmail.com">gmail.com</option>
          <option value="daum.net">daum.net</option>
          <option value="daum.net">직접 입력</option>
        </select>
      </div>
      <div class="input-group">
        <input type="tel" placeholder="휴대폰 번호 입력('-' 제외 11자리 입력)" />
      </div>
      <button type="button" class="submit-button">가입하기</button>
    </form>
  </div>
</template>

<script>
import { ref } from "vue";
import { useUserStore } from "@/store/user";

export default {
  setup() {
    
    const store = useUserStore();
    const id = ref("");
    const pw = ref("");

    const login = () => {
      store.userLogin(id.value, pw.value);
    };

    return {
      id,
      pw,
      login
    };
  }
};
</script>

<style scoped>
.signup-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

h2 {
  text-align: left;
  margin-bottom: 10px;
}

p {
  text-align: left;
  margin-bottom: 20px;
}

.input-group {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  position: relative;
}

input[type="text"],
input[type="password"],
input[type="email"],
input[type="tel"] {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 4px;
}

input.input-with-button {
  padding-right: 90px; /* Adjust padding to accommodate the button */
}

.check-button {
  position: absolute;
  right: 0;
  top: 0;
  height: 100%;
  padding: 8px;
  background-color: #8a2be2;
  color: white;
  border: none;
  cursor: pointer;
  font-size: 14px;
  border-top-right-radius: 4px;
  border-bottom-right-radius: 4px;
}

select {
  width: 150px;
  padding: 8px;
  box-sizing: border-box;
  margin-left: 5px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

span {
  margin-left: 5px;
}

.submit-button {
  width: 100%;
  padding: 10px;
  background-color: rgb(55, 53, 53);
  color: white;
  border: none;
  cursor: pointer;
  font-size: 16px;
  border-radius: 4px;
}

input::placeholder {
  font-size: 14px;
}

select {
  font-size: 14px;
}
</style>
