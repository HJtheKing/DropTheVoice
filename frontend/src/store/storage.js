import { defineStore } from 'pinia';
import axios from 'axios';
import { useUserStore } from '@/store/user';

export const useStorageStore = defineStore('storage', {
  state: () =>({
    pickVoices: [],
    likedVoices: [],
    currentPagePick: 1,
    currentPageLiked: 1,
    pageSize: 10,
    isFetching: false,
    hasMorePickVoices: true,
    hasMoreLikedVoices: true,
    activeTab: 'pick',
    eventSource: null,
    hasNewNotifications: false,
    hasNewLikeNotifications: false,
    hasNewPickNotifications: false,
  }),
  getters: {
    hasMoreVoices(state) {
      return state.activeTab === 'pick' ? state.hasMorePickVoices : state.hasMoreLikedVoices;
    },
  },
  actions: {
    resetPagination() {
      this.currentPagePick = 1;
      this.currentPageLiked = 1;
      this.hasMorePickVoices = true;
      this.hasMoreLikedVoices = true;
      this.pickVoices = [];
      this.likedVoices = [];
      this.isFetching = false;
    },
    async fetchPickVoices(page = 1) {
      const userStore = useUserStore();
      if (!this.hasMorePickVoices) return;
      this.isFetching = true;
      try {
        const response = await axios.get(`${import.meta.env.VITE_BASE_URL}/api-storage/picks/${page}/${this.pageSize}`, {
          params: {
            memberId: userStore.loginUserId,
          }
        });
        if (response.data.length < this.pageSize) {
          this.hasMorePickVoices = false;
        }
        if (page === 1) {
          this.pickVoices = response.data;
        } else {
          this.pickVoices.push(...response.data);
        }
      } catch (error) {
        console.error('Error fetching pick voices:', error);
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
        if (this.activeTab === 'pick') {
          this.currentPagePick++;
          await this.fetchPickVoices(this.currentPagePick);
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
      if (tab === 'pick') {
        this.hasNewPickNotifications = false;
      } if(this.pickVoices.length === 0) {
        this.fetchPickVoices();
      } else if (tab === 'liked') {
        this.hasNewLikeNotifications = false;
      } if (this.likedVoices.length === 0) {
        this.fetchLikedVoices();
      }
    },
    reloadPickVoices() {
      this.resetPagination();
      this.fetchPickVoices();
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
        this.hasNewLikeNotifications = true;
        this.fetchLikedVoices();
      } else if (data.type === 'Pick Notification') {
        this.hasNewPickNotifications = true;
        this.fetchPickVoices();
      }
      };

      this.eventSource.addEventListener('MESSAGE', (event) => {
        console.log("Message Event: ", event.data);
        this.hasNewNotifications = true;
        const message = event.data;
        if (message.includes('like')) {
          this.hasNewLikeNotifications = true;
        } else if (message.includes('Pick')) {
          this.hasNewPickNotifications = true;
        }
      });

      this.eventSource.onerror = (error) => {
        console.error('SSE Error: ', error);
        this.eventSource.close();
      };
    },
    resetNotification() {
      this.hasNewNotifications = false;
    },
    resetLikeNotification() {
      this.hasNewLikeNotifications = false;
    },
    resetPickNotification() {
      this.hasNewPickNotifications = false;
    }
  },
});
