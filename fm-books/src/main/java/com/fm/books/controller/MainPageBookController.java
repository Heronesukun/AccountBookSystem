package com.fm.books.controller;

import com.fm.books.service.MainPageBookService;
import com.fm.common.pojo.FMResult;
import com.fm.pojo.AccountBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description
 * @Time 2025/1/2 20:06
 */

@RestController
@RequestMapping("/mainPage")
@CrossOrigin
public class MainPageBookController {
@Autowired
    private MainPageBookService mainPageBookService;

//获取属于自己的主页账本
    @RequestMapping("/getMainPageBooks")
    public Object getMainPageBooks(String bookMaster)
    {
        return FMResult.success(mainPageBookService.getMainPageBooks(bookMaster));
    }
//获取自己参与的账本
    @RequestMapping("/getMainPageBooksWhenParticipants")
    public Object getMainPageBooksByParticipants(String bookMaster)
    {
        return FMResult.success(mainPageBookService.getMainPageBooksByParticipants(bookMaster));
    }

    //创建新账本
    @PutMapping("/createNewBook")
    public Object createNewBook(@RequestBody AccountBook accountBook) {
        int result = mainPageBookService.createNewBook(accountBook);
        if (result > 0) {
            return FMResult.success(accountBook);  // 返回创建的账本对象
        } else {
            return FMResult.error("Failed to create the book");
        }
    }
    //更新账本名称
    @PutMapping("/updateBookName")
    public Object updateBookName(@RequestBody AccountBook accountBook) {
        int result = mainPageBookService.updateBookName(accountBook.getBookId(), accountBook.getBookName());
        if (result > 0) {
            return FMResult.success("Book name updated successfully");
        } else {
            return FMResult.error("Failed to update book name");
        }
    }
    //删除账本
    @PutMapping("/deleteBook")
    public Object deleteBook(@RequestBody AccountBook accountBook) {
        int result = mainPageBookService.deleteBook(accountBook.getBookId());
        if (result > 0) {
            return FMResult.success("Book deleted successfully");
        } else {
            return FMResult.error("Failed to delete book");
        }
    }

}
