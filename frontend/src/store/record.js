import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { useUserStore } from './user';
import axios from 'axios';

export const useRecordStore = defineStore('record', () => {
  const isRecording = ref(false);
  const audioUrl = ref(null);
  const audioBlob = ref(null);
  const activeBars = ref(0);
  const analyser = ref(null); // 오디오 분석기
  const dataArray = ref(null); // 오디오 데이터 배열
  const bufferLength = ref(0); // 분석할 데이터 크기
  const stream = ref(null); // MediaStream 객체
  const javascriptNode = ref(null);
  const isPlaying = ref(false);

  const userStore = useUserStore();
  const memberId = computed(() => userStore.loginUserId);
  const locationMessage = ref('');
  const latitude = ref(0);
  const longitude = ref(0);

  function getLocation() {
    return new Promise((resolve, reject) => {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(resolve, reject);
      } else {
        reject(new Error('Geolocation is not supported by this browser.'));
      }
    });
  }

  function showPosition(position) {
    latitude.value = position.coords.latitude;
    longitude.value = position.coords.longitude;
    locationMessage.value = `Latitude: ${latitude.value} | Longitude: ${longitude.value}`;
    console.log(locationMessage.value);
  }

  function showError(error) {
    switch(error.code) {
      case error.PERMISSION_DENIED:
        locationMessage.value = 'User denied the request for Geolocation.';
        break;
      case error.POSITION_UNAVAILABLE:
        locationMessage.value = 'Location information is unavailable.';
        break;
      case error.TIMEOUT:
        locationMessage.value = 'The request to get user location timed out.';
        break;
      case error.UNKNOWN_ERROR:
        locationMessage.value = 'An unknown error occurred.';
        break;
    }
  };

  const uploadFile = async () => {
    try {
      // 위치 정보 가져오기
      const position = await getLocation();
      showPosition(position);
    } catch (error) {
      showError(error);
    }
    
  }

  return {
    isRecording,
    audioUrl,
    audioBlob,
    activeBars,
    analyser,
    dataArray,
    bufferLength,
    stream,
    javascriptNode,
    isPlaying,
    uploadFile,
  };
});
