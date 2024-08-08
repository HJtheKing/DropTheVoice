<script setup>
import {onMounted, ref} from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const voices = ref([]);
const voiceDetail = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);
const isFetching = ref(false);
const hasMoreVoices = ref(true);
const router = useRouter();


async function fetchBestVoices(page = 1) {
  if (isFetching.value || !hasMoreVoices.value) return;
  isFetching.value = true;

  try {
    const res = await axios.get(`${import.meta.env.VITE_BASE_URL}/api-voice/best-heart-voice/${page}/${pageSize.value}`);
    if (res.data.length < pageSize.value) {
      hasMoreVoices.value = false;
    }
    voices.value.push(...res.data);
  } catch (error) {
    console.error('Error fetching best voices:', error);
  } finally {
    isFetching.value = false;
  }
}

function navigateToDetail(id) {
  router.push({ name: 'audioplayer', params: { id } });
}

const handleScroll = () => {
  const bottomOfWindow = window.innerHeight + window.scrollY >= document.documentElement.offsetHeight - 10;
  if (bottomOfWindow && !isFetching.value && hasMoreVoices.value) {
    currentPage.value++;
    fetchBestVoices(currentPage.value);
  }
};

function resetPagination() {
  voices.value = [];
  currentPage.value = 1;
  hasMoreVoices.value = true;
  isFetching.value = false;
}

onMounted(() => {
  resetPagination();
  fetchBestVoices(); // 초기 데이터를 가져옴
  window.addEventListener('scroll', handleScroll);
});

</script>

<template>
  <v-container class="py-4">
    <v-row align="center" justify="space-between">
      <v-col cols="12" md="6">
        <h2>오늘의 인기 음성</h2>
      </v-col>
      <!-- <v-col cols="12" md="6" class="text-right">
        <v-btn class="more-button" text>더보기</v-btn>
      </v-col> -->
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-card class="mb-4 list-items" elevation="2" v-for="(item, index) in voices" :key="index"
                @click="navigateToDetail(item.id)">
          <v-row no-gutters>
            <v-col cols="4">
              <v-img :src="item.imageUrl" height="100px" contain></v-img>
            </v-col>
            <v-col cols="8">
              <v-card-title>
                <div class="content">
                  <h3>{{ item.title }}</h3>
                  <p>{{ item.listenCount }} Listeners</p>
                  <v-avatar size="36">
                    <img :src="item.imageUrl" class="avatar-img"/>
                  </v-avatar>
                  <span class="author-name">{{ item.userName }}</span>
                </div>
              </v-card-title>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>

    <!-- 로딩 애니메이션 -->
    <v-row justify="center" v-if="isFetching">
      <v-progress-circular indeterminate color="primary"></v-progress-circular>
    </v-row>

    <!-- 더 이상 데이터가 없을 때 표시할 메시지 -->
    <v-row justify="center" v-if="!hasMoreVoices && !isFetching">
      <p>더 이상 불러올 데이터가 없습니다.</p>
    </v-row>
  </v-container>
</template>

<style scoped>
.more-button {
  background-color: #f3b549;
  color: #000;
  font-weight: bold;
}

.list-items {
  background-color: #000;
  color: #fff;
  transition: transform 0.3s ease;
}

.list-items:hover {
  transform: scale(1.02);
}

.content {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.author-name {
  color: #fff;
  font-weight: bold;
}

.avatar-img {
  object-fit: cover;
  width: 100%;
  height: 100%;
}

h2 {
  color: #fff;
  font-weight: bold;
  margin: 0;
}

h3 {
  color: #fff;
  margin: 0;
}

p {
  color: #ccc;
  margin: 0;
}

/* 반응형 스타일 */
@media (max-width: 600px) {
  .list-items {
    margin-bottom: 16px;
  }

  .content h3 {
    font-size: 18px;
  }

  .content p {
    font-size: 14px;
  }

  .content .author-name {
    font-size: 14px;
  }

  .content .avatar-img {
    width: 24px;
    height: 24px;
  }
}

@media (min-width: 601px) and (max-width: 960px) {
  .list-items {
    margin-bottom: 20px;
  }

  .content h3 {
    font-size: 20px;
  }

  .content p {
    font-size: 16px;
  }

  .content .author-name {
    font-size: 16px;
  }

  .content .avatar-img {
    width: 28px;
    height: 28px;
  }
}

@media (min-width: 961px) {
  .list-items {
    margin-bottom: 24px;
  }

  .content h3 {
    font-size: 22px;
  }

  .content p {
    font-size: 18px;
  }

  .content .author-name {
    font-size: 18px;
  }

  .content .avatar-img {
    width: 32px;
    height: 32px;
  }
}
</style>
