import { defineStore } from 'pinia';
import axios from 'axios';
import { useUserStore } from '@/store/user';

export const useStorageStore = defineStore('storage', {
  state: () =>({
    pickVoices: [],
    likedVoices: [],
    spreadVoices: [],
    currentPagePick: 1,
    currentPageLiked: 1,
    currentPageSpread: 1, 
    pageSize:10,
    isFetching: false,
    hasMorePickVoices: true,
    hasMoreLikedVoices: true,
    hasMoreSpreadVoices: true,
    activeTab: 'pick',
  }),
  getters: {
    hasMoreVoices(state) {
      if (state.activeTab === 'pick') {
        return state.hasMorePickVoices;
      } else if (state.activeTab === 'liked') {
        return state.hasMoreLikedVoices;
      } else if (state.activeTab === 'spread') {
        return state.hasMoreSpreadVoices;
      }
      return false;
    },
  },
  actions: {
    resetPagination() {
      this.currentPagePick = 1;
      this.currentPageLiked = 1;
      this.currentPageSpread = 1;  
      this.hasMorePickVoices = true;
      this.hasMoreLikedVoices = true;
      this.hasMoreSpreadVoices = true;
      this.pickVoices = [];
      this.likedVoices = [];
      this.spreadVoices = [];
      this.isFetching = false;
    },
    async fetchPickVoices(page = 1) {
      const userStore = useUserStore();
      const token = sessionStorage.getItem('access-token')
      if (!this.hasMorePickVoices) return;
      this.isFetching = true;
      try {
        const response = await axios.get(`${import.meta.env.VITE_BASE_URL}/api-storage/picks/${page}/${this.pageSize}`, {
          headers: { Authorization: `Bearer ${token}` },
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
      const token = sessionStorage.getItem('access-token')
      if (!this.hasMoreLikedVoices) return;
      this.isFetching = true;
      try {
        const response = await axios.get(`${import.meta.env.VITE_BASE_URL}/api-storage/heart/${page}/${this.pageSize}`, {
          headers: { Authorization: `Bearer ${token}` },
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
    async fetchSpreadVoices(page = 1) {
      const userStore = useUserStore();
      const token = sessionStorage.getItem('access-token')
      if (!this.hasMoreSpreadVoices) return;
      this.isFetching = true;
      try {
        const response = await axios.get(`${import.meta.env.VITE_BASE_URL}/api-storage/spread/${page}/${this.pageSize}`, {
          headers: { Authorization: `Bearer ${token}` },
          params: {
            memberId: userStore.loginUserId
          }
        });
        if (response.data.length < this.pageSize) {
          this.hasMoreSpreadVoices = false;
        }
        if (page === 1) {
          this.spreadVoices = response.data;
        } else {
          this.spreadVoices.push(...response.data);
        }
      } catch (error) {
        console.error('Error fetching spread voices:', error);
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
        } else if(this.activeTab ==='liked') {
          this.currentPageLiked++;
          await this.fetchLikedVoices(this.currentPageLiked);
        } else if (this.activeTab === 'spread') {
          this.currentPageSpread++;
          await this.fetchSpreadVoices(this.currentPageSpread);
        }
      } catch (error) {
        console.error('Error loading more voices:', error);
      } finally {
        this.isFetching = false;
      }
    },
    changeTab(tab) {
      const userStore = useUserStore();
      this.activeTab = tab;
      this.resetPagination();
      if (tab === 'pick') {
        userStore.hasNewPickNotifications = false;
        if (this.pickVoices.length === 0) {
          this.fetchPickVoices();
        }
      } else if (tab === 'liked') {
        userStore.hasNewLikeNotifications = false;
        if (this.likedVoices.length === 0) {
          this.fetchLikedVoices();
        }
      } else if (tab === 'spread') {
        userStore.hasNewSpreadNotifications = false;
        if (this.spreadVoices.length === 0) {
          this.fetchSpreadVoices();
        }
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
    reloadSpreadVoices() {
      this.resetPagination();
      this.fetchSpreadVoices();
    },
  }
}); 
