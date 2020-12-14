package cn.notfound945.demo.config;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    // ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(defaultWebSecurityManager);

        /*
         *
         * 添加拦截器、授权器
         * anon 无需认证就可使用
         * authc 必须认证才能使用
         * user 必须拥有 记住我 功能才能用
         * perms 拥有对某个资源的权限才能访问
         * role 拥有某个角色权限才能访问
         */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        // 添加权限
        filterChainDefinitionMap.put("/user/all", "perms[user:view]");
        filterChainDefinitionMap.put("/user/deleteuserbyid/*", "perms[user:delete]");
        // 添加拦截
        filterChainDefinitionMap.put("/log", "authc");
        filterChainDefinitionMap.put("/user/*", "authc");
        filterChainDefinitionMap.put("/login", "anon");


        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        bean.setUnauthorizedUrl("/noAuth");
        bean.setLoginUrl("/noLogin");

        Map<String, Filter> filters = bean.getFilters();
        // 自定授权拦截器 不自动跳转登录页
        filters.put("authc", new ShiroLoginFilter());
        return bean;
    }

    // DefaultWebSecurityManager
    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // Relation UserRealm
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }
}
