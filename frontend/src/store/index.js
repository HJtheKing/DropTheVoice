import { createStore } from 'vuex';
import SockJS from 'sockjs-client';
import Webstomp from 'webstomp-client';

var latitude = 0.0;
var longitude = 0.0;
let pcListMap = new Map();
let mySessionId;
let otherSessionIdList = [];
let sendChannelMap = new Map();
let dataMap = new Map();
var stompClient = null;
const peerConfiguration = {
    iceServers: [
        {
            urls: "turn:i11a505.p.ssafy.io",  // TURN 서버의 URL
            username: "usr",   // TURN 서버의 사용자 이름
            credential: "pass"  // TURN 서버의 비밀번호
        }
    ]
};

export default createStore({
    state: {
        stompClient: null,
        isConnected: false,
        messages: [],
        sessionId: null,
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
            //const uri = 'i11a505.p.ssafy.io';
            const uri = import.meta.env.VITE_BASE_URL;
            //const uri = 'localhost';
            //const socket = new SockJS('http's'://' + uri + '/stomp/handshake');

            const socket = new SockJS('http://' + uri + '/stomp/handshake');
            this.state.stompClient = Webstomp.over(socket);
            stompClient = this.state.stompClient;
            stompClient.connect({}, () => {
                commit('SET_IS_CONNECTED', true);
                console.log('WebSocket connected');
                var url = this.state.stompClient.ws._transport.url;
                var urls = url.split('/');
                mySessionId = urls[6];

                console.log(mySessionId + "is my sesss!!!!!!!!!!!");
                console.log(`/topic/others/${mySessionId}`);


                stompClient.subscribe('/topic/messages/' + mySessionId, function (message) {
                    console.log("-----------");
                    console.log(message);
                    const body = JSON.parse(message.body);
                    commit('ADD_MESSAGE', body);
                });
                stompClient.subscribe(`/topic/peer/iceCandidate/${mySessionId}`, candidate => {
                    const key = JSON.parse(candidate.body).key
                    const message = JSON.parse(candidate.body).body;

                    // 해당 key에 해당되는 peer 에 받은 정보를 addIceCandidate 해준다.
                    pcListMap.get(key).addIceCandidate(new RTCIceCandidate({ candidate: message.candidate, sdpMLineIndex: message.sdpMLineIndex, sdpMid: message.sdpMid }));

                });

                //answer peer 교환을 위한 subscribe
                stompClient.subscribe(`/topic/peer/answer/${mySessionId}`, answer => {
                    const key = JSON.parse(answer.body).key;
                    const message = JSON.parse(answer.body).body;

                    // 해당 key에 해당되는 Peer 에 받은 정보를 setRemoteDescription 해준다.
                    pcListMap.get(key).setRemoteDescription(new RTCSessionDescription(message));

                });

                //offer peer 교환을 위한 subscribe
                stompClient.subscribe(`/topic/peer/offer/${mySessionId}`, offer => {
                    
                    console.log("offer subscribe log");
                    
                    const key = JSON.parse(offer.body).mySessionId;
                    const message = JSON.parse(offer.body).body;

                    // 해당 key에 새로운 peerConnection 를 생성해준후 pcListMap 에 저장해준다.
                    pcListMap.set(key, createPeerConnection(key));
                    // 생성한 peer 에 offer정보를 setRemoteDescription 해준다.

                    pcListMap.get(key).setRemoteDescription(new RTCSessionDescription({ type: message.type, sdp: message.sdp }));
                    //sendAnswer 함수를 호출해준다.

                    sendAnswer(pcListMap.get(key), key);

                });

                //다른사람들의 key 리스트를 받게됨.
                stompClient.subscribe(`/topic/others/${mySessionId}`, message => {
                    console.log("i received");
                    console.log("receive others key!!!!!!!!!!!!!!!");
                    console.log(`/topic/others/${mySessionId}`);
                    const sessions = JSON.parse(message.body);
                    sessions.forEach(otherSessionId => {
                        console.log("others session id is " + otherSessionId);
                        if (!(mySessionId === otherSessionId)) {
                            otherSessionIdList.push(otherSessionId);
                        }
                    });

                    otherSessionIdList.map((sessionID) => {
                        if (!pcListMap.has(sessionID)) {
                            console.log("b2 ------ new ssession connection peer");
                            pcListMap.set(sessionID, createPeerConnection(sessionID));
                            sendOffer(pcListMap.get(sessionID), sessionID);
                        }
                    });
                });
                this.dispatch('startSendingMessages', 'hi');

            }, (error) => {
                console.error('WebSocket error:', error);
            });

            commit('SET_STOMP_CLIENT', this.state.stompClient);
        },
        sendMessage({ state }, file) {
            sendFileInner(file);
        }
        ,
        disconnectWebSocket({ state, commit }) {
            if (state.stompClient) {
                state.stompClient.disconnect(() => {
                    commit('SET_IS_CONNECTED', false);
                    console.log('WebSocket disconnected');
                });
                commit('SET_STOMP_CLIENT', null);
            }
        },
        async sendFile({ state }, file) {
            console.log("inner function called");
            sendFileInner(file);
        },
        async sendMessage({ state }, message) {
            console.log("message is !!!!!!!!!!");
            console.log(message);
            const { latitude, longitude } = await getGeo();
            if (state.stompClient && state.isConnected) {
                state.stompClient.send('/ws/position', JSON.stringify({ name: message.name, x: latitude, y: longitude }));
            } else {
                console.log("fail");
            }
        },
        startSendingMessages({ dispatch }, message) {
            setInterval(() => {
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
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    latitude = position.coords.latitude;
                    longitude = position.coords.longitude;
                    console.log(latitude);
                    console.log(longitude);
                    resolve({ latitude, longitude });

                },
                (error) => {
                    console.log("위치 정보를 가져오는데 실패했습니다: " + error.message);
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

const setLocalAndSendMessage = (pc, sessionDescription) => {
    pc.setLocalDescription(sessionDescription);
}

const sendAnswer = (pc, otherSessionId) => {
    pc.createAnswer().then(answer => {
        setLocalAndSendMessage(pc, answer);
        this.state.stompClient.send(`/ws/peer/answer/${mySessionId}/${otherSessionId}`, {}, JSON.stringify({
            key: mySessionId,
            body: answer
        }));
        console.log('Send answer');
    });
};

const onIceCandidate = (event, otherSessionId) => {
    if (event.candidate) {
        console.log('ICE candidate');
        this.state.stompClient.send(`/ws/peer/iceCandidate/${mySessionId}/${otherSessionId}`, {}, JSON.stringify({
            key: mySessionId,
            body: event.candidate
        }));
    }
};

let onTrack = (event, otherSessionId) => {

    if (document.getElementById(`${otherSessionId}`) === null) {
        const video = document.createElement('video');

        video.autoplay = true;
        video.controls = true;
        video.id = otherSessionId;
        video.srcObject = event.streams[0];

        document.getElementById('remoteStreamDiv').appendChild(video);
    }
};

const createPeerConnection = (otherSessionID) => {
    const pc = new RTCPeerConnection(peerConfiguration);
    var conn2;
    try {
        pc.addEventListener('icecandidate', (event) => {
            onIceCandidate(event, otherSessionID);
        });
        pc.addEventListener('track', (event) => {
            onTrack(event, otherSessionID);
        });
        pc.addEventListener('datachannel', (event) => {
            console.log('connected to data channel');
            conn2 = event.channel;
            conn2.sessionId = otherSessionID;
            event.channel.onmessage = function (evt) {
                accumulateStringData(conn2.sessionId, evt.data);
            };
        })

        var channel = pc.createDataChannel('temp channel');
        console.log("data channel created");
        sendChannelMap.set(otherSessionID, channel);

        channel.onmessage = function (msg) {
            console.log("sended message is " + msg);
        };
        console.log('PeerConnection created');
    } catch (error) {
        console.error('PeerConnection failed: ', error);
    }
    return pc;
}

function tryPeerConnect() {
    console.log('b');
    console.log(mySessionId + "is best!!!!!!!!!!!");
    console.log(stompClient);
    var url = stompClient.ws._transport.url;
    var urls = url.split('/');
    var temp = urls[6];
    console.log(temp + "is calculated");
    console.log(JSON.stringify(temp));
    stompClient.send(`/ws/spread/50/50`, {}, 'hi');
    console.log("-----end of b----------try peer Connect");
}

let sendOffer = (pc, otherSessionId) => {
    console.log("send offer init");
    pc.createOffer().then(offer => {
        console.log("offer start!!!!!!!!!!!!");
        setLocalAndSendMessage(pc, offer);
        stompClient.send(`/ws/peer/offer/${mySessionId}/${otherSessionId}`, {}, JSON.stringify({
            mySessionId: mySessionId,
            body: offer
        }));
        console.log('Send offer');
    });
};


function accumulateStringData(otherSessionId, data) {
    if (data === "end") {
        let finalData = dataMap.get(otherSessionId);
        handleReceiveMessage(finalData);
    } else {
        if (dataMap.get(otherSessionId) == null) {
            let temp = [];
            temp.push(data);
            dataMap.set(otherSessionId, temp);
        } else {
            dataMap.get(otherSessionId).push(data);
        }
    }
}

function handleReceiveMessage(data) {
    let receiveFileInfo = document.querySelector('#receiveFileInfo');
    let totalLength = 0;
    blobs = []
    data.forEach(part => {
        let binaryString = atob(part);
        let len = binaryString.length;
        totalLength += len;
        let bytes = new Uint8Array(len);
        for (let i = 0; i < len; i++) {
            bytes[i] = binaryString.charCodeAt(i);
        }
        let blob = new Blob([bytes]); //, { type: 'audio/mp3' }); // or other appropriate MIME type
        blobs.push(blob);
    });

    let largeBlob = new Blob(blobs, { type: 'audio/mp3' });

    let url = URL.createObjectURL(largeBlob);
    receiveFileInfo.innerHTML += `<p>Received file chunk. <a href="${url}" download="received.mp3">Download</a></p>`;
    console.log("done!!!!!!!!!!!!!!!!!!!");
}

async function sendFileInner(file) {
    if (!file) {
        console.log("file has some error");
        return;
    }

    if (file) {
        console.log('a')
        await tryPeerConnect();
        console.log('c');
        console.log('send start');
        const chunkSize = 16384;
        let offset = 0;
        const reader = new FileReader();

        reader.onload = (event) => {
            const result = event.target.result;
            const base64String = btoa(String.fromCharCode(...new Uint8Array(result)));
            otherSessionIdList.forEach(session => {
                channel = sendChannelMap.get(session);
                if (channel.readyState === 'open') {
                    channel.send(base64String);
                } else {
                    console.error('Channel is not open. Current state:', channel.readyState);
                }
            });
            offset += chunkSize;
            if (offset < file.size) {
                readSlice(offset);
            } else {
                otherSessionIdList.forEach(session => {
                    channel = sendChannelMap.get(session);
                    if (channel.readyState === 'open') {
                        channel.send("end");
                    } else {
                        console.error('Channel is not open. Current state:', channel.readyState);
                    }
                });
            }
        };

        const readSlice = o => {
            let slice = file.slice(offset, o + chunkSize);
            reader.readAsArrayBuffer(slice);
        };
        readSlice(0);
    }
}
