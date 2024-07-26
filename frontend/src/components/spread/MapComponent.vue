<template>
  <naver-map style="width: 100%; height: 400px" :mapOptions="mapOptions" :initLayers="initLayers"
             @onLoad="onLoadMap($event)">
    <naver-marker v-if="nowPosition" :latitude="nowPosition.latitude"
                  :longitude="nowPosition.longitude" :icon="{ url: MY_POSITION_IMAGE }"
                  @onLoad="onLoadMyMarker($event)">
    </naver-marker>

    <naver-marker v-if="nowPosition" :latitude="nowPosition.latitude" :longitude="nowPosition.longitude"
                  @onLoad="onLoadMarker($event)">
    </naver-marker>
    <naver-circle v-if="nowPosition" :latitude="nowPosition.latitude"
                  :longitude="nowPosition.longitude" :radius="limitedRadius" @onLoad="onLoadCircle($event)"/>
  </naver-map>
</template>

<script setup>
import MY_POSITION_IMAGE from "@/assets/map/my-marker.png";
import {NaverCircle, NaverInfoWindow, NaverMap, NaverMarker} from "vue3-naver-maps";
import {ref, watch} from "vue";

const map = ref(null);
const myMarker = ref(null);
const nowPosition = ref({latitude: 37.51347, longitude: 127.041722});
const limitedRadius = ref(1000);
const circle = ref(null);

watch(nowPosition, (newPosition) => {
  if (myMarker.value) {
    myMarker.value.setPosition(new naver.maps.LatLng(newPosition.latitude, newPosition.longitude));
    dropMarker.value.setPosition(new naver.maps.LatLng(newPosition.latitude, newPosition.longitude));
  }
  if (circle.value) {
    circle.value.setCenter(new naver.maps.LatLng(newPosition.latitude, newPosition.longitude));
    circle.value.setRadius = limitedRadius.value;
  }
  if (map.value) {
    map.value.setCenter(new naver.maps.LatLng(newPosition.latitude, newPosition.longitude));
  }

  nowPosition.value = newPosition;
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
  console.log('onLoadMap --->'+map.value)
};

const onLoadMyMarker = (markerObject) => {
  myMarker.value = markerObject;
  myMarker.value.setIcon({url: MY_POSITION_IMAGE});
  myMarker.value.setAnimation(naver.maps.Animation.BOUNCE);
};

const onLoadCircle = (circleObject) => {
  circle.value = circleObject
};

const moveNowPosition = async () => {
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

const dropMarker = ref();
const onLoadMarker = (markerObject) => {
  dropMarker.value = markerObject
  console.log('onLoadMarker --->'+map.value)
};

watch([map, dropMarker], ([newMap, newDropMarker]) => {
  if (newMap && newDropMarker) {
    naver.maps.Event.addListener(map.value,'click', (e) => {
      const clickedPosition = {
        latitude: e.coord.lat(),
        longitude: e.coord.lng()
      };
      const distance = calculate(nowPosition.value, clickedPosition);

      if (distance > 1) {
        alert('해당 위치는 불가영역입니다.')
      }

      dropMarker.value.setPosition(e.coord);
    })
  }
});
const calculate = (coords1, coords2) => {
  const toRad = (value) => value * Math.PI / 180;

  const R = 6371; // Earth radius in kilometers
  const dLat = toRad(coords2.latitude - coords1.latitude);
  const dLon = toRad(coords2.longitude - coords1.longitude);
  const lat1 = toRad(coords1.latitude);
  const lat2 = toRad(coords2.latitude);

  const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

  return R*c;
};


</script>


<style scoped>

</style>