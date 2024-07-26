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
            component: UserLoginView,
        },
        {
            path: "/signup",
            name: "signup",
            component: UserSignUpView,
        },
        {
            path: '/search',
            name: 'search',
            component: PickItUpView
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
            component: UserMypageView,
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
    ]
})

export default router
