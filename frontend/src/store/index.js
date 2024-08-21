import { createStore } from 'vuex';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { saveVoice, getVoice } from '../utils/rtc';

let latitude = 0.0;
let longitude = 0.0;
let pcListMap = new Map();
let mySessionId = null;
let otherSessionIdList = [];
let sendChannelMap = new Map();
let dataMap = new Map();
let stompClient = null;
let voiceId = null;
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

            const socket = new SockJS(uri + '/stomp/handshake');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, () => {
                commit('SET_IS_CONNECTED', true);
                console.log('WebSocket connected');

                const token = sessionStorage.getItem("access-token");
                const decodedToken = JSON.parse(atob(token.split(".")[1]));
                const userId = decodedToken["id"];

                mySessionId = userId;

                console.log(mySessionId + "is my sessionId");
                console.log(`/topic/others/${mySessionId}`);


                stompClient.subscribe('/topic/messages/' + mySessionId, function (message) {
                    const body = JSON.parse(message.body);
                    commit('ADD_MESSAGE', body);
                });
                stompClient.subscribe(`/topic/peer/iceCandidate/${mySessionId}`, candidate => {
                    console.log("------------ice candidate start----------");
                    const key = JSON.parse(candidate.body).key
                    const message = JSON.parse(candidate.body).body;

                    // 해당 key에 해당되는 peer 에 받은 정보를 addIceCandidate 해준다.
                    pcListMap.get(key).addIceCandidate(new RTCIceCandidate({ candidate: message.candidate, sdpMLineIndex: message.sdpMLineIndex, sdpMid: message.sdpMid }));
                    console.log("------------ice candidate end----------");

                });

                //answer peer 교환을 위한 subscribe
                stompClient.subscribe(`/topic/peer/answer/${mySessionId}`, answer => {
                    console.log("------------answer start----------");

                    const key = JSON.parse(answer.body).key;
                    const message = JSON.parse(answer.body).body;

                    // 해당 key에 해당되는 Peer 에 받은 정보를 setRemoteDescription 해준다.
                    if(pcListMap.get(key).signalingState !== "stable"){
                        pcListMap.get(key).setRemoteDescription(new RTCSessionDescription(message));
                    } else{
                        console.warn("PeerConnection is already in stable state. Skipping setRemoteDescription.");
                    }
                    console.log("------------answer end----------");

                });

                //offer peer 교환을 위한 subscribe
                stompClient.subscribe(`/topic/peer/offer/${mySessionId}`, offer => {
                    console.log("------------offer start----------");

                    const key = JSON.parse(offer.body).mySessionId;
                    const message = JSON.parse(offer.body).body;

                    // 해당 key에 새로운 peerConnection 를 생성해준후 pcListMap 에 저장해준다.
                    pcListMap.set(key, createPeerConnection(key));
                    // 생성한 peer 에 offer정보를 setRemoteDescription 해준다.
                    console.log('message.type -----------')
                    console.log(message.type)
                    console.log('message.sdp ------------')
                    console.log(message.sdp)
                    pcListMap.get(key).setRemoteDescription(new RTCSessionDescription({ type: message.type, sdp: message.sdp }));
                    //sendAnswer 함수를 호출해준다.

                    sendAnswer(pcListMap.get(key), key);
                    console.log("------------offer end----------");

                });

                //다른사람들의 key 리스트를 받게됨.
                stompClient.subscribe(`/topic/others/${mySessionId}`, message => {
                    console.log("------------others start----------");

                    console.log("receive others key");
                    console.log("mySessionId : " + mySessionId)
                    console.log(`/topic/others/${mySessionId}`);
                    const sessions = JSON.parse(message.body);
                    console.log(sessions);
                    sessions.forEach(otherSessionId => {
                        console.log("others session id is " + otherSessionId);
                        if (!(mySessionId == otherSessionId)) {
                            console.log("compare "+mySessionId+" "+otherSessionId);
                            otherSessionIdList.push(otherSessionId);
                        }
                    });

                    otherSessionIdList.map((sessionID) => {
                        if (!pcListMap.has(sessionID)) {
                            console.log("new ssession connection peer");
                            pcListMap.set(Number(sessionID), createPeerConnection(sessionID));
                            sendOffer(pcListMap.get(Number(sessionID)), Number(sessionID));
                        }
                    });
                    console.log("------------others end----------");
                });
                
                // 음성 업로드시 backend에서 생성된 voiceId 반환
                stompClient.subscribe(`/topic/voiceId/${mySessionId}`, message => {
                    voiceId = JSON.parse(message.body);
                });

                this.dispatch('startSendingMessages', 'hi');

            }, (error) => {
                console.error('WebSocket error:', error);
            });

            commit('SET_STOMP_CLIENT', stompClient);
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
            sendFileInner(file);
        },
        async sendMessage({ state }, message) {
            console.log("send location of me");
            const { latitude, longitude } = await getGeo();
            if(mySessionId === null) return;
            if (stompClient && state.isConnected) {
                stompClient.send('/ws/position', {}, JSON.stringify({ name: mySessionId, x: longitude, y: latitude }));
            } else {
                console.log("fail");
            }
        },
        startSendingMessages({ dispatch }, message) {
            setInterval(() => {
                dispatch('sendMessage', message);
            }, 3000);
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
                    // console.log(latitude);
                    // console.log(longitude);
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
        stompClient.send(`/ws/peer/answer/${mySessionId}/${otherSessionId}`, {}, JSON.stringify({
            key: mySessionId,
            body: answer
        }));
        console.log('Send answer');
    });
};

const onIceCandidate = (event, otherSessionId) => {
    if (event.candidate) {
        console.log('ICE candidate');
        stompClient.send(`/ws/peer/iceCandidate/${mySessionId}/${otherSessionId}`, {}, JSON.stringify({
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
        pc.addEventListener('iceconnectionstatechange', () => {
            if (pc.iceConnectionState === 'connected') {
                console.log('ICE connection established!');
                // 두 피어 간의 연결이 성공적으로 성립됨.
            } else if (pc.iceConnectionState === 'completed') {
                console.log('ICE connection is complete!');
                // 모든 ICE 프로세스가 완료되고, 연결이 안정됨.
            } else if (pc.iceConnectionState === 'failed') {
                console.log('ICE connection failed.');
                // 연결 시도 실패
            }
        });

        let channel = pc.createDataChannel('temp channel');
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

let sendOffer = (pc, otherSessionId) => {
    console.log("send offer init");
    pc.createOffer().then(offer => {
        setLocalAndSendMessage(pc, offer);
        console.log("mySessionId : " + mySessionId + " otherSessionId : " + otherSessionId)
        stompClient.send(
            `/ws/peer/offer/${mySessionId}/${otherSessionId}`,
            {},
            JSON.stringify({
                mySessionId: mySessionId,
                body: offer
            })
        )
        console.log('Send offer end');
    });
};

function accumulateStringData(otherSessionId, data) {
    if (data === "end") {
        let finalData = dataMap.get(otherSessionId);
        handleReceiveMessage(finalData);
        clearConnections();
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

let receivedFileData = [];
async function handleReceiveMessage(data) {

    await waitForVoiceId();

    // ID가 설정된 후에만 파일 데이터를 처리
    console.log('@@@@@@@@@@@@@@@@@@@@ receivedId : ', voiceId)
    data.forEach((part) => {
        let binaryString = atob(part);
        let len = binaryString.length;
        let bytes = new Uint8Array(len);
        for (let i = 0; i < len; i++) {
            bytes[i] = binaryString.charCodeAt(i);
        }
        receivedFileData.push(bytes);
    });
    // 모든 파일 조각을 받아서 Blob으로 변환
    let largeBlob = new Blob(receivedFileData, { type: 'audio/mp3' });
    console.log(largeBlob);
    // indexedDB에 파일 저장
    saveVoice(voiceId, largeBlob);
    // 상태 초기화
    receivedFileData = [];
    voiceId = null;
}

function waitForVoiceId() {
    return new Promise((resolve) => {
        const checkInterval = setInterval(() => {
            if (voiceId) {
                clearInterval(checkInterval);
                resolve();
            }
        }, 100); // 100ms마다 voiceId를 확인
    });
}

async function waitForConnectionReady() {
    return new Promise((resolve, reject) => {
        let checkInterval = setInterval(() => {
            let allChannelsReady = true;
            otherSessionIdList.forEach(session => {
                let channel = sendChannelMap.get(session);
                if (channel.readyState !== 'open') {
                    allChannelsReady = false;
                }
            });

            if (allChannelsReady) {
                clearInterval(checkInterval);
                resolve();
            }
        }, 100); // 100ms마다 연결 상태를 체크
    });
}

async function sendFileInner(file) {

    if (!file) {
        console.log("file has some error");
        return;
    }

    console.log("Waiting for all connections to be ready...");

    await waitForConnectionReady();

    console.log('All connections are ready. Sending file...');

    if (file) {
        console.log('send start');
        const chunkSize = 16384;
        let offset = 0;
        const reader = new FileReader();

        reader.onload = (event) => {
            const result = event.target.result;
            const base64String = btoa(String.fromCharCode(...new Uint8Array(result)));
            otherSessionIdList.forEach(session => {
                console.log("other sesion id is "+session);
                let channel = sendChannelMap.get(session);
                if (channel.readyState === 'open') {
                    channel.send(base64String);
                    console.log('##############Send Done##############')
                }
            });
            offset += chunkSize;
            if (offset < file.size) {
                readSlice(offset);
            } else {
                otherSessionIdList.forEach(session => {
                    let channel = sendChannelMap.get(session);
                    if (channel.readyState === 'open') {
                        channel.send("end");
                    } else {
                        console.log('Channel is waiting for open. Current state:', channel.readyState);
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

function clearConnections(){
    pcListMap = new Map();
    otherSessionIdList = [];
    sendChannelMap = new Map();
    dataMap = new Map();
}
