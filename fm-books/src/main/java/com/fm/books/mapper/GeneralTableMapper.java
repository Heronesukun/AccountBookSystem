package com.fm.books.mapper;

import com.fm.pojo.GeneralTable;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 流水记录管理Mapper接口
 * @Time 2025/4/15 14:30
 */
public interface GeneralTableMapper {
    
    /**
     * 添加流水记录
     * @param generalTable 流水记录信息
     * @return 影响行数
     */
    int insertGeneralTable(GeneralTable generalTable);
    
    /**
     * 根据ID查询流水记录
     * @param id 流水记录ID
     * @return 流水记录信息
     */
    GeneralTable selectGeneralTableById(Long id);
    
    /**
     * 根据ID查询流水记录（包含详细信息）
     * @param id 流水记录ID
     * @return 流水记录详细信息
     */
    GeneralTable selectGeneralTableDetailById(Long id);
    
    /**
     * 根据账本ID查询流水记录
     * @param bid 账本ID
     * @param userId 用户ID
     * @return 流水记录列表
     */
    List<GeneralTable> selectGeneralTablesByBid(@Param("bid") Long bid, @Param("userId") Long userId);
    
    /**
     * 根据账本ID查询流水记录（包含详细信息）
     * @param bid 账本ID
     * @param userId 用户ID
     * @return 流水记录详细信息列表
     */
    List<GeneralTable> selectGeneralTableDetailsByBid(@Param("bid") Long bid, @Param("userId") Long userId);
    
    /**
     * 根据条件查询流水记录
     * @param params 查询条件
     * @return 流水记录列表
     */
    List<GeneralTable> selectGeneralTablesByCondition(Map<String, Object> params);
    
    /**
     * 根据条件查询流水记录（包含详细信息）
     * @param params 查询条件
     * @return 流水记录详细信息列表
     */
    List<GeneralTable> selectGeneralTableDetailsByCondition(Map<String, Object> params);
    
    /**
     * 更新流水记录
     * @param generalTable 流水记录信息
     * @return 影响行数
     */
    int updateGeneralTable(GeneralTable generalTable);
    
    /**
     * 删除流水记录
     * @param id 流水记录ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteGeneralTable(@Param("id") Long id, @Param("userId") Long userId);
    
    /**
     * 批量删除流水记录
     * @param ids 流水记录ID列表
     * @param userId 用户ID
     * @return 影响行数
     */
    int batchDeleteGeneralTables(@Param("ids") List<Long> ids, @Param("userId") Long userId);
    
    /**
     * 按日期汇总流水记录（日汇总）
     * @param bid 账本ID
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日汇总数据
     */
    List<Map<String, Object>> selectDailySummary(
            @Param("bid") Long bid, 
            @Param("userId") Long userId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
    
    /**
     * 按日期汇总流水记录（周汇总）
     * @param bid 账本ID
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 周汇总数据
     */
    List<Map<String, Object>> selectWeeklySummary(
            @Param("bid") Long bid, 
            @Param("userId") Long userId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
    
    /**
     * 按日期汇总流水记录（月汇总）
     * @param bid 账本ID
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 月汇总数据
     */
    List<Map<String, Object>> selectMonthlySummary(
            @Param("bid") Long bid, 
            @Param("userId") Long userId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
    
    /**
     * 按日期汇总流水记录（年汇总）
     * @param bid 账本ID
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 年汇总数据
     */
    List<Map<String, Object>> selectYearlySummary(
            @Param("bid") Long bid, 
            @Param("userId") Long userId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
}