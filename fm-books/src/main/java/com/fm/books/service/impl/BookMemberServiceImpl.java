package com.fm.books.service.impl;

import com.fm.books.mapper.BookMemberMapper;
import com.fm.books.service.BookMemberService;
import com.fm.common.pojo.FMResult;
import com.fm.pojo.AccountBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 账本成员管理服务实现类
 * @Time 2025/3/10 10:20
 */
@Service
public class BookMemberServiceImpl implements BookMemberService {

    @Autowired
    private BookMemberMapper bookMemberMapper;
    
    @Override
    public FMResult getBookMembers(Integer bookId) {
        try {
            AccountBook book = bookMemberMapper.getBookWithMembers(bookId);
            if (book == null) {
                return FMResult.error("账本不存在");
            }
            
            List<Map<String, Object>> members = new ArrayList<>();
            
            // 添加账本拥有者
            Map<String, Object> master = new HashMap<>();
            master.put("username", book.getBookMaster());
            master.put("role", "master");
            members.add(master);
            
            // 添加参与者
            if (book.getBookParticipant1() != null) {
                Map<String, Object> p1 = new HashMap<>();
                p1.put("username", book.getBookParticipant1());
                p1.put("role", "participant");
                p1.put("position", 1);
                members.add(p1);
            }
            
            if (book.getBookParticipant2() != null) {
                Map<String, Object> p2 = new HashMap<>();
                p2.put("username", book.getBookParticipant2());
                p2.put("role", "participant");
                p2.put("position", 2);
                members.add(p2);
            }
            
            if (book.getBookParticipant3() != null) {
                Map<String, Object> p3 = new HashMap<>();
                p3.put("username", book.getBookParticipant3());
                p3.put("role", "participant");
                p3.put("position", 3);
                members.add(p3);
            }
            
            if (book.getBookParticipant4() != null) {
                Map<String, Object> p4 = new HashMap<>();
                p4.put("username", book.getBookParticipant4());
                p4.put("role", "participant");
                p4.put("position", 4);
                members.add(p4);
            }
            
            return FMResult.success(members);
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error("获取账本成员失败: " + e.getMessage());
        }
    }

    @Override
    public FMResult addBookMember(Integer bookId, String memberName, Integer position) {
        try {
            // 检查位置是否有效
            if (position < 1 || position > 4) {
                return FMResult.error("无效的位置，位置必须在1-4之间");
            }
            
            // 检查该位置是否已有成员
            AccountBook book = bookMemberMapper.getBookWithMembers(bookId);
            if (book == null) {
                return FMResult.error("账本不存在");
            }
            
            // 检查该用户是否已经是成员
            if (bookMemberMapper.checkIsMember(bookId, memberName) > 0) {
                return FMResult.error("该用户已经是账本成员");
            }
            
            // 检查该位置是否已有成员
            String existingMember = null;
            switch (position) {
                case 1:
                    existingMember = book.getBookParticipant1();
                    break;
                case 2:
                    existingMember = book.getBookParticipant2();
                    break;
                case 3:
                    existingMember = book.getBookParticipant3();
                    break;
                case 4:
                    existingMember = book.getBookParticipant4();
                    break;
            }
            
            if (existingMember != null) {
                return FMResult.error("该位置已有成员");
            }
            
            // 添加成员
            int result = bookMemberMapper.addBookMember(bookId, memberName, position);
            if (result > 0) {
                return FMResult.success("成员添加成功");
            } else {
                return FMResult.error("成员添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error("添加成员失败: " + e.getMessage());
        }
    }

    @Override
    public FMResult removeBookMember(Integer bookId, Integer position) {
        try {
            // 检查位置是否有效
            if (position < 1 || position > 4) {
                return FMResult.error("无效的位置，位置必须在1-4之间");
            }
            
            // 检查该位置是否有成员
            AccountBook book = bookMemberMapper.getBookWithMembers(bookId);
            if (book == null) {
                return FMResult.error("账本不存在");
            }
            
            String existingMember = null;
            switch (position) {
                case 1:
                    existingMember = book.getBookParticipant1();
                    break;
                case 2:
                    existingMember = book.getBookParticipant2();
                    break;
                case 3:
                    existingMember = book.getBookParticipant3();
                    break;
                case 4:
                    existingMember = book.getBookParticipant4();
                    break;
            }
            
            if (existingMember == null) {
                return FMResult.error("该位置没有成员");
            }
            
            // 删除成员
            int result = bookMemberMapper.removeBookMember(bookId, position);
            if (result > 0) {
                return FMResult.success("成员删除成功");
            } else {
                return FMResult.error("成员删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error("删除成员失败: " + e.getMessage());
        }
    }

    @Override
    public FMResult updateBookMember(Integer bookId, String oldMemberName, String newMemberName) {
        try {
            // 检查账本是否存在
            AccountBook book = bookMemberMapper.getBookWithMembers(bookId);
            if (book == null) {
                return FMResult.error("账本不存在");
            }
            
            // 检查旧成员是否存在
            if (bookMemberMapper.checkIsMember(bookId, oldMemberName) == 0) {
                return FMResult.error("原成员不存在于该账本");
            }
            
            // 检查新成员是否已经是成员
            if (bookMemberMapper.checkIsMember(bookId, newMemberName) > 0) {
                return FMResult.error("新成员已经是账本成员");
            }
            
            // 更新成员
            int result = bookMemberMapper.updateBookMember(bookId, oldMemberName, newMemberName);
            if (result > 0) {
                return FMResult.success("成员更新成功");
            } else {
                return FMResult.error("成员更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error("更新成员失败: " + e.getMessage());
        }
    }

    @Override
    public FMResult checkIsMember(Integer bookId, String memberName) {
        try {
            // 检查账本是否存在
            AccountBook book = bookMemberMapper.getBookWithMembers(bookId);
            if (book == null) {
                return FMResult.error("账本不存在");
            }
            
            // 检查用户是否为成员
            int count = bookMemberMapper.checkIsMember(bookId, memberName);
            
            Map<String, Object> result = new HashMap<>();
            result.put("isMember", count > 0);
            
            // 如果是成员，确定角色
            if (count > 0) {
                if (memberName.equals(book.getBookMaster())) {
                    result.put("role", "master");
                } else {
                    result.put("role", "participant");
                    
                    // 确定位置
                    if (memberName.equals(book.getBookParticipant1())) {
                        result.put("position", 1);
                    } else if (memberName.equals(book.getBookParticipant2())) {
                        result.put("position", 2);
                    } else if (memberName.equals(book.getBookParticipant3())) {
                        result.put("position", 3);
                    } else if (memberName.equals(book.getBookParticipant4())) {
                        result.put("position", 4);
                    }
                }
            }
            
            return FMResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error("检查成员失败: " + e.getMessage());
        }
    }
}