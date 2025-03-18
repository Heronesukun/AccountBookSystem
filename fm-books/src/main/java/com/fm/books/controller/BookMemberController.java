package com.fm.books.controller;

import com.fm.books.service.BookMemberService;
import com.fm.common.pojo.FMResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 账本成员管理控制器
 * @Time 2025/3/10 11:00
 */
@RestController
@RequestMapping("/bookMember")
@CrossOrigin
public class BookMemberController {

    @Autowired
    private BookMemberService bookMemberService;
    
    /**
     * 获取账本所有成员
     * @param bookId 账本ID
     * @return 成员列表
     */
    @GetMapping("/getMembers")
    public FMResult getBookMembers(@RequestParam Integer bookId) {
        return bookMemberService.getBookMembers(bookId);
    }
    
    /**
     * 添加账本成员
     * @param bookId 账本ID
     * @param memberName 成员用户名
     * @param position 位置(1-4)
     * @return 操作结果
     */
    @PostMapping("/add")
    public FMResult addBookMember(
            @RequestParam Integer bookId, 
            @RequestParam String memberName, 
            @RequestParam Integer position) {
        return bookMemberService.addBookMember(bookId, memberName, position);
    }
    
    /**
     * 删除账本成员
     * @param bookId 账本ID
     * @param position 位置(1-4)
     * @return 操作结果
     */
    @DeleteMapping("/remove")
    public FMResult removeBookMember(
            @RequestParam Integer bookId, 
            @RequestParam Integer position) {
        return bookMemberService.removeBookMember(bookId, position);
    }
    
    /**
     * 更新账本成员
     * @param bookId 账本ID
     * @param oldMemberName 原成员用户名
     * @param newMemberName 新成员用户名
     * @return 操作结果
     */
    @PutMapping("/update")
    public FMResult updateBookMember(
            @RequestParam Integer bookId, 
            @RequestParam String oldMemberName, 
            @RequestParam String newMemberName) {
        return bookMemberService.updateBookMember(bookId, oldMemberName, newMemberName);
    }
    
    /**
     * 检查用户是否为账本成员
     * @param bookId 账本ID
     * @param memberName 成员用户名
     * @return 检查结果
     */
    @GetMapping("/check")
    public FMResult checkIsMember(
            @RequestParam Integer bookId, 
            @RequestParam String memberName) {
        return bookMemberService.checkIsMember(bookId, memberName);
    }
}