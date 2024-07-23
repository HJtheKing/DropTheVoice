import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true, // 쿠키를 포함하도록 설정
});

export default apiClient;
