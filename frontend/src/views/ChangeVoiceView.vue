<script setup>
import OriginalSpreadButton from '@/Components/spread/OriginalSpreadButton.vue'
import VoiceChangeButton from '@/Components/spread/VoiceChangeButton.vue'
</script>

<template>
    <div
        style="display: flex; flex-direction: column; align-items: center; justify-content: space-evenly; background-color: black;">
        <h1>실시간 녹음하기</h1>>
        <div style="padding: 10%; width: 90%; display: flex; flex-direction: row; justify-content: center;"><OriginalSpreadButton></OriginalSpreadButton></div>
        <div style="padding: 10%; width: 90%; display: flex; flex-direction: row; justify-content: center;"><VoiceChangeButton></VoiceChangeButton></div>
    </div>
</template>


<style scoped></style>