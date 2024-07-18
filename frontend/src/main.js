import '@mdi/font/css/materialdesignicons.css' // MDI 아이콘 폰트 추가
import 'vuetify/styles'

import { createNaverMap } from 'vue3-naver-maps'

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
app.use(createNaverMap, { clientId: 'dd', language: 'kr' })
app.config.devtools = false

app.mount('#app')

window.global = window
