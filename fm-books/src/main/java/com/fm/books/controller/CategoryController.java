package com.fm.books.controller;

import com.fm.common.pojo.FMResult;
import com.fm.pojo.Category;
import com.fm.books.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 分类管理控制器
 * @Time 2025/1/2 21:45
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    
    /**
     * 添加分类
     * @param category 分类信息
     * @return 操作结果
     */
    @PostMapping("/add")
    public FMResult addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }
    
    /**
     * 根据ID查询分类
     * @param id 分类ID
     * @return 分类信息
     */
    @GetMapping("/get/{id}")
    public FMResult getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }
    
    /**
     * 查询所有分类
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 分类列表
     */
    @GetMapping("/list")
    public FMResult getAllCategories(@RequestParam Long bookId, @RequestParam String userId) {
        return categoryService.getAllCategories(bookId, userId);
    }
    
    /**
     * 查询一级分类
     * @param bookId 账本ID
     * @param userId 用户ID
     * @param type 分类类型
     * @return 一级分类列表
     */
    @GetMapping("/parent")
    public FMResult getParentCategories(
            @RequestParam Long bookId, 
            @RequestParam String userId, 
            @RequestParam String type) {
        return categoryService.getParentCategories(bookId, userId, type);
    }
    
    /**
     * 查询二级分类
     * @param parentId 父分类ID
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 二级分类列表
     */
    @GetMapping("/child")
    public FMResult getChildCategories(
            @RequestParam Long parentId, 
            @RequestParam Long bookId, 
            @RequestParam String userId) {
        return categoryService.getChildCategories(parentId, bookId, userId);
    }
    
    /**
     * 更新分类信息
     * @param category 分类信息
     * @return 操作结果
     */
    @PutMapping("/update")
    public FMResult updateCategory(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }
    
    /**
     * 删除分类
     * @param id 分类ID
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/delete")
    public FMResult deleteCategory(@RequestParam Long id, @RequestParam String userId) {
        return categoryService.deleteCategory(id, userId);
    }
    
    /**
     * 批量删除分类
     * @param ids 分类ID列表
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/batchDelete")
    public FMResult batchDeleteCategories(@RequestParam List<Long> ids, @RequestParam String userId) {
        return categoryService.batchDeleteCategories(ids, userId);
    }
}