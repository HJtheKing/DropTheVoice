<template>
  <v-bottom-navigation
    v-model="activeNav"
    grow
    class="fixed-bottom bottom-navy"
  >
    <v-btn value="홈" @click="navigateTo('home')">
      <v-icon>mdi-home</v-icon>
      <span>홈</span>
    </v-btn>
    <v-btn value="음성찾기" @click="navigateTo('finder')">
      <v-icon>mdi-compass</v-icon>
      <span>음성 줍기</span>
    </v-btn>
    <v-btn value="보관함" @click="navigateToStorage">
      <v-badge
        v-if="storageStore.hasNewNotifications"
        color="yellow"
        dot
        overlap
      >
        <v-icon>mdi-folder</v-icon>
      </v-badge>
      <v-icon v-else>mdi-folder</v-icon>
      <span>보관함</span>
    </v-btn>
    <v-btn value="마이페이지" @click="navigateTo('mypage')">
      <v-icon>mdi-view-grid</v-icon>
      <span>마이페이지</span>
    </v-btn>
  </v-bottom-navigation>
</template>

<script>
import { useStorageStore } from '@/store/storage';

export default {
  data() {
    return {
      activeNav: 'HOME'
    };
  },
  computed: {
    storageStore() {
      return useStorageStore();
    }
  },
  methods: {
    navigateTo(routeName) {
      if (this.$route.name === routeName) {
        window.scrollTo({ top: 0, behavior: 'smooth' });
      } else {
        this.$router.push({ name: routeName });
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    },
    navigateToStorage() {
      this.storageStore.resetNotification();
      this.$router.push({ name: 'storage' });
    },
  },
};
</script>

<style scoped>
.bottom-navy {
  background-color: #252836;
  color: #808191;
}

/* 반응형 스타일 */
@media (max-width: 600px) {
  .v-btn .v-icon {
    font-size: 20px;
  }
  .v-btn span {
    font-size: 10px;
  }
}

@media (min-width: 601px) and (max-width: 960px) {
  .v-btn .v-icon {
    font-size: 22px;
  }
  .v-btn span {
    font-size: 11px;
  }
}

@media (min-width: 961px) {
  .v-btn .v-icon {
    font-size: 24px;
  }
  .v-btn span {
    font-size: 12px;
  }
}
</style>
