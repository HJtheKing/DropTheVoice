package com.ssafy.a505.Domain.User;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class User {

    long userId; // 유저 아이디
    String userEmail; // 유저 이메일
    String userName; // 유저 이름
    String userPassword; // 유저 패스워드
    String profileImgUrl; // 프로필 이미지 URL
    int remainChangeCount; // 남은 음성 변조 수
    int totalSpreadCount; // 총 음성 확산 수
    int totalUploadCount; // 총 음성 업로드 수

    public User(){};

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public int getRemainChangeCount() {
        return remainChangeCount;
    }

    public void setRemainChangeCount(int remainChangeCount) {
        this.remainChangeCount = remainChangeCount;
    }

    public int getTotalSpreadCount() {
        return totalSpreadCount;
    }

    public void setTotalSpreadCount(int totalSpreadCount) {
        this.totalSpreadCount = totalSpreadCount;
    }

    public int getTotalUploadCount() {
        return totalUploadCount;
    }

    public void setTotalUploadCount(int totalUploadCount) {
        this.totalUploadCount = totalUploadCount;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userEmail='" + userEmail + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", profileImgUrl='" + profileImgUrl + '\'' +
                ", remainChangeCount=" + remainChangeCount +
                ", totalSpreadCount=" + totalSpreadCount +
                ", totalUploadCount=" + totalUploadCount +
                '}';
    }
}
