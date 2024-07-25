import { defineStore } from 'pinia';
import axios from 'axios';

export const useStorageStore = defineStore('stoarge', {
  state: () => ({
    allVoices: [],
    likedVoices: [],
  }),
  actions: {
    async fetchAllVoices() {
      try {
        const response = await axios.get('http://localhost:8080/api-storage/all');
        this.allVoices = response.data;
      } catch (error) {
        console.error('Error fetching all voices:', error);
      }
    },
    async fetchLikedVoices() {
      try {
        const response = await axios.get('http://localhost:8080/api-storage/like');
        this.likedVoices = response.data;
      } catch (error) {
        console.error('Error fetching liked voices:', error);
      }
    },
  },
});
