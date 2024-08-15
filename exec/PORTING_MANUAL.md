# 1. 빌드 및 배포 문서

**개발환경**
- VS Code : 1.64.2,
- IntelliJ : 2024.01.02
- JVM : eclipse-temurin:21-jdk
- Node.js : v22.4.1
- SERVER : AWS EC2 Ubuntu 20.04.6 LTS
- DB : MySQL:8.0, Redis:alpine

**사용도구**
- 이슈 관리 : JIRA
- 형상 관리 : Gitlab
- 커뮤니케이션 : Notion, Slack, Mattermost
- 디자인 : Figma
- CI/CD : GITLAB

**배포에 필요한 파일**

- application-local.properties
```yaml
# aws s3
cloud.aws.credentials.access-key= AKIA47CRXX2RTVOGSE6L
cloud.aws.credentials.secret-key= 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
cloud.aws.region.static= ap-southeast-2
cloud.aws.s3.bucketName= voice-dev-test-bucket
cloud.aws.stack.auto= false
cloud.aws.s3.folder1= original-voice/
cloud.aws.s3.folder2= processed-voice/
cloud.aws.s3.folder3= member-img/
cloud.aws.s3.folder4= voice-img/

# flask
spring.flask.url=http://127.0.0.1:5000/process-audio


# mysql db
MYSQL_URL=jdbc:mysql://mysql-container:3306/dropthevoice
MYSQL_USERNAME=ssafy
MYSQL_PASSWORD=ssafy

# redis
REDIS_HOST=redis
REDIS_PORT=6379
- ```

- env
```dotenv
VITE_NAVER_MAP_CLIENT_ID="a9nbd038oa"
VITE_BASE_URL="https://i11a505.p.ssafy.io/back"
VITE_OPENAI_API_KEY="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"
```



# 2. 외부 서비스 문서
- AWS S3

# 3. 시연 시나리오


1. 메인화면진입

<div style="display: flex; justify-content: space-around;" >
<img style="width: 300px; padding: 10px" src="./image/첫 화면 페이지.png"/>
<img style="width: 300px; padding: 10px" src="./image/회원가입.png"/>
<img style="width: 300px; padding: 10px" src="./image/로그인.png"/>
</div>

2. 뿌리기
<div style="display: flex; justify-content: space-around;" >
<img style="width: 200px; padding: 10px" src="./image/메인페이지.png"/>
<img style="width: 200px; padding: 10px" src="./image/뿌리기.png"/>
<img style="width: 200px; padding: 10px" src="./image/실시간 녹음하기.png"/>
<img style="width: 200px; padding: 10px" src="./image/녹음후 음성뿌리기.png"/>
</div>

3. 포켓몬
<div style="display: flex; justify-content: space-around;" >
<img style="width: 200px; padding: 10px" src="./image/주변음성탐색하기.png"/>
<img style="width: 200px; padding: 10px" src="./image/주변음성반경으로탐색하기.png"/>
<img style="width: 200px; padding: 10px" src="./image/내가주운음성및좋아요및바이러스에서주운음성.png"/>
<img style="width: 200px; padding: 10px" src="./image/음성듣기.png"/>
</div>
