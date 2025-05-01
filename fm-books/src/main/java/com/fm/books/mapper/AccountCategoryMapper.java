package com.fm.books.mapper;

import com.fm.pojo.AccountCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 账户分类管理Mapper接口
 * @Time 2025/1/5 10:35
 */
public interface AccountCategoryMapper {
    
    /**
     * 添加账户分类
     * @param accountCategory 账户分类信息
     * @return 影响行数
     */
    int insertAccountCategory(AccountCategory accountCategory);
    
    /**
     * 根据ID查询账户分类
     * @param id 账户分类ID
     * @return 账户分类信息
     */
    AccountCategory selectAccountCategoryById(Long id);
    
    /**
     * 查询所有账户分类
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 账户分类列表
     */
    List<AccountCategory> selectAllAccountCategories(@Param("bookId") Long bookId, @Param("userId") String userId);
    
    /**
     * 查询一级账户分类
     * @param bookId 账本ID
     * @param userId 用户ID
     * @param type 账户分类类型
     * @return 一级账户分类列表
     */
    List<AccountCategory> selectParentAccountCategories(@Param("bookId") Long bookId, 
                                         @Param("userId") String userId, 
                                         @Param("type") String type);
    
    /**
     * 查询二级账户分类
     * @param parentId 父账户分类ID
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 二级账户分类列表
     */
    List<AccountCategory> selectChildAccountCategories(@Param("parentId") Long parentId, 
                                        @Param("bookId") Long bookId, 
                                        @Param("userId") String userId);
    
    /**
     * 查询计入净资产的账户分类
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 账户分类列表
     */
    /**
     * 查询所有计入净资产的账户分类
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 账户分类列表
     */
    List<AccountCategory> selectNetWorthAccountCategories(Long bookId, String userId);
    
    /**
     * 更新账户分类信息
     * @param accountCategory 账户分类信息
     * @return 影响行数
     */
    // 将原来的动态更新方法改名
    int updateAccountCategorySelective(AccountCategory accountCategory);
    
    /**
     * 逻辑删除账户分类
     * @param id 账户分类ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteAccountCategory(@Param("id") Long id, @Param("userId") String userId);
    
    /**
     * 批量逻辑删除账户分类
     * @param ids 账户分类ID列表
     * @param userId 用户ID
     * @return 影响行数
     */
    int batchDeleteAccountCategories(@Param("ids") List<Long> ids, @Param("userId") String userId);
    
    /**
     * 更新账户金额
     * @param id 账户分类ID
     * @param userId 用户ID
     * @param amount 金额
     * @return 影响行数
     */
    int updateAccountAmount(@Param("id") Long id, @Param("userId") String userId, @Param("amount") Double amount);
}