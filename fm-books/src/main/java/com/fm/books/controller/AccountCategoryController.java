package com.fm.books.controller;

import com.fm.common.pojo.FMResult;
import com.fm.pojo.AccountCategory;
import com.fm.books.service.AccountCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 账户分类管理控制器
 * @Time 2025/1/5 10:50
 */
@RestController
@RequestMapping("/accountCategory")
public class AccountCategoryController {

    @Autowired
    private AccountCategoryService accountCategoryService;
    
    /**
     * 添加账户分类
     * @param accountCategory 账户分类信息
     * @return 操作结果
     */
    @PostMapping("/add")
    public FMResult addAccountCategory(@RequestBody AccountCategory accountCategory) {
        return accountCategoryService.addAccountCategory(accountCategory);
    }
    
    /**
     * 根据ID查询账户分类
     * @param id 账户分类ID
     * @return 账户分类信息
     */
    @GetMapping("/get/{id}")
    public FMResult getAccountCategoryById(@PathVariable Long id) {
        return accountCategoryService.getAccountCategoryById(id);
    }
    
    /**
     * 查询所有账户分类
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 账户分类列表
     */
    @GetMapping("/list")
    public FMResult getAllAccountCategories(@RequestParam Long bookId, @RequestParam String userId) {
        return accountCategoryService.getAllAccountCategories(bookId, userId);
    }
    
    /**
     * 查询一级账户分类
     * @param bookId 账本ID
     * @param userId 用户ID
     * @param type 账户分类类型
     * @return 一级账户分类列表
     */
    @GetMapping("/parent")
    public FMResult getParentAccountCategories(
            @RequestParam Long bookId, 
            @RequestParam String userId, 
            @RequestParam String type) {
        return accountCategoryService.getParentAccountCategories(bookId, userId, type);
    }
    
    /**
     * 查询二级账户分类
     * @param parentId 父账户分类ID
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 二级账户分类列表
     */
    @GetMapping("/child")
    public FMResult getChildAccountCategories(
            @RequestParam Long parentId, 
            @RequestParam Long bookId, 
            @RequestParam String userId) {
        return accountCategoryService.getChildAccountCategories(parentId, bookId, userId);
    }
    
    /**
     * 更新账户分类信息
     * @param accountCategory 账户分类信息
     * @return 操作结果
     */
    @PutMapping("/update")
    public FMResult updateAccountCategory(@RequestBody AccountCategory accountCategory) {
        return accountCategoryService.updateAccountCategory(accountCategory);
    }
    
    /**
     * 删除账户分类
     * @param id 账户分类ID
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/delete")
    public FMResult deleteAccountCategory(@RequestParam Long id, @RequestParam String userId) {
        return accountCategoryService.deleteAccountCategory(id, userId);
    }
    
    /**
     * 批量删除账户分类
     * @param ids 账户分类ID列表
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/batchDelete")
    public FMResult batchDeleteAccountCategories(@RequestParam List<Long> ids, @RequestParam String userId) {
        return accountCategoryService.batchDeleteAccountCategories(ids, userId);
    }
    
    /**
     * 获取账本净资产
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 净资产信息
     */
    @GetMapping("/netWorth")
    public FMResult getNetWorth(@RequestParam Long bookId, @RequestParam String userId) {
        return accountCategoryService.getNetWorth(bookId, userId);
    }
    
    /**
     * 更新账户金额
     * @param id 账户分类ID
     * @param userId 用户ID
     * @param amount 金额
     * @return 操作结果
     */
    @PutMapping("/updateAmount")
    public FMResult updateAccountAmount(
            @RequestParam Long id, 
            @RequestParam String userId, 
            @RequestParam Double amount) {
        return accountCategoryService.updateAccountAmount(id, userId, amount);
    }
}