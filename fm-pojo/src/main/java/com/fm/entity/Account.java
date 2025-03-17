package com.fm.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Account {
    private Long id;
    private String name;
    private BigDecimal balance;
    private String icon;
    private String accountType;
    private Long bookId;
    private String userId;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}