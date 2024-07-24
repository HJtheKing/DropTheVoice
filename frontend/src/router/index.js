import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '@/views/HomeView.vue';
import SpreadView from '@/views/SpreadView.vue';
import UserLogin from "@/views/UserLogin.vue";
import UserSignUp from "@/views/UserSignUp.vue";
import UserMypage from "@/views/UserMypage.vue";
import AudioPlayView from '@/views/AudioPlayView.vue';
import PlayListVue from '@/views/PlayListVue.vue';
import RecordView from '@/views/RecordView.vue';
import ChangeVoiceView from '@/views/ChangeVoiceView.vue';
import StorageView from '@/views/StorageView.vue';

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
            path: '/finder',
            name: 'finder',
            component: () => import('@/views/VoiceFinderView.vue')
        },
        {
            path: "/playlist",
            name: "playlist",
            component: PlayListVue,
        },
        {
            path: "/audioplayer",
            name: "audioplayer",
            component: AudioPlayView,
        },
        {
            path: "/mypage",
            name: "mypage",
            component: UserMypage,
        },
        {
            path: "/record",
            name: "record",
            component: RecordView,
        },
        {
            path: "/change",
            name: "change",
            component: ChangeVoiceView,
        },
        {
            path: "/storage",
            name: "storage",
            component: StorageView,
        },
    ]
})

export default router
