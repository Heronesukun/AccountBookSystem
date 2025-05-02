package com.fm.books.service.impl;

import com.fm.common.pojo.FMResult;
import com.fm.books.mapper.GeneralTableMapper;
import com.fm.books.mapper.AccountCategoryMapper;
import com.fm.books.mapper.CategoryMapper;
import com.fm.books.mapper.MerchantMapper;
import com.fm.pojo.AccountCategory;
import com.fm.pojo.Category;
import com.fm.pojo.GeneralTable;
import com.fm.pojo.Merchant;
import com.fm.books.service.GeneralTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 流水记录管理服务实现
 * @Time 2025/4/15 15:00
 */
@Service
public class GeneralTableServiceImpl implements GeneralTableService {

    @Autowired
    private GeneralTableMapper generalTableMapper;
    
    @Autowired
    private AccountCategoryMapper accountCategoryMapper;
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    @Autowired
    private MerchantMapper merchantMapper;


    @Override
    public FMResult getGeneralTableById(Long id) {
        try {
            GeneralTable generalTable = generalTableMapper.selectGeneralTableById(id);
            if (generalTable != null) {
                return FMResult.success(generalTable);
            }
            return FMResult.error(404, "流水记录不存在");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "查询流水记录异常: " + e.getMessage());
        }
    }

    @Override
    public FMResult getGeneralTablesByBid(Long bid, Long userId) {
        try {
            List<GeneralTable> generalTables = generalTableMapper.selectGeneralTablesByBid(bid, userId);
            return FMResult.success(generalTables);
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "查询流水记录列表异常: " + e.getMessage());
        }
    }

    @Override
    public FMResult getGeneralTablesByCondition(Map<String, Object> params) {
        try {
            List<GeneralTable> generalTables = generalTableMapper.selectGeneralTablesByCondition(params);
            return FMResult.success(generalTables);
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "条件查询流水记录异常: " + e.getMessage());
        }
    }

    @Override
    public FMResult getGeneralTableDetailById(Long id) {
        try {
            GeneralTable generalTable = generalTableMapper.selectGeneralTableDetailById(id);
            if (generalTable != null) {
                return FMResult.success(generalTable);
            }
            return FMResult.error(404, "流水记录不存在");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "查询流水记录详情异常: " + e.getMessage());
        }
    }

    @Override
    public FMResult getGeneralTableDetailsByBid(Long bid, Long userId) {
        try {
            List<GeneralTable> generalTables = generalTableMapper.selectGeneralTableDetailsByBid(bid, userId);
            return FMResult.success(generalTables);
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "查询流水记录详情列表异常: " + e.getMessage());
        }
    }

    @Override
    public FMResult getGeneralTableDetailsByCondition(Map<String, Object> params) {
        try {
            List<GeneralTable> generalTables = generalTableMapper.selectGeneralTableDetailsByCondition(params);
            return FMResult.success(generalTables);
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "条件查询流水记录详情异常: " + e.getMessage());
        }
    }
    
    // 修改现有方法，在添加和更新时手动填充相关名称信息
    @Override
    @Transactional
    public FMResult addGeneralTable(GeneralTable generalTable) {
        try {
            // 设置创建时间
            if (generalTable.getDate() == null) {
                generalTable.setDate(new Date());
            }
            
            // 获取并设置用户名、账户名、商家名、分类名和图标
            // 1. 获取用户名
            if (generalTable.getUserName() == null && generalTable.getUserId() != null) {
                // 这里需要调用用户服务获取用户名，暂时使用默认值
                generalTable.setUserName("用户" + generalTable.getUserId());
            }
            
            // 2. 获取账户名
            if (generalTable.getAccountCategoryName() == null && generalTable.getAccountCategoryId() != null) {
                AccountCategory accountCategory = accountCategoryMapper.selectAccountCategoryById(generalTable.getAccountCategoryId());
                if (accountCategory != null) {
                    generalTable.setAccountCategoryName(accountCategory.getName());
                }
            }
            
            // 3. 获取商家名
            if (generalTable.getMerchantName() == null && generalTable.getMerchantId() != null) {
                Merchant merchant = merchantMapper.selectMerchantById(generalTable.getMerchantId());
                if (merchant != null) {
                    generalTable.setMerchantName(merchant.getName());
                }
            }
            
            // 4. 获取分类名和图标
            if ((generalTable.getCategoryName() == null || generalTable.getGeneralIcon() == null) && generalTable.getCategoryId() != null) {
                Category category = categoryMapper.selectCategoryById(generalTable.getCategoryId());
                if (category != null) {
                    generalTable.setCategoryName(category.getName());
                    generalTable.setGeneralIcon(category.getIcon());
                }
            }
            
            // 插入流水记录
            int result = generalTableMapper.insertGeneralTable(generalTable);
            if (result > 0) {
                // 根据不同类型处理账户余额
                String type = generalTable.getType();
                if (type != null) {
                    switch (type) {
                        case "支出":
                            // 支出：减少账户余额
                            updateAccountBalance(generalTable.getAccountCategoryId(), 
                                    generalTable.getUserId().toString(), 
                                    -Math.abs(generalTable.getAmount()));
                            break;
                        case "收入":
                            // 收入：增加账户余额
                            updateAccountBalance(generalTable.getAccountCategoryId(), 
                                    generalTable.getUserId().toString(), 
                                    Math.abs(generalTable.getAmount()));
                            break;
                        case "转账":
                            // 转账：减少转出账户余额，增加转入账户余额
                            if (generalTable.getTransferAccountId() != null) {
                                // 减少转出账户余额
                                updateAccountBalance(generalTable.getAccountCategoryId(), 
                                        generalTable.getUserId().toString(), 
                                        -Math.abs(generalTable.getAmount()));
                                
                                // 增加转入账户余额
                                updateAccountBalance(generalTable.getTransferAccountId(), 
                                        generalTable.getUserId().toString(), 
                                        Math.abs(generalTable.getAmount()));
                            }
                            break;
                        case "余额":
                            // 余额：直接设置账户余额为指定值
                            setAccountBalance(generalTable.getAccountCategoryId(), 
                                    generalTable.getUserId().toString(), 
                                    generalTable.getAmount());
                            break;
                        case "退款":
                            // 退款：增加账户余额
                            updateAccountBalance(generalTable.getAccountCategoryId(), 
                                    generalTable.getUserId().toString(), 
                                    Math.abs(generalTable.getAmount()));
                            break;
                        default:
                            // 默认处理方式
                            updateAccountBalance(generalTable.getAccountCategoryId(), 
                                    generalTable.getUserId().toString(), 
                                    generalTable.getAmount());
                            break;
                    }
                } else {
                    // 如果没有指定类型，使用默认处理方式
                    updateAccountBalance(generalTable.getAccountCategoryId(), 
                            generalTable.getUserId().toString(), 
                            generalTable.getAmount());
                }
                
                return FMResult.success(generalTable.getId());
            }
            return FMResult.error(500, "添加流水记录失败");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "添加流水记录异常: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public FMResult updateGeneralTable(GeneralTable generalTable) {
        try {
            // 检查流水记录是否存在
            GeneralTable existGeneralTable = generalTableMapper.selectGeneralTableById(generalTable.getId());
            if (existGeneralTable == null) {
                return FMResult.error(404, "流水记录不存在");
            }
            
            // 检查用户权限
            if (!existGeneralTable.getUserId().equals(generalTable.getUserId())) {
                return FMResult.error(403, "无权限修改该流水记录");
            }
            
            // 获取并设置用户名、账户名、商家名、分类名和图标
            // 1. 获取用户名
            if (generalTable.getUserName() == null && generalTable.getUserId() != null) {
                // 这里需要调用用户服务获取用户名，暂时使用默认值
                generalTable.setUserName("用户" + generalTable.getUserId());
            }
            
            // 2. 获取账户名
            if (generalTable.getAccountCategoryName() == null && generalTable.getAccountCategoryId() != null) {
                AccountCategory accountCategory = accountCategoryMapper.selectAccountCategoryById(generalTable.getAccountCategoryId());
                if (accountCategory != null) {
                    generalTable.setAccountCategoryName(accountCategory.getName());
                }
            }
            
            // 3. 获取商家名
            if (generalTable.getMerchantName() == null && generalTable.getMerchantId() != null) {
                Merchant merchant = merchantMapper.selectMerchantById(generalTable.getMerchantId());
                if (merchant != null) {
                    generalTable.setMerchantName(merchant.getName());
                }
            }
            
            // 4. 获取分类名和图标
            if ((generalTable.getCategoryName() == null || generalTable.getGeneralIcon() == null) && generalTable.getCategoryId() != null) {
                Category category = categoryMapper.selectCategoryById(generalTable.getCategoryId());
                if (category != null) {
                    generalTable.setCategoryName(category.getName());
                    generalTable.setGeneralIcon(category.getIcon());
                }
            }
            
            // 先恢复原账户余额
            String oldType = existGeneralTable.getType();
            if (oldType != null) {
                switch (oldType) {
                    case "支出":
                        // 支出：恢复账户余额（增加）
                        updateAccountBalance(existGeneralTable.getAccountCategoryId(), 
                                existGeneralTable.getUserId().toString(), 
                                Math.abs(existGeneralTable.getAmount()));
                        break;
                    case "收入":
                        // 收入：恢复账户余额（减少）
                        updateAccountBalance(existGeneralTable.getAccountCategoryId(), 
                                existGeneralTable.getUserId().toString(), 
                                -Math.abs(existGeneralTable.getAmount()));
                        break;
                    case "转账":
                        // 转账：恢复转出账户和转入账户余额
                        if (existGeneralTable.getTransferAccountId() != null) {
                            // 恢复转出账户余额（增加）
                            updateAccountBalance(existGeneralTable.getAccountCategoryId(), 
                                    existGeneralTable.getUserId().toString(), 
                                    Math.abs(existGeneralTable.getAmount()));
                            
                            // 恢复转入账户余额（减少）
                            updateAccountBalance(existGeneralTable.getTransferAccountId(), 
                                    existGeneralTable.getUserId().toString(), 
                                    -Math.abs(existGeneralTable.getAmount()));
                        }
                        break;
                    case "退款":
                        // 退款：恢复账户余额（减少）
                        updateAccountBalance(existGeneralTable.getAccountCategoryId(), 
                                existGeneralTable.getUserId().toString(), 
                                -Math.abs(existGeneralTable.getAmount()));
                        break;
                    default:
                        // 默认处理方式
                        updateAccountBalance(existGeneralTable.getAccountCategoryId(), 
                                existGeneralTable.getUserId().toString(), 
                                -existGeneralTable.getAmount());
                        break;
                }
            } else {
                // 如果没有指定类型，使用默认处理方式
                updateAccountBalance(existGeneralTable.getAccountCategoryId(), 
                        existGeneralTable.getUserId().toString(), 
                        -existGeneralTable.getAmount());
            }
            
            // 再更新新账户余额
            String newType = generalTable.getType();
            if (newType != null) {
                switch (newType) {
                    case "支出":
                        // 支出：减少账户余额
                        updateAccountBalance(generalTable.getAccountCategoryId(), 
                                generalTable.getUserId().toString(), 
                                -Math.abs(generalTable.getAmount()));
                        break;
                    case "收入":
                        // 收入：增加账户余额
                        updateAccountBalance(generalTable.getAccountCategoryId(), 
                                generalTable.getUserId().toString(), 
                                Math.abs(generalTable.getAmount()));
                        break;
                    case "转账":
                        // 转账：减少转出账户余额，增加转入账户余额
                        if (generalTable.getTransferAccountId() != null) {
                            // 减少转出账户余额
                            updateAccountBalance(generalTable.getAccountCategoryId(), 
                                    generalTable.getUserId().toString(), 
                                    -Math.abs(generalTable.getAmount()));
                            
                            // 增加转入账户余额
                            updateAccountBalance(generalTable.getTransferAccountId(), 
                                    generalTable.getUserId().toString(), 
                                    Math.abs(generalTable.getAmount()));
                        }
                        break;
                    case "余额":
                        // 余额：直接设置账户余额为指定值
                        setAccountBalance(generalTable.getAccountCategoryId(), 
                                generalTable.getUserId().toString(), 
                                generalTable.getAmount());
                        break;
                    case "退款":
                        // 退款：增加账户余额
                        updateAccountBalance(generalTable.getAccountCategoryId(), 
                                generalTable.getUserId().toString(), 
                                Math.abs(generalTable.getAmount()));
                        break;
                    default:
                        // 默认处理方式
                        updateAccountBalance(generalTable.getAccountCategoryId(), 
                                generalTable.getUserId().toString(), 
                                generalTable.getAmount());
                        break;
                }
            } else {
                // 如果没有指定类型，使用默认处理方式
                updateAccountBalance(generalTable.getAccountCategoryId(), 
                        generalTable.getUserId().toString(), 
                        generalTable.getAmount());
            }
            
            int result = generalTableMapper.updateGeneralTable(generalTable);
            if (result > 0) {
                return FMResult.success();
            }
            return FMResult.error(500, "更新流水记录失败");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "更新流水记录异常: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public FMResult deleteGeneralTable(Long id, Long userId) {
        try {
            // 检查流水记录是否存在
            GeneralTable existGeneralTable = generalTableMapper.selectGeneralTableById(id);
            if (existGeneralTable == null) {
                return FMResult.error(404, "流水记录不存在");
            }
            
            // 检查用户权限
            if (!existGeneralTable.getUserId().equals(userId)) {
                return FMResult.error(403, "无权限删除该流水记录");
            }
            
            // 恢复账户余额
            String type = existGeneralTable.getType();
            if (type != null) {
                switch (type) {
                    case "支出":
                        // 支出：恢复账户余额（增加）
                        updateAccountBalance(existGeneralTable.getAccountCategoryId(), 
                                existGeneralTable.getUserId().toString(), 
                                Math.abs(existGeneralTable.getAmount()));
                        break;
                    case "收入":
                        // 收入：恢复账户余额（减少）
                        updateAccountBalance(existGeneralTable.getAccountCategoryId(), 
                                existGeneralTable.getUserId().toString(), 
                                -Math.abs(existGeneralTable.getAmount()));
                        break;
                    case "转账":
                        // 转账：恢复转出账户和转入账户余额
                        if (existGeneralTable.getTransferAccountId() != null) {
                            // 恢复转出账户余额（增加）
                            updateAccountBalance(existGeneralTable.getAccountCategoryId(), 
                                    existGeneralTable.getUserId().toString(), 
                                    Math.abs(existGeneralTable.getAmount()));
                            
                            // 恢复转入账户余额（减少）
                            updateAccountBalance(existGeneralTable.getTransferAccountId(), 
                                    existGeneralTable.getUserId().toString(), 
                                    -Math.abs(existGeneralTable.getAmount()));
                        }
                        break;
                    case "退款":
                        // 退款：恢复账户余额（减少）
                        updateAccountBalance(existGeneralTable.getAccountCategoryId(), 
                                existGeneralTable.getUserId().toString(), 
                                -Math.abs(existGeneralTable.getAmount()));
                        break;
                    default:
                        // 默认处理方式
                        updateAccountBalance(existGeneralTable.getAccountCategoryId(), 
                                existGeneralTable.getUserId().toString(), 
                                -existGeneralTable.getAmount());
                        break;
                }
            } else {
                // 如果没有指定类型，使用默认处理方式
                updateAccountBalance(existGeneralTable.getAccountCategoryId(), 
                        existGeneralTable.getUserId().toString(), 
                        -existGeneralTable.getAmount());
            }
            
            int result = generalTableMapper.deleteGeneralTable(id, userId);
            if (result > 0) {
                return FMResult.success();
            }
            return FMResult.error(500, "删除流水记录失败");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "删除流水记录异常: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public FMResult batchDeleteGeneralTables(List<Long> ids, Long userId) {
        try {
            if (ids == null || ids.isEmpty()) {
                return FMResult.error(400, "请选择要删除的流水记录");
            }
            
            // 批量删除前，先恢复各账户余额
            for (Long id : ids) {
                GeneralTable existGeneralTable = generalTableMapper.selectGeneralTableById(id);
                if (existGeneralTable != null && existGeneralTable.getUserId().equals(userId)) {
                    // 恢复账户余额
                    String type = existGeneralTable.getType();
                    if (type != null) {
                        switch (type) {
                            case "支出":
                                // 支出：恢复账户余额（增加）
                                updateAccountBalance(existGeneralTable.getAccountCategoryId(), 
                                        existGeneralTable.getUserId().toString(), 
                                        Math.abs(existGeneralTable.getAmount()));
                                break;
                            case "收入":
                                // 收入：恢复账户余额（减少）
                                updateAccountBalance(existGeneralTable.getAccountCategoryId(), 
                                        existGeneralTable.getUserId().toString(), 
                                        -Math.abs(existGeneralTable.getAmount()));
                                break;
                            case "转账":
                                // 转账：恢复转出账户和转入账户余额
                                if (existGeneralTable.getTransferAccountId() != null) {
                                    // 恢复转出账户余额（增加）
                                    updateAccountBalance(existGeneralTable.getAccountCategoryId(), 
                                            existGeneralTable.getUserId().toString(), 
                                            Math.abs(existGeneralTable.getAmount()));
                                    
                                    // 恢复转入账户余额（减少）
                                    updateAccountBalance(existGeneralTable.getTransferAccountId(), 
                                            existGeneralTable.getUserId().toString(), 
                                            -Math.abs(existGeneralTable.getAmount()));
                                }
                                break;
                            case "退款":
                                // 退款：恢复账户余额（减少）
                                updateAccountBalance(existGeneralTable.getAccountCategoryId(), 
                                        existGeneralTable.getUserId().toString(), 
                                        -Math.abs(existGeneralTable.getAmount()));
                                break;
                            default:
                                // 默认处理方式
                                updateAccountBalance(existGeneralTable.getAccountCategoryId(), 
                                        existGeneralTable.getUserId().toString(), 
                                        -existGeneralTable.getAmount());
                                break;
                        }
                    } else {
                        // 如果没有指定类型，使用默认处理方式
                        updateAccountBalance(existGeneralTable.getAccountCategoryId(), 
                                existGeneralTable.getUserId().toString(), 
                                -existGeneralTable.getAmount());
                    }
                }
            }
            
            int result = generalTableMapper.batchDeleteGeneralTables(ids, userId);
            if (result > 0) {
                return FMResult.success();
            }
            return FMResult.error(500, "批量删除流水记录失败");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "批量删除流水记录异常: " + e.getMessage());
        }
    }

    @Override
    public FMResult getSummaryByDate(Long bid, Long userId, String summaryType, Date startDate, Date endDate) {
        try {
            List<Map<String, Object>> summaryData;
            
            switch (summaryType.toLowerCase()) {
                case "day":
                    summaryData = generalTableMapper.selectDailySummary(bid, userId, startDate, endDate);
                    break;
                case "week":
                    summaryData = generalTableMapper.selectWeeklySummary(bid, userId, startDate, endDate);
                    break;
                case "month":
                    summaryData = generalTableMapper.selectMonthlySummary(bid, userId, startDate, endDate);
                    break;
                case "year":
                    summaryData = generalTableMapper.selectYearlySummary(bid, userId, startDate, endDate);
                    break;
                default:
                    return FMResult.error(400, "不支持的汇总类型");
            }
            
            return FMResult.success(summaryData);
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "汇总流水记录异常: " + e.getMessage());
        }
    }
    
    /**
     * 设置账户余额为指定值
     * @param accountCategoryId 账户分类ID
     * @param userId 用户ID
     * @param amount 设置的余额值
     */
    private void setAccountBalance(Long accountCategoryId, String userId, Double amount) {
        try {
            // 查询账户信息
            AccountCategory accountCategory = accountCategoryMapper.selectAccountCategoryById(accountCategoryId);
            if (accountCategory != null && accountCategory.getUserId().equals(userId)) {
                // 直接设置账户余额
                accountCategory.setAssetAmount(amount);
                accountCategoryMapper.updateAccountAmount(accountCategory.getId(), accountCategory.getUserId(), amount);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("设置账户余额失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新账户余额
     * @param accountCategoryId 账户分类ID
     * @param userId 用户ID
     * @param amount 金额变动（正数为增加，负数为减少）
     */
    private void updateAccountBalance(Long accountCategoryId, String userId, Double amount) {
        try {
            // 查询账户信息
            AccountCategory accountCategory = accountCategoryMapper.selectAccountCategoryById(accountCategoryId);
            if (accountCategory != null && accountCategory.getUserId().equals(userId)) {
                // 计算新余额
                Double currentAmount = accountCategory.getAssetAmount() == null ? 0.0 : accountCategory.getAssetAmount();
                Double newAmount = currentAmount + amount;
                
                // 更新账户余额
                accountCategory.setAssetAmount(newAmount);
                accountCategoryMapper.updateAccountAmount(accountCategory.getId(), accountCategory.getUserId(), newAmount);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("更新账户余额失败: " + e.getMessage());
        }
    }
}