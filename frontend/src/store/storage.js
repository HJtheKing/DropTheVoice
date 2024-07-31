import { defineStore } from 'pinia';
import axios from 'axios';

export const useStorageStore = defineStore('storage', {
  state: () => ({
    allVoices: [],
    likedVoices: [],
    currentPageAll: 1,
    currentPageLiked: 1,
    pageSize: 10,
    isFetching: false,
    activeTab: 'all',
  }),
  actions: {
    async fetchAllVoices(page = 1) {
      try {
        console.log('api call@@@@')
        const response = await axios.get(`http://localhost:8080/api-storage/all/${page}/${this.pageSize}`);
        console.log(response)
        if (page === 1) {
          this.allVoices = response.data;
        } else {
          this.allVoices.push(...response.data); // 추가 데이터를 기존 리스트에 추가
        }
      } catch (error) {
        console.error('Error fetching all voices:', error);
      }
    },
    async fetchLikedVoices(page = 1) {
      try {
        const response = await axios.get(`http://localhost:8080/api-storage/like/${page}/${this.pageSize}`);
        if (page === 1) {
          this.likedVoices = response.data;
        } else {
          this.likedVoices.push(...response.data); // 추가 데이터를 기존 리스트에 추가
        }
      } catch (error) {
        console.error('Error fetching liked voices:', error);
      }
    },
    async loadMoreVoices() {
      if (this.isFetching) return;

      this.isFetching = true;

      try {
        if (this.activeTab === 'all') {
          this.currentPageAll++;
          await this.fetchAllVoices(this.currentPageAll);
        } else {
          this.currentPageLiked++;
          await this.fetchLikedVoices(this.currentPageLiked);
        }
      } catch (error) {
        console.error('Error loading more voices:', error);
      } finally {
        this.isFetching = false;
      }
    },
    changeTab(tab) {
      this.activeTab = tab;
      if (tab === 'all' && this.allVoices.length === 0) {
        this.fetchAllVoices();
      } else if (tab === 'liked' && this.likedVoices.length === 0) {
        this.fetchLikedVoices();
      }
    },
  },
});

