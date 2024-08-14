<template>
  <v-app class="bg-black">
    <v-main>
      <v-container class="custom-container">
        <v-row justify="center" class="py-4">
          <v-col cols="12" class="text-center">
            <h1 style="color: white;">주워 듣기(실시간 알람함)</h1>
          </v-col>
        </v-row>

        <v-row justify="center" class="py-4">
          <v-col cols="12" sm="6">
            <v-text-field
              v-model="searchQuery"
              placeholder="검색"
              outlined
              class="white-placeholder"
            ></v-text-field>
          </v-col>
          <v-col cols="12" sm="4">
            <v-select
              v-model="sortOption"
              :items="sortOptions"
              item-text="text"
              item-value="value"
              item-title="text"
              placeholder="정렬 기준"
              outlined
              class="white-placeholder"
            ></v-select>
          </v-col>
          <v-col cols="12" sm="2">
            <v-btn @click="searchItems" block color="primary">검색</v-btn>
          </v-col>
        </v-row>

        <v-row>
          <v-col v-for="(item, index) in voices" :key="index" cols="12" sm="6" md="6">
  <RecordFile :item="item" @click="navigateToDetail(item.voiceId)"></RecordFile>
</v-col>
        </v-row>

        <v-row justify="center" v-if="isFetching">
          <v-progress-circular indeterminate color="primary"></v-progress-circular>
        </v-row>

        <v-row justify="center" v-if="!hasMoreVoices && !isFetching">
          <p>더 이상 불러올 데이터가 없습니다.</p>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
import RecordFile from '@/components/RecordFile.vue';
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const voices = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);
const searchQuery = ref('');
const sortOption = ref('latest');
const isFetching = ref(false);
const hasMoreVoices = ref(true);
const router = useRouter();

const sortOptions = [
  { text: '최신순', value: 'latest' },
  { text: '청취수 순', value: 'listenCount' },
  { text: '좋아요 순', value: 'heartCount' }
];

async function fetchBestVoices(page = 1) {
  if (isFetching.value || !hasMoreVoices.value) return;
  isFetching.value = true;

  try {
    const res = await axios.get(`${import.meta.env.VITE_BASE_URL}/api-voice/search/${page}/${pageSize.value}`, {
      params: {
        keyword: searchQuery.value || '',
        sort: sortOption.value
      }
    });
    if (res.data.content.length < pageSize.value) {
      hasMoreVoices.value = false;
    }
    voices.value.push(...res.data.content);
    console.log(voices.value[0].voiceId)
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
  fetchBestVoices(); // 초기 데이터를 가져옴
}

onMounted(() => {
  resetPagination();
  window.addEventListener('scroll', handleScroll);
});

function searchItems() {
  resetPagination();
}
</script>

<style scoped>
.bg-black {
  background-color: #000;
}

.custom-container {
  max-width: 800px;
  margin: 0 auto;
  padding-bottom: 80px;
}

.white-placeholder .v-input__control .v-input__slot::before .v-input__slot::after {
  border-color: #fff;
}


.white-placeholder input::placeholder {
  color: #000;
}

button {
  background-color: #1e1e1e;
  color: #fff;
  border: 1px solid #333;
  border-radius: 5px;
  padding: 10px;
  cursor: pointer;
}

button:hover {
  background-color: #333;
}

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
