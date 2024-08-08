
import axios from 'axios';
import { useUserStore } from '@/store/user';
import router from '@/router';

const userStore = useUserStore();

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL, // 백엔드 API 서버 URL
  withCredentials: true, // 자격 증명 포함
});

axiosInstance.interceptors.request.use(
  async (config) => {
    let token = sessionStorage.getItem('access-token');

    if (!token) {
      try {
        // Refresh Token으로 새로운 Access Token 발급 요청
        const response = await axios.post(`${import.meta.env.VITE_BASE_URL}/api-user/refresh`, {}, { withCredentials: true });
        token = response.data['access-token'];
        sessionStorage.setItem('access-token', token);
      } catch (error) {
        // Refresh Token이 유효하지 않거나 만료된 경우
        userStore.logout();
        router.push('/login');
        return Promise.reject(error);
      }
    }

    config.headers.Authorization = `Bearer ${token}`;
    return config;
  },
  (error) => Promise.reject(error)
);

export default axiosInstance;

