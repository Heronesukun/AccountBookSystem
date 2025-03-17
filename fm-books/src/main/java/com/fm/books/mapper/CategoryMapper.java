package com.fm.books.mapper;

import com.fm.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 分类管理Mapper接口
 * @Time 2025/1/2 21:15
 */
public interface CategoryMapper {
    
    /**
     * 添加分类
     * @param category 分类信息
     * @return 影响行数
     */
    int insertCategory(Category category);
    
    /**
     * 根据ID查询分类
     * @param id 分类ID
     * @return 分类信息
     */
    Category selectCategoryById(Long id);
    
    /**
     * 查询所有分类
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 分类列表
     */
    List<Category> selectAllCategories(@Param("bookId") Long bookId, @Param("userId") String userId);
    
    /**
     * 查询一级分类
     * @param bookId 账本ID
     * @param userId 用户ID
     * @param type 分类类型
     * @return 一级分类列表
     */
    List<Category> selectParentCategories(@Param("bookId") Long bookId, 
                                         @Param("userId") String userId, 
                                         @Param("type") String type);
    
    /**
     * 查询二级分类
     * @param parentId 父分类ID
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 二级分类列表
     */
    List<Category> selectChildCategories(@Param("parentId") Long parentId, 
                                        @Param("bookId") Long bookId, 
                                        @Param("userId") String userId);
    
    /**
     * 更新分类信息
     * @param category 分类信息
     * @return 影响行数
     */
    int updateCategory(Category category);
    
    /**
     * 逻辑删除分类
     * @param id 分类ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteCategory(@Param("id") Long id, @Param("userId") String userId);
    
    /**
     * 批量逻辑删除分类
     * @param ids 分类ID列表
     * @param userId 用户ID
     * @return 影响行数
     */
    int batchDeleteCategories(@Param("ids") List<Long> ids, @Param("userId") String userId);
}