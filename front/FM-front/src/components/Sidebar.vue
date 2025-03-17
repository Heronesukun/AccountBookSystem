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
        { id: 'merchants', name: '商家管理', icon: '🏬' },
        { id: 'projects', name: '项目管理', icon: '📁' }
      ]
    };
  },
  methods: {
    changePage(pageId) {
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