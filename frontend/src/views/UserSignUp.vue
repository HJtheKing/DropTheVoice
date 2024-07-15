<template>
  <div class="signup-container">
    <h4>회원가입</h4>
    <p>We ship within 2 working days</p>
    <div class="input-group">
      <label for="username">이름</label>
      <input type="text" id="username" placeholder="이름" />
    </div>
    <div class="input-group">
      <label for="id">아이디</label>
      <input type="text" id="id" placeholder="아이디" />
    </div>
    <div class="input-group">
      <label for="password">비밀번호</label>
      <input type="password" id="password" placeholder="비밀번호" />
    </div>
    <div class="input-group">
      <label for="passwordCheck">비밀번호 확인</label>
      <input type="password" id="passwordCheck" placeholder="비밀번호 확인" />
    </div>
    <button class="signup-button">회원가입</button>
  </div>
</template>

<style>
.signup-container {
  width: 300px;
  padding: 20px;
  background-color: white;
  border-radius: 10px;
  text-align: center;
  font-family: Arial, sans-serif;
}

.signup-container h4 {
  font-size: 24px;
  margin-bottom: 10px;
}

.signup-container p {
  font-size: 14px;
  color: #888;
  margin-bottom: 20px;
}

.input-group {
  margin-bottom: 15px;
  text-align: left;
}

.input-group label {
  display: block;
  font-size: 14px;
  margin-bottom: 5px;
}

input[type="text"],
input[type="password"] {
  width: 100%;
  padding: 10px;
  margin: 5px 0;
  box-sizing: border-box;
  border: 2px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
}

.signup-button {
  width: 100%;
  padding: 10px;
  background-color: #333;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
}

.signup-button:hover {
  background-color: #555;
}
</style>
