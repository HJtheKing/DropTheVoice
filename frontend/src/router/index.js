import {createRouter, createWebHistory} from 'vue-router'

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
            component: () => import('@/views/UserLoginView.vue')
        },
        {
            path: "/signup",
            name: "signup",
            component: () => import('@/views/UserSignUpView.vue'),
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
            component: () => import('@/views/UserMypageView.vue')
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
        {
            path: "/test",
            name: "test",
            component: () => import('@/views/MyVtest.vue')
        },
    ]
})

export default router
