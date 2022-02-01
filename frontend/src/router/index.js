import Vue from 'vue'
import Router from 'vue-router'
import Home from "@/components/Home";
import Login from "@/components/Login";
import Register from "@/components/Register";
import LinkList from "@/components/LinkList";

Vue.use(Router)

export default new Router({
    mode: 'history',
    routes:[
        {path: '/', component: Home},
        {path: '/auth', component: Login},
        {path: '/links', component: LinkList},
        {path: '/register', component: Register}
    ]
})