<template>
  <v-app class="bg-black">
    <v-main>
      <v-container class="custom-container">
        <v-row justify="center" class="py-4">
          <v-col cols="12" class="text-center">
            <h1 class="title">주변 음성 듣기</h1>
          </v-col>
        </v-row>
        <v-row>
          <naver-map style="width: 100%; height: 400px; margin-bottom: 20px;"
                     :mapOptions="mapOptions"
                     :initLayers="initLayers"
                     @onLoad="onLoadMap">
            <naver-marker v-if="nowPosition"
                          :latitude="nowPosition.latitude"
                          :longitude="nowPosition.longitude"
                          :icon="{ url: MY_POSITION_IMAGE }"
                          @onLoad="onLoadMyMarker">
            </naver-marker>
            <naver-circle v-if="nowPosition"
                          :latitude="nowPosition.latitude"
                          :longitude="nowPosition.longitude"
                          :radius="limitedRadius"
                          @onLoad="onLoadCircle"/>
            <naver-marker v-for="(voice, index) in aroundVoices"
                          :key="voice.id"
                          :latitude="voice.latitude"
                          :longitude="voice.longitude"
                          @onLoad="(marker) => onLoadMarker(marker, index)"
                          @click="selectMarker(voice, index)">
            </naver-marker>
            <naver-info-window v-for="(voice, index) in aroundVoices"
                               :key="index"
                               :marker="voiceMarkerMetas[index]"
                               :open="isOpenMarker[index]">
            <div class="custom-infowindow">
            <h3>{{ voice.title }}</h3>
            </div>
            </naver-info-window>
            </naver-map>
        </v-row>
        <v-row justify="space-between" class="py-4">
          <v-col cols="8">
            <h2>음성 목록</h2>
          </v-col>
          <v-col cols="4">
            <v-select class="radius-select"
                      :items="radiusCandidates"
                      item-title="distance"
                      label="반경"
                      v-model="selectedRadius"
                      @change="updateRadius">
            </v-select>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12">
  <v-card class="mb-4 list-items"
        elevation="3"
        v-for="(item, index) in aroundVoices"
        :key="index"
        @click="selectMarker(item, index); scrollToTop()"
        color="black">
  <v-row no-gutters>
    <v-col cols="4">
      <v-img :src="item.imageUrl" height="150px" contain></v-img>
    </v-col>
    <v-col cols="8">
      <v-card-title>
        <div class="content">
          <h3 class="item-title">{{ item.title }}</h3>
          <div class="location-info">
            <v-icon small class="mdi mdi-map-marker"></v-icon>
            <p>{{ item.latitude }} | {{ item.longitude }}</p>
          </div>
          <div class="stats">
            <div class="stat-item">
              <v-icon small class="mr-2">mdi-thumb-up</v-icon>
              <span>{{ item.heartCount }}</span>
            </div>
            <div class="stat-item">
              <v-icon small class="mr-2">mdi-headphones</v-icon>
              <span>{{ item.listenCount }}</span>
            </div>
            <v-btn 
  v-if="!item.picked"
  @click="pickItem(item); togglePick" 
  color="black" 
  class="pick-button"
>
  <v-icon left>mdi-hand-pointing-right</v-icon> 줍기
</v-btn>
<v-btn 
  v-else
  disabled 
  color="grey" 
  class="pick-button"
>
  <v-icon left color="black">mdi-check</v-icon> 이미 줍기 완료
</v-btn>
          </div>
        </div>
      </v-card-title>
    </v-col>
  </v-row>
</v-card>

          </v-col>
        </v-row>

        <v-row justify="center">
          <v-progress-circular v-if="loading" color="primary" indeterminate></v-progress-circular>
          <v-dialog v-else-if="aroundVoices.length === 0" type="info">주변에 음성이 없습니다.</v-dialog>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
import MY_POSITION_IMAGE from '@/assets/map/my-marker.png';
import { ref, watch } from "vue";
import { NaverCircle, NaverInfoWindow, NaverMap, NaverMarker } from "vue3-naver-maps";
import axios from 'axios';

const map = ref(null);
const myMarker = ref(null);
const nowPosition = ref(null);  
const limitedRadius = ref(1000);
const circle = ref(null);
const selectedRadius = ref('1km (default)');
const aroundVoices = ref([]);
const voiceMarkerMetas = ref([]);
const isOpenMarker = ref([]);
const loading = ref(false);
const pick = ref(false);

const getNearbyVoices = async (latitude, longitude, radius) => {
  try {
    loading.value = true;
    const token = sessionStorage.getItem('access-token')
    const response = await axios.get(`${import.meta.env.VITE_BASE_URL}/api-voice/nearby`, {
      headers: { Authorization: `Bearer ${token}` },
      params: {
        latitude: latitude,
        longitude: longitude,
        radius: radius
      },
    });
    aroundVoices.value = response.data.map(voice => {
      return {
        ...voice,
        picked: voice.picked
      }
    });
    console.log(aroundVoices.value)
    loading.value = false;
  } catch (error) {
    console.error('Error fetching nearby voices:', error);
    loading.value = false;
  }
};

const onLoadMap = (mapObject) => {
  map.value = mapObject;
  moveNowPosition();
};

const scrollToTop = () => {
  window.scrollTo({
    top: 0, behavior: 'smooth'
  });
};

const onLoadMyMarker = (markerObject) => {
  myMarker.value = markerObject;
  myMarker.value.setIcon({ url: MY_POSITION_IMAGE });
  myMarker.value.setAnimation(naver.maps.Animation.BOUNCE);
};

const onLoadCircle = (circleObject) => {
  circle.value = circleObject;
  updateCircleRadius();
};

const updateCircleRadius = () => {
  if (circle.value && nowPosition.value) {
    circle.value.setRadius(limitedRadius.value);
    circle.value.setCenter(new naver.maps.LatLng(nowPosition.value.latitude, nowPosition.value.longitude));
  }
};

const updateMapZoomAndCenter = () => {
  if (map.value && nowPosition.value) {
    const radiusOption = radiusCandidates.find(option => option.distance === selectedRadius.value);
    if (radiusOption) {
      map.value.setZoom(radiusOption.zoom);
      map.value.panTo(new naver.maps.LatLng(nowPosition.value.latitude, nowPosition.value.longitude));
    }
  }
};

const onLoadMarker = (markerObject, index) => {
  voiceMarkerMetas.value[index] = markerObject;
  isOpenMarker.value[index] = false;
};

const selectMarker = (voice, index) => {
  isOpenMarker.value[index] = !isOpenMarker.value[index];
};

const moveNowPosition = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition((position) => {
      nowPosition.value = { latitude: position.coords.latitude, longitude: position.coords.longitude };
      map.value.setCenter(new naver.maps.LatLng(nowPosition.value.latitude, nowPosition.value.longitude));
      updateCircleRadius();
      updateMapZoomAndCenter();
      getNearbyVoices(nowPosition.value.latitude, nowPosition.value.longitude, limitedRadius.value);
    }, () => {
      alert('위치 엑세스를 허용해주세요. 새로고침 해주세요.');
    });
  } else {
    alert('해당 브라우저에서는 서비스 이용에 제약이 있습니다. Chrome 브라우저를 이용해주세요.');
  }
};

watch(selectedRadius, (newRadius) => {
  const radiusOption = radiusCandidates.find(option => option.distance === newRadius);
  if (radiusOption) {
    limitedRadius.value = getRadiusFromZoom(radiusOption.zoom);
    updateCircleRadius();
    updateMapZoomAndCenter();
    getNearbyVoices(nowPosition.value.latitude, nowPosition.value.longitude, limitedRadius.value);
  }
});

const getRadiusFromZoom = (zoom) => {
  switch (zoom) {
    case 14: return 1000;
    case 13: return 2000;
    case 12: return 3000;
    case 11: return 5000;
    case 10: return 10000;
    case 9: return 20000;
    default: return 1000;
  }
};

const radiusCandidates = [
  { distance: '1km (default)', zoom: 14 },
  { distance: '2km', zoom: 13 },
  { distance: '3km', zoom: 12 },
  { distance: '5km', zoom: 11 },
  { distance: '10km', zoom: 10 },
  { distance: '20km', zoom: 9 }
];

const pickItem = async (item) => {
  if (item.picked) {
    alert(`${item.title} 음성은 이미 주웠습니다.`)
    return;
  } else {
    try {
      const success = await togglePick(item);
      if (success) {
        item.picked = true;
        alert(`${item.title}을(를) 주웠습니다! 보관함을 확인해주세요`);
      } else {
        console.error("error");
      }
    } catch (error) {
      console.error('Error picking item:', error);
    }
  }
};

const togglePick = async (item) => {
  const token = sessionStorage.getItem('access-token');
  if (!token) {
    console.error('JWT 토큰이 없습니다.');
    return;
  }
  try {
    const url = `${import.meta.env.VITE_BASE_URL}/api-voice/${item.voiceId}/pick`;

    const response = await axios.post(url, null, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    if (response.status === 200 && response.data) {
      pick.value = response.data.isPicked;
      return pick.value;
    } else {
      console.error('서버에서 응답이 제대로 오지 않았습니다.');
      return false;
    }
  } catch (error) {
    console.error('Error like:', error);
  }
};

</script>

<style scoped>
.custom-container {
  max-width: 800px;
  margin: 0 auto;
  padding-bottom: 80px;
}

.custom-infowindow {
  background-color: #333;
  color: #fff;
  padding: 10px;
  border-radius: 8px;
  text-align: center;
  cursor: pointer;
}

.custom-infowindow h3 {
  margin: 0;
  font-size: 16px;
  font-weight: bold;
}

h3 {
  color: #fff;
  margin: 0;
}

.radius-select {
  max-width: 100%;
}

@media (max-width: 600px) {
  .custom-container {
    padding: 0 10px;
  }
  .infowindow-style {
    font-size: 16px;
  }
  h3 {
    font-size: 14px;
  }
}

.v-card {
  background-color: #000 !important;
  color: #fff;
}

.content {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100%;
  padding: 10px;
}

.item-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #fff;
}

.location-info {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #ccc;
  margin-bottom: 10px;
}

.location-info v-icon {
  margin-right: 5px;
  color: #ccc;
}

.stats {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  margin-top: auto;
}

.stat-item {
  display: flex;
  align-items: center;
  margin-right: 20px;
  font-size: 14px;
  color: #ccc;
}

.stat-item v-icon {
  margin-right: 5px;
  color: #ccc;
}

.pick-button {
  position: absolute; 
  right: 10px; 
  bottom: 10px; 
  background-color: #4caf50; 
  color: white;
  border-radius: 8px;
  font-weight: bold;
}

.pick-button v-icon {
  margin-right: 5px;
  color: white;
}
</style>
