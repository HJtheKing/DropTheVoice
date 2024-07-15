import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useSpreadStore = defineStore('spread', () => {
  const isRecording = ref(false)
  const isUploading = ref(false)
  const activeTab = ref('pokemon')  // Default to 'pokemon'

  const startRecording = () => {
    isRecording.value = !isRecording.value
    console.log(isRecording.value ? '녹음 시작' : '녹음 중지')
  }

  const uploadFile = () => {
    isUploading.value = true
    console.log('파일 업로드')
    isUploading.value = false
  }

  const setTab = (tab) => {
    activeTab.value = tab
  }

  return { isRecording, isUploading, startRecording, uploadFile, activeTab, setTab }
})
