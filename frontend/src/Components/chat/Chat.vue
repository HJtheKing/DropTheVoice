<script setup>
import { useRoute } from 'vue-router';
import { onMounted, ref } from 'vue';
import SockJS from 'sockjs-client';
import Webstomp from 'webstomp-client';


const route = useRoute();

const roomName = ref("");
const roomId = ref();

const curmessage = ref("");

const messages = ref([]);

const latitude = ref("");
const longitude = ref("");

var stomp;
const username = ref("");
var sessionId;


onMounted(() => {
    roomId.value = route.params.id; // 동적 세그먼트로부터 userId 추출
    roomName.value = route.params.title;
    username.value = localStorage.getItem('username');

    var socket = new SockJS("http://localhost:8080/stomp/handshake/");
    stomp = Webstomp.over(socket);
    stomp.connect({}, function () {
        //var url = stomp.ws._transport.url;
        //var urls = url.split('/');
        //sessionId = urls[6];
        var subscription = stomp.subscribe("/topic/test", function (chat) {
            var content = JSON.parse(chat.body);
            console.log(content + " is new message");
        });

        stomp.send('/app/position', JSON.stringify({ name: '김병관', x: 50.0, y: 50.0 }));

        //라이브 메시지 탐색
    });
})


function send() {
    if (curmessage.value !== "") {
        stomp.send('/app/position', JSON.stringify({ name: '김병관', latitude: '50', longitude: '50' }));
        curmessage.value = "";
    }
    if (typeof window !== 'undefined') {
        // window 객체를 사용하는 코드
        console.log("window 없음");
    } else {
        console.log("존재")
    }
}


function sendAndScroll() {
    send();

    const chatBody = document.getElementById('chatBody');
    chatBody.scrollTop = chatBody.scrollHeight;
}

function updatePos() {
    stomp.send('/app/position', JSON.stringify({ name: "김병관", x: latitude.value, y: longitude.value }));
}

if ("geolocation" in navigator) {
    navigator.geolocation.getCurrentPosition(
        (position) => {
            latitude.value = position.coords.latitude;
            longitude.value = position.coords.longitude;
        },
        (error) => {
            alert("위치 정보를 가져오는데 실패했습니다: " + error.message);
        },
        {
            enableHighAccuracy: true,
            timeout: 5000,
            maximumAge: 0
        }
    );
} else {
    alert("이 브라우저에서는 위치 정보 서비스를 지원하지 않습니다.");
}

function getPos() {
    if ("geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                latitude.value = position.coords.latitude;
                longitude.value = position.coords.longitude;
                console.log(latitude.value + "입니다");
            },
            (error) => {
                alert("위치 정보를 가져오는데 실패했습니다: " + error.message);
            },
            {
                enableHighAccuracy: true,
                timeout: 5000,
                maximumAge: 0
            }
        );
    } else {
        alert("이 브라우저에서는 위치 정보 서비스를 지원하지 않습니다.");
    }

    updatePos();
}

async function getPublicIP() {
    const configuration = {
        iceServers: [{
            urls: ['turn:3.36.124.153:3478'],  // EC2에서 실행 중인 TURN 서버 주소
            username: 'testuser',  // TURN 서버 인증을 위한 사용자 이름
            credential: 'testpassword'  // TURN 서버 인증을 위한 비밀번호
        }]
    };

    // RTCPeerConnection 객체 생성
    const peerConnection = new RTCPeerConnection(configuration);

    // ICE 후보자 처리 이벤트 핸들러
    peerConnection.onicecandidate = event => {
        if (event.candidate) {
            console.log('New ICE candidate:', event.candidate);
        } else {
            // 모든 ICE 후보자 수집 완료
            console.log('ICE Candidate gathering done');
        }
    };

    // 연결 상태 변경 이벤트 핸들러
    peerConnection.onconnectionstatechange = () => {
        console.log('Connection state change:', peerConnection.connectionState);
    };

    // 간단한 데이터 채널 예제
    const dataChannel = peerConnection.createDataChannel("sampleChannel");
    dataChannel.onopen = () => console.log("Data channel opened!");
    dataChannel.onclose = () => console.log("Data channel closed!");

    // 연결 시도
    peerConnection.createOffer()
        .then(offer => peerConnection.setLocalDescription(offer))
        .then(() => console.log("Offer created and set as local description"))
        .catch(error => console.error("Error creating offer: ", error));
}
</script>

<template>
    <div>
        <button @click="getPos()">위치 뚫어</button>
    </div>

    <div>
        <button @click="getPublicIP">공인 IP 가져오기</button>
        <div v-if="publicIP">
            공인 IP 주소: {{ publicIP }}
        </div>
    </div>

</template>

<style scoped></style>