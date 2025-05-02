package com.fm.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 总账单表实体类
 */
public class GeneralTable {
    private Long id;          // 总表id
    private Long bid;         // 对应账单id
    private Long userId;      // 对应用户id
    private Long accountCategoryId;  // 所属账户id
    private Long merchantId;  // 商家id
    private Long categoryId;  // 分类id
    private Double amount;    // 金额
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;        // 日期时间
    
    private String desc;      // 备注
    private String type;    // 种类(区分收入,支出,转账,余额,退款)
    private String reser2;    // 备用字段2
    private Integer isDeleted;      // 删除状态字段
    private Long transferAccountId;      // 接收转账账户id
    
    // 新增字段
    private String userName;              // 记账用户名
    private String accountCategoryName;   // 记账账户名
    private String merchantName;          // 商家名
    private String categoryName;          // 分类名
    private String generalIcon;           // 图标

    // 无参构造函数
    public GeneralTable() {
    }

    // getter和setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAccountCategoryId() {
        return accountCategoryId;
    }

    public void setAccountCategoryId(Long accountCategoryId) {
        this.accountCategoryId = accountCategoryId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReser2() {
        return reser2;
    }

    public void setReser2(String reser2) {
        this.reser2 = reser2;
    }


    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getTransferAccountId() {
        return transferAccountId;
    }

    public void setTransferAccountId(Long transferAccountId) {
        this.transferAccountId = transferAccountId;
    }
    
    // 新增字段的getter和setter方法
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccountCategoryName() {
        return accountCategoryName;
    }

    public void setAccountCategoryName(String accountCategoryName) {
        this.accountCategoryName = accountCategoryName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getGeneralIcon() {
        return generalIcon;
    }

    public void setGeneralIcon(String generalIcon) {
        this.generalIcon = generalIcon;
    }
}