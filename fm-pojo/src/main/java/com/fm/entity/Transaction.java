package com.fm.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
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
    private LocalDate transactionDate;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}