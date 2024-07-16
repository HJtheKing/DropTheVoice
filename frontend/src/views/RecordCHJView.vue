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
import CurrentTime from '@/Components/RecordCHJ/CurrentTime.vue'
import MyTimer from '@/Components/RecordCHJ/MyTimer.vue'
import MyRecordButton from '@/Components/RecordCHJ/MyRecordButton.vue'
</script>

<style scoped>

.home-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: black;
  height: 100vh;
  color: white;
  padding: 20px;
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