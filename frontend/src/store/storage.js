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
      const useStore = useUserStore();
      if (!this.hasMoreAllVoices) return;

      this.isFetching = true;
      try {
        const response = await axios.get(`http://localhost:8080/api-storage/spread/${page}/${this.pageSize}`, {
          params: {
            memberId: 1, // 테스트용. jwt 인증 구현 되면 밑에꺼 쓰면 됨
            // memberId: useStore.loginUserId,
          }
        });
        if (response.data.length < this.pageSize) {
          this.hasMoreAllVoices = false; // 더 이상 가져올 데이터가 없음을 표시
        }
        if (page === 1) {
          this.allVoices = response.data;
        } else {
          this.allVoices.push(...response.data); // 추가 데이터를 기존 리스트에 추가
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
        const response = await axios.get(`http://localhost:8080/api-storage/heart/${page}/${this.pageSize}`, {
          params: {
            memberId: 1, // 테스트용. jwt 인증 구현 되면 밑에꺼 쓰면 됨
            // memberId: useStore.loginUserId,
          } 
        });
        if (response.data.length < this.pageSize) {
          this.hasMoreLikedVoices = false; // 더 이상 가져올 데이터가 없음을 표시
        }
        if (page === 1) {
          this.likedVoices = response.data;
        } else {
          this.likedVoices.push(...response.data); // 추가 데이터를 기존 리스트에 추가
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
    }
  },
});
