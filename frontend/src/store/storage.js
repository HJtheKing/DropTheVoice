import { defineStore } from 'pinia';
import axios from 'axios';
import { useUserStore } from '@/store/user';

export const useStorageStore = defineStore('storage', {
  state: () => ({
    allVoices: [],
    likedVoices: [],
    currentPageAll: 1,
    currentPageLiked: 1,
    pageSize: 10,
    isFetching: false,
    hasMoreAllVoices: true,
    hasMoreLikedVoices: true,
    activeTab: 'all',
    newItemsAvailable: false,  
    sseSource: null,
  }),
  getters: {
    hasMoreVoices(state) {
      return state.activeTab === 'all' ? state.hasMoreAllVoices : state.hasMoreLikedVoices;
    },
  },
  actions: {
    initSSE() {
      const userStore = useUserStore();
      if (this.sseSource) {
        this.sseSource.close();
      }
      this.sseSource = new EventSource(`${import.meta.env.VITE_BASE_URL}/api-sse/sse/notifications?memberId=${userStore.loginUserId}`)
      this.sseSource.onmessage = (event) => {
        console.log('SSE Message Received:', event.data);
        const data = JSON.parse(event.data);
        console.log('Parsed SSE Data:', data);
        this.newItemsAvailable = true;
      };

      this.sseSource.onerror = (error) => {
        console.error('SSE error: ', error);
        this.sseSource.close();
      }
    },
    stopSSE() {
      if (this.sseSource) {
        this.sseSource.close();
        this.sseSource = null;
      }
    },
    resetPagination() {
      this.currentPageAll = 1;
      this.currentPageLiked = 1;
      this.hasMoreAllVoices = true;
      this.hasMoreLikedVoices = true;
      this.allVoices = [];
      this.likedVoices = [];
      this.isFetching = false;
    },
    async fetchAllVoices(page = 1) {
      const useStore = useUserStore();
      if (!this.hasMoreAllVoices) return;
      this.isFetching = true;
      try {
        const response = await axios.get(`${import.meta.env.VITE_BASE_URL}/api-storage/spread/${page}/${this.pageSize}`, {
          params: {
            memberId: useStore.loginUserId,
          }
        });
        if (response.data.length < this.pageSize) {
          this.hasMoreAllVoices = false;
        }
        if (page === 1) {
          this.allVoices = response.data;
        } else {
          this.allVoices.push(...response.data);
        }
        if (page > 1 && response.data.length > 0) {
          this.newItemsAvailable = true; 
        }
      } catch (error) {
        console.error('Error fetching all voices:', error);
      } finally {
        this.isFetching = false;
      }
    },
    async fetchLikedVoices(page = 1) {
      const useStore = useUserStore();
      if (!this.hasMoreLikedVoices) return;

      this.isFetching = true;
      try {
        const response = await axios.get(`${import.meta.env.VITE_BASE_URL}/api-storage/heart/${page}/${this.pageSize}`, {
          params: {
            memberId: useStore.loginUserId,
          } 
        });
        if (response.data.length < this.pageSize) {
          this.hasMoreLikedVoices = false;
        }
        if (page === 1) {
          this.likedVoices = response.data;
        } else {
          this.likedVoices.push(...response.data);
        }
        if (page > 1 && response.data.length > 0) {
          this.newItemsAvailable = true;  
        }
      } catch (error) {
        console.error('Error fetching liked voices:', error);
      } finally {
        this.isFetching = false;
      }
    },
    async loadMoreVoices() {
      if (this.isFetching || !this.hasMoreVoices) return;
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
      this.resetPagination();
      if (tab === 'all' && this.allVoices.length === 0) {
        this.fetchAllVoices();
      } else if (tab === 'liked' && this.likedVoices.length === 0) {
        this.fetchLikedVoices();
      }
    },
    reloadAllVoices() {
      this.resetPagination();
      this.fetchAllVoices();
    },
    reloadLikedVoices() {
      this.resetPagination();
      this.fetchLikedVoices();
    },
    resetNotification() {
      this.newItemsAvailable = false; 
    },
  },
});
