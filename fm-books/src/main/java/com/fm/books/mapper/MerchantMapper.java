package com.fm.books.mapper;

import com.fm.pojo.Merchant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 商家管理Mapper接口
 * @Time 2025/3/15 14:10
 */
public interface MerchantMapper {
    
    /**
     * 添加商家
     * @param merchant 商家信息
     * @return 影响行数
     */
    int insertMerchant(Merchant merchant);
    
    /**
     * 根据ID查询商家
     * @param id 商家ID
     * @return 商家信息
     */
    Merchant selectMerchantById(Long id);
    
    /**
     * 查询所有商家
     * @param bookId 账本ID
     * @param userId 用户ID
     * @param keyword 搜索关键词
     * @return 商家列表
     */
    List<Merchant> selectAllMerchants(@Param("bookId") Long bookId, 
                                     @Param("userId") String userId, 
                                     @Param("keyword") String keyword);
    
    /**
     * 更新商家信息
     * @param merchant 商家信息
     * @return 影响行数
     */
    int updateMerchant(Merchant merchant);
    
    /**
     * 逻辑删除商家
     * @param id 商家ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteMerchant(@Param("id") Long id, @Param("userId") String userId);
    
    /**
     * 批量逻辑删除商家
     * @param ids 商家ID列表
     * @param userId 用户ID
     * @return 影响行数
     */
    int batchDeleteMerchants(@Param("ids") List<Long> ids, @Param("userId") String userId);
    
    /**
     * 根据分类ID查询商家
     * @param categoryId 分类ID
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 商家列表
     */
    List<Merchant> selectMerchantsByCategory(@Param("categoryId") Long categoryId, 
                                            @Param("bookId") Long bookId, 
                                            @Param("userId") String userId);
}