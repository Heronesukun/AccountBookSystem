package com.fm.books.controller;

import com.fm.common.pojo.FMResult;
import com.fm.pojo.GeneralTable;
import com.fm.books.service.GeneralTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 流水记录管理控制器
 * @Time 2025/4/15 15:30
 */
@RestController
@RequestMapping("/general-table")
public class GeneralTableController {

    @Autowired
    private GeneralTableService generalTableService;

    /**
     * 添加流水记录
     */
    @PostMapping("/add")
    public FMResult addGeneralTable(@RequestBody GeneralTable generalTable) {
        return generalTableService.addGeneralTable(generalTable);
    }

    /**
     * 根据ID查询流水记录
     */
    @GetMapping("/get/{id}")
    public FMResult getGeneralTableById(@PathVariable Long id) {
        return generalTableService.getGeneralTableById(id);
    }

    /**
     * 根据账本ID查询流水记录
     */
    @GetMapping("/list/{bid}/{userId}")
    public FMResult getGeneralTablesByBid(@PathVariable Long bid, @PathVariable Long userId) {
        return generalTableService.getGeneralTablesByBid(bid, userId);
    }

    /**
     * 条件查询流水记录
     */
    @PostMapping("/search")
    public FMResult searchGeneralTables(@RequestBody Map<String, Object> params) {
        return generalTableService.getGeneralTablesByCondition(params);
    }

    /**
     * 更新流水记录
     */
    @PutMapping("/update")
    public FMResult updateGeneralTable(@RequestBody GeneralTable generalTable) {
        return generalTableService.updateGeneralTable(generalTable);
    }

    /**
     * 删除流水记录
     */
    @DeleteMapping("/delete/{id}/{userId}")
    public FMResult deleteGeneralTable(@PathVariable Long id, @PathVariable Long userId) {
        return generalTableService.deleteGeneralTable(id, userId);
    }

    /**
     * 批量删除流水记录
     */
    @DeleteMapping("/batch-delete")
    public FMResult batchDeleteGeneralTables(@RequestBody Map<String, Object> params) {
        List<Long> ids = (List<Long>) params.get("ids");
        Long userId = Long.valueOf(params.get("userId").toString());
        return generalTableService.batchDeleteGeneralTables(ids, userId);
    }

    /**
     * 按日期汇总流水记录
     */
    @GetMapping("/summary/{bid}/{userId}/{summaryType}")
    public FMResult getSummaryByDate(
            @PathVariable Long bid,
            @PathVariable Long userId,
            @PathVariable String summaryType,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return generalTableService.getSummaryByDate(bid, userId, summaryType, startDate, endDate);
    }
}