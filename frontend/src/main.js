import { createApp } from 'vue'
import App from './App.vue'
import router from './router/router'
import { PrimeVue } from '@primevue/core'
import { ToastService } from 'primevue'
import Aura from '@primevue/themes/aura'

const app = createApp(App)

app.use(router)
app.use(PrimeVue, { theme: { preset: Aura } })
app.use(ToastService)

app.mount('#app')
