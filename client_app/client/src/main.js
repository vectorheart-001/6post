import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import 'vuetify/styles'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import { createVuetify } from 'vuetify'

const vuetify = createVuetify({ components, directives })

export default {
    enhanceApp({ app }) {
        app.use(vuetify)
    },
}


createApp(App).use(store).use(router).use(vuetify).mount('#app')
