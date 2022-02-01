<template>
  <nav class="navbar navbar-expand-lg navbar-dark sticky-top bg-primary">
    <div class="container">

      <router-link to="/" class="navbar-brand">Home</router-link>

      <div class="collapse navbar-collapse">
        <ul class="navbar-nav">
          <li class="nav-item">
            <router-link to="/links" class="nav-link active">Links</router-link>
          </li>
        </ul>
      </div>

      <div>
        <ul v-if="!user" class="navbar-nav me-auto mb-2 mb-md-0">
          <li class="nav-item">
            <router-link to="/auth" class="nav-link">Login</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/register" class="nav-link">Register</router-link>
          </li>
        </ul>
        <ul v-else class="navbar-nav me-auto mb-2 mb-md-0">
          <li class="nav-item">
            <a @click = "logoutHandler" href="javascript:void(0)" class="nav-link">Logout</a>
          </li>
        </ul>
      </div>

    </div>
  </nav>
</template>
<script>
import {mapGetters} from 'vuex'
export default {
  name: 'Nav',
  computed:{
    ...mapGetters(['user'])
  },
  methods:{
    logoutHandler(){
      localStorage.removeItem('token')
      this.$store.dispatch('user', null)
      this.$router.push('/')
    }
  }
}
</script>