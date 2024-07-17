<template>
  <div class="profile-container">
    <div class="profile-header">
      <img src="@/assets/images/user.png" alt="Profile Image" class="profile-image" />
      <div class="profile-info">
        <h2>김싸피</h2>
        <p>@a505</p>
      </div>
      <img src="@/assets/images/goldtier.png" alt="Badge" class="badge-image" />
    </div>
    <div class="profile-stats">
      <p>음성 업로드 수 :</p>
      <p>음성 확산 수 :</p>
    </div>
    <div class="profile-links">
      <div class="service">
        <h3>문의</h3>
        <div class="thick-divider"></div>
        <div class="menu-item" @click="navigate('customer-service')">
          <span>고객센터</span>
        </div>
        <div class="menu-item" @click="navigate('privacy-policy')">
          <span>개인 정보 처리 방침</span>
        </div>
      </div>
      <br>
      <div class="service">
        <h3>회원정보 변경</h3>
        <div class="thick-divider"></div>
        <div class="menu-item" @click="navigate('password-change')">
          <span>비밀번호 변경</span>
        </div>
        <div class="menu-item" @click="navigate('account-deletion')">
          <span>회원 탈퇴</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    navigate(route) {
      // Navigation logic here
      console.log(`Navigate to ${route}`);
    }
  }
};
</script>

<style scoped>
.profile-container {
  width: 400px;
  margin: 0 auto;
  font-family: Arial, sans-serif;
}

.profile-header {
  background-color: #2e004b;
  color: white;
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.profile-image {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}

.profile-info {
  flex-grow: 1;
  margin-left: 10px;
}

.profile-info h2 {
  margin: 0;
  font-size: 18px;
}

.profile-info p {
  margin: 0;
  font-size: 14px;
  color: #ccc;
}

.badge-image {
  width: 50px;
  height: 50px;
}

.profile-stats {
  background-color: #2e004b;
  color: white;
  padding: 20px;
  font-size: 14px;
}

.profile-links {
  background-color: white;
  color: black;
  padding: 20px;
}

.profile-links h3 {
  margin: 0 0 10px 0;
  font-size: 16px;
}

.menu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.menu-item:hover {
  background-color: #f0f0f0;
}

.menu-item span {
  font-size: 14px;
}

.thick-divider {
  height: 2px;
  background-color: #000;
  margin: 10px 0;
}
</style>
