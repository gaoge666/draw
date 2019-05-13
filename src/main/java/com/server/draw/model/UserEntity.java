package com.server.draw.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="jduser")
public class UserEntity {

    @Id
    @Column(name="wechatid")
    private String userId;

    @Column(name="nickname")
    private String nickName;

    @Column(name = "photo")
    private String photo;

    @Column(name="correctguess")
    private int correctGuess = 0;

    @Column(name="wrongguess")
    private int wrongGuess = 0;

    @Column(name="totalscore")
    private int totalScore = 0;

    @Column(name="rank")
    private int rank = 0;


    public UserEntity() {
        System.out.print(userId);
    }

    public UserEntity(String userId,
                      String nickName,
                      String photo,
                      int correctGuess,
                      int wrongGuess,
                      int totalScore,
                      int rank) {
        this.userId =  userId;
        this.nickName = nickName;
        this.photo = photo;
        this.correctGuess = correctGuess;
        this.wrongGuess = wrongGuess;
        this.totalScore = totalScore;
        this.rank = rank;
    }

    public UserEntity(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserid(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getCorrectGuess() {
        return correctGuess;
    }

    public void setCorrectGuess(int correctGuess) {
        this.correctGuess = correctGuess;
    }

    public int getWrongGuess() {
        return wrongGuess;
    }

    public void setWrongGuess(int wrongGuess) {
        this.wrongGuess = wrongGuess;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
