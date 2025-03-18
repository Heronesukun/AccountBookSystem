import { createRouter, createWebHistory } from 'vue-router'
import loginRequest from '@/api/login.js';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path:"/",
      component:()=>import("@/views/login.vue")
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/Register.vue')
    },
    {
      path:"/home",
      component:()=>import("@/views/home.vue"),
      children:[

      ]
    },
    {
      path:"/LedgerDetail",
      component:()=>import("@/views/LedgerDetail.vue")
    },
    {
      path:"/notebook/:username/:id",  // 新增路由，支持从home.vue跳转
      component:()=>import("@/views/LedgerDetail.vue")
    },
    {
      path:"/account/:bookId",  // 添加账户管理页面路由
      name: "account",
      component:()=>import("@/views/AccountManagement.vue"),
      props: true
    },
    {
      path:"/members/:bookId",  // 添加成员管理页面路由
      name: "members",
      component:()=>import("@/views/MemberManagement.vue"),
      props: true
    },
    // 添加商家管理路由
    {
      path: '/merchant/:bookId',
      name: 'MerchantManagement',
      component: () => import('@/views/MerchantManagement.vue'),
      meta: { requiresAuth: true }
    },
  ]
})

// 添加路由守卫，进行登录判断
router.beforeEach(async  (to, from) => {
  console.log("to:" + to.path);
  console.log("from:" + from.path);
  if(to.path != '/'){
    // 不是访问登录页面,访问其他页面，这时就需要判断是否登录。
    // 判断策略就是将token发送到服务器端进行判断。  但是token在哪？
    // 获取token
    let token = localStorage.getItem("token")
    // 发送请求进行校验
    let result = await  loginRequest.checkLogin(token);
    console.log(result)
    if(result.code == '200'){
      return true;
    }else if(result.code == '507'){// 续签成功
      // 续签之后，我们要将最新的token存储在localStorage中，替换原来的token
      localStorage.setItem("token",result.msg);
      return true;
    }else{
      return "/";
    }
  }else {
    // 返回 false 以取消导航
    return true;
  }
})

export default router