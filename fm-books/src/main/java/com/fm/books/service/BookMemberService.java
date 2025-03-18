package com.fm.books.service;

import com.fm.common.pojo.FMResult;
import com.fm.pojo.AccountBook;

import java.util.List;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 账本成员管理服务接口
 * @Time 2025/3/10 10:00
 */
public interface BookMemberService {
    
    /**
     * 获取账本所有成员
     * @param bookId 账本ID
     * @return 成员列表
     */
    FMResult getBookMembers(Integer bookId);
    
    /**
     * 添加账本成员
     * @param bookId 账本ID
     * @param memberName 成员用户名
     * @param position 位置(1-4)
     * @return 操作结果
     */
    FMResult addBookMember(Integer bookId, String memberName, Integer position);
    
    /**
     * 删除账本成员
     * @param bookId 账本ID
     * @param position 位置(1-4)
     * @return 操作结果
     */
    FMResult removeBookMember(Integer bookId, Integer position);
    
    /**
     * 更新账本成员
     * @param bookId 账本ID
     * @param oldMemberName 原成员用户名
     * @param newMemberName 新成员用户名
     * @return 操作结果
     */
    FMResult updateBookMember(Integer bookId, String oldMemberName, String newMemberName);
    
    /**
     * 检查用户是否为账本成员
     * @param bookId 账本ID
     * @param memberName 成员用户名
     * @return 检查结果
     */
    FMResult checkIsMember(Integer bookId, String memberName);
}