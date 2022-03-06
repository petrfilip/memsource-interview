import {createApp} from "vue";
import App from "./App.vue";
import router from "./router";
import {defaultConfig, plugin} from '@formkit/vue'

const app = createApp(App).use(plugin, defaultConfig);

app.use(router);

app.mount("#app");
