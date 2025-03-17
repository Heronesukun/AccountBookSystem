package com.fm.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 交易流水实体类
 * @Time 2025/1/2 20:30
 */
public class Transaction {
    private Long id;
    private String type;
    private BigDecimal amount;
    private Long categoryId;
    private Long accountId;
    private Long fromAccountId;
    private Long toAccountId;
    private Long originalCategoryId;
    private BigDecimal newBalance;
    private Long bookId;
    private String userId;
    private String notes;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date transactionDate;
    private Integer isDeleted;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public Long getOriginalCategoryId() {
        return originalCategoryId;
    }

    public void setOriginalCategoryId(Long originalCategoryId) {
        this.originalCategoryId = originalCategoryId;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(BigDecimal newBalance) {
        this.newBalance = newBalance;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Transaction() {
    }

    public Transaction(Long id, String type, BigDecimal amount, Long categoryId, Long accountId, Long fromAccountId, Long toAccountId, Long originalCategoryId, BigDecimal newBalance, Long bookId, String userId, String notes, Date transactionDate, Integer isDeleted, Date createdAt, Date updatedAt) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.categoryId = categoryId;
        this.accountId = accountId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.originalCategoryId = originalCategoryId;
        this.newBalance = newBalance;
        this.bookId = bookId;
        this.userId = userId;
        this.notes = notes;
        this.transactionDate = transactionDate;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", categoryId=" + categoryId +
                ", accountId=" + accountId +
                ", fromAccountId=" + fromAccountId +
                ", toAccountId=" + toAccountId +
                ", originalCategoryId=" + originalCategoryId +
                ", newBalance=" + newBalance +
                ", bookId=" + bookId +
                ", userId='" + userId + '\'' +
                ", notes='" + notes + '\'' +
                ", transactionDate=" + transactionDate +
                ", isDeleted=" + isDeleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}