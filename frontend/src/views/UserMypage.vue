<template>
  <div class="container">
    <div class="header">마이페이지</div>
    <div class="profile">
      <div class="profile-image">
        <img src="https://via.placeholder.com/50" alt="Profile Picture">
      </div>
      <div class="profile-details">
        <strong>김싸피</strong>
        <span>@a505</span>
      </div>
    </div>
    <div class="statistics">
      <div>음성 업로드 수 :</div>
      <div>음성 확산 수 :</div>
    </div>
    <div class="menu-item" @click="navigate('password')">
      <span>비밀번호 수정</span>
      <i class="fas fa-chevron-right arrow"></i>
    </div>
    <div class="menu-item" @click="navigate('customer-center')">
      <span>고객센터</span>
      <i class="fas fa-chevron-right arrow"></i>
    </div>
    <div class="menu-item" @click="navigate('privacy-policy')">
      <span>개인 정보 처리 방침</span>
      <i class="fas fa-chevron-right arrow"></i>
    </div>
    <div class="menu-item" @click="navigate('leave-membership')">
      <span>회원탈퇴</span>
      <i class="fas fa-chevron-right arrow"></i>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MyPage',
  methods: {
    navigate(page) {
      console.log(`Navigating to ${page}`);
      // Implement navigation logic here
    }
  }
}
</script>

<style scoped>
body {
  font-family: 'Arial', sans-serif;
}
.container {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
}
.header {
  text-align: center;
  font-size: 24px;
  padding: 10px 0;
  border-bottom: 1px solid #ddd;
}
.profile {
  display: flex;
  align-items: center;
  padding: 20px 0;
  border-bottom: 1px solid #ddd;
}
.profile-image {
  margin-right: 10px;
}
.profile-image img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}
.profile-details {
  font-size: 18px;
}
.profile-details span {
  display: block;
  color: #666;
}
.statistics {
  padding: 20px 0;
  border-bottom: 1px solid #ddd;
}
.menu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #ddd;
  font-size: 18px;
  color: #333;
  transition: background-color 0.3s;
}
.menu-item:last-child {
  border-bottom: none;
}
.menu-item:hover {
  background-color: #f0f0f0;
}
.menu-item i {
  color: #FFC107;
}
@media (max-width: 600px) {
  .container {
    padding: 10px;
  }
  .header {
    font-size: 20px;
  }
  .profile-details {
    font-size: 16px;
  }
  .menu-item {
    font-size: 16px;
  }
}
</style>
