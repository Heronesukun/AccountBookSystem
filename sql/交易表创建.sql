CREATE TABLE fm_category (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL COMMENT '分类名称',
  type VARCHAR(20) NOT NULL COMMENT '分类类型：INCOME(收入)、EXPENSE(支出)',
  parent_id BIGINT DEFAULT NULL COMMENT '父分类ID，NULL表示一级分类',
  level INT NOT NULL DEFAULT 1 COMMENT '分类级别：1(一级分类)、2(二级分类)',
  icon VARCHAR(100) COMMENT '分类图标',
  sort_order INT DEFAULT 0 COMMENT '排序顺序',
  book_id BIGINT NOT NULL COMMENT '所属账本ID',
  user_id VARCHAR(50) NOT NULL COMMENT '用户ID',
  is_default TINYINT(1) DEFAULT 0 COMMENT '是否默认分类',
  is_system TINYINT(1) DEFAULT 0 COMMENT '是否系统预设分类',
  is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_parent_id (parent_id),
  INDEX idx_book_id (book_id),
  INDEX idx_user_id (user_id)
);
CREATE TABLE fm_transaction (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  type VARCHAR(20) NOT NULL COMMENT '交易类型',
  amount DECIMAL(12,2) NOT NULL COMMENT '交易金额',
  category_id BIGINT COMMENT '分类ID',
  account_id BIGINT COMMENT '账户ID',
  from_account_id BIGINT COMMENT '转出账户ID',
  to_account_id BIGINT COMMENT '转入账户ID',
  original_category_id BIGINT COMMENT '原支出分类ID',
  new_balance DECIMAL(12,2) COMMENT '新余额',
  book_id BIGINT NOT NULL COMMENT '所属账本ID',
  user_id VARCHAR(50) NOT NULL COMMENT '记录用户ID',
  notes VARCHAR(255) COMMENT '备注',
  transaction_date DATE NOT NULL COMMENT '交易日期',
  is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_category_id (category_id),
  INDEX idx_account_id (account_id),
  INDEX idx_book_id (book_id),
  INDEX idx_user_id (user_id),
  INDEX idx_transaction_date (transaction_date)
);
-- 插入支出一级分类
INSERT INTO fm_category (name, type, parent_id, level, icon, sort_order, book_id, user_id, is_default, is_system)
VALUES 
('购物消费', 'EXPENSE', NULL, 1, 'shopping_icon', 1, 1, 'user001', 1, 1),
('餐饮美食', 'EXPENSE', NULL, 1, 'food_icon', 2, 1, 'user001', 1, 1),
('交通出行', 'EXPENSE', NULL, 1, 'transport_icon', 3, 1, 'user001', 1, 1),
('居家生活', 'EXPENSE', NULL, 1, 'home_icon', 4, 1, 'user001', 1, 1);

-- 插入购物消费的二级分类
INSERT INTO fm_category (name, type, parent_id, level, icon, sort_order, book_id, user_id, is_default, is_system)
VALUES 
('日常用品', 'EXPENSE', 1, 2, 'daily_icon', 1, 1, 'user001', 1, 1),
('超市购物', 'EXPENSE', 1, 2, 'supermarket_icon', 2, 1, 'user001', 1, 1),
('洗护用品', 'EXPENSE', 1, 2, 'toiletry_icon', 3, 1, 'user001', 1, 1),
('数码电器', 'EXPENSE', 1, 2, 'digital_icon', 4, 1, 'user001', 1, 1);

-- 插入收入一级分类
INSERT INTO fm_category (name, type, parent_id, level, icon, sort_order, book_id, user_id, is_default, is_system)
VALUES 
('职业收入', 'INCOME', NULL, 1, 'salary_icon', 1, 1, 'user001', 1, 1),
('投资理财', 'INCOME', NULL, 1, 'investment_icon', 2, 1, 'user001', 1, 1),
('其他收入', 'INCOME', NULL, 1, 'other_income_icon', 3, 1, 'user001', 1, 1);

-- 插入职业收入的二级分类
INSERT INTO fm_category (name, type, parent_id, level, icon, sort_order, book_id, user_id, is_default, is_system)
VALUES 
('工资收入', 'INCOME', 5, 2, 'salary_icon', 1, 1, 'user001', 1, 1),
('奖金收入', 'INCOME', 5, 2, 'bonus_icon', 2, 1, 'user001', 1, 1),
('绩效收入', 'INCOME', 5, 2, 'performance_icon', 3, 1, 'user001', 1, 1);

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