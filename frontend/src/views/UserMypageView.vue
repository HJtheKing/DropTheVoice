<template>
  <v-app class="bg-black">
    <v-main>
      <v-container class="mypage-container">
        <v-row justify="center" class="py-4">
          <v-col cols="12" class="text-center">
            <h1 class="title">마이페이지</h1>
          </v-col>
        </v-row>
        <v-card-title class="profile-header" v-if="member">
          <v-avatar size="50" @click="openUploadModal">
            <template v-if="member.profileImgUrl">
              <img :src="member.profileImgUrl" alt="Profile Image" class="profile-image" />
            </template>
            <template v-else>
              <img :src="userImage" alt="Profile Image" class="profile-image" />
            </template>
          </v-avatar>
          <div class="profile-info">
            <h2>{{ member.memberName }}</h2>
            <p>{{ member.memberEmail }}</p>
          </div>
          <v-avatar size="50">
            <img :src="badgeImage" alt="Badge" class="badge-image" />
          </v-avatar>
        </v-card-title>
        <v-card-subtitle class="profile-stats" v-if="member">
          <p>음성 업로드 수 : {{ member.totalUploadCount }}</p>
          <p>음성 확산 수 : {{ member.totalSpreadCount }}</p>
        </v-card-subtitle>
        <v-divider></v-divider>
        <v-card-text class="profile-links">
          <v-list>
            <v-subheader class="subheader">문의</v-subheader>
            <v-divider class="thick-divider"></v-divider>
            <v-list-item @click="navigate('customer-center')">
              <v-list-item-title>고객센터</v-list-item-title>
            </v-list-item>
            <v-list-item @click="navigate('privacy-policy')">
              <v-list-item-title>개인 정보 처리 방침</v-list-item-title>
            </v-list-item>
          </v-list>
          <v-list>
            <v-subheader class="subheader">회원정보 변경</v-subheader>
            <v-divider class="thick-divider"></v-divider>
            <v-list-item @click="logout">
              <v-list-item-title>로그아웃</v-list-item-title>
            </v-list-item>
            <v-list-item @click="openPasswordChangeModal">
              <v-list-item-title>비밀번호 변경</v-list-item-title>
            </v-list-item>
            <v-list-item @click="confirmUnsign">
              <v-list-item-title>회원 탈퇴</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-card-text>
      </v-container>
      <ChangePassword ref="changePasswordModal" />

      <!-- 이미지 업로드 모달 -->
      <v-dialog v-model="uploadDialog" max-width="500px">
        <v-card>
          <v-card-title>프로필 사진 업로드</v-card-title>
          <v-card-text>
            <input type="file" @change="appendImage" />
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="closeUploadModal">취소</v-btn>
            <v-btn color="blue darken-1" text @click="submitImg">업로드</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-main>
  </v-app>
</template>

<script setup>
import { ref, computed } from "vue";
import { useUserStore } from "@/store/user";
import { useRouter } from 'vue-router';
import axios from "axios";
import ChangePassword from '@/views/ChangePasswordView.vue';

import userImageSrc from '@/assets/images/user.png';
import unranked from '@/assets/images/unranked.png'
import bronzeBadgeSrc from '@/assets/images/bronze.png';
import silverBadgeSrc from '@/assets/images/silver.png';
import goldBadgeSrc from '@/assets/images/gold.png';
import platinumBadgeSrc from '@/assets/images/platinum.png';
import diamondBadgeSrc from '@/assets/images/diamond.png';

const userStore = useUserStore();
const router = useRouter();

const member = computed(() => userStore.user);
const loginUserId = computed(() => userStore.loginUserId);

const changePasswordModal = ref(null);
const uploadDialog = ref(false);
let file = null;

const logout = () => {
  userStore.logout();
};

const confirmUnsign = () => {
  if (confirm("정말로 회원탈퇴를 진행하시겠습니까?")) {
    unsign();
  }
};

const unsign = function () {
  userStore.deleteUser(loginUserId.value);
};

const navigate = (routeName) => {
  router.push({ name: routeName });
};

const openPasswordChangeModal = () => {
  changePasswordModal.value.openDialog();
};

const openUploadModal = () => {
  uploadDialog.value = true;
};

const closeUploadModal = () => {
  uploadDialog.value = false;
};

// 프로필 이미지 경로 설정
const userImage = userImageSrc;

// 배지 이미지를 동적으로 결정하는 computed property
const badgeImage = computed(() => {
  if (!member.value.totalUploadCount || member.value.totalUploadCount <= 5) {
    return unranked;
  }

  const spreadRatio = member.value.totalSpreadCount / member.value.totalUploadCount;

  if (spreadRatio >= 0.8) {
    return diamondBadgeSrc;
  } else if (spreadRatio >= 0.6) {
    return platinumBadgeSrc;
  } else if (spreadRatio >= 0.4) {
    return goldBadgeSrc;
  } else if (spreadRatio >= 0.2) {
    return silverBadgeSrc;
  } else {
    return bronzeBadgeSrc;
  }
});

const appendImage = (e) => {
  file = e.target.files[0];
};

const submitImg = () => {
  let formData = new FormData();
  formData.append("file", file);

  const userData = JSON.stringify({ id: loginUserId.value });
  formData.append("userData", userData);

  axios({
    method: "post",
    url: `http://localhost:8080/api-member/image`,
    data: formData,
    headers: { "Content-Type": "multipart/form-data" },
  })
    .then((response) => {
      alert("프로필 사진이 업데이트 되었습니다.");
      console.log(response);
      closeUploadModal(); // 업로드 후 모달 닫기
      window.location.reload();
    })
    .catch((err) => {
      alert("프로필 사진이 업데이트 중 오류가 발생했습니다.");
      console.log(err);
    });
};

</script>




<style scoped>
.mypage-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

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
