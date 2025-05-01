package com.fm.books.service;

import com.fm.common.pojo.FMResult;
import com.fm.pojo.GeneralTable;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 流水记录管理服务接口
 * @Time 2025/4/15 14:45
 */
public interface GeneralTableService {
    
    /**
     * 添加流水记录
     * @param generalTable 流水记录信息
     * @return 操作结果
     */
    FMResult addGeneralTable(GeneralTable generalTable);
    
    /**
     * 根据ID查询流水记录
     * @param id 流水记录ID
     * @return 流水记录信息
     */
    FMResult getGeneralTableById(Long id);
    
    /**
     * 根据账本ID查询流水记录
     * @param bid 账本ID
     * @param userId 用户ID
     * @return 流水记录列表
     */
    FMResult getGeneralTablesByBid(Long bid, Long userId);
    
    /**
     * 根据条件查询流水记录
     * @param params 查询条件
     * @return 流水记录列表
     */
    FMResult getGeneralTablesByCondition(Map<String, Object> params);
    
    /**
     * 更新流水记录
     * @param generalTable 流水记录信息
     * @return 操作结果
     */
    FMResult updateGeneralTable(GeneralTable generalTable);
    
    /**
     * 删除流水记录
     * @param id 流水记录ID
     * @param userId 用户ID
     * @return 操作结果
     */
    FMResult deleteGeneralTable(Long id, Long userId);
    
    /**
     * 批量删除流水记录
     * @param ids 流水记录ID列表
     * @param userId 用户ID
     * @return 操作结果
     */
    FMResult batchDeleteGeneralTables(List<Long> ids, Long userId);
    
    /**
     * 按日期汇总流水记录
     * @param bid 账本ID
     * @param userId 用户ID
     * @param summaryType 汇总类型（day, week, month, year）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 汇总数据
     */
    FMResult getSummaryByDate(Long bid, Long userId, String summaryType, Date startDate, Date endDate);
}