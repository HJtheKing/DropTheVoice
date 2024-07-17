<template>
  <div class="login-container">
    <h2>로그인</h2>
    <form>
      <div class="input-group">
        <label for="username">아이디</label>
        <input type="text" id="username" placeholder="아이디" />
      </div>
      <div class="input-group">
        <label for="password">비밀번호</label>
        <input type="password" id="password" placeholder="비밀번호" />
      </div>
      <div class="remember-me">
        <input type="checkbox" id="remember" />
        <label for="remember">아이디 저장</label>
      </div>
      <button type="button">로그인</button>
      <div class="social-login">
        <button class="naver-login">
          <img src="@/assets/images/icon-naver.png" alt="Naver Login" />
          네이버로 로그인
        </button>
        <button class="kakao-login">
          <img src="@/assets/images/icon-kakao.png" alt="Kakao Login" />
          카카오로 로그인
        </button>
      </div>
    </form>
    <div class="footer-links">
      <a href="#">아이디 찾기</a>
      <span>|</span>
      <a href="#">비밀번호 찾기</a>
      <span>|</span>
      <RouterLink :to="{ name: 'signup' }">회원가입</RouterLink>
    </div>
  </div>
</template>

<script>
export default {
};
</script>

<style scoped>
/* 로그인 컨테이너의 스타일을 설정 */
.login-container {
  max-width: 400px; /* 최대 폭을 300px에서 400px로 변경 */
  margin: 0 auto; /* 중앙 정렬 */
  padding: 20px; /* 내부 패딩 */
  font-family: Arial, sans-serif; /* 폰트 설정 */
}

/* h2 태그의 스타일을 설정 */
h2 {
  text-align: left; /* 가운데 정렬 */
  margin-bottom: 20px; /* 하단 여백 */
}

/* input 그룹의 스타일을 설정 */
.input-group {
  margin-bottom: 15px; /* 하단 여백 */
}

/* label 태그의 스타일을 설정 */
label {
  display: block; /* 블록 요소로 설정 */
  margin-bottom: 5px; /* 하단 여백 */
}

/* 텍스트 입력 필드의 스타일을 설정 */
input[type="text"],
input[type="password"] {
  width: 100%; /* 너비 100% */
  padding: 8px; /* 내부 패딩 */
  border: 1px solid #ccc;
  box-sizing: border-box; /* 박스 크기 설정 */
}

/* 아이디 저장 체크박스의 스타일을 설정 */
.remember-me {
  display: flex; /* 플렉스 박스로 설정 */
  align-items: center; /* 아이템을 가운데 정렬 */
  margin-bottom: 20px; /* 하단 여백 */
}

.remember-me input {
  margin-right: 5px; /* 오른쪽 여백 */
}

/* 로그인 버튼의 스타일을 설정 */
button[type="button"] {
  width: 100%; /* 너비 100% */
  padding: 10px; /* 내부 패딩 */
  background-color: rgb(55, 53, 53); /* 배경색 검정 */
  color: white; /* 글자색 흰색 */
  border: none; /* 테두리 없음 */
  cursor: pointer; /* 커서 포인터 */
}

/* 소셜 로그인 버튼 그룹의 스타일을 설정 */
.social-login {
  display: flex; /* 플렉스 박스로 설정 */
  justify-content: space-between; /* 공간을 고르게 분배 */
  margin-top: 20px; /* 상단 여백 */
}

/* 네이버 로그인 버튼의 스타일을 설정 */
.naver-login,
.kakao-login {
  display: flex; /* 플렉스 박스로 설정 */
  align-items: center; /* 아이템을 가운데 정렬 */
  padding: 10px; /* 내부 패딩 */
  border: none; /* 테두리 없음 */
  cursor: pointer; /* 커서 포인터 */
  width: 48%; /* 너비 48% */
}

.naver-login {
  background-color: #1ec800; /* 배경색 네이버 녹색 */
  color: white; /* 글자색 흰색 */
}

/* 카카오 로그인 버튼의 스타일을 설정 */
.kakao-login {
  background-color: #fee500; /* 배경색 카카오 노랑 */
  color: black; /* 글자색 검정 */
}

/* 소셜 로그인 이미지의 스타일을 설정 */
.social-login img {
  margin-right: 5px; /* 오른쪽 여백 */
  width: 20px; /* 너비 20px */
  height: 20px; /* 높이 20px */
}

/* 하단 링크의 스타일을 설정 */
.footer-links {
  text-align: center; /* 가운데 정렬 */
  margin-top: 20px; /* 상단 여백 */
}

.footer-links a {
  text-decoration: none; /* 밑줄 없음 */
  color: black; /* 글자색 검정 */
  margin: 0 5px; /* 좌우 여백 */
}

.footer-links span {
  margin: 0 5px; /* 좌우 여백 */
}
</style>
