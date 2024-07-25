<template>
  <v-app class="black-background">
    <v-container class="custom-container">
      <main>
        <naver-map style="width: 100%; height: 400px" :mapOptions="mapOptions" :initLayers="initLayers"
                   @onLoad="onLoadMap($event)">
          <section>
            <naver-marker v-if="nowPosition" :latitude="nowPosition.latitude"
                          :longitude="nowPosition.longitude" :icon="{ url: MY_POSITION_IMAGE }"
                          @onLoad="onLoadMyMarker($event)">
            </naver-marker>
            <naver-circle v-if="nowPosition" :latitude="nowPosition.latitude"
                          :longitude="nowPosition.longitude" :radius="limitedRadius" @onLoad="onLoadCircle($event)"/>
          </section>

          <section v-for="(voice, index) in randomVoiceMarkers" :key="voice.id">
            <naver-marker :latitude="voice.latitude" :longitude="voice.longitude"
                          @onLoad="onLoadMarker($event, index)" @click="selectMarker(voice, index)">
            </naver-marker>

            <naver-info-window :marker="voiceMarkerMetas[index]" :open="isOpenMarker[index]">
              <div class="infowindow-style">정보 ==> {{ voice }}</div>
            </naver-info-window>
          </section>
        </naver-map>
      </main>

      <main>
        <div>
          <v-row>
            <v-col>
              <h2>주변 음성 목록</h2>
            </v-col>
            <v-select aria-selected="true" class="text-right" :items="radiusCandidates" item-title="distance" label="반경"
                      :max-width="200">
              <v-list-item item></v-list-item>
            </v-select>
          </v-row>
          <section>
            <v-row>
              <v-col cols="12">
                <v-infinite-scroll @load="loadInfinite">
                  <v-card class="mb-4 list-items" elevation="2" v-for="(item, index) in aroundVoices" :key="index">
                    <v-row no-gutters @click="selectMarker(item,index)">
                      <v-col cols="4">
                        <v-img :src="THUMBNAIL_IMAGE" height="150px" contain></v-img>
                      </v-col>
                      <v-col cols="8">
                        <v-card-title>
                          <div class="content">
                            <v-row class="mb-6">
                              <v-col cols="4">
                                <h3 class="pa-2 ma-2"> {{item.title}} </h3>
                              </v-col>
                              <v-col cols="4" offset="4">
                                <button class="pa-2 ma-2" @click="listen"> 듣기 </button>
                              </v-col>
                            </v-row>
                            <p> {{item.latitude}} 위도</p>
                            <p>{{ item.longitude }} 경도</p>
                          </div>
                        </v-card-title>
                      </v-col>
                    </v-row>
                  </v-card>

                  <template v-slot:loading>
                    <v-progress-circular color="primary" indeterminate></v-progress-circular>
                  </template>

                  <template v-if="myMarker" v-slot:empty>
                    <v-alert type="info">모두 다 발견되었어요 🌈</v-alert>
                  </template>
                </v-infinite-scroll>
              </v-col>
            </v-row>
          </section>
        </div>
      </main>
    </v-container>
  </v-app>
</template>


<script setup>
import MY_POSITION_IMAGE from '@/assets/map/my-marker.png';
import THUMBNAIL_IMAGE from '@/assets/images/thumb2.jpg';
import {computed, ref, watch} from "vue";
import {NaverCircle, NaverInfoWindow, NaverMap, NaverMarker} from "vue3-naver-maps";

const map = ref(null);
const myMarker = ref(null);
const nowPosition = ref({latitude: 37.51347, longitude: 127.041722});
const limitedRadius = ref(1000);
const circle = ref(null);

const randomVoiceMarkers = computed(() => {
  return aroundVoices.value;
});

const flag = ref(true);
let aroundVoices = ref(null);

let randomVoice = ref([]);
randomVoice.value.push({id: 1, title: '가', latitude: 37.522875557050604, longitude: 126.73707428498797});
randomVoice.value.push({id: 2, title: '나', latitude: 37.5216328910883, longitude: 126.73739538716526});
randomVoice.value.push({id: 3, title: '다', latitude: 37.52031663531412, longitude: 126.7370380163574});
randomVoice.value.push({id: 4, title: '라', latitude: 37.52070632570098, longitude: 126.7421496981059});

let randomVoice2 = ref([]);
randomVoice2.value.push({id: 5, title: '마', latitude: 37.50186257688321, longitude: 127.03732898332551});
randomVoice2.value.push({id: 6, title: '바', latitude: 37.50116063547292, longitude: 127.03455789540513});
randomVoice2.value.push({id: 7, title: '사', latitude: 37.50036670550219, longitude: 127.03796154358065});
randomVoice2.value.push({id: 8, title: '아', latitude: 37.499682460030634, longitude: 127.03631009579408});

const getNearByVoice = async (lat, lng) => {
  try {
    // const response = await axios.get(`http://localhost:8080?lat=${lat}&lgt=${lng}`);
    setTimeout(() => {
      if (!flag.value) {
        aroundVoices.value = randomVoice.value;
      } else {
        aroundVoices.value = randomVoice2.value;
      }
    }, 2000);
  } catch (error) {
    alert('관리자에게 문의해주세요. 죄송해요')
  }
};

watch(nowPosition, (newPosition) => {
  if (myMarker.value) {
    myMarker.value.setPosition(new naver.maps.LatLng(newPosition.latitude, newPosition.longitude));
  }
  if (circle.value) {
    circle.value.setCenter(new naver.maps.LatLng(newPosition.latitude, newPosition.longitude));
    circle.value.setRadius = limitedRadius.value;
  }
  if (map.value) {
    map.value.setCenter(new naver.maps.LatLng(newPosition.latitude, newPosition.longitude));
  }

  nowPosition.value = newPosition;
  getNearByVoice(newPosition.latitude, newPosition.longitude);
})

const mapOptions = {
  latitude: 37.51347,
  longitude: 127.041722,
  zoom: 14,
  minZoom: 14,
  zoomControl: false,
  zoomControlOptions: {position: "TOP_RIGHT"},
};

const initLayers = [
  "BACKGROUND",
  "BACKGROUND_DETAIL",
  "POI_KOREAN",
  "TRANSIT",
  "ENGLISH",
];

const onLoadMap = (mapObject) => {
  map.value = mapObject;
  moveNowPosition();
};

const onLoadMyMarker = (markerObject) => {
  myMarker.value = markerObject;
  myMarker.value.setIcon({url: MY_POSITION_IMAGE});
  myMarker.value.setAnimation(naver.maps.Animation.BOUNCE);
};

const onLoadCircle = (circleObject) => {
  circle.value = circleObject
};

const moveNowPosition = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition((position) => {
      const lat = position.coords.latitude;
      const lng = position.coords.longitude;
      nowPosition.value = {latitude: lat, longitude: lng};
      const currentLocation = new naver.maps.LatLng(lat, lng);
      if (map.value) {
        map.value.setCenter(currentLocation);
      }
    }, (error) => {
      alert('위치 엑세스를 허용해주세요. 그리고 새로고침을 해주셔야해요.');
    });
  } else {
    alert('해당 브라우저에서는 서비스 이용에 제약이 있습니다. Chrome 브라우저를 이용해주세요');
  }
};

const listen = ()=>{
  alert('listen')
}

const voiceMarkerMetas = ref([]);
const isOpenMarker = ref([]);
const onLoadMarker = (markerObject, index) => {
  voiceMarkerMetas.value[index] = markerObject
  isOpenMarker.value[index] = false;
};

const selectedVoiceMarker = ref();
const selectMarker = (voice, index) => {
  isOpenMarker.value[index] = !isOpenMarker.value[index];
  selectedVoiceMarker.value = voice;
};

const loadInfinite = ({done}) => {
  setTimeout(() => {
    done('empty')
  }, 2000);
}

const radiusCandidates = [
  {
    distance: '1km (default)',
    isPurchased: 'true'
  },
  {
    distance: '5km',
    isPurchased: 'false',

  },
  {
    distance: '10km',
    isPurchased: 'false',
  },
]
</script>

<style scoped>
.custom-container {
  max-width: 800px;
  margin: 0 auto;
  padding-bottom: 80px;
}

.infowindow-style {
  color: black;
  background-color: white;
  text-align: center;
  font-weight: 600;
  font-size: 20px;
  padding: 6px 8px;
}

h3 {
  color: #fff;
  margin: 0;
}

p {
  color: #ccc;
  margin: 0;
}
button {
  color: #ccc;
  margin: 0;
}


.list-items {
  background-color: #000;
}
</style>
