import { ref, onMounted } from "vue";
import { defineStore } from "pinia";
import router from "@/router";
import axios from "axios";

const REST_USER_API = `${import.meta.env.VITE_BASE_URL}/api-member`;

export const useUserStore = defineStore("user", () => {
  const isLogin = ref(false);
  const loginUserId = ref(null);
  const loginUserName = ref(null);
  const user = ref(null);
  const eventSource = ref(null);
  const hasNewNotifications = ref(false);
  const hasNewLikeNotifications = ref(false);
  const hasNewPickNotifications = ref(false);
  const hasNewSpreadNotifications = ref(false);

  const userLogin = function (id, password) {
    axios
      .post(`${REST_USER_API}/login`, {
        memberName: id,
        memberPassword: password,
      })
      .then((res) => {
        const token = res.data["access-token"];
        sessionStorage.setItem("access-token", token);

        const decodedToken = JSON.parse(atob(token.split(".")[1]));
        const userName = decodedToken["name"];
        const userId = decodedToken["id"];

        axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;

        isLogin.value = true;
        loginUserName.value = userName;
        loginUserId.value = userId;

        getUser(userId);
        
        router.push("/home");
        window.location.reload();
      })
      .catch(() => {
        alert("유효하지 않은 아이디 혹은 비번입니다");
        window.location.reload();
      });
  };

  // 로그아웃 메서드
  const logout = () => {
    sessionStorage.removeItem("access-token");
    isLogin.value = false;
    loginUserId.value = null;
    user.value = null;
    delete axios.defaults.headers.common["Authorization"];
    
    router.push("/login");
    // window.location.reload();
  };

  // SSE 초기화 메서드
  const initializeSSE = () => {
    if (!loginUserId.value) return;
    
    if (!eventSource.value) {
      eventSource.value = new EventSource(`${import.meta.env.VITE_BASE_URL}/api-sse/subscribe/${loginUserId.value}`);
    }
    
    eventSource.value.onopen = () => {
      console.log('SSE 연결 성공');
    };

    eventSource.value.onmessage = (event) =>
    {
      console.log("SSE 메시지 수신")
      console.log(event.data);

      if (event.data === 'Picked') {
        hasNewPickNotifications.value = true;
        hasNewNotifications.value = true;
      } else if (event.data === 'Liked') {
        hasNewLikeNotifications.value = true;
        hasNewNotifications.value = true;
      } else if (event.data === 'Spread') {
        hasNewSpreadNotifications.value = true;
        hasNewNotifications.value = true;
      }
    };

    eventSource.value.onerror = (error) => {
      console.error('SSE Error: ', error);
      eventSource.value.close();
      eventSource.value = null;

      setTimeout(() => {
      initializeSSE();
    }, 3000);
    };
  };

  const resetNotification = () => {
    hasNewNotifications.value = false;
  };

  const resetPickNotification = () => {
    hasNewPickNotifications.value = false;
  };

  const resetLikeNotification = () => {
    hasNewLikeNotifications.value = false;
  };

  const resetSpreadNotification = () => {
    hasNewSpreadNotifications.value = false;
  };

  // 회원가입 메서드
  const createUser = function (user) {
    axios
      .post(`${REST_USER_API}/register`, user.value)
      .then(() => {
        router.push({ name: "login" });
      })
      .catch(() => {
        alert("유효하지 않은 아이디 혹은 비번입니다");
        window.location.reload();
      });
  };

  const changePassword = function (oldPassword, newPassword) {
    return axios
      .post(`${REST_USER_API}/changePassword`, {
        memberName: loginUserName.value,
        oldPassword: oldPassword,
        newPassword: newPassword,
      })
      .then(() => {
        return Promise.resolve();
      })
      .catch((error) => {
        return Promise.reject(error);
      });
  };

  // 유저 정보 가져오기
  const getUser = function (userId) {
    axios.get(`${REST_USER_API}/${userId}`).then((response) => {
      user.value = response.data;
    });
  };

  // 회원탈퇴메서드
  const deleteUser = function (userId) {
    axios
      .delete(`${REST_USER_API}/${userId}`)
      .then(() => {
        alert("회원 탈퇴를 완료하였습니다");
        logout();
      })
      .catch(() => {
        alert("실패하였습니다");
        window.location.reload();
      });
  };

  // 새로고침 시 로그인 상태 복구
  const tryAutoLogin = () => {
    const token = sessionStorage.getItem("access-token");
    if (!token) {
      router.push({ name: "login" });
      return;
    }
    const decodedToken = JSON.parse(atob(token.split(".")[1]));
    const userId = decodedToken["id"];
    const userName = decodedToken["name"];

    isLogin.value = true;
    loginUserId.value = userId;
    loginUserName.value = userName;
    axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;

    getUser(userId);
    // initializeSSE(); // 자동 로그인 시에도 SSE 연결 초기화
  };

  // Store 초기화 시 자동 로그인 시도
  onMounted(() => {
    tryAutoLogin();
  });

  return {
    isLogin,
    loginUserId,
    loginUserName,
    user,
    userLogin,
    logout,
    createUser,
    getUser,
    deleteUser,
    tryAutoLogin,
    changePassword,
    initializeSSE,
    resetNotification,
    resetPickNotification,
    resetLikeNotification,
    resetSpreadNotification,
    hasNewNotifications,
    hasNewPickNotifications,
    hasNewLikeNotifications,
    hasNewSpreadNotifications,
  };
});
