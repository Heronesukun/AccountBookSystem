package com.fm.books.service;

import com.fm.common.pojo.FMResult;
import com.fm.pojo.Category;

import java.util.List;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 分类管理服务接口
 * @Time 2025/1/2 21:30
 */
public interface CategoryService {
    
    /**
     * 添加分类
     * @param category 分类信息
     * @return 操作结果
     */
    FMResult addCategory(Category category);
    
    /**
     * 根据ID查询分类
     * @param id 分类ID
     * @return 分类信息
     */
    FMResult getCategoryById(Long id);
    
    /**
     * 查询所有分类
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 分类列表
     */
    FMResult getAllCategories(Long bookId, String userId);
    
    /**
     * 查询一级分类
     * @param bookId 账本ID
     * @param userId 用户ID
     * @param type 分类类型
     * @return 一级分类列表
     */
    FMResult getParentCategories(Long bookId, String userId, String type);
    
    /**
     * 查询二级分类
     * @param parentId 父分类ID
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 二级分类列表
     */
    FMResult getChildCategories(Long parentId, Long bookId, String userId);
    
    /**
     * 更新分类信息
     * @param category 分类信息
     * @return 操作结果
     */
    FMResult updateCategory(Category category);
    
    /**
     * 删除分类
     * @param id 分类ID
     * @param userId 用户ID
     * @return 操作结果
     */
    FMResult deleteCategory(Long id, String userId);
    
    /**
     * 批量删除分类
     * @param ids 分类ID列表
     * @param userId 用户ID
     * @return 操作结果
     */
    FMResult batchDeleteCategories(List<Long> ids, String userId);
}