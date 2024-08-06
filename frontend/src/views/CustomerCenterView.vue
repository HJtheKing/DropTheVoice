<template>
  <v-app class="bg-black">
    <v-main>
      <v-container class="custom-container">
        <v-row justify="center" class="py-4">
          <v-col cols="12" class="text-center">
            <h1 class="title">고객센터</h1>
          </v-col>
        </v-row>
        <v-card class="page-card">
          <v-card-title class="page-header">
            문의
          </v-card-title>
          <v-divider></v-divider>
          <v-card-text class="page-content">
            <p class="welcome-text">고객센터에 오신 것을 환영합니다.</p>
            <p>개선할 것들을 알려주세요.</p>
            <v-textarea
              label="문의"
              v-model="question"
              outlined
              rows="10"
              auto-grow
            />
            <v-btn color="primary" @click="submitQuestion" class="submit-button">제출</v-btn>
          </v-card-text>
        </v-card>

        <!-- Snackbar -->
        <v-snackbar v-model="snackbar" class="snackbar" :timeout="1000" centered color="blue-grey">
          작성된 글이 없습니다.
        </v-snackbar>
        
        <!-- Modal -->
        <v-dialog v-model="dialog" max-width="400">
          <v-card>
            <v-card-title class="headline">감사합니다</v-card-title>
            <v-card-text>검토하여 노력하겠습니다.</v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="green darken-1" text @click="dialog = false">확인</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

      </v-container>
    </v-main>
  </v-app>
</template>

<script>
export default {
  data() {
    return {
      question: '',
      dialog: false,
      snackbar: false
    };
  },
  methods: {
    submitQuestion() {
      if (this.question.trim() === '') {
        this.snackbar = true; // Show the snackbar
      } else {
        // Logic to submit the question
        console.log(`Submitted question: ${this.question}`);
        this.dialog = true; // Show the modal
      }
    }
  }
};
</script>

<style scoped>


.custom-container {
  max-width: 800px;
  margin: 0 auto;
  font-family: Arial, sans-serif;
  padding-bottom: 80px;
}

.page-card {
  background-color: #1e1e1e;
  margin: 0;
  padding: 20px;
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
  background-color: #1e1e1e;
  color: white;
  padding: 20px;
}

.snackbar{
  position:'fixed';
  left:50%;
  top:40%;
  transform: translate(-50%, -50%);
}

.welcome-text {
  font-size: 16px;
  margin-bottom: 20px;
}

.submit-button {
  margin-top: 10px;
  left: 80%;
  align-self: flex-start;
  color: white;
}

.v-snackbar__content {
  display: flex;
  justify-content: center;
  align-items: center;
}

.v-btn--text {
  color: white !important;
}

.title {
  color: #fff;
  font-weight: bold;
  font-size: 24px;
  text-align: center;
}

/* 반응형 스타일 */
@media (max-width: 600px) {
  .page-header, .page-content {
    padding: 10px;
  }
  .submit-button{
    left:80%;
  }
  .title {
    font-size: 20px;
  }
  .welcome-text {
    font-size: 14px;
  }
}

@media (min-width: 601px) and (max-width: 960px) {
  .page-header, .page-content {
    padding: 15px;
  }
  .submit-button{
    left: 70%;
  }
  .title {
    font-size: 22px;
  }
  .welcome-text {
    font-size: 15px;
  }
}

@media (min-width: 961px) {
  .page-header, .page-content {
    padding: 20px;
  }
  .submit-button{
    left: 90%;
  }
  .title {
    font-size: 24px;
  }
  .welcome-text {
    font-size: 16px;
  }
}
</style>
