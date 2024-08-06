<template>
  <v-app class="bg-black">
    <v-container class="page-container">
      <v-card outlined>
        <v-card-title class="page-header">
          개인 정보 처리 방침
        </v-card-title>
        <v-card-text class="page-content">
          <v-row>
            <v-col cols="12">
              <p class="intro-text">
                귀하의 개인 정보는 저희의 최우선 사항입니다. 저희는 귀하의 정보를 보호하고 안전하게 유지하기 위해 최선을 다하고 있습니다.
              </p>
              <p class="details-text">
                본 개인 정보 처리 방침은 귀하가 저희 서비스를 이용할 때 수집, 사용, 공개, 보호되는 정보에 대해 설명합니다.
              </p>
              <h3 class="section-header">1. 수집하는 정보</h3>
              <p>
                저희는 다음과 같은 개인 정보를 수집할 수 있습니다:
              </p>
              <ul class="policy-list">
                <li class="policy-item">이름, 이메일 주소, 전화번호 등 연락처 정보</li>
                <li class="policy-item">서비스 사용 기록, 접속 로그, 쿠키 및 기타 추적 기술을 통한 정보</li>
                <li class="policy-item">결제 정보 (신용카드 번호 등)</li>
              </ul>
              <h3 class="section-header">2. 정보 사용 방법</h3>
              <p>
                저희는 수집된 정보를 다음과 같은 목적으로 사용합니다:
              </p>
              <ul class="policy-list">
                <li class="policy-item">서비스 제공 및 유지</li>
                <li class="policy-item">고객 지원 제공</li>
                <li class="policy-item">서비스 개선 및 개발</li>
                <li class="policy-item">마케팅 및 광고 목적으로 사용</li>
              </ul>
              <h3 class="section-header">3. 정보 보호 방법</h3>
              <p>
                저희는 귀하의 정보를 보호하기 위해 다양한 보안 조치를 시행하고 있습니다:
              </p>
              <ul class="policy-list">
                <li class="policy-item">데이터 암호화</li>
                <li class="policy-item">접근 통제</li>
                <li class="policy-item">보안 소프트웨어 사용</li>
              </ul>
              <h3 class="section-header">4. 연락처</h3>
              <p>
                개인 정보 처리 방침에 대한 질문이 있으시면, 아래 연락처로 문의해주시기 바랍니다:
              </p>
              <ul class="policy-list">
                <li class="policy-item">이메일: ssafy@ssafy.com</li>
                <li class="policy-item">전화: 02) 456-7890</li>
                <li class="policy-item">주소: 서울특별시 강남구 테헤란로 212</li>
              </ul>
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>
    </v-container>
  </v-app>
</template>
<style scoped>
.page-container {
  max-width: 600px;
  margin: 0 auto;
  font-family: Arial, sans-serif;
}
.page-header {
  background-color: #2e004b;
  color: white;
  padding: 20px;
  font-size: 24px;
  text-align: center;
  border-bottom: 2px solid #fff;
}
.page-content {
  background-color: white;
  color: black;
  padding: 20px;
}
.intro-text {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
}
.details-text {
  font-size: 14px;
  margin-bottom: 20px;
}
.section-header {
  font-size: 18px;
  margin-top: 20px;
  margin-bottom: 10px;
  color: #2e004b;
}
.policy-list {
  list-style-type: disc;
  padding-left: 20px;
}
.policy-item {
  font-size: 14px;
  margin-bottom: 10px;
}
</style>
