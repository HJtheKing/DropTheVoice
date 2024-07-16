import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useSpreadStore = defineStore('spread', () => {
  const isRecording = ref(false)
  const isUploading = ref(false)
  const activeTab = ref('pokemon')  // Default to 'pokemon'

  const uploadFile = () => {
    isUploading.value = true
    console.log('파일 업로드')
    isUploading.value = false
  }

  const setTab = (tab) => {
    activeTab.value = tab
  }

  return { isRecording, isUploading, uploadFile, activeTab, setTab }
})
