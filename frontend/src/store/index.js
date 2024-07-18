import { createStore } from 'vuex';
import SockJS from 'sockjs-client';
import Webstomp from 'webstomp-client';

var latitude = 0.0;
var longitude = 0.0;

export default createStore({
    state: {
        stompClient: null,
        isConnected: false,
        messages: []
    },
    mutations: {
        SET_STOMP_CLIENT(state, client) {
            state.stompClient = client;
        },
        SET_IS_CONNECTED(state, isConnected) {
            state.isConnected = isConnected;
        },
        ADD_MESSAGE(state, message) {
            state.messages.push(message);
        }
    },
    actions: {
        connectWebSocket({ commit }) {
            const socket = new SockJS('http://localhost:8080/stomp/handshake');
            const stompClient = Webstomp.over(socket);

            stompClient.connect({}, () => {
                commit('SET_IS_CONNECTED', true);
                console.log('WebSocket connected');

                stompClient.subscribe('/topic/messages', (message) => {
                    const body = JSON.parse(message.body);
                    commit('ADD_MESSAGE', body);
                });
                this.dispatch('startSendingMessages','hi');

            }, (error) => {
                console.error('WebSocket error:', error);
            });

            commit('SET_STOMP_CLIENT', stompClient);
        },
        disconnectWebSocket({ state, commit }) {
            if (state.stompClient) {
                state.stompClient.disconnect(() => {
                    commit('SET_IS_CONNECTED', false);
                    console.log('WebSocket disconnected');
                });
                commit('SET_STOMP_CLIENT', null);
            }
        },
        async sendMessage({ state }, message) {
            console.log("message is ");
            console.log(message);
            const { latitude, longitude } = await getGeo();
            if (state.stompClient && state.isConnected) {
                state.stompClient.send('/app/position', JSON.stringify({ name: message.name, x: latitude, y: longitude }));
            } else {
                console.log("fail");
            }
        },
        startSendingMessages({ dispatch }, message) {
            setInterval(() => {
                console.log("HIHI");
                dispatch('sendMessage', message);
            }, 30000);
        }
    },
    getters: {
        stompClient: (state) => state.stompClient,
        isConnected: (state) => state.isConnected,
        messages: (state) => state.messages
    }
});

function getGeo() {
    return new Promise((resolve, reject) => {
        if ("geolocation" in navigator) {
            console.log("xxxxxxxxxxx");
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    latitude = position.coords.latitude;
                    longitude = position.coords.longitude;
                    console.log("_----------------");
                    console.log(latitude);
                    console.log(longitude);
                    resolve({ latitude, longitude });

                },
                (error) => {
                    alert("위치 정보를 가져오는데 실패했습니다: " + error.message);
                    reject(new Error("위치 정보를 가져오는데 실패했습니다: " + error.message));

                },
                {
                    enableHighAccuracy: true,
                    timeout: 5000,
                    maximumAge: 0
                }
            );
        } else {
            alert("이 브라우저에서는 위치 정보 서비스를 지원하지 않습니다.");
            reject(new Error("위치 정보를 가져오는데 실패했습니다: "));
        }
    })
}