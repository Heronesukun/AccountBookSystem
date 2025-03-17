package com.fm.books.service.impl;

import com.fm.common.pojo.FMResult;
import com.fm.books.mapper.CategoryMapper;
import com.fm.pojo.Category;
import com.fm.books.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 分类管理服务实现
 * @Time 2025/1/2 21:35
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    public FMResult addCategory(Category category) {
        try {
            // 设置默认值
            if (category.getIsDeleted() == null) {
                category.setIsDeleted(0);
            }
            if (category.getIsDefault() == null) {
                category.setIsDefault(0);
            }
            if (category.getIsSystem() == null) {
                category.setIsSystem(0);
            }
            if (category.getSortOrder() == null) {
                category.setSortOrder(0);
            }
            
            int result = categoryMapper.insertCategory(category);
            if (result > 0) {
                return FMResult.success(category.getId());
            }
            return FMResult.success(500, "添加分类失败");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "添加分类异常: " + e.getMessage());
        }
    }

    @Override
    public FMResult getCategoryById(Long id) {
        try {
            Category category = categoryMapper.selectCategoryById(id);
            if (category != null) {
                return FMResult.success(category);
            }
            return FMResult.error(404, "分类不存在");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "查询分类异常: " + e.getMessage());
        }
    }

    @Override
    public FMResult getAllCategories(Long bookId, String userId) {
        try {
            List<Category> categories = categoryMapper.selectAllCategories(bookId, userId);
            return FMResult.success(categories);
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "查询分类列表异常: " + e.getMessage());
        }
    }

    @Override
    public FMResult getParentCategories(Long bookId, String userId, String type) {
        try {
            List<Category> categories = categoryMapper.selectParentCategories(bookId, userId, type);
            return FMResult.success(categories);
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "查询一级分类异常: " + e.getMessage());
        }
    }

    @Override
    public FMResult getChildCategories(Long parentId, Long bookId, String userId) {
        try {
            List<Category> categories = categoryMapper.selectChildCategories(parentId, bookId, userId);
            return FMResult.success(categories);
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "查询二级分类异常: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public FMResult updateCategory(Category category) {
        try {
            // 检查分类是否存在
            Category existCategory = categoryMapper.selectCategoryById(category.getId());
            if (existCategory == null) {
                return FMResult.error(404, "分类不存在");
            }
            
            // 检查用户权限
            if (!existCategory.getUserId().equals(category.getUserId())) {
                return FMResult.error(403, "无权限修改该分类");
            }
            
            int result = categoryMapper.updateCategory(category);
            if (result > 0) {
                return FMResult.success();
            }
            return FMResult.error(500, "更新分类失败");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "更新分类异常: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public FMResult deleteCategory(Long id, String userId) {
        try {
            // 检查分类是否存在
            Category existCategory = categoryMapper.selectCategoryById(id);
            if (existCategory == null) {
                return FMResult.error(404, "分类不存在");
            }
            
            // 检查用户权限
            if (!existCategory.getUserId().equals(userId)) {
                return FMResult.error(403, "无权限删除该分类");
            }
            
            // 检查是否为系统预设分类
            if (existCategory.getIsSystem() == 1) {
                return FMResult.error(400, "系统预设分类不可删除");
            }
            
            int result = categoryMapper.deleteCategory(id, userId);
            if (result > 0) {
                return FMResult.success();
            }
            return FMResult.error(500, "删除分类失败");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "删除分类异常: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public FMResult batchDeleteCategories(List<Long> ids, String userId) {
        try {
            if (ids == null || ids.isEmpty()) {
                return FMResult.error(400, "请选择要删除的分类");
            }
            
            // 批量删除前检查是否包含系统预设分类
            for (Long id : ids) {
                Category category = categoryMapper.selectCategoryById(id);
                if (category != null && category.getIsSystem() == 1) {
                    return FMResult.error(400, "包含系统预设分类，不可批量删除");
                }
            }
            
            int result = categoryMapper.batchDeleteCategories(ids, userId);
            if (result > 0) {
                return FMResult.success();
            }
            return FMResult.error(500, "批量删除分类失败");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "批量删除分类异常: " + e.getMessage());
        }
    }
}