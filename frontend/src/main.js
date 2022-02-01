import Vue from 'vue'
import App from './App.vue'
import router from "@/router";
import axios from "axios";
import store from "@/store/index"
import './assets/styles/styles.scss'

Vue.config.productionTip = false
axios.defaults.baseURL = 'http://localhost:8099'

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')