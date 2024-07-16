<template>
    <AudioPlayer
      :option="{
          src: 'https://your-audio-file.mp3',
          title: 'your-audio-title',
          coverImage: 'https://your-cover-image.png',
     }"
    />
  </template>

<script>
import AudioPlayer from 'vue3-audio-player'
import 'vue3-audio-player/dist/style.css'
</script>