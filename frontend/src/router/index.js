import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            redirect: '/login',
        },
        {
            path: '/home',
            name: 'home',
            component: () => import('@/views/HomeView.vue'),
            meta: { requiresAuth: true }
        },
        {
            path: '/spread',
            name: 'spread',
            component: () => import('@/views/SpreadView.vue'),
            meta: { requiresAuth: true }
        },
        {
            path: "/login",
            name: "login",
            component: () => import('@/views/UserLoginView.vue'),
            meta: { guest: true }
        },
        {
            path: "/signup",
            name: "signup",
            component: () => import('@/views/UserSignUpView.vue'),
            meta: { guest: true }
        },
        {
            path: '/finder',
            name: 'finder',
            component: () => import('@/views/VoiceFinderView.vue'),
            meta: { requiresAuth: true }
        },
        {
            path: "/playlist",
            name: "playlist",
            component: () => import('@/views/PlayListView.vue'),
            meta: { requiresAuth: true }
        },
        {
            path: "/audioplayer",
            name: "audioplayer",
            component: () => import('@/views/AudioPlayView.vue'),
            meta: { requiresAuth: true }
        
        },
        {
            path: "/mypage",
            name: "mypage",
            component: () => import('@/views/UserMypageView.vue'),
            meta: { requiresAuth: true }
        },
        {
            path: "/record",
            name: "record",
            component: () => import('@/views/RecordView.vue'),
            meta: { requiresAuth: true }
        },
        {
            path: "/change",
            name: "change",
            component: () => import('@/views/ChangeVoiceView.vue'),
            meta: { requiresAuth: true }
        },
        {
            path: "/storage",
            name: "storage",
            component: () => import('@/views/StorageView.vue'),
            meta: { requiresAuth: true }
        },
        {
            path: "/mypage/customer-center",
            name: "customer-center",
            component: ()=> import('@/views/CustomerCenterView.vue'),
            meta: { requiresAuth: true }
        },
        {
            path: "/mypage/privacy-policy",
            name: "privacy-policy",
            component:() =>import('@/views/PrivacyPolicyVuew.vue'),
            meta: { requiresAuth: true }
        },
        {
            path: "/spread/upload",
            name: "upload-voice",
            component:() => import('@/views/UploadVoiceView.vue'),
            meta: { requiresAuth: true }
        },
    ]
})

export default router
