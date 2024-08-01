<template>
  <v-container class="profile-container">
    <v-card>
      <v-card-title class="profile-header">
        <v-avatar size="50">
          <img src="@/assets/images/user.png" alt="Profile Image" class="profile-image" />
        </v-avatar>
        <div class="profile-info">
          <h2>김싸피</h2>
          <p>@a505</p>
        </div>
        <v-avatar size="50">
          <img src="@/assets/images/goldtier.png" alt="Badge" class="badge-image" />
        </v-avatar>
      </v-card-title>
      <v-card-subtitle class="profile-stats">
        <p>음성 업로드 수 :</p>
        <p>음성 확산 수 :</p>
      </v-card-subtitle>
      <v-divider></v-divider>
      <v-card-text class="profile-links">
        <v-list>
          <v-subheader>문의</v-subheader>
          <v-divider class="thick-divider"></v-divider>
          <v-list-item @click="navigate('customer-service')">
            <v-list-item-title>고객센터</v-list-item-title>
          </v-list-item>
          <v-list-item @click="navigate('privacy-policy')">
            <v-list-item-title>개인 정보 처리 방침</v-list-item-title>
          </v-list-item>
        </v-list>
        <v-list>
          <v-subheader>회원정보 변경</v-subheader>
          <v-divider class="thick-divider"></v-divider>
          <v-list-item @click="navigate('password-change')">
            <v-list-item-title>비밀번호 변경</v-list-item-title>
          </v-list-item>
          <v-list-item @click="navigate('account-deletion')">
            <v-list-item-title>회원 탈퇴</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-card-text>
    </v-card>
  </v-container>
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
  max-width: 400px;
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
  border-radius: 50%;
}

.profile-stats {
  background-color: #2e004b;
  color: white;
  padding: 20px;
  font-size: 14px;
  text-align: left;
}

.profile-links {
  background-color: white;
  color: black;
  padding: 20px;
}

.v-subheader {
  margin: 0;
  font-size: 16px;
}

.v-list-item {
  padding: 10px 0;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.v-list-item:hover {
  background-color: #f0f0f0;
}

.v-list-item-title {
  font-size: 14px;
}

.thick-divider {
  height: 2px;
  background-color: #000;
  margin: 10px 0;
}
</style>
