# Drop The Voice
<img src="upload/logo.png" width="300" height="300" style="display: block; margin: 0 auto;"/>

## 프로젝트 소개
Local to Countrywide! 지역 음성 SNS 플랫폼 Drop The Voice입니다.<br>

지역 기반의 커뮤니케이션 기능을 통해 지역 커뮤니티 활성화 및 사용자 경험 증대를 기대합니다.

음성 에어드랍으로 자신만의 이야기를 근처의 사용자에게 전파합니다. 이야기를 들은 사용자가 해당 내용이 마음에 들었다면, '좋아요' 기능을 통해 이를 또 다른 사용자들에게 전파할 수 있습니다

또한 이야기를 전파하지 않고 떨어뜨려, 인근의 다른 사용자가 해당 음성을 주워서 이를 들을 수 있는 유저 간의 재미있는 상호작용이 가능합니다.

자신의 목소리를 퍼뜨리는 것이 부담이라면, 음성 변조 기능을 통해 익명성을 보장할 수 있습니다.

Drop The Voice를 통해 자신의 이야기를 전국으로 전파하세요!


## 개발환경
- VS Code : 1.64.2,
- IntelliJ : 2024.01.02
- JVM : eclipse-temurin:21-jdk
- Node.js : v22.4.1
- SERVER : AWS EC2 Ubuntu 20.04.6 LTS
- DB : MySQL:8.0, Redis:alpine

## 사용도구
- 이슈 관리 : <img src="https://img.shields.io/badge/Jira-0052CC?style=flat-square&logo=Jira&logoColor=white" />
- 형상 관리 : <img src="https://img.shields.io/badge/GitLab-FC6D26?style=flat-square&logo=GitLab&logoColor=white" />
- 커뮤니케이션 : <img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=Notion&logoColor=white" /> <img src="https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=Slack&logoColor=white" /> <img src="https://img.shields.io/badge/Mattermost-0058CC?style=flat-square&logo=Mattermost&logoColor=white" />

- 디자인 : <img src="https://img.shields.io/badge/Figma-F24E1E?style=flat-square&logo=Figma&logoColor=white" />
- CI/CD : <img src="https://img.shields.io/badge/GitLab-FC6D26?style=flat-square&logo=Gitlab&logoColor=white" />


## 주요 기능
### ⭐️ WebRTC : 음성 전파
- 현재 접속 중인 반경 내의 무작위 N명의 사용자에게 P2P로 음성 전파
    - 전파 받은 사용자의 브라우저 IndexedDB에 음성 저장, 서버에서 음성을 다운 받지 않아도 됨
    - 서버의 부하 최소화
- WebSocket, Stomp.js를 활용한 Signaling 서버 구축
- EC2 서버에 Turn 서버 구축(coturn)
- 최초 전파, 좋아요 기능을 통해 확산
### ⭐️ Redis : 사용자 정보 관리
- 접속 중인 사용자 세션 관리
- 3초마다 사용자 위치정보 갱신
- geospatial을 활용한 반경 내 사용자 위치 탐색
    - 동일 Key 내의 값들에 개별 TTL 부여 로직 구현
- 떨어뜨린 음성의 생명 주기 관리

### ⭐️ 음성 변조 및 가공
- Flask를 이용한 음성 변조 기능
    - Pitch를 조절하여 원하는 톤으로 음성 변조 가능
- Lamejs, Ffmpeg를 통한 음질 개선 및 압축


## 화면 구성
