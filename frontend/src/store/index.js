import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'
import createPersistedState from 'vuex-persistedstate'

Vue.use(Vuex)
export default new Vuex.Store({
  plugins: [createPersistedState({
    storage: window.sessionStorage,
  })],
  state:{
    user: null,
    links: []
  },
  mutations:{
    user (state, user) {
      state.user = user;
    },
    setLinksToState: (state, payload) => {
      const linksMap = [...payload.data.data];
      state.links = Object.values(linksMap).sort((a, b) => b.id - a.id);
    }
  },
  actions:{
    user (context, user) {
      context.commit('user', user);
    },
    getLinksFromApi({commit}){
      console.log("here1");
      return axios.get('/links', {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('token')
        }
      }).then((result) => {
        console.log(result);
        commit('setLinksToState', result);
        return result;
      }).then((error) => {
        console.log(error);
        return error;
      })
    }
  },
  getters: {
    user: (state) =>{
      return state.user;
    },
    links: (state) => {
      return state.links;
    }
  }
})