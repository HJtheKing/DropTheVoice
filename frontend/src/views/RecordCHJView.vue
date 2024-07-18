<template>
    <div class="home-container">
        
    <header>
        <h1>실시간 녹음하기</h1>
    </header>
    <CurrentTime />
    <div class="recording-container">
      <MyTimer />
      <MyRecordButton />
    </div>

    
    </div>
</template>

<script setup>
import CurrentTime from '@/components/RecordCHJ/CurrentTime.vue'
import MyTimer from '@/components/RecordCHJ/MyTimer.vue'
import MyRecordButton from '@/components/RecordCHJ/MyRecordButton.vue'
</script>

<style scoped>

.home-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  /* background-color: black; */
  height: 100vh;
  color: rgb(255, 255, 255);
  padding: 20px;
  min-height: 100vh
}

.recording-container {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin: 20px 0;
}

</style>