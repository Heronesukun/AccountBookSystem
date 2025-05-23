<template>
  <div class="category-management-container">
    <div class="page-header">
      <h2>收支分类管理</h2>
      <el-button type="primary" @click="showAddCategoryDialog">添加分类</el-button>
    </div>

    <!-- 分类标签页 -->
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="支出分类" name="EXPENSE">
        <div class="category-list">
          <!-- 支出分类列表 -->
          <div v-for="parent in expenseCategories" :key="parent.id" class="category-group">
            <div class="parent-category">
              <div class="category-info">
                <span class="category-icon">
                  <font-awesome-icon v-if="getFontAwesomeIcon(parent.icon)" :icon="getFontAwesomeIcon(parent.icon)" />
                  <span v-else>📋</span>
                </span>
                <span class="category-name">{{ parent.name }}</span>
              </div>
              <div class="category-actions">
                <el-button size="small" @click="editCategory(parent)">编辑</el-button>
                <el-button size="small" type="danger" @click="confirmDelete(parent)" :disabled="parent.isSystem === 1">删除</el-button>
                <el-button size="small" type="success" @click="showAddChildDialog(parent)">添加子分类</el-button>
              </div>
            </div>
            <div class="child-categories">
              <div v-for="child in getChildCategories(parent.id)" :key="child.id" class="child-category">
                <div class="category-info">
                  <span class="category-icon">
                    <font-awesome-icon v-if="getFontAwesomeIcon(child.icon)" :icon="getFontAwesomeIcon(child.icon)" />
                    <span v-else>📝</span>
                  </span>
                  <span class="category-name">{{ child.name }}</span>
                </div>
                <div class="category-actions">
                  <el-button size="small" @click="editCategory(child)">编辑</el-button>
                  <el-button size="small" type="danger" @click="confirmDelete(child)" :disabled="child.isSystem === 1">删除</el-button>
                </div>
              </div>
              <div v-if="getChildCategories(parent.id).length === 0" class="no-child">
                <span class="empty-text">暂无子分类</span>
              </div>
            </div>
          </div>
          <div v-if="expenseCategories.length === 0" class="empty-category">
            <el-empty description="暂无支出分类" />
          </div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="收入分类" name="INCOME">
        <div class="category-list">
          <!-- 收入分类列表 -->
          <div v-for="parent in incomeCategories" :key="parent.id" class="category-group">
            <div class="parent-category">
              <div class="category-info">
                <span class="category-icon">
                  <font-awesome-icon v-if="getFontAwesomeIcon(parent.icon)" :icon="getFontAwesomeIcon(parent.icon)" />
                  <span v-else>📋</span>
                </span>
                <span class="category-name">{{ parent.name }}</span>
              </div>
              <div class="category-actions">
                <el-button size="small" @click="editCategory(parent)">编辑</el-button>
                <el-button size="small" type="danger" @click="confirmDelete(parent)" :disabled="parent.isSystem === 1">删除</el-button>
                <el-button size="small" type="success" @click="showAddChildDialog(parent)">添加子分类</el-button>
              </div>
            </div>
            <div class="child-categories">
              <div v-for="child in getChildCategories(parent.id)" :key="child.id" class="child-category">
                <div class="category-info">
                  <span class="category-icon">
                    <font-awesome-icon v-if="getFontAwesomeIcon(child.icon)" :icon="getFontAwesomeIcon(child.icon)" />
                    <span v-else>📝</span>
                  </span>
                  <span class="category-name">{{ child.name }}</span>
                </div>
                <div class="category-actions">
                  <el-button size="small" @click="editCategory(child)">编辑</el-button>
                  <el-button size="small" type="danger" @click="confirmDelete(child)" :disabled="child.isSystem === 1">删除</el-button>
                </div>
              </div>
              <div v-if="getChildCategories(parent.id).length === 0" class="no-child">
                <span class="empty-text">暂无子分类</span>
              </div>
            </div>
          </div>
          <div v-if="incomeCategories.length === 0" class="empty-category">
            <el-empty description="暂无收入分类" />
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 添加/编辑分类对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
    >
      <el-form :model="categoryForm" :rules="rules" ref="categoryFormRef" label-width="100px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="categoryForm.name" placeholder="请输入分类名称"></el-input>
        </el-form-item>
        <el-form-item label="分类图标" prop="icon">
          <div class="icon-selector">
            <el-input v-model="categoryForm.icon" placeholder="请输入分类图标"></el-input>
            <div class="icon-preview" v-if="categoryForm.icon">
              <span>预览：</span>
              <font-awesome-icon v-if="getFontAwesomeIcon(categoryForm.icon)" :icon="getFontAwesomeIcon(categoryForm.icon)" />
              <span v-else>无效图标</span>
            </div>
            <div class="icon-list">
              <div class="icon-list-title">选择图标：</div>
              <div class="icon-grid">
                <div 
                  v-for="(icon, name) in allIcons" 
                  :key="name" 
                  class="icon-item" 
                  :class="{ 'icon-selected': categoryForm.icon === name }"
                  @click="selectIcon(name)"
                >
                  <font-awesome-icon :icon="icon" />
                  <div class="icon-name">{{ name.replace('_icon', '') }}</div>
                </div>
              </div>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="分类类型" prop="type" v-if="!isEditMode && !isAddChildMode">
          <el-select v-model="categoryForm.type" placeholder="请选择分类类型">
            <el-option label="收入" value="INCOME"></el-option>
            <el-option label="支出" value="EXPENSE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序顺序" prop="sortOrder">
          <el-input-number v-model="categoryForm.sortOrder" :min="0" :max="100"></el-input-number>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveCategory">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 删除确认对话框 -->
    <el-dialog
      title="确认删除"
      v-model="deleteDialogVisible"
      width="400px"
    >
      <p>确定要删除分类 "{{ categoryToDelete?.name }}" 吗？</p>
      <p v-if="categoryToDelete?.level === 1" class="warning-text">注意：删除一级分类将同时删除其下所有二级分类！</p>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deleteDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="deleteCategory">确定删除</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import axios from 'axios';
import config from '@/config/config.js'; // 导入配置文件
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'; // 导入FontAwesomeIcon组件
export default {
  name: 'CategoryManagementPage',
  components: {
    FontAwesomeIcon
  },
  props: {
    bookId: {
      type: [String, Number],
      required: true
    }
  },
  setup(props) {
    // 状态定义
    const activeTab = ref('EXPENSE');
    const allCategories = ref([]);
    const dialogVisible = ref(false);
    const deleteDialogVisible = ref(false);
    const categoryFormRef = ref(null);
    const isEditMode = ref(false);
    const isAddChildMode = ref(false);
    const parentCategory = ref(null);
    const categoryToDelete = ref(null);
    const activeIconCategories = ref(['common']); // 默认展开常用图标分类
    
    // 图标映射函数
    const getFontAwesomeIcon = (iconName) => {
      if (!iconName) return null;
      
      // 图标名称到Font Awesome图标的映射
      const iconMap = {
        'shopping_icon': ['fas', 'shopping-bag'],
        'food_icon': ['fas', 'utensils'],
        'transport_icon': ['fas', 'car'],
        'home_icon': ['fas', 'home'],
        'daily_icon': ['fas', 'box'],
        'supermarket_icon': ['fas', 'shopping-cart'],
        'toiletry_icon': ['fas', 'toilet-paper'],
        'digital_icon': ['fas', 'mobile-alt'],
        'salary_icon': ['fas', 'money-bill-wave'],
        'investment_icon': ['fas', 'chart-line'],
        'other_income_icon': ['fas', 'ellipsis-h'],
        'bonus_icon': ['fas', 'wallet'],
        'performance_icon': ['fas', 'medal'],
        'cash_icon': ['fas', 'money-bill-wave'],
        'savings_icon': ['fas', 'piggy-bank'],
        'wallet_icon': ['fas', 'wallet'],
        'icbc_icon': ['fas', 'university'],
        'ccb_icon': ['fas', 'university'],
        'abc_icon': ['fas', 'university'],
        'stock_icon': ['fas', 'chart-line'],
        'fund_icon': ['fas', 'chart-pie'],
        'finance_icon': ['fas', 'money-bill-wave'],
        'alipay_icon': ['fab', 'alipay'],
        'wechat_icon': ['fab', 'weixin'],
        'credit_icon': ['far', 'credit-card'],
        'loan_icon': ['fas', 'hand-holding-usd'],
        'other_liability_icon': ['fas', 'ellipsis-h'],
        'boc_icon': ['fas', 'university'],
        'icbc_credit_icon': ['far', 'credit-card'],
        'cmb_icon': ['fas', 'university'],
        'house_loan_icon': ['fas', 'home'],
        'car_loan_icon': ['fas', 'car'],
        'consumer_loan_icon': ['fas', 'tags']
      };
      
      return iconMap[iconName] || null;
    };
    
    // 所有图标合并到一个对象中
    const allIcons = {
      'shopping_icon': ['fas', 'shopping-bag'],
      'food_icon': ['fas', 'utensils'],
      'transport_icon': ['fas', 'car'],
      'home_icon': ['fas', 'home'],
      'daily_icon': ['fas', 'box'],
      'supermarket_icon': ['fas', 'shopping-cart'],
      'toiletry_icon': ['fas', 'toilet-paper'],
      'digital_icon': ['fas', 'mobile-alt'],
      'salary_icon': ['fas', 'money-bill-wave'],
      'investment_icon': ['fas', 'chart-line'],
      'other_income_icon': ['fas', 'ellipsis-h'],
      'bonus_icon': ['fas', 'wallet'],
      'performance_icon': ['fas', 'medal'],
      'cash_icon': ['fas', 'money-bill-wave'],
      'savings_icon': ['fas', 'piggy-bank'],
      'wallet_icon': ['fas', 'wallet'],
      'icbc_icon': ['fas', 'university'],
      'ccb_icon': ['fas', 'university'],
      'abc_icon': ['fas', 'university'],
      'stock_icon': ['fas', 'chart-line'],
      'fund_icon': ['fas', 'chart-pie'],
      'finance_icon': ['fas', 'money-bill-wave'],
      'alipay_icon': ['fab', 'alipay'],
      'wechat_icon': ['fab', 'weixin'],
      'credit_icon': ['far', 'credit-card'],
      'loan_icon': ['fas', 'hand-holding-usd'],
      'other_liability_icon': ['fas', 'ellipsis-h'],
      'boc_icon': ['fas', 'university'],
      'icbc_credit_icon': ['far', 'credit-card'],
      'cmb_icon': ['fas', 'university'],
      'house_loan_icon': ['fas', 'home'],
      'car_loan_icon': ['fas', 'car'],
      'consumer_loan_icon': ['fas', 'tags']
    };
    
    // 选择图标
    const selectIcon = (iconName) => {
      categoryForm.icon = iconName;
    };
    
    // 表单数据
    const categoryForm = reactive({
      id: null,
      name: '',
      icon: '',
      type: 'EXPENSE',
      parentId: null,
      level: 1,
      sortOrder: 0,
      bookId: props.bookId,
      userId: localStorage.getItem('userId') || ''
    });
    
    // 表单验证规则
    const rules = {
      name: [
        { required: true, message: '请输入分类名称', trigger: 'blur' },
        { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
      ],
      type: [
        { required: true, message: '请选择分类类型', trigger: 'change' }
      ]
    };
    
    // 计算属性：对话框标题
    const dialogTitle = computed(() => {
      if (isEditMode.value) {
        return '编辑分类';
      } else if (isAddChildMode.value) {
        return '添加子分类';
      } else {
        return '添加分类';
      }
    });
    
    // 计算属性：支出分类列表（一级）
    const expenseCategories = computed(() => {
      return allCategories.value.filter(item => item.type === 'EXPENSE' && item.level === 1);
    });
    
    // 计算属性：收入分类列表（一级）
    const incomeCategories = computed(() => {
      return allCategories.value.filter(item => item.type === 'INCOME' && item.level === 1);
    });
    
    // 获取子分类列表
    const getChildCategories = (parentId) => {
      return allCategories.value.filter(item => item.parentId === parentId);
    };
    
    // 加载分类数据
    const loadCategories = async () => {
      try {
        const userId = localStorage.getItem('userId');
        if (!userId) {
          ElMessage.error('用户未登录');
          return;
        }
        
        // 修改API请求路径，添加mdBaseUrl
        const response = await axios.get(`${config.mdBaseUrl}/category/list`, {
          params: {
            bookId: props.bookId,
            userId: userId
          },
          headers: { token: localStorage.getItem('token') } // 添加token到请求头
        });
        
        // 修改响应处理逻辑，匹配后端返回格式
        if (response.data.code === 200) {  // 修改status为code
          allCategories.value = response.data.data || [];
        } else {
          ElMessage.error(response.data.msg || '加载分类失败');
        }
      } catch (error) {
        console.error('加载分类异常:', error);
        ElMessage.error('加载分类异常');
      }
    };
    
    // 切换标签页
    const handleTabClick = () => {
      // 切换标签页时，如果要添加新分类，更新表单的类型
      if (!isEditMode.value && !isAddChildMode.value) {
        categoryForm.type = activeTab.value;
      }
    };
    
    // 显示添加分类对话框
    const showAddCategoryDialog = () => {
      isEditMode.value = false;
      isAddChildMode.value = false;
      parentCategory.value = null;
      
      // 重置表单
      Object.assign(categoryForm, {
        id: null,
        name: '',
        icon: '',
        type: activeTab.value,
        parentId: null,
        level: 1,
        sortOrder: 0,
        bookId: props.bookId,
        userId: localStorage.getItem('userId') || ''
      });
      
      dialogVisible.value = true;
    };
    
    // 显示添加子分类对话框
    const showAddChildDialog = (parent) => {
      isEditMode.value = false;
      isAddChildMode.value = true;
      parentCategory.value = parent;
      
      // 设置子分类表单
      Object.assign(categoryForm, {
        id: null,
        name: '',
        icon: '',
        type: parent.type,
        parentId: parent.id,
        level: 2,
        sortOrder: 0,
        bookId: props.bookId,
        userId: localStorage.getItem('userId') || ''
      });
      
      dialogVisible.value = true;
    };
    
    // 编辑分类
    const editCategory = (category) => {
      isEditMode.value = true;
      isAddChildMode.value = false;
      
      // 复制分类数据到表单
      Object.assign(categoryForm, {
        id: category.id,
        name: category.name,
        icon: category.icon,
        type: category.type,
        parentId: category.parentId,
        level: category.level,
        sortOrder: category.sortOrder || 0,
        bookId: category.bookId,
        userId: category.userId
      });
      
      dialogVisible.value = true;
    };
    
    // 保存分类
    const saveCategory = async () => {
      if (!categoryFormRef.value) return;
      
      await categoryFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            let response;
            
            if (isEditMode.value) {
              // 更新分类，添加mdBaseUrl
              response = await axios.put(`${config.mdBaseUrl}/category/update`, categoryForm, {
                headers: { token: localStorage.getItem('token') } // 添加token到请求头
              });
            } else {
              // 添加分类，添加mdBaseUrl
              response = await axios.post(`${config.mdBaseUrl}/category/add`, categoryForm, {
                headers: { token: localStorage.getItem('token') } // 添加token到请求头
              });
            }
            
            // 修改响应处理逻辑
            if (response.data.code === 200) {  // 修改status为code
              ElMessage.success(isEditMode.value ? '更新分类成功' : '添加分类成功');
              dialogVisible.value = false;
              loadCategories(); // 重新加载分类列表
            } else {
              ElMessage.error(response.data.msg || (isEditMode.value ? '更新分类失败' : '添加分类失败'));
            }
          } catch (error) {
            console.error('保存分类异常:', error);
            ElMessage.error('保存分类异常');
          }
        }
      });
    };
    
    // 删除分类
    const deleteCategory = async () => {
      if (!categoryToDelete.value) return;
      
      try {
        // 修改API请求路径，添加mdBaseUrl
        const response = await axios.delete(`${config.mdBaseUrl}/category/delete`, {
          params: {
            id: categoryToDelete.value.id,
            userId: localStorage.getItem('userId')
          },
          headers: { token: localStorage.getItem('token') } // 添加token到请求头
        });
        
        // 修改响应处理逻辑
        if (response.data.code === 200) {  // 修改status为code
          ElMessage.success('删除分类成功');
          deleteDialogVisible.value = false;
          loadCategories(); // 重新加载分类列表
        } else {
          ElMessage.error(response.data.msg || '删除分类失败');
        }
      } catch (error) {
        console.error('删除分类异常:', error);
        ElMessage.error('删除分类异常');
      }
    };
    
    // 确认删除
    const confirmDelete = (category) => {
      categoryToDelete.value = category;
      deleteDialogVisible.value = true;
    };
    
    // 页面加载时获取分类数据
    onMounted(() => {
      loadCategories();
    });
    
    return {
      activeTab,
      expenseCategories,
      incomeCategories,
      dialogVisible,
      deleteDialogVisible,
      categoryForm,
      categoryFormRef,
      rules,
      dialogTitle,
      categoryToDelete,
      getChildCategories,
      handleTabClick,
      showAddCategoryDialog,
      showAddChildDialog,
      editCategory,
      saveCategory,
      confirmDelete,
      deleteCategory,
      getFontAwesomeIcon,
      allIcons,
      selectIcon,
      activeIconCategories
    };
  }
}
</script>

<style scoped>
.category-management-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.category-list {
  margin-top: 20px;
}

.category-group {
  margin-bottom: 20px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
}

.parent-category {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 15px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #ebeef5;
}

.child-categories {
  padding: 10px 15px;
}

.child-category {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px dashed #ebeef5;
}

.child-category:last-child {
  border-bottom: none;
}

.category-info {
  display: flex;
  align-items: center;
}

.category-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  margin-right: 10px;
  font-size: 16px;
}

.category-name {
  font-size: 14px;
}

.category-actions {
  display: flex;
  gap: 5px;
}

.no-child {
  padding: 20px 0;
  text-align: center;
  color: #909399;
}

.empty-category {
  padding: 40px 0;
}

.warning-text {
  color: #e6a23c;
  font-weight: bold;
}

/* 图标选择器样式 */
.icon-selector {
  width: 100%;
}

.icon-preview {
  margin-top: 10px;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.icon-list {
  margin-top: 15px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 15px;
  max-height: 300px;
  overflow-y: auto;
}

.icon-list-title {
  font-weight: bold;
  margin-bottom: 10px;
  color: #606266;
}

.icon-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 10px;
}

.icon-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 8px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.icon-item:hover {
  background-color: #f5f7fa;
  border-color: #409eff;
}

.icon-selected {
  background-color: #ecf5ff;
  border-color: #409eff;
  box-shadow: 0 0 5px rgba(64, 158, 255, 0.3);
}

.icon-name {
  font-size: 10px;
  margin-top: 5px;
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 100%;
}
</style>