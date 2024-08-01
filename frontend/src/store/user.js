import { ref, } from "vue";
import { defineStore } from "pinia";
import router from "@/router";
import axios from "axios";

const REST_USER_API = `http://localhost:8080/api-user`;

export const useUserStore = defineStore("user", () => {
  const isLogin = ref(false);
  const loginUserId = ref(null);
  const user = ref(null);

  // 로그인 메서드
  const userLogin = function (id, password) {

    console.log("asdasd");

    axios
      .post(`${REST_USER_API}/login`, {
        userId: id,
        userPassword: password,
      }, { withCredentials: true })
      .then((res) => {
        const token = res.data["access-token"];
        sessionStorage.setItem("access-token", token);

        const decodedToken = JSON.parse(atob(token.split(".")[1]));
        let userId = decodedToken["id"];

        axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;

        isLogin.value = true;
        loginUserId.value = userId;
        getUser(userId);

        router.push("/");
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
    router.push("/"); // 홈페이지로 이동
  };

  // 유저 정보 가져오기
  const getUser = function (userId) {
    axios.get(`${REST_USER_API}/${userId}`).then((response) => {
      user.value = response.data;
    });
  };


  return {
    isLogin,
    loginUserId,
    user,
    userLogin,
    logout,
    getUser,
  };
});
