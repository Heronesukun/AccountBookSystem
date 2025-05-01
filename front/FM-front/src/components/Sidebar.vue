<template>
  <div class="sidebar">
    <!-- 记一笔按钮 -->
    <div class="add-record">
      <el-button type="primary" class="add-record-btn" @click="openRecordDrawer" size="large">记一笔</el-button>
    </div>

    <!-- 主导航 -->
    <el-menu
      :default-active="activePage"
      class="el-menu-vertical"
      @select="changePage"
      :collapse="false"
      background-color="#f5f7fa"
      text-color="#303133"
      active-text-color="#409EFF"
    >
      <!-- 主导航项 -->
      <el-menu-item v-for="item in mainNavItems" :key="item.id" :index="item.id">
        <span class="nav-icon">{{ item.icon }}</span>
        <span>{{ item.name }}</span>
      </el-menu-item>

      <!-- 分类标签折叠菜单 -->
      <el-sub-menu index="category">
        <template #title>
          <span class="nav-icon">🏷️</span>
          <span>分类标签</span>
        </template>
        <el-menu-item v-for="item in categoryItems" :key="item.id" :index="item.id">
          <span class="nav-icon">{{ item.icon }}</span>
          <span>{{ item.name }}</span>
        </el-menu-item>
      </el-sub-menu>
      
      <!-- 设置选项 -->
      <el-menu-item index="settings" class="settings-nav">
        <span class="nav-icon">⚙️</span>
        <span>设置</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: 'Sidebar',
  props: {
    activePage: {
      type: String,
      required: true
    },
    // 添加bookId作为prop
    bookId: {
      type: [String, Number],
      default: null
    }
  },
  data() {
    return {
      mainNavItems: [
        { id: 'home', name: '首页', icon: '🏠' },
        { id: 'transactions', name: '流水', icon: '📝' },
        { id: 'reports', name: '报表', icon: '📊' }
      ],
      categoryItems: [
        { id: 'categories', name: '收支分类', icon: '📋' },
        { id: 'accounts', name: '账户管理', icon: '💳' },
        { id: 'members', name: '成员管理', icon: '👥' },
        { id: 'merchants', name: '商家管理', icon: '🏬' }
      ]
    };
  },
  methods: {
    changePage(pageId) {
      // 确保在控制台输出当前点击的页面ID，便于调试
      console.log('点击了页面:', pageId);
      
      // 检查是否点击了商家管理
      if (pageId === 'merchants') {
        console.log('点击了商家管理');
        // 优先使用props中的bookId，如果没有再尝试从localStorage获取
        const bookId = this.bookId || localStorage.getItem('currentBookId');
        
        // 增加日志记录，便于调试
        console.log('获取到的bookId:', bookId);
        console.log('props中的bookId:', this.bookId);
        console.log('localStorage中的currentBookId:', localStorage.getItem('currentBookId'));
        
        if (!bookId) {
          console.error('未找到当前账本ID');
          // 可以添加用户提示
          this.$emit('show-error', '未找到当前账本ID，请返回首页重新选择账本');
          return;
        }
        
        // 确保将bookId存入localStorage
        localStorage.setItem('currentBookId', bookId);
        // 直接发出带有bookId的事件
        this.$emit('change-page', pageId, bookId);
        return;
      }
      
      this.$emit('change-page', pageId);
    },
    openRecordDrawer() {
      this.$emit('open-record-drawer');
    }
  }
};
</script>

<style scoped>
.sidebar {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.add-record {
  padding: 16px;
}

.add-record-btn {
  width: 100%;
}

.el-menu-vertical {
  border-right: none;
  flex-grow: 1;
}

.nav-icon {
  margin-right: 10px;
  font-size: 18px;
}

.settings-nav {
  margin-top: auto;
}

/* 确保设置菜单项在底部 */
.el-menu {
  display: flex;
  flex-direction: column;
}

.el-menu-item.settings-nav {
  margin-top: auto;
}
</style>