import HomeView from '@/views/HomeView.vue';
import SpreadView from '@/views/SpreadView.vue';
import UserLogin from "@/views/UserLogin.vue";
import UserSignUp from "@/views/UserSignUp.vue";
import UserMypage from "@/views/UserMypage.vue";
import ExampleView1 from "@/views/ExampleView1.vue";
import ExampleView2 from "@/views/ExampleView2.vue";
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/home',
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView
    },
    {
      path: '/example1',
      name: 'example1',
      component: ExampleView1,
    },
    {
      path: '/example2',
      name: 'example2',
      component: ExampleView2,
    },
    {
      path: '/spread',
      name: 'spread',
      component: SpreadView
    },
    {
      path: "/login",
      name: "login",
      component: UserLogin,
    },
    {
      path: "/signup",
      name: "signup",
      component: UserSignUp,
    },
    {
      path: "/mypage",
      name: "mypage",
      component: UserMypage,
    },
  ]
})

export default router
