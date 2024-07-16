import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useRecordStore = defineStore('record', () => {

    const isRecording = ref(false)
    const audioUrl = ref('')
  return {isRecording, audioUrl}
})
