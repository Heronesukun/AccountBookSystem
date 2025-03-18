CREATE TABLE fm_merchant (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL COMMENT '商家名称',
  category_id BIGINT COMMENT '关联的分类ID',
  logo VARCHAR(100) COMMENT '商家logo',
  address VARCHAR(200) COMMENT '商家地址',
  contact VARCHAR(50) COMMENT '联系方式',
  description VARCHAR(255) COMMENT '商家描述',
  sort_order INT DEFAULT 0 COMMENT '排序顺序',
  book_id BIGINT NOT NULL COMMENT '所属账本ID',
  user_id VARCHAR(50) NOT NULL COMMENT '用户ID',
  is_default TINYINT(1) DEFAULT 0 COMMENT '是否默认商家',
  is_system TINYINT(1) DEFAULT 0 COMMENT '是否系统预设商家',
  is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_category_id (category_id),
  INDEX idx_book_id (book_id),
  INDEX idx_user_id (user_id)
);

-- 插入系统预设商家
INSERT INTO fm_merchant (name, category_id, logo, sort_order, book_id, user_id, is_default, is_system)
VALUES 
('沃尔玛超市', 2, 'walmart_logo', 1, 1, 'user001', 1, 1),
('肯德基', 2, 'kfc_logo', 2, 1, 'user001', 1, 1),
('麦当劳', 2, 'mcdonalds_logo', 3, 1, 'user001', 1, 1),
('星巴克', 2, 'starbucks_logo', 4, 1, 'user001', 1, 1),
('滴滴出行', 3, 'didi_logo', 5, 1, 'user001', 1, 1);