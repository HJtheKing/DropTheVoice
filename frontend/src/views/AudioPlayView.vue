<template>
  <v-app class="black-background">
    <v-container class="audio-play">
      <audio-player ref="audioPlayer"></audio-player>
      <v-btn class="load-btn" @click="playSampleAudio">오디오 불러오기</v-btn>
    </v-container>
</v-app>
</template>

<script>
import AudioPlayer from '@/components/AudioPlayer.vue';
import audioFile from '@/assets/tracks/Computer_Love-Feel_The_Funk.mp3';

export default {
  components: {
    AudioPlayer
  },
  methods: {
    playSampleAudio() {
      this.$refs.audioPlayer.loadAudio(audioFile);
    }
  }
};
</script>

<style scoped>
.audio-play {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.black-background {
  background-color: #000;
  color: #fff;
}
.load-btn {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #f3b549;
  color: #000;
  font-weight: bold;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
.load-btn:hover {
  background-color: #cd9e01;
}
</style>
