<template>
  <div id="app">
    <Nav/>
    <div class="main-container">
      <router-view :key = 'componentKey' />
    </div>
  </div>
</template>

<script>
import Nav from "@/components/Nav";

export default {
  name: 'App',
  components:{
    Nav
  },
  data() {
    return {
      componentKey: 0,
    };
  },
  beforeMount() {
    this.$root.$on('forceRenderAppComponent', () => {
      this.componentKey = this.componentKey + 1;
    });
  }
}
</script>

<style lang="scss">
.main-container {
  display: flex;
  justify-content: center;
  width: 100%;
}
.form-group {
  width: 330px;
  margin-top: 10%;
}
.main-container .form-control {
  position: relative;
  box-sizing: border-box;
  height: auto;
  padding: 10px;
  font-size: 16px;
}
.main-container .form-control:focus {
  z-index: 2;
}
.main-container input[type="email"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}
.main-container input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}
</style>