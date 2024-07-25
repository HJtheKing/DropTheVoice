import { defineStore } from 'pinia';

export const useSeletedTrackStore = defineStore('selectedTrack', {
  state: () => ({
    selectedItem: {
      title: '',
      imgURL: '',
      author: ''
    }
  }),
  actions: {
    selectItem(item) {
      this.selectedItem = item;
    }
  },
  getters: {
    getSelectedItem: (state) => state.selectedItem
  }
});
