<template>
  <v-app class="bg-black">
    <v-main>
      <v-container class="custom-container">
    <v-row justify="center" class="py-4">
      <v-col cols="12" class="text-center">
      <h1 class="title">마이페이지</h1>
      </v-col>
    </v-row>
    <v-card-title class="profile-header">
      <v-avatar size="50">
        <img src="@/assets/images/user.png" alt="Profile Image" class="profile-image" />
      </v-avatar>
      <div class="profile-info">
        <h2>{{ member.userNickname }}</h2>
        <p>{{ member.email }}</p>
      </div>
      <v-avatar size="50">
        <img src="@/assets/images/goldtier.png" alt="Badge" class="badge-image" />
      </v-avatar>
    </v-card-title>
    <v-card-subtitle class="profile-stats">
      <p>음성 업로드 수 : {{ member.uploadCount }}</p>
      <p>음성 확산 수 : {{ member.spreadCount }}</p>
    </v-card-subtitle>
    <v-divider></v-divider>
    <v-card-text class="profile-links">
      <v-list>
        <v-subheader class="subheader">문의</v-subheader>
        <v-divider class="thick-divider"></v-divider>
        <v-list-item @click="navigateTo('customer-center')">
          <v-list-item-title>고객센터</v-list-item-title>
        </v-list-item>
        <v-list-item @click="navigateTo('privacy-policy')">
          <v-list-item-title>개인 정보 처리 방침</v-list-item-title>
        </v-list-item>
      </v-list>
      <v-list>
        <v-subheader class="subheader">회원정보 변경</v-subheader>
        <v-divider class="thick-divider"></v-divider>
        <v-list-item @click="navigateTo('password-change')">
          <v-list-item-title>비밀번호 변경</v-list-item-title>
        </v-list-item>
        <v-list-item @click="navigateTo('account-deletion')">
          <v-list-item-title>회원 탈퇴</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-card-text>
   </v-container>
    </v-main>
  </v-app>
</template>

<script>
import axios from 'axios';
export default {
  
  data() {
    return {
      member: {
        profileImage: '',
        userNickname: '김싸피',
        email: 'a505@example.com',
        uploadCount: 0,
        spreadCount: 0,
      },
    };
  },
  methods: {
    navigateTo(routeName) {
      this.$router.push({ name: routeName });
    },
  },
};
</script>

<style scoped>
.title {
  color: #fff;
  font-weight: bold;
  font-size: 24px;
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
  object-fit: contain;
  width: 100%;
  height: 100%;
  border-color: white;
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
  object-fit: contain;
  width: 100%;
  height: 100%;
}
.profile-stats {
  background-color: #2e004b;
  color: white;
  padding: 20px;
  font-size: 14px;
  text-align: left;
}
.profile-links {
  background-color: #1e1e1e;
  color: white;
  padding: 20px;
}
.subheader {
  margin: 0;
  font-size: 16px;
  color: #fff !important;
  font-weight: bold;
}
.v-list {
  background-color: #2e2e2e;
}
.v-list-item {
  background-color: #2e2e2e;
  padding: 10px 0;
  cursor: pointer;
  transition: background-color 0.3s ease;
}
.v-list-item:hover {
  background-color: #444;
}
.v-list-item-title {
  font-size: 14px;
  color: #ccc;
}
.thick-divider {
  height: 2px;
  background-color: #555;
  margin: 10px 0;
}

/* 반응형 스타일 */
@media (max-width: 600px) {
  .profile-header,
  .profile-stats,
  .profile-links {
    padding: 10px;
  }
  .profile-info h2 {
    font-size: 16px;
  }
  .profile-info p {
    font-size: 12px;
  }
  .profile-stats p {
    font-size: 12px;
  }
  .subheader {
    font-size: 14px;
  }
  .v-list-item-title {
    font-size: 12px;
  }
}

@media (min-width: 601px) and (max-width: 960px) {
  .profile-header,
  .profile-stats,
  .profile-links {
    padding: 15px;
  }
  .profile-info h2 {
    font-size: 17px;
  }
  .profile-info p {
    font-size: 13px;
  }
  .profile-stats p {
    font-size: 13px;
  }
  .subheader {
    font-size: 15px;
  }
  .v-list-item-title {
    font-size: 13px;
  }
}

@media (min-width: 961px) {
  .profile-header,
  .profile-stats,
  .profile-links {
    padding: 20px;
  }
  .profile-info h2 {
    font-size: 18px;
  }
  .profile-info p {
    font-size: 14px;
  }
  .profile-stats p {
    font-size: 14px;
  }
  .subheader {
    font-size: 16px;
  }
  .v-list-item-title {
    font-size: 14px;
  }
}
</style>
