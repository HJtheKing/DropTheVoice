import {createPinia} from 'pinia';
import {createApp} from 'vue';

import '@mdi/font/css/materialdesignicons.css'; // MDI 아이콘 폰트 추가
import {createVuetify} from 'vuetify';
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';
import 'vuetify/styles';

import {createNaverMap} from 'vue3-naver-maps';
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios';

const app = createApp(App)
const vuetify = createVuetify({
    components,
    directives,
    icons: {
        defaultSet: 'mdi'
    }
})

app.use(createPinia())
app.use(vuetify)
app.use(router)
app.use(createNaverMap, {clientId: `${import.meta.env.VITE_NAVER_MAP_CLIENT_ID}`, language: 'kr'})
app.config.devtools = false
app.use(store)
app.use(axios)

// 개발 모드에서만 Vue Devtools 활성화
app.config.devtools = false

app.mount('#app')
