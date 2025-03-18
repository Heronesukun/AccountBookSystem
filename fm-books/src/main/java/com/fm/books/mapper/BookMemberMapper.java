package com.fm.books.mapper;

import com.fm.pojo.AccountBook;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 账本成员管理Mapper接口
 * @Time 2025/3/10 10:10
 */
public interface BookMemberMapper {
    
    /**
     * 获取账本信息（包含成员）
     * @param bookId 账本ID
     * @return 账本信息
     */
    AccountBook getBookWithMembers(Integer bookId);
    
    /**
     * 添加账本成员
     * @param bookId 账本ID
     * @param memberName 成员用户名
     * @param position 位置(1-4)
     * @return 影响行数
     */
    int addBookMember(@Param("bookId") Integer bookId, 
                      @Param("memberName") String memberName, 
                      @Param("position") Integer position);
    
    /**
     * 删除账本成员
     * @param bookId 账本ID
     * @param position 位置(1-4)
     * @return 影响行数
     */
    int removeBookMember(@Param("bookId") Integer bookId, 
                         @Param("position") Integer position);
    
    /**
     * 更新账本成员
     * @param bookId 账本ID
     * @param oldMemberName 原成员用户名
     * @param newMemberName 新成员用户名
     * @return 影响行数
     */
    int updateBookMember(@Param("bookId") Integer bookId, 
                         @Param("oldMemberName") String oldMemberName, 
                         @Param("newMemberName") String newMemberName);
    
    /**
     * 检查用户是否为账本成员
     * @param bookId 账本ID
     * @param memberName 成员用户名
     * @return 是否为成员
     */
    int checkIsMember(@Param("bookId") Integer bookId, 
                      @Param("memberName") String memberName);
}