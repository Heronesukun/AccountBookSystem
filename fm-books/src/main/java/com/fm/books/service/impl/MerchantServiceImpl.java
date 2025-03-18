package com.fm.books.service.impl;

import com.fm.books.mapper.MerchantMapper;
import com.fm.books.service.MerchantService;
import com.fm.common.pojo.FMResult;
import com.fm.pojo.Merchant;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author hitsune
 * @Company kafu chino
 * @Description 商家管理服务实现
 * @Time 2025/3/15 14:30
 */
@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantMapper merchantMapper;

    @Override
    @Transactional
    public FMResult addMerchant(Merchant merchant) {
        try {
            // 设置默认值
            if (merchant.getIsDeleted() == null) {
                merchant.setIsDeleted(0);
            }
            if (merchant.getIsSystem() == null) {
                merchant.setIsSystem(0);
            }
            if (merchant.getSortOrder() == null) {
                merchant.setSortOrder(0);
            }
            
            int result = merchantMapper.insertMerchant(merchant);
            if (result > 0) {
                return FMResult.success(merchant.getId());
            }
            return FMResult.error(500, "添加商家失败");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "添加商家异常: " + e.getMessage());
        }
    }

    @Override
    public FMResult getMerchantById(Long id) {
        try {
            Merchant merchant = merchantMapper.selectMerchantById(id);
            if (merchant != null) {
                return FMResult.success(merchant);
            }
            return FMResult.error(404, "商家不存在");
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "查询商家异常: " + e.getMessage());
        }
    }

    @Override
    public FMResult getAllMerchants(Long bookId, String userId, String keyword, Integer page, Integer pageSize) {
        try {
            // 分页查询
            PageHelper.startPage(page, pageSize);
            List<Merchant> merchants = merchantMapper.selectAllMerchants(bookId, userId, keyword);
            PageInfo<Merchant> pageInfo = new PageInfo<>(merchants);
            
            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("list", pageInfo.getList());
            result.put("total", pageInfo.getTotal());
            result.put("pages", pageInfo.getPages());
            
            return FMResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "查询商家列表异常: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public FMResult updateMerchant(Merchant merchant) {
        try {
            // 检查商家是否存在
            Merchant existingMerchant = merchantMapper.selectMerchantById(merchant.getId());
            if (existingMerchant == null) {
                return FMResult.error(404, "商家不存在");
            }
            
            // 检查是否为系统预设商家
            if (existingMerchant.getIsSystem() == 1) {
                // 系统预设商家只允许修改部分字段
                merchant.setName(existingMerchant.getName());
                merchant.setLogo(existingMerchant.getLogo());
            }
            
            int result = merchantMapper.updateMerchant(merchant);
            if (result > 0) {
                return FMResult.success("商家更新成功");
            } else {
                return FMResult.error(500, "商家更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "更新商家异常: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public FMResult deleteMerchant(Long id, String userId) {
        try {
            // 检查商家是否存在
            Merchant merchant = merchantMapper.selectMerchantById(id);
            if (merchant == null) {
                return FMResult.error(404, "商家不存在");
            }
            
            // 检查是否为系统预设商家
            if (merchant.getIsSystem() == 1) {
                return FMResult.error(403, "系统预设商家不可删除");
            }
            
            int result = merchantMapper.deleteMerchant(id, userId);
            if (result > 0) {
                return FMResult.success("商家删除成功");
            } else {
                return FMResult.error(500, "商家删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "删除商家异常: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public FMResult batchDeleteMerchants(List<Long> ids, String userId) {
        try {
            int result = merchantMapper.batchDeleteMerchants(ids, userId);
            if (result > 0) {
                return FMResult.success("批量删除商家成功");
            } else {
                return FMResult.error(500, "批量删除商家失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "批量删除商家异常: " + e.getMessage());
        }
    }

    @Override
    public FMResult getMerchantsByCategory(Long categoryId, Long bookId, String userId) {
        try {
            List<Merchant> merchants = merchantMapper.selectMerchantsByCategory(categoryId, bookId, userId);
            return FMResult.success(merchants);
        } catch (Exception e) {
            e.printStackTrace();
            return FMResult.error(500, "查询分类商家异常: " + e.getMessage());
        }
    }
}