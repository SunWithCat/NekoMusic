import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Layout from '@/views/Layout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path:'/',
      name:'layout',
      component: Layout,
      children: [
        {
          path: '',
          name: 'home',
          component: () => import('../views/HomeView.vue')
        },
        {
          path: 'playlist/:id',
          name: 'playlist-detail',
          component: () => import('../views/PlaylistDetail.vue')
        },{
          path: 'search',
          name: 'search',
          component: () => import('../views/SearchView.vue')
        }
      ]
    }
  ],
})

export default router
