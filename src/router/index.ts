import { createRouter, createWebHistory } from 'vue-router'
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
        },
        {
          path: 'song/:id',
          name: 'song-detail',
          component: () => import('../views/SongDetailView.vue')
        },
        {
          path: 'search',
          name: 'search',
          component: () => import('../views/SearchView.vue')
        }
      ]
    }
  ],
})

export default router
