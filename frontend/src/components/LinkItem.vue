<template>
  <tr>
    <td>{{ linkItem.link }}</td>
    <td>{{ linkItem.originUrl }}</td>
    <td>{{ linkItem.dateOfExpiration }}</td>
    <td>{{ linkItem.follows }}</td>
    <td>{{ linkItem.uniqueFollows }}</td>
    <td><button class="btn" @click="deleteLink(linkItem.hash)">Delete</button></td>
  </tr>
</template>

<script>
  import axios from "axios";

  export default {
    name: "LinkItem",
    props: {
      linkItem: {
        type: Object,
        default() {
          return {}
        }
      }
    },
    methods: {
      async deleteLink(hash){
        console.log("delete " + hash);
        await axios.delete('/links', {
          params: {
            hash: hash
          },
          headers:{
            Authorization: 'Bearer ' + localStorage.getItem('token')
          }
        })
        this.$root.$emit('forceRenderAppComponent');
      }
    }
  }
</script>

<style lang="scss">
.note-item {
  width: 400px;
  height: 200px;
}
</style>