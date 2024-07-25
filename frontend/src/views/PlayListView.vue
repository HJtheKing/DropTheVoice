<template>
  <div style="background-color: black;">
      <div style="display: flex; flex-direction: column; align-items: center;">
          <h1 style="color: white;" >{{ title }}</h1>
          <template v-for="(item, index) in state.items" :key="index">
              <button style="width: 90%; margin: 1%;" @click="selectItemAndJump(item)">
                  <RecordFile :item="item"></RecordFile>
              </button>
          </template>
      </div>
  </div>
</template>

<script setup>
import RecordFile from '@/components/RecordFile.vue';
import { ref } from 'vue';
import { reactive, onMounted } from 'vue';
import { useRouter } from "vue-router";
import axios from 'axios';
import { useSeletedTrackStore } from '@/store/playList.js';

const router = useRouter();
const store = useSeletedTrackStore();

const title = ref('주워 듣기(실시간 알람함)');

const state = reactive({
  title: '재생목록',
  items: []

});

onMounted(async () =>{
  try{
      const response = await axios.get("http://localhost:8080/api-virus/virus")
      state.items = response.data
      console.log(state.items)
  } catch(error){
      console.error('Error fetching items', error)
  }
})

function selectItemAndJump(item) {
  store.selectItem(item);
  router.push({ name: 'audioplayer' });
}
</script>

<style lang="scss" scoped></style>
