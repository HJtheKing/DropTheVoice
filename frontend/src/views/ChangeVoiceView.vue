<script setup>
import OriginalSpreadButton from '@/components/change/OriginalSpreadButton.vue'
import VoiceChangeButton from '@/components/change/VoiceChangeButton.vue'
import {ref} from 'vue';

const remain = ref(3);
</script>

<template>
    <div
        style="display: flex; flex-direction: column; align-items: center; justify-content: center; background-color: black; height: 100%;">
        <h1 style="color: white; background-color: red;">실시간 녹음하기</h1>>
        <div
            style="display: flex; flex-direction: column; align-items: center; justify-content: flex-start; background-color: blue; height: 100%;">
            <div style="padding-bottom: 30px; width: 90%; display: flex; flex-direction: row; justify-content: center;">
                <OriginalSpreadButton></OriginalSpreadButton>
            </div>
            <div style="padding-top: 30px; width: 90%; display: flex; flex-direction: row; justify-content: center;">
                <VoiceChangeButton :remain="remain"></VoiceChangeButton>
            </div>
        </div>
    </div>
</template>


<style scoped></style>
