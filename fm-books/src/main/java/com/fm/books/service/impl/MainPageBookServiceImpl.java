package com.fm.books.service.impl;

import com.fm.books.mapper.MainPageBookMapper;
import com.fm.books.service.MainPageBookService;
import com.fm.pojo.AccountBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description
 * @Time 2025/1/2 20:08
 */

@Service
public class MainPageBookServiceImpl implements MainPageBookService {
    @Autowired
    private MainPageBookMapper mainPageBookMapper;

    @Override
    public List<AccountBook> getMainPageBooks(String bookMaster) {
        return mainPageBookMapper.getMainPageBooks(bookMaster);
    }

    @Override
    public List<AccountBook> getMainPageBooksByParticipants(String bookMaster) {
        return mainPageBookMapper.getMainPageBooksByParticipants(bookMaster);
    }

    @Override
    public int createNewBook(AccountBook accountBook) {
        return mainPageBookMapper.createNewBook(accountBook);  // 返回插入的行数
    }

    @Override
    public int updateBookName(Integer bookId, String bookName) {
        return mainPageBookMapper.updateBookName(bookId, bookName);
    }

    @Override
    public int deleteBook(Integer bookId) {
        return mainPageBookMapper.deleteBook(bookId);
    }
}
