package com.fm.books.service;

import com.fm.common.pojo.FMResult;
import com.fm.pojo.AccountCategory;

import java.util.List;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 账户分类管理服务接口
 * @Time 2025/1/5 10:40
 */
public interface AccountCategoryService {
    
    /**
     * 添加账户分类
     * @param accountCategory 账户分类信息
     * @return 操作结果
     */
    FMResult addAccountCategory(AccountCategory accountCategory);
    
    /**
     * 根据ID查询账户分类
     * @param id 账户分类ID
     * @return 账户分类信息
     */
    FMResult getAccountCategoryById(Long id);
    
    /**
     * 查询所有账户分类
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 账户分类列表
     */
    FMResult getAllAccountCategories(Long bookId, String userId);
    
    /**
     * 查询一级账户分类
     * @param bookId 账本ID
     * @param userId 用户ID
     * @param type 账户分类类型
     * @return 一级账户分类列表
     */
    FMResult getParentAccountCategories(Long bookId, String userId, String type);
    
    /**
     * 查询二级账户分类
     * @param parentId 父账户分类ID
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 二级账户分类列表
     */
    FMResult getChildAccountCategories(Long parentId, Long bookId, String userId);
    
    /**
     * 更新账户分类信息
     * @param accountCategory 账户分类信息
     * @return 操作结果
     */
    FMResult updateAccountCategory(AccountCategory accountCategory);
    
    /**
     * 删除账户分类
     * @param id 账户分类ID
     * @param userId 用户ID
     * @return 操作结果
     */
    FMResult deleteAccountCategory(Long id, String userId);
    
    /**
     * 批量删除账户分类
     * @param ids 账户分类ID列表
     * @param userId 用户ID
     * @return 操作结果
     */
    FMResult batchDeleteAccountCategories(List<Long> ids, String userId);
    
    /**
     * 获取账本净资产
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 净资产信息
     */
    FMResult getNetWorth(Long bookId, String userId);
    
    /**
     * 更新账户金额
     * @param id 账户分类ID
     * @param userId 用户ID
     * @param amount 金额
     * @return 操作结果
     */
    FMResult updateAccountAmount(Long id, String userId, Double amount);
}