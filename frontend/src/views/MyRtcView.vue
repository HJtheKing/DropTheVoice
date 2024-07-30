<template>
    <v-container>
      <v-row>
        <v-col cols="12" md="6">
          <v-text-field
            v-model="roomId"
            label="Room ID"
            type="number"
            outlined
            ref="roomIdInput"
          ></v-text-field>
          <v-btn color="primary" @click="enterRoom">Enter Room</v-btn>
          <v-btn
            color="secondary"
            v-if="streamStarted"
            @click="startStream"
          >
            Start Streams
          </v-btn>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12">
          <video
            ref="localStreamElement"
            autoplay
            playsinline
            controls
            v-show="localStreamActive"
          ></video>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12">
          <div id="remoteStreamDiv"></div>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12">
          <h2>WebRTC File Transfer</h2>
          <v-file-input
            ref="fileInput"
            label="Choose File"
            outlined
          ></v-file-input>
          <v-btn color="primary" @click="sendFile">Send File</v-btn>
          <div id="receiveFileInfo"></div>
        </v-col>
      </v-row>
    </v-container>
  </template>
  
  <script setup>
  import { ref, reactive } from 'vue'
  import SockJS from 'sockjs-client'
  import { Client as StompClient } from '@stomp/stompjs'
  
  const roomId = ref('')
  const streamStarted = ref(false)
  const localStreamActive = ref(false)
  const localStreamElement = ref(null)
  const fileInput = ref(null)
  const roomIdInput = ref(null) // Vue의 ref 사용
  
  const myKey = Math.random().toString(36).substring(2, 11)
  const pcListMap = reactive(new Map())
  let mySessionId = ''
  const otherSessionIdList = reactive([])
  let localStream = null
  const sendChannelMap = reactive(new Map())
  const dataMap = reactive(new Map())
  let stompClient = null // 전역 변수로 stompClient 설정
  
  const peerConfiguration = {
    iceServers: [
      {
        urls: 'turn:i11a505.p.ssafy.io',
        username: 'usr',
        credential: 'pass'
      }
    ]
  }
  
  const startCam = async () => {
    if (navigator.mediaDevices !== undefined) {
      try {
        const stream = await navigator.mediaDevices.getUserMedia({ audio: true, video: true })
        console.log('Stream found')
        localStream = stream
        stream.getAudioTracks()[0].enabled = true
        localStreamElement.value.srcObject = localStream
        localStreamActive.value = true
      } catch (error) {
        console.error('Error accessing media devices:', error)
      }
    }
  }
  
  const connectSocket = async () => {
    const socket = new SockJS('http://ubuntu@i11a505.p.ssafy.io:8080/stomp/handshake/')
    stompClient = new StompClient({
      webSocketFactory: () => socket,
      debug: (str) => {
        console.log(str)
      },
      onConnect: () => {
        console.log('Connected to WebRTC server')
  
        const url = stompClient.webSocket._transport.url
        const urls = url.split('/')
        mySessionId = urls[6]
  
        console.log('send my id')
  
        stompClient.subscribe(`/topic/peer/iceCandidate/${mySessionId}`, (message) => {
          const key = JSON.parse(message.body).key
          const candidate = JSON.parse(message.body).body
          pcListMap.get(key).addIceCandidate(new RTCIceCandidate({ candidate: candidate.candidate, sdpMLineIndex: candidate.sdpMLineIndex, sdpMid: candidate.sdpMid }))
        })
  
        stompClient.subscribe(`/topic/peer/answer/${mySessionId}`, (message) => {
          const key = JSON.parse(message.body).key
          const answer = JSON.parse(message.body).body
          pcListMap.get(key).setRemoteDescription(new RTCSessionDescription(answer))
        })
  
        stompClient.subscribe(`/topic/peer/offer/${mySessionId}`, (message) => {
          const key = JSON.parse(message.body).mySessionId
          const offer = JSON.parse(message.body).body
          pcListMap.set(key, createPeerConnection(key))
          pcListMap.get(key).setRemoteDescription(new RTCSessionDescription({ type: offer.type, sdp: offer.sdp }))
          sendAnswer(pcListMap.get(key), key)
        })
  
        stompClient.subscribe(`/topic/others/${mySessionId}`, (message) => {
          const sessions = JSON.parse(message.body)
          sessions.forEach((otherSessionId) => {
            if (mySessionId !== otherSessionId) {
              otherSessionIdList.push(otherSessionId)
            }
          })
        })
  
        stompClient.publish({ destination: `/ws/spread/50/50`, body: mySessionId })
      }
    })
  
    stompClient.activate()
  }
  
  const onTrack = (event, otherSessionId) => {
    if (document.getElementById(`${otherSessionId}`) === null) {
      const video = document.createElement('video')
      video.autoplay = true
      video.controls = true
      video.id = otherSessionId
      video.srcObject = event.streams[0]
      document.getElementById('remoteStreamDiv').appendChild(video)
    }
  }
  
  const createPeerConnection = (otherSessionID) => {
    const pc = new RTCPeerConnection(peerConfiguration)
    let conn2
    try {
      pc.addEventListener('icecandidate', (event) => {
        onIceCandidate(event, otherSessionID)
      })
      pc.addEventListener('track', (event) => {
        onTrack(event, otherSessionID)
      })
      pc.addEventListener('datachannel', (event) => {
        console.log('connected to data channel')
        conn2 = event.channel
        conn2.sessionId = otherSessionID
        event.channel.onmessage = function (evt) {
          accumulateStringData(conn2.sessionId, evt.data)
        }
      })
      if (localStream !== undefined) {
        localStream.getTracks().forEach((track) => {
          pc.addTrack(track, localStream)
        })
      }
      const channel = pc.createDataChannel('temp channel')
      sendChannelMap.set(otherSessionID, channel)
  
      channel.onmessage = function (msg) {
        console.log('sended message is ' + msg)
      }
      console.log('PeerConnection created')
    } catch (error) {
      console.error('PeerConnection failed: ', error)
    }
    return pc
  }
  
  const onIceCandidate = (event, otherSessionId) => {
    if (event.candidate) {
      console.log('ICE candidate')
      stompClient.publish({ destination: `/ws/peer/iceCandidate/${mySessionId}/${otherSessionId}`, body: JSON.stringify({
        key: mySessionId,
        body: event.candidate
      })})
    }
  }
  
  const enterRoom = async () => {
    await startCam()
    if (localStream !== undefined) {
      streamStarted.value = true
    }
    roomIdInput.value.$el.disabled = true // Vue의 ref를 사용하여 요소에 접근
    await connectSocket()
  }
  
  const startStream = async () => {
    if (stompClient && stompClient.connected) {
      stompClient.publish({ destination: `/ws/spread/50/50`, body: {} })
      setTimeout(() => {
        otherSessionIdList.forEach((sessionID) => {
          if (!pcListMap.has(sessionID)) {
            pcListMap.set(sessionID, createPeerConnection(sessionID))
            sendOffer(pcListMap.get(sessionID), sessionID)
          }
        })
      }, 1000)
    } else {
      console.error('Stomp client is not connected.')
    }
  }
  
  const sendOffer = (pc, otherSessionId) => {
    pc.createOffer().then((offer) => {
      setLocalAndSendMessage(pc, offer)
      stompClient.publish({ destination: `/ws/peer/offer/${mySessionId}/${otherSessionId}`, body: JSON.stringify({
        mySessionId: mySessionId,
        body: offer
      })})
      console.log('Send offer')
    })
  }
  
  const sendAnswer = (pc, otherSessionId) => {
    pc.createAnswer().then((answer) => {
      setLocalAndSendMessage(pc, answer)
      stompClient.publish({ destination: `/ws/peer/answer/${mySessionId}/${otherSessionId}`, body: JSON.stringify({
        key: mySessionId,
        body: answer
      })})
      console.log('Send answer')
    })
  }
  
  const setLocalAndSendMessage = (pc, sessionDescription) => {
    pc.setLocalDescription(sessionDescription)
  }
  
  const sendFile = () => {
    const file = fileInput.value.files[0]
    if (file) {
      console.log('send start')
      const chunkSize = 16384
      let offset = 0
      const reader = new FileReader()
      reader.onload = (event) => {
        const result = event.target.result
        const base64String = btoa(String.fromCharCode(...new Uint8Array(result)))
        otherSessionIdList.forEach((session) => {
          const channel = sendChannelMap.get(session)
          if (channel.readyState === 'open') {
            channel.send(base64String)
          } else {
            console.error('Channel is not open. Current state:', channel.readyState)
          }
        })
        offset += chunkSize
        if (offset < file.size) {
          readSlice(offset)
        } else {
          otherSessionIdList.forEach((session) => {
            const channel = sendChannelMap.get(session)
            if (channel.readyState === 'open') {
              channel.send('end')
            } else {
              console.error('Channel is not open. Current state:', channel.readyState)
            }
          })
        }
      }
  
      const readSlice = (o) => {
        const slice = file.slice(offset, o + chunkSize)
        reader.readAsArrayBuffer(slice)
      }
      readSlice(0)
    }
  }
  
  const accumulateStringData = (otherSessionId, data) => {
    if (data === 'end') {
      const finalData = dataMap.get(otherSessionId)
      handleReceiveMessage(finalData)
    } else {
      if (dataMap.get(otherSessionId) == null) {
        const temp = []
        temp.push(data)
        dataMap.set(otherSessionId, temp)
      } else {
        dataMap.get(otherSessionId).push(data)
      }
    }
  }
  
  const handleReceiveMessage = (data) => {
    const receiveFileInfo = document.getElementById('receiveFileInfo')
    let totalLength = 0
    const blobs = []
    data.forEach((part) => {
      const binaryString = atob(part)
      const len = binaryString.length
      totalLength += len
      const bytes = new Uint8Array(len)
      for (let i = 0; len > i; i++) {
        bytes[i] = binaryString.charCodeAt(i)
      }
      const blob = new Blob([bytes])
      blobs.push(blob)
    })
  
    const largeBlob = new Blob(blobs, { type: 'audio/mp3' })
    const url = URL.createObjectURL(largeBlob)
    receiveFileInfo.innerHTML += `<p>Received file chunk. <a href="${url}" download="received.mp3">Download</a></p>`
    console.log('done!!!!!!!!!!!!!!!!!!!')
  }
  </script>
  
  <style scoped>
  /* Add your styles here */
  </style>
  