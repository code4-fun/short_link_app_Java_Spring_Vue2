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
    email: null,
    firstName: null,
    links: []
  },
  mutations:{
    email (state, email) {
      state.email = email;
    },
    firstName (state, firstName) {
      state.firstName = firstName;
    },
    setLinksToState: (state, payload) => {
      const linksMap = [...payload.data.data];
      state.links = Object.values(linksMap).sort((a, b) => b.id - a.id);
    },
    updateFollows: (state, payload) => {
      let links = [...state.links];
      let index = links.findIndex(item => item.hash === payload.hash);
      if(index !== -1){
        links[index].follows = payload.follows;
        links[index].uniqueFollows = payload.uniqueFollows;
      }
      state.links = links;
    }
  },
  actions:{
    email (context, email) {
      context.commit('email', email);
    },
    firstName (context, firstName) {
      context.commit('firstName', firstName);
    },
    getLinksFromApi({commit}){
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
    },
    refreshFollows(context, param){
      context.commit('updateFollows', param);
    }
  },
  getters: {
    email: (state) =>{
      return state.email;
    },
    firstName: (state) =>{
      return state.firstName;
    },
    links: (state) => {
      return state.links;
    }
  }
})