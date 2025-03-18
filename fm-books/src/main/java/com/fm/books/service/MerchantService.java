package com.fm.books.service;

import com.fm.common.pojo.FMResult;
import com.fm.pojo.Merchant;

import java.util.List;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 商家管理服务接口
 * @Time 2025/3/15 14:20
 */
public interface MerchantService {
    
    /**
     * 添加商家
     * @param merchant 商家信息
     * @return 操作结果
     */
    FMResult addMerchant(Merchant merchant);
    
    /**
     * 根据ID查询商家
     * @param id 商家ID
     * @return 商家信息
     */
    FMResult getMerchantById(Long id);
    
    /**
     * 查询所有商家
     * @param bookId 账本ID
     * @param userId 用户ID
     * @param keyword 搜索关键词
     * @param page 页码
     * @param pageSize 每页大小
     * @return 商家列表
     */
    FMResult getAllMerchants(Long bookId, String userId, String keyword, Integer page, Integer pageSize);
    
    /**
     * 更新商家信息
     * @param merchant 商家信息
     * @return 操作结果
     */
    FMResult updateMerchant(Merchant merchant);
    
    /**
     * 删除商家
     * @param id 商家ID
     * @param userId 用户ID
     * @return 操作结果
     */
    FMResult deleteMerchant(Long id, String userId);
    
    /**
     * 批量删除商家
     * @param ids 商家ID列表
     * @param userId 用户ID
     * @return 操作结果
     */
    FMResult batchDeleteMerchants(List<Long> ids, String userId);
    
    /**
     * 根据分类ID查询商家
     * @param categoryId 分类ID
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 商家列表
     */
    FMResult getMerchantsByCategory(Long categoryId, Long bookId, String userId);
}