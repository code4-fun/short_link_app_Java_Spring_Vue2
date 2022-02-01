<template>
  <div class="link-list-container">
    <div v-if="user">
      <div style="font-size: 25px; font-family: 'Raleway', sans-serif; margin: 20px auto">
        Hello, {{ user }}!
      </div>
      <br>
      <link-form></link-form>
      <br>
      <div style="font-size: 25px; font-family: 'Raleway', sans-serif; margin: 20px 0 5px 0">
        Your links
      </div>
      <div v-if="links.length > 0">
        <table class="styled-table">
          <thead>
            <tr>
              <th>Link</th>
              <th>Origin</th>
              <th>Expiration</th>
              <th>Follows</th>
              <th>Unique follows</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <link-item
                v-for='link in links'
                :key='link.hash'
                :linkItem='link'
            />
          </tbody>
        </table>
      </div>
    </div>
    <div v-else style="font-size: 25px; font-family: 'Raleway', sans-serif; margin: 300px auto">
      You are not logged in.
    </div>
  </div>
</template>

<script>
  import {mapGetters, mapActions} from 'vuex'
  import LinkItem from "./LinkItem";
  import LinkForm from "./LinkForm";

  export default {
    name: "LinkList",
    components: {
      LinkItem,
      LinkForm
    },
    data() {
      return{
        response: ''
      }
    },
    computed:{
      ...mapGetters(['user', 'links'])
    },
    methods: {
      ...mapActions(['getLinksFromApi'])
    },
    created() {
      this.getLinksFromApi();
    }
  }
</script>

<style lang="scss">
</style>