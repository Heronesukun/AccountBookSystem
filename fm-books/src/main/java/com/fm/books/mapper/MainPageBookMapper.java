package com.fm.books.mapper;

import com.fm.pojo.AccountBook;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MainPageBookMapper {
    public List<AccountBook> getMainPageBooks(String bookMaster);
    public List<AccountBook> getMainPageBooksByParticipants(String bookMaster);
    public int createNewBook(AccountBook accountBook);
    public int updateBookName(@Param("bookId") Integer bookId, @Param("bookName") String bookName);
    public int deleteBook(Integer bookId);
    public AccountBook getBookWithMembers(Integer bookId);
}
