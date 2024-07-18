<template>
    <v-app class="black-background">
        <v-container class="custom-container">
            <top>
                <naver-map style="width: 100%; height: 400px" :mapOptions="mapOptions" :initLayers="initLayers"
                    @onLoad="onLoadMap($event)">
                    <my-position>
                        <naver-marker v-if="nowPosition" :latitude="nowPosition.latitude"
                            :longitude="nowPosition.longitude" :icon="{ url: myMarkerImage }"
                            @onLoad="onLoadMyMarker($event)">
                        </naver-marker>
                        <naver-circle v-if="nowPosition" :latitude="nowPosition.latitude"
                            :longitude="nowPosition.longitude" :radius="limitedRadius" @onLoad="onLoadCircle($event)" />
                    </my-position>
                    <around-voice v-for="(voice, index) in randomVoiceMarkers" :key="voice.id">
                        <naver-marker :latitude="voice.latitude" :longitude="voice.longitude"
                            @onLoad="onLoadMarker($event, index)" @click="selectMarker(voice, index)">
                        </naver-marker>

                        <naver-info-window :marker="voiceMarkerMetas[index]" :open="infoWindowisOpen[index]">
                            <div class="infowindow-style">정보 ==> {{ voice }}</div>
                        </naver-info-window>
                    </around-voice>
                </naver-map>
            </top>

            <bottom>
                <v-row>
                    <v-col>
                        <h2>주변 음성 목록</h2>
                    </v-col>
                    <v-col class="text-right">
                        반경 1km
                    </v-col>
                </v-row>
                <voiceies :v-if="aroundVoices">
                    <v-infinite-scroll height="400" @load="loadInfinite">
                        <template v-for="(voice)  in randomVoiceMarkers" :key="voice">
                            <div>
                                voice #{{ voice }}
                            </div>
                        </template>

                        <template v-slot:loading>
                            <v-progress-circular color="primary" indeterminate></v-progress-circular>
                        </template>

                        <template v-slot:empty>
                            <v-alert type="info">모두 다 발견되었어요 🌈</v-alert>
                        </template>
                    </v-infinite-scroll>
                </voiceies>
            </bottom>
        </v-container>
    </v-app>
</template>


<script setup>
import myMarkerImage from '@/assets/map/my-marker.png';
import { computed, ref, watch } from "vue";
import { NaverCircle, NaverInfoWindow, NaverMap, NaverMarker } from "vue3-naver-maps";

const map = ref(null);
const myMarker = ref(null);
const nowPosition = ref({ latitude: 37.51347, longitude: 127.041722 });
const limitedRadius = ref(1000);
const circle = ref(null);

const randomVoiceMarkers = computed(() => {
    return aroundVoices.value;
});

const flag = ref(true);
let aroundVoices = ref(null);

let randomVoice = ref([]);
randomVoice.value.push({ id: 1, title: '가', latitude: 37.522875557050604, longitude: 126.73707428498797 });
randomVoice.value.push({ id: 2, title: '나', latitude: 37.5216328910883, longitude: 126.73739538716526 });
randomVoice.value.push({ id: 3, title: '다', latitude: 37.52031663531412, longitude: 126.7370380163574 });
randomVoice.value.push({ id: 4, title: '라', latitude: 37.52070632570098, longitude: 126.7421496981059 });

let randomVoice2 = ref([]);
randomVoice2.value.push({ id: 5, title: '마', latitude: 37.50186257688321, longitude: 127.03732898332551 });
randomVoice2.value.push({ id: 6, title: '바', latitude: 37.50116063547292, longitude: 127.03455789540513 });
randomVoice2.value.push({ id: 7, title: '사', latitude: 37.50036670550219, longitude: 127.03796154358065 });
randomVoice2.value.push({ id: 8, title: '아', latitude: 37.499682460030634, longitude: 127.03631009579408 });

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
    zoomControlOptions: { position: "TOP_RIGHT" },
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
    myMarker.value.setIcon({ url: myMarkerImage });
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
            nowPosition.value = { latitude: lat, longitude: lng };
            const currentLocation = new naver.maps.LatLng(lat, lng);
            if (map.value) {
                map.value.setCenter(currentLocation);
            }
            console.log("ddddddd");
        }, (error) => {
            alert('위치 엑세스를 허용해주세요. 그리고 새로고침을 해주셔야해요.');
        });
    } else {
        alert('해당 브라우저에서는 서비스 이용에 제약이 있습니다. Chrome 브라우저를 이용해주세요');
    }
};

const voiceMarkerMetas = ref([]);
const infoWindowisOpen = ref([]);
const onLoadMarker = (markerObject, index) => {
    voiceMarkerMetas.value[index] = markerObject
    infoWindowisOpen.value[index] = false;
};


const selectedVoiceMarker = ref();
const selectMarker = (voice, index) => {
    infoWindowisOpen.value[index] = !infoWindowisOpen.value[index];
    selectedVoiceMarker.value = voice;
};

const loadInfinite = ({ done }) => {
    setTimeout(() => {
        done('empty')
    }, 2000);
}
</script>
<style scoped>
.infowindow-style {
    color: black;
    background-color: white;
    text-align: center;
    font-weight: 600;
    font-size: 20px;
    padding: 6px 8px;
}
</style>
