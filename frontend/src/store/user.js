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
        let userName = decodedToken["name"];
        let userId = decodedToken["id"];

        axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;

        isLogin.value = true;
        loginUserName.value = userName;
        loginUserId.value = userId;

        getUser(userId);

        router.push("/home");
      })
      .catch(() => {
        alert("유효하지 않은 아이디 혹은 비번입니다");
        window.location.reload();
      });
  };

  // 로그아웃 메서드
  const logout = () => {
    sessionStorage.removeItem("access-token");
    isLogin.value = false; // 로그인 상태 업데이트
    loginUserId.value = null;
    user.value = null;
    delete axios.defaults.headers.common["Authorization"];
    router.push("/login"); // 홈페이지로 이동
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

  // 비밀번호 변경
  const changePassword = function (oldPassword, newPassword){
    axios
      .post(`${REST_USER_API}/changePassword`, {
        memberName : loginUserName.value,
        oldPassword : oldPassword,
        newPassword : newPassword
      })
  }

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
    
  };

  // Call tryAutoLogin when the store is initialized
  onMounted(() => {
    tryAutoLogin();
  });

  return {
    isLogin,
    loginUserId,
    user,
    userLogin,
    logout,
    createUser,
    getUser,
    deleteUser,
    tryAutoLogin,
    changePassword,
  };
});
