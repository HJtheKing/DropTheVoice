<template>
  <v-app class="black-background">
    <v-main>
      <v-container class="custom-container">
        <v-row justify="space-between" class="upper-content py-4">
          <h1 class="title">실시간 녹음하기</h1>
          <v-icon @click="saveRecord">mdi-upload</v-icon>
        </v-row>
        <v-row justify="center">
          <v-col cols="12" sm="8" md="6">
            <CurrentTime />
          </v-col>
        </v-row>
        <MyTimer />
        <MyRecordButton ref="myRecordButton" />
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
import { ref } from 'vue';
import CurrentTime from '@/components/record/CurrentTime.vue';
import MyTimer from '@/components/record/MyTimer.vue';
import MyRecordButton from '@/components/record/MyRecordButton.vue';
import { useSpreadStore } from '@/store/spread';
import * as lamejs from '@breezystack/lamejs';
import axios from 'axios';

const myRecordButton = ref(null);
const locationMessage = ref('');
const spreadStore = useSpreadStore();

let mp3Blob = null; // mp3 변환된 오디오 데이터를 저장할 Blob
let mp3Url = null; // mp3 변환 후 url

const saveRecord = async () => {
  if (myRecordButton.value) {
    const audioBlob = myRecordButton.value.getAudioBlob(); // MyRecordButton의 getAudioBlob 메서드 호출
    if (!audioBlob) return;

    mp3Blob = await convertWavToMp3(audioBlob);
    mp3Url = URL.createObjectURL(mp3Blob);

    // MP3 다운로드
    const downloadLink = document.createElement('a');
    downloadLink.href = mp3Url;
    downloadLink.download = `dropthevoice_음성녹음_${Date.now()}.mp3`;
    downloadLink.click();
    console.log("Started downloading");

    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(showPosition, showError);
    } else {
      locationMessage.value = 'Geolocation is not supported by this browser.';
    }
  }
};

const showPosition = (position) => {
  const latitude = position.coords.latitude;
  const longitude = position.coords.longitude;
  locationMessage.value = `Latitude: ${latitude} | Longitude: ${longitude}`;
  console.log(locationMessage.value);

  // 서버로 위치 데이터 전송
  sendVoiceInfoToServer(latitude, longitude);
};

const showError = (error) => {
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

const sendVoiceInfoToServer = (lat, lon) => {
  const formData = new FormData();
  formData.append('voiceType', spreadStore.activeTab);
  formData.append('latitude', lat);
  formData.append('longitude', lon);
  formData.append('voiceUrl', mp3Url);
  formData.append('voiceFile', mp3Blob);

  axios.post('http://localhost:8080/api-record/record', formData)
    .then(response => {
      console.log('Location sent to server:', response.data);
    })
    .catch(error => {
      console.error('Error sending location to server:', error);
    });
};

async function convertWavToMp3(wavBlob) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = function () {
      const arrayBuffer = this.result;
      const wavHeader = lamejs.WavHeader.readHeader(new DataView(arrayBuffer));
      const wavSamples = new Int16Array(arrayBuffer, wavHeader.dataOffset, wavHeader.dataLen / 2);
      const channels = 1;
      const sampleRate = wavHeader.sampleRate;
      const bitRate = 128;

      const mp3Encoder = new lamejs.Mp3Encoder(channels, sampleRate, bitRate);
      const mp3Data = [];
      const sampleBlockSize = 1152 * 4;

      let mp3Buffer;
      for (let i = 0; i < wavSamples.length; i += sampleBlockSize) {
        mp3Buffer = mp3Encoder.encodeBuffer(wavSamples.subarray(i, i + sampleBlockSize));
        if (mp3Buffer.length > 0) {
          mp3Data.push(new Int8Array(mp3Buffer));
        }
      }

      mp3Buffer = mp3Encoder.flush();
      if (mp3Buffer.length > 0) {
        mp3Data.push(new Int8Array(mp3Buffer));
      }

      const mp3Blob = new Blob(mp3Data, { type: 'audio/mp3' });
      resolve(mp3Blob);
      console.log("Successfully converted to mp3");
    };

    reader.onerror = reject;
    reader.readAsArrayBuffer(wavBlob);
  });
}
</script>


<style scoped>
.custom-container {
  max-width: 800px;
  margin: 0 auto;
  padding-bottom: 80px;
}

.upper-content {
  margin: 10px;
}

.black-background {
  background-color: #000;
  color: #fff;
}

.title {
  color: #fff;
  font-weight: bold;
  font-size: 24px;
}
</style>
