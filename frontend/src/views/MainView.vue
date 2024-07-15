<template>
  <v-app class="black-background">
    <v-main>
      <v-container class="custom-container">
        <!-- 제목 추가 -->
        <v-row justify="center" class="py-4">
          <h1 class="title">Drop The Voice</h1>
        </v-row>
        <v-row justify="center" class="py-8">
          <v-col cols="6" class="d-flex justify-center">
            <v-card class="main-button" elevation="2">
              <v-card-text class="text-center">
                <v-icon large>mdi-send</v-icon>
                <div class="main-button-text">뿌리기</div>
              </v-card-text>
            </v-card>
          </v-col>
          <v-col cols="6" class="d-flex justify-center">
            <v-card class="main-button" elevation="2">
              <v-card-text class="text-center">
                <v-icon large>mdi-headphones</v-icon>
                <div class="main-button-text">주워듣기</div>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <h2>오늘의 인기 음성</h2>
          </v-col>
          <v-col class="text-right">
            <v-btn class="more-button" text>더보기</v-btn>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12">
            <v-card class="mb-4" elevation="2" v-for="(item, index) in popularVoices" :key="index">
              <v-row no-gutters>
                <v-col cols="4">
                  <v-img :src="item.image" height="100px" contain></v-img>
                </v-col>
                <v-col cols="8">
                  <v-card-title>
                    <div>
                      <h3>{{ item.title }}</h3>
                      <p>{{ item.listeners }} Listeners</p>
                      <v-avatar>
                        <img :src="item.avatar" />
                      </v-avatar>
                      <span>{{ item.author }}</span>
                    </div>
                  </v-card-title>
                </v-col>
              </v-row>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
      <v-bottom-navigation
        v-model="activeNav"
        grow
        class="fixed-bottom bottom-navy"
      >
        <v-btn value="home">
          <v-icon>mdi-home</v-icon>
          <span>Home</span>
        </v-btn>
        <v-btn value="browse">
          <v-icon>mdi-magnify</v-icon>
          <span>Browse</span>
        </v-btn>
        <v-btn value="find">
          <v-icon>mdi-account-search</v-icon>
          <span>Bookmark</span>
        </v-btn>
        <v-btn value="more">
          <v-icon>mdi-dots-horizontal</v-icon>
          <span>More</span>
        </v-btn>
      </v-bottom-navigation>
    </v-main>
  </v-app>
</template>

<script>
export default {
  data() {
    return {
      activeNav: 'home',
      popularVoices: [
        {
          title: '재미있는 이야기',
          listeners: '78.4K',
          author: '김싸피',
          image: 'path-to-your-image-1',
          avatar: 'path-to-your-avatar-1',
        },
        {
          title: '무서운 이야기',
          listeners: '23.5K',
          author: '김싸피',
          image: 'path-to-your-image-2',
          avatar: 'path-to-your-avatar-2',
        },
        {
          title: '스토킹 당하고 있어요',
          listeners: '20.5K',
          author: 'Agnes',
          image: 'path-to-your-image-3',
          avatar: 'path-to-your-avatar-3',
        },
      ],
    };
  },
};
</script>

<style>
.custom-container {
  max-width: 800px; /* 원하는 너비로 조정 */
  margin: 0 auto;
  padding-bottom: 80px; /* 하단 네비게이션 바 공간 확보 */
}
.black-background {
  background-color: #000;
  color: #fff;
}
.fixed-bottom {
  position: fixed;
  bottom: 0;
  width: 100%;
}
.bottom-navy {
  background-color: #252836; /* 남색 */
  color: #808191; 
}
.title {
  color: #fff;
  font-weight: bold;
  font-size: 24px;
}
.main-button {
  height: calc(50vw - 24px);
  max-height: 344px;
  width: calc(50vw - 24px);
  max-width: 344px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  background-color: #3a3a3a;
  color: #fff;
}
.main-button-text {
  color: #fff;
  font-weight: bold;
}
.square-card v-icon {
  font-size: 48px;
  margin-bottom: 8px;
}
.more-button {
  background-color: #f3b549; /* 머스타드색 */
  color: #000;
  font-weight: bold;
}
h2 {
  color: #fff;
  font-weight: bold;
}
h3 {
  color: #fff;
  margin: 0;
}
p {
  color: #ccc;
  margin: 0;
}
</style>
