<template>
  <div>
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
        <v-card class="mb-4 list-items" elevation="2" v-for="(item, index) in popularVoices" :key="index">
          <v-row no-gutters>
            <v-col cols="4">
              <v-img :src="item.image" height="100px" contain></v-img>
            </v-col>
            <v-col cols="8">
              <v-card-title>
                <div class="content">
                  <h3>{{ item.title }}</h3>
                  <p>{{ item.listeners }} Listeners</p>
                  <v-avatar size="36">
                    <img :src="item.avatar" class="avatar-img" />
                  </v-avatar>
                  <span class="author-name">{{ item.author }}</span>
                </div>
              </v-card-title>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script>
export default {
  props: {
    popularVoices: {
      type: Array,
      required: true,
    },
  },
};
</script>

<style scoped>
.more-button {
  background-color: #f3b549;
  color: #000;
  font-weight: bold;
}
.list-items {
  background-color: #000;
}
.author-name {
  color: #fff;
  font-weight: bold;
}
.avatar-img {
  object-fit: cover; /* 이미지 아바타 영역에 맞게 조정 */
  width: 100%;
  height: 100%;
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
