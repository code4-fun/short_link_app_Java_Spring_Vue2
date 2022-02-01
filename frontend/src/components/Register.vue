<template>
  <div class="form-group">
    <form @submit.prevent="onSubmit">
      <h1 class="h3 mb-3 fw-normal">Please register</h1>
      <div class="mb-3">
        <label>First Name</label>
        <input v-model="firstName" type="text" class="form-control">
      </div>
      <div class="mb-3">
        <label>Last Name</label>
        <input v-model="lastName" type="text" class="form-control">
      </div>
      <div class="mb-3">
        <label>Email</label>
        <input v-model="email" type="email" class="form-control">
      </div>
      <div class="mb-3">
        <label>Password</label>
        <input v-model="password" type="password" class="form-control">
      </div>
      <div class="mb-4">
        <label>Confirm Password</label>
        <input v-model="passwordConfirm" type="password" class="form-control">
      </div>
      <button class="w-100 btn btn-lg btn-primary" type="submit">Sign in</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Register',
  data(){
    return{
      firstName: '',
      lastName: '',
      email: '',
      password: '',
      passwordConfirm: ''
    }
  },
  methods:{
    async onSubmit(){
      await axios.post('register', {
        firstName: this.firstName,
        lastName: this.lastName,
        email: this.email,
        password: this.password
      })

      await this.$router.push('/auth')

      this.firstName = ''
      this.lastName = ''
      this.email = ''
      this.password = ''
      this.passwordConfirm = ''
    }
  }
}
</script>

<style lang="scss">
</style>