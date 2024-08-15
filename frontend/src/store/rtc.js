import { defineStore } from 'pinia';

export const useRtcStore = defineStore('rtc', {
    state: () => ({
        files: JSON.parse(localStorage.getItem('rtcFiles')) || {},
    }),
    actions: {
        addFile(voiceId, fileBlob){
            this.files[voiceId] = fileBlob;
            localStorage.setItem('rtcFiles', JSON.stringify(this.files));
        },
    },
    getters: {
        getFile: (state) => (fileId) => {
            return state.files[fileId];
        },
        getAllFiles: (state) => {
            return state.files;
        },
    },
});