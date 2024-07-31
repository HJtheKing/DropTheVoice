<<<<<<< HEAD
import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue';
import SpreadView from '@/views/SpreadView.vue';
import UserLoginView from "@/views/UserLoginView.vue";
import UserSignUpView from "@/views/UserSignUpView.vue";
import UserMypageView from "@/views/UserMypageView.vue";
import AudioPlayView from '@/views/AudioPlayView.vue';
import PlayListVue from '@/views/PlayListVue.vue';
import RecordView from '@/views/RecordView.vue';
import ChangeVoiceView from '@/views/ChangeVoiceView.vue';
import PickItUpView from '@/views/PickItUpView.vue';
=======
import {createRouter, createWebHistory} from 'vue-router'
>>>>>>> master

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
            component: () => import('@/views/HomeView.vue')
        },
        {
            path: '/spread',
            name: 'spread',
            component: () => import('@/views/SpreadView.vue')
        },
        {
            path: "/login",
            name: "login",
<<<<<<< HEAD
            component: UserLoginView,
=======
            component: () => import('@/views/UserLoginView.vue')
>>>>>>> master
        },
        {
            path: "/signup",
            name: "signup",
<<<<<<< HEAD
            component: UserSignUpView,
=======
            component: () => import('@/views/UserSignUpView.vue'),
>>>>>>> master
        },
        {
            path: '/finder',
            name: 'finder',
            component: () => import('@/views/VoiceFinderView.vue')
        },
        {
            path: "/playlist",
            name: "playlist",
            component: () => import('@/views/PlayListView.vue')
        },
        {
            path: "/audioplayer",
            name: "audioplayer",
            component: () => import('@/views/AudioPlayView.vue')
        },
        {
            path: "/mypage",
            name: "mypage",
<<<<<<< HEAD
            component: UserMypageView,
=======
            component: () => import('@/views/UserMypageView.vue')
>>>>>>> master
        },
        {
            path: "/record",
            name: "record",
            component: () => import('@/views/RecordView.vue')
        },
        {
            path: "/change",
            name: "change",
            component: () => import('@/views/ChangeVoiceView.vue')
        },
        {
            path: "/storage",
            name: "storage",
            component: () => import('@/views/StorageView.vue')
        },
    ]
})

export default router
