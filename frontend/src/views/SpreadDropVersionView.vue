<template>
  <v-app class="black-background">
    <v-container class="custom-container">
      <main>
        <div>
        <v-row>
          <v-col>
            <h2>음성뿌리기</h2>
          </v-col>
        </v-row>
        </div>
        <div class="">
          <v-button :class="{ active: activeTab === 'pokemon' }" @click="setTab('pokemon')">포켓몬</v-button>
          <v-button :class="{ active: activeTab === 'virus' }" @click="setTab('virus')">바이러스</v-button>
        </div>

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
        </naver-map>
      </main>

      <main>
        <div>
          <v-row>
            <v-col>
              <h2>음성뿌리기</h2>
            </v-col>
          </v-row>
          <section>
            <v-row>
                  <!-- 여기에 버튼 두개 이동-->
            </v-row>
          </section>

        </div>
      </main>
    </v-container>
  </v-app>
</template>

<script setup>
import {useSpreadStore} from '@/store/spread.js'
import RecordButton from '@/components/spread/RecordButton.vue'
import UploadButton from '@/components/spread/UploadButton.vue'
import {storeToRefs} from 'pinia'

const spreadStore = useSpreadStore()
const {activeTab} = storeToRefs(spreadStore)
const {setTab} = spreadStore
</script>

<style scoped>
.custom-container {
  max-width: 800px;
  margin: 0 auto;
  padding-bottom: 80px;
}

.tab-container button {
  background: none;
  border: none;
  color: #999;
  font-size: 16px;
  cursor: pointer;
  padding: 10px 20px;
  border-radius: 10px;
}
.tab-container {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
  background-color: #333;
  padding: 5px;
  border-radius: 10px;
}
.tab-container button.active {
  color: #1e1e1e;
  background-color: white;
  font-weight: bold;
  border: 2px solid white;
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

.tab-container button:not(.active) {
  color: white;
  border: 2px solid transparent;
}
</style>
