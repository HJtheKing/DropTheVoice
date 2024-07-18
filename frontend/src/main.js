import { createPinia } from 'pinia'
import { createApp } from 'vue'

import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import 'vuetify/styles'
import '@mdi/font/css/materialdesignicons.css' // MDI 아이콘 폰트 추가

import App from './App.vue'
import router from './router'
import store from './store'

const app = createApp(App)
const vuetify = createVuetify({
  components,
  directives,
  icons: {
    defaultSet: 'mdi',
  },
})

app.use(createPinia())
app.use(vuetify)
app.use(router)
app.use(store)

// 개발 모드에서만 Vue Devtools 활성화
  app.config.devtools = false


app.mount('#app')

window.global = window;