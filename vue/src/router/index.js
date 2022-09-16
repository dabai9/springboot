import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "../store";
// import Home from '../views/Manage.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Manage',
        component: () => import('../views/Manage'),
        redirect: "/home",
        children: [
            {path: 'home', name: '首页', component: () => import('../views/Home')},
            {path: 'user', name: '用户管理', component: () => import('../views/User')},
            {path: 'person', name: '个人信息', component: () => import('../views/Person')},
        ]
    },
    {
        path: '/about',
        name: 'About',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login')
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('../views/Register')
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

//路由守卫
router.beforeEach((to, from, next) => {
    localStorage.setItem('currentPathName', to.name) //设置当前的路由名称
    store.commit("setPath") //触发数据更新
    next() //放行路由
})

export default router
