<template>
  <div class="audio-play">
    <audio-player ref="audioPlayer"></audio-player>
    <button @click="playSampleAudio">오디오 불러오기</button>
  </div>
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
button {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #007BFF;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
button:hover {
  background-color: #0056b3;
}
</style>
