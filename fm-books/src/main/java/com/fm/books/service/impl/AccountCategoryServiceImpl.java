package com.fm.books.service.impl;

import com.fm.common.pojo.FMResult;
import com.fm.books.mapper.AccountCategoryMapper;
import com.fm.pojo.AccountCategory;
import com.fm.books.service.AccountCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 账户分类管理服务实现
 * @Time 2025/1/5 10:45
 */
@Service
public class AccountCategoryServiceImpl implements AccountCategoryService {

    @Autowired
    private AccountCategoryMapper accountCategoryMapper;

    @Override
    @Transactional
    public FMResult addAccountCategory(AccountCategory accountCategory) {
        try {
            // 设置默认值
            if (accountCategory.getIsDeleted() == null) {
                accountCategory.setIsDeleted(0);
            }
            if (accountCategory.getIsDefault() == null) {
                accountCategory.setIsDefault(0);
            }
            if (accountCategory.getIsSystem() == null) {
                accountCategory.setIsSystem(0);
            }
            if (accountCategory.getSortOrder() == null) {
                accountCategory.setSortOrder(0);
            }
            
            // 修改这里的判断逻辑，确保安全处理null值
            accountCategory.setAssetAmount(accountCategory.getAssetAmount() == null ? 0.0 : accountCategory.getAssetAmount());
            accountCategory.setIncludeInNetWorth(accountCategory.getIncludeInNetWorth() == null ? 1 : accountCategory.getIncludeInNetWorth());
            
            if (accountCategory.getLevel() == null) {
                // 如果有父分类，则为二级分类
                accountCategory.setLevel(accountCategory.getParentId() == null ? 1 : 2);
            }
            
            int result = accountCategoryMapper.insertAccountCategory(accountCategory);
            if (result > 0) {
                return FMResult.success(accountCategory.getId());
            }
            return FMResult.error(500, "添加账户分类失败");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "添加账户分类异常: " + e.getMessage());
        }
    }

    @Override
    public FMResult getAccountCategoryById(Long id) {
        try {
            AccountCategory accountCategory = accountCategoryMapper.selectAccountCategoryById(id);
            if (accountCategory != null) {
                return FMResult.success(accountCategory);
            }
            return FMResult.error(404, "账户分类不存在");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "查询账户分类异常: " + e.getMessage());
        }
    }

    @Override
    public FMResult getAllAccountCategories(Long bookId, String userId) {
        try {
            List<AccountCategory> accountCategories = accountCategoryMapper.selectAllAccountCategories(bookId, userId);
            return FMResult.success(accountCategories);
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "查询账户分类列表异常: " + e.getMessage());
        }
    }

    @Override
    public FMResult getParentAccountCategories(Long bookId, String userId, String type) {
        try {
            List<AccountCategory> accountCategories = accountCategoryMapper.selectParentAccountCategories(bookId, userId, type);
            return FMResult.success(accountCategories);
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "查询一级账户分类异常: " + e.getMessage());
        }
    }

    @Override
    public FMResult getChildAccountCategories(Long parentId, Long bookId, String userId) {
        try {
            List<AccountCategory> accountCategories = accountCategoryMapper.selectChildAccountCategories(parentId, bookId, userId);
            return FMResult.success(accountCategories);
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "查询二级账户分类异常: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public FMResult updateAccountCategory(AccountCategory accountCategory) {
        try {
            // 检查账户分类是否存在
            AccountCategory existAccountCategory = accountCategoryMapper.selectAccountCategoryById(accountCategory.getId());
            if (existAccountCategory == null) {
                return FMResult.error(404, "账户分类不存在");
            }
            
            // 检查用户权限
            if (!existAccountCategory.getUserId().equals(accountCategory.getUserId())) {
                return FMResult.error(403, "无权限修改该账户分类");
            }
            
            // 如果有使用动态更新的地方，将调用改为
            int result = accountCategoryMapper.updateAccountCategorySelective(accountCategory);
            if (result > 0) {
                return FMResult.success();
            }
            return FMResult.error(500, "更新账户分类失败");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "更新账户分类异常: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public FMResult deleteAccountCategory(Long id, String userId) {
        try {
            // 检查账户分类是否存在
            AccountCategory existAccountCategory = accountCategoryMapper.selectAccountCategoryById(id);
            if (existAccountCategory == null) {
                return FMResult.error(404, "账户分类不存在");
            }
            
            // 检查用户权限
            if (!existAccountCategory.getUserId().equals(userId)) {
                return FMResult.error(403, "无权限删除该账户分类");
            }
            
            // 检查是否为系统预设分类
            if (existAccountCategory.getIsSystem() == 1) {
                return FMResult.error(400, "系统预设账户分类不可删除");
            }
            
            int result = accountCategoryMapper.deleteAccountCategory(id, userId);
            if (result > 0) {
                return FMResult.success();
            }
            return FMResult.error(500, "删除账户分类失败");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "删除账户分类异常: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public FMResult batchDeleteAccountCategories(List<Long> ids, String userId) {
        try {
            if (ids == null || ids.isEmpty()) {
                return FMResult.error(400, "请选择要删除的账户分类");
            }
            
            // 批量删除前检查是否包含系统预设分类
            for (Long id : ids) {
                AccountCategory accountCategory = accountCategoryMapper.selectAccountCategoryById(id);
                if (accountCategory != null && accountCategory.getIsSystem() == 1) {
                    return FMResult.error(400, "包含系统预设账户分类，不可批量删除");
                }
            }
            
            int result = accountCategoryMapper.batchDeleteAccountCategories(ids, userId);
            if (result > 0) {
                return FMResult.success();
            }
            return FMResult.error(500, "批量删除账户分类失败");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "批量删除账户分类异常: " + e.getMessage());
        }
    }

    @Override
    public FMResult getNetWorth(Long bookId, String userId) {
        try {
            // 获取所有计入净资产的账户分类
            List<AccountCategory> accountCategories = accountCategoryMapper.selectNetWorthAccountCategories(bookId, userId);
            
            double totalAssets = 0.0;
            double totalLiabilities = 0.0;
            
            for (AccountCategory category : accountCategories) {
                Double amount = category.getAssetAmount();
                // 确保安全处理null值
                if (amount == null) {
                    amount = 0.0;
                }
                
                if ("ASSET".equals(category.getType())) {
                    totalAssets += amount;
                } else if ("LIABILITY".equals(category.getType())) {
                    totalLiabilities += Math.abs(amount);
                }
            }
            
            double netWorth = totalAssets - totalLiabilities;
            
            Map<String, Object> result = new HashMap<>();
            result.put("totalAssets", totalAssets);
            result.put("totalLiabilities", totalLiabilities);
            result.put("netWorth", netWorth);
            
            return FMResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "获取净资产异常: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public FMResult updateAccountAmount(Long id, String userId, Double amount) {
        try {
            // 检查账户分类是否存在
            AccountCategory existAccountCategory = accountCategoryMapper.selectAccountCategoryById(id);
            if (existAccountCategory == null) {
                return FMResult.error(404, "账户分类不存在");
            }
            
            // 检查用户权限
            if (!existAccountCategory.getUserId().equals(userId)) {
                return FMResult.error(403, "无权限修改该账户分类");
            }
            
            int result = accountCategoryMapper.updateAccountAmount(id, userId, amount);
            if (result > 0) {
                return FMResult.success();
            }
            return FMResult.error(500, "更新账户金额失败");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "更新账户金额异常: " + e.getMessage());
        }
    }
}