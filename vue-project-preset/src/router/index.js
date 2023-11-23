import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'

Vue.use(VueRouter)

const routes = [
  // {
  //   path: '/',
  //   name: 'home',
  //   component: HomeView
  // },
  {
    path: '/admin',
    redirect:'/admin/home',
    component: () => import( '../views/HomeView.vue'),
    children:[
      {
        path:'home',
        component:()=>import('../views/admin/home/HomeView')
      },
      {
        path:'category',
        component:()=>import('../views/admin/category/CategoryView')
      },
      {
        path:'news',
        component:()=>import('../views/admin/news/NewsView')
      },
      {
        path:'newsDetail',
        component:()=>import('../views/admin/news/NewsDetailView')
      }
    ]
  }

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
