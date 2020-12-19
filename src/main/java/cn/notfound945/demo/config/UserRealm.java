package cn.notfound945.demo.config;

import cn.notfound945.demo.pojo.User;
import cn.notfound945.demo.service.serviceImpl.UserServiceImpl;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserServiceImpl userService;

    /**
     * 授权
     * @param principalCollection 用户信息
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("Execute doGetAuthorizationInfo（执行授权）.");
        // 取出用户信息进行授权
        User loginUser = (User) principalCollection.getPrimaryPrincipal();

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 仅以 RealName 字段演示
        simpleAuthorizationInfo.addStringPermission(loginUser.getRealName());
//        simpleAuthorizationInfo.setStringPermissions(permissions);
//        simpleAuthorizationInfo.setRoles(roles);
/*        检查权限、角色
        Subject subject = SecurityUtils.getSubject();
        subject.checkPermission("user:view");
        subject.checkRoles("admin");*/

        return simpleAuthorizationInfo;
    }

    /**
     * 用户认证
     * @param authenticationToken 令牌
     * @return AuthenticationInfo 认证信息
     * @throws AuthenticationException 认证异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("Execute doGetAuthenticationInfo（执行认证）.");
        String userName = "";
        String password = "";
        User loginUser = null;
        // 1.从主体传过来的认证信息中，获得用户名
        try {
            userName = (String) authenticationToken.getPrincipal();
            // 2.通过用户名对比数据库信息
            loginUser = userService.getPasswordByUserName(userName);
            password = loginUser.getPassword();
        } catch (Exception e) {
            System.out.println("用户名 " + userName + " 不存在.");
            return null;
        }
        return new SimpleAuthenticationInfo(loginUser, password, "userRealm");
    }
}
