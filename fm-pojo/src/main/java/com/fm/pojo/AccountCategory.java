package com.fm.pojo;

import java.util.Date;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 账户分类实体类
 * @Time 2025/1/5 10:30
 */
public class AccountCategory {
    private Long id;
    private String name;
    private String type;  // ASSET(资产)、LIABILITY(负债)
    private Long parentId;
    private Integer level;
    private String icon;
    private Integer sortOrder;
    private Long bookId;
    private String userId;
    private Double assetAmount;  // 资产金额
    private Integer includeInNetWorth;  // 是否计入净资产
    private Integer isDefault;
    private Integer isSystem;
    private Integer isDeleted;
    private Date createdAt;
    private Date updatedAt;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
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
    
    public Double getAssetAmount() {
        return assetAmount;
    }

    public void setAssetAmount(Double assetAmount) {
        this.assetAmount = assetAmount;
    }

    public Integer getIncludeInNetWorth() {
        return includeInNetWorth;
    }

    public void setIncludeInNetWorth(Integer includeInNetWorth) {
        this.includeInNetWorth = includeInNetWorth;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Integer isSystem) {
        this.isSystem = isSystem;
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
}