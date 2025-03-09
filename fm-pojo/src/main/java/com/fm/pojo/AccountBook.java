package com.fm.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description
 * @Time 2025/1/2 19:57
 */

public class AccountBook {
    private Integer bookId;
    private String bookMaster;
    private String bookName;

    //参与者们的姓名
    private String bookParticipant1;
    private String bookParticipant2;
    private String bookParticipant3;
    private String bookParticipant4;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Integer bookStatus;

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookMaster() {
        return bookMaster;
    }

    public void setBookMaster(String bookMaster) {
        this.bookMaster = bookMaster;
    }

    public String getBookParticipant1() {
        return bookParticipant1;
    }

    public void setBookParticipant1(String bookParticipant1) {
        this.bookParticipant1 = bookParticipant1;
    }

    public String getBookParticipant2() {
        return bookParticipant2;
    }

    public void setBookParticipant2(String bookParticipant2) {
        this.bookParticipant2 = bookParticipant2;
    }

    public String getBookParticipant3() {
        return bookParticipant3;
    }

    public void setBookParticipant3(String bookParticipant3) {
        this.bookParticipant3 = bookParticipant3;
    }

    public String getBookParticipant4() {
        return bookParticipant4;
    }

    public void setBookParticipant4(String bookParticipant4) {
        this.bookParticipant4 = bookParticipant4;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(Integer bookStatus) {
        this.bookStatus = bookStatus;
    }

    public AccountBook() {
    }

    public AccountBook(Integer bookId, String bookMaster, String bookName, String bookParticipant1, String bookParticipant2, String bookParticipant3, String bookParticipant4, Date createTime, Integer bookStatus) {
        this.bookId = bookId;
        this.bookMaster = bookMaster;
        this.bookName = bookName;
        this.bookParticipant1 = bookParticipant1;
        this.bookParticipant2 = bookParticipant2;
        this.bookParticipant3 = bookParticipant3;
        this.bookParticipant4 = bookParticipant4;
        this.createTime = createTime;
        this.bookStatus = bookStatus;
    }
}
