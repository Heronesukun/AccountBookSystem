package com.fm.books.controller;

import com.fm.books.service.MerchantService;
import com.fm.common.pojo.FMResult;
import com.fm.pojo.Merchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 商家管理控制器
 * @Time 2025/3/15 15:00
 */
@RestController
@RequestMapping("/merchant")
@CrossOrigin
public class MerchantController {

    @Autowired
    private MerchantService merchantService;
    
    /**
     * 添加商家
     * @param merchant 商家信息
     * @return 操作结果
     */
    @PostMapping("/add")
    public FMResult addMerchant(@RequestBody Merchant merchant) {
        return merchantService.addMerchant(merchant);
    }
    
    /**
     * 根据ID查询商家
     * @param id 商家ID
     * @return 商家信息
     */
    @GetMapping("/get/{id}")
    public FMResult getMerchantById(@PathVariable Long id) {
        return merchantService.getMerchantById(id);
    }
    
    /**
     * 查询所有商家
     * @param bookId 账本ID
     * @param userId 用户ID
     * @param keyword 搜索关键词
     * @param page 页码
     * @param pageSize 每页大小
     * @return 商家列表
     */
    @GetMapping("/list")
    public FMResult getAllMerchants(
            @RequestParam Long bookId, 
            @RequestParam String userId, 
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page, 
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return merchantService.getAllMerchants(bookId, userId, keyword, page, pageSize);
    }
    
    /**
     * 更新商家信息
     * @param merchant 商家信息
     * @return 操作结果
     */
    @PutMapping("/update")
    public FMResult updateMerchant(@RequestBody Merchant merchant) {
        return merchantService.updateMerchant(merchant);
    }
    
    /**
     * 删除商家
     * @param id 商家ID
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/delete")
    public FMResult deleteMerchant(
            @RequestParam Long id, 
            @RequestParam String userId) {
        return merchantService.deleteMerchant(id, userId);
    }
    
    /**
     * 批量删除商家
     * @param ids 商家ID列表
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/batchDelete")
    public FMResult batchDeleteMerchants(
            @RequestParam List<Long> ids, 
            @RequestParam String userId) {
        return merchantService.batchDeleteMerchants(ids, userId);
    }
    
    /**
     * 根据分类ID查询商家
     * @param categoryId 分类ID
     * @param bookId 账本ID
     * @param userId 用户ID
     * @return 商家列表
     */
    @GetMapping("/category/{categoryId}")
    public FMResult getMerchantsByCategory(
            @PathVariable Long categoryId,
            @RequestParam Long bookId, 
            @RequestParam String userId) {
        return merchantService.getMerchantsByCategory(categoryId, bookId, userId);
    }
}