-- 创建账户分类表
CREATE TABLE fm_account_category (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL COMMENT '分类名称',
  type VARCHAR(20) NOT NULL COMMENT '分类类型：ASSET(资产)、LIABILITY(负债)',
  parent_id BIGINT DEFAULT NULL COMMENT '父分类ID，NULL表示一级分类',
  level INT NOT NULL DEFAULT 1 COMMENT '分类级别：1(一级分类)、2(二级分类)',
  icon VARCHAR(100) COMMENT '分类图标',
  sort_order INT DEFAULT 0 COMMENT '排序顺序',
  book_id BIGINT NOT NULL COMMENT '所属账本ID',
  user_id VARCHAR(50) NOT NULL COMMENT '用户ID',
  asset_amount DECIMAL(12,2) DEFAULT 0.00 COMMENT '资产金额',
  include_in_net_worth TINYINT(1) DEFAULT 1 COMMENT '是否计入净资产',
  is_default TINYINT(1) DEFAULT 0 COMMENT '是否默认分类',
  is_system TINYINT(1) DEFAULT 0 COMMENT '是否系统预设分类',
  is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_parent_id (parent_id),
  INDEX idx_book_id (book_id),
  INDEX idx_user_id (user_id)
);
-- 插入资产账户一级分类
INSERT INTO fm_account_category (name, type, parent_id, level, icon, sort_order, book_id, user_id, asset_amount, include_in_net_worth, is_default, is_system)
VALUES 
('现金账户', 'ASSET', NULL, 1, 'cash_icon', 1, 1, 'user001', 0.00, 1, 1, 1),
('储蓄账户', 'ASSET', NULL, 1, 'savings_icon', 2, 1, 'user001', 0.00, 1, 1, 1),
('投资账户', 'ASSET', NULL, 1, 'investment_icon', 3, 1, 'user001', 0.00, 1, 1, 1),
('数字账户', 'ASSET', NULL, 1, 'digital_icon', 4, 1, 'user001', 0.00, 1, 1, 1);

-- 插入现金账户的二级分类
INSERT INTO fm_account_category (name, type, parent_id, level, icon, sort_order, book_id, user_id, asset_amount, include_in_net_worth, is_default, is_system)
VALUES 
('工资卡', 'ASSET', 1, 2, 'salary_card_icon', 1, 1, 'user001', 5000.00, 1, 1, 1),
('现金钱包', 'ASSET', 1, 2, 'wallet_icon', 2, 1, 'user001', 1000.00, 1, 1, 1);

-- 插入储蓄账户的二级分类
INSERT INTO fm_account_category (name, type, parent_id, level, icon, sort_order, book_id, user_id, asset_amount, include_in_net_worth, is_default, is_system)
VALUES 
('工行储蓄卡', 'ASSET', 2, 2, 'icbc_icon', 1, 1, 'user001', 10000.00, 1, 1, 1),
('建行储蓄卡', 'ASSET', 2, 2, 'ccb_icon', 2, 1, 'user001', 8000.00, 1, 1, 1),
('农行储蓄卡', 'ASSET', 2, 2, 'abc_icon', 3, 1, 'user001', 5000.00, 1, 1, 1);

-- 插入投资账户的二级分类
INSERT INTO fm_account_category (name, type, parent_id, level, icon, sort_order, book_id, user_id, asset_amount, include_in_net_worth, is_default, is_system)
VALUES 
('股票账户', 'ASSET', 3, 2, 'stock_icon', 1, 1, 'user001', 20000.00, 1, 1, 1),
('基金账户', 'ASSET', 3, 2, 'fund_icon', 2, 1, 'user001', 15000.00, 1, 1, 1),
('理财产品', 'ASSET', 3, 2, 'finance_icon', 3, 1, 'user001', 30000.00, 1, 1, 1);

-- 插入数字账户的二级分类
INSERT INTO fm_account_category (name, type, parent_id, level, icon, sort_order, book_id, user_id, asset_amount, include_in_net_worth, is_default, is_system)
VALUES 
('支付宝', 'ASSET', 4, 2, 'alipay_icon', 1, 1, 'user001', 3000.00, 1, 1, 1),
('微信钱包', 'ASSET', 4, 2, 'wechat_icon', 2, 1, 'user001', 2000.00, 1, 1, 1);

-- 插入负债账户一级分类
INSERT INTO fm_account_category (name, type, parent_id, level, icon, sort_order, book_id, user_id, asset_amount, include_in_net_worth, is_default, is_system)
VALUES 
('信用账户', 'LIABILITY', NULL, 1, 'credit_icon', 1, 1, 'user001', 0.00, 1, 1, 1),
('贷款账户', 'LIABILITY', NULL, 1, 'loan_icon', 2, 1, 'user001', 0.00, 1, 1, 1),
('其他负债', 'LIABILITY', NULL, 1, 'other_liability_icon', 3, 1, 'user001', 0.00, 1, 1, 1);

-- 插入信用账户的二级分类
INSERT INTO fm_account_category (name, type, parent_id, level, icon, sort_order, book_id, user_id, asset_amount, include_in_net_worth, is_default, is_system)
VALUES 
('中行信用卡', 'LIABILITY', 5, 2, 'boc_icon', 1, 1, 'user001', -2000.00, 1, 1, 1),
('工行信用卡', 'LIABILITY', 5, 2, 'icbc_credit_icon', 2, 1, 'user001', -1500.00, 1, 1, 1),
('招行信用卡', 'LIABILITY', 5, 2, 'cmb_icon', 3, 1, 'user001', -3000.00, 1, 1, 1);

-- 插入贷款账户的二级分类
INSERT INTO fm_account_category (name, type, parent_id, level, icon, sort_order, book_id, user_id, asset_amount, include_in_net_worth, is_default, is_system)
VALUES 
('房贷', 'LIABILITY', 6, 2, 'house_loan_icon', 1, 1, 'user001', -500000.00, 1, 1, 1),
('车贷', 'LIABILITY', 6, 2, 'car_loan_icon', 2, 1, 'user001', -80000.00, 1, 1, 1),
('消费贷', 'LIABILITY', 6, 2, 'consumer_loan_icon', 3, 1, 'user001', -20000.00, 1, 1, 1);