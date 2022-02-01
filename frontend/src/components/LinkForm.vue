<template>
  <div class="link-form">
    <form @submit.prevent="onSubmit" class="row gy-2 gx-3 align-items-center">

      <div class="col-auto">
        <input v-model="link" type="text"
               class="form-control"
               placeholder="Create link for URL"
               style="height: 34px; margin-right: 191px;">
      </div>

      <div class="col-auto">
        <date-picker v-model="date"
                     lang="en"
                     type="date"
                     format="YYYY-MM-DD"
                     placeholder="Expiration date"
                     value-type="YYYY-MM-DD"
                     class="date-picker">
        </date-picker>
      </div>

      <div class="col-auto">
        <button type="submit"
                class="btn btn-primary"
                style="height: 34px; line-height:20px;">Create
        </button>
      </div>

    </form>
  </div>
</template>

<script>
  import DatePicker from "vue2-datepicker"
  import 'vue2-datepicker/index.css';
  import axios from 'axios'

  export default {
    name: "LinkForm",
    components: {
      DatePicker
    },
    data(){
      return {
        link: '',
        date: ''
      }
    },
    methods:{
      async onSubmit () {
        await axios.post('/links', {
          originUrl: this.link,
          dateOfExpiration: this.date
        },{
          headers:{
            Authorization: 'Bearer ' + localStorage.getItem('token')
          }
        })
        this.link = ''
        this.date = ''
        this.$root.$emit('forceRenderAppComponent');
      }
    }
  }
</script>

<style lang="scss">
</style>