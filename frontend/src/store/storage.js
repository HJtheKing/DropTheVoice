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
    eventSource: null,
    hasNewNotifications: false,
  }),
  getters: {
    hasMoreVoices(state) {
      return state.activeTab === 'all' ? state.hasMoreAllVoices : state.hasMoreLikedVoices;
    },
  },
  actions: {
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
      const userStore = useUserStore();
      if (!this.hasMoreAllVoices) return;
      this.isFetching = true;
      try {
        const response = await axios.get(`${import.meta.env.VITE_BASE_URL}/api-storage/spread/${page}/${this.pageSize}`, {
          params: {
            memberId: userStore.loginUserId,
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
      } catch (error) {
        console.error('Error fetching all voices:', error);
      } finally {
        this.isFetching = false;
      }
    },
    async fetchLikedVoices(page = 1) {
      const userStore = useUserStore();
      if (!this.hasMoreLikedVoices) return;

      this.isFetching = true;
      try {
        const response = await axios.get(`${import.meta.env.VITE_BASE_URL}/api-storage/heart/${page}/${this.pageSize}`, {
          params: {
            memberId: userStore.loginUserId,
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
    initializeSSE() {  
      const userStore = useUserStore();
      const memberId = userStore.loginUserId;
      if (!memberId) return;

      if (this.eventSource) {
        this.eventSource.close();
      }

      this.eventSource = new EventSource(`${import.meta.env.VITE_BASE_URL}/api-sse/subscribe/${memberId}`);
      this.eventSource.onopen = () => {
        console.log('SSE 연결이 열렸습니다.');
      };
      this.eventSource.onmessage = (event) => {
        const data = JSON.parse(event.data);
        console.log("Received SSE: ", data);

        if (data.type === 'Like Notification') {
          this.hasNewNotifications = true;
          this.fetchLikedVoices();
        }
      };

      this.eventSource.addEventListener('MESSAGE', (event) => {
        console.log("Message Event: ", event.data);
        this.hasNewNotifications = true;
      });

      this.eventSource.onerror = (error) => {
        console.error('SSE Error: ', error);
        this.eventSource.close();
      };
    },
    resetNotification() {
      this.hasNewNotifications = false;
    }
  },
});

