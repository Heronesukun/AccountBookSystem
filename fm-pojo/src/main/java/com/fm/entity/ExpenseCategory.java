package com.fm.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExpenseCategory {
    private Long id;
    private String name;
    private String icon;
    private Long bookId;
    private String userId;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}