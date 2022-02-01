<template>
  <div class="form-group">
    <form @submit.prevent="onSubmit">

      <error v-if="error" :error="error" />

      <h1 class="h3 mb-3 fw-normal">Please sign in</h1>

      <div class="mb-3">
        <label>Email</label>
        <input v-model="email" type="email" class="form-control">
      </div>

      <div class="mb-4">
        <label>Password</label>
        <input v-model="password" type="password" class="form-control">
      </div>

      <button class="w-100 btn btn-lg btn-primary" type="submit">Sign in</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios'
import Error from './Error'

export default {
  name: 'Login',
  components:{
    Error
  },
  data(){
    return{
      email: '',
      password: '',
      error: ''
    }
  },
  methods: {
    async onSubmit(){
      try{
        const resp = await axios.post('auth', {
          email: this.email,
          password: this.password
        })
        localStorage.setItem('token', resp.data.token)
        await this.$store.dispatch('user', resp.data.email)
        await this.$router.push('/links')
      } catch (e){
        this.email = ''
        this.password = ''
        this.error = 'Invalid email or password'
      }
      this.email = ''
      this.password = ''
    }
  }
}
</script>
<style lang="scss">
</style>