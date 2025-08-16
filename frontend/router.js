// 定义路由
const routes = [
    { path: '/', component: LoginPage },
    { path: '/dashboard', component: DashboardPage }
];

// 创建路由实例
const router = new VueRouter({
    routes
});

// 添加路由守卫，检查登录状态
router.beforeEach((to, from, next) => {
    // 检查是否登录（这里简化处理，实际应从localStorage或Cookie读取登录状态）
    const isLoggedIn = localStorage.getItem('userInfo') !== null;
    
    // 如果访问的是dashboard页面且未登录，则重定向到登录页面
    if (to.path === '/dashboard' && !isLoggedIn) {
        next('/');
    } 
    // 如果已登录且访问的是登录页面，则重定向到dashboard
    else if (to.path === '/' && isLoggedIn) {
        next('/dashboard');
    } else {
        next();
    }
});

// 创建 Vue 实例
new Vue({
    el: '#app',
    router
});