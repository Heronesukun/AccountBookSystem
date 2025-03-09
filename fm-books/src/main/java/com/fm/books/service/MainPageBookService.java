package com.fm.books.service;

import com.fm.pojo.AccountBook;

import java.util.List;

public interface MainPageBookService {
    //查询自己创建的账本
    public List<AccountBook> getMainPageBooks(String bookMaster);
    //查询自己参与的账本
    public List<AccountBook> getMainPageBooksByParticipants(String bookMaster);
    //创建新账本
    public int createNewBook(AccountBook accountBook);
    public int updateBookName(Integer bookId, String bookName);
    public int deleteBook(Integer bookId);
}
