package cn.notfound945.demo;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.jupiter.api.Test;

public class encryptHashTests {
    @Test
    public void encryptHash() {
        String password = "rootroot";
        String salt = "notfound945";
        String encodePassword = new SimpleHash("md5", password, salt, 5).toString();
        System.out.printf(" 原始密码是 %s \n 盐是： %s\n 运算出来的密文是：%s ", password, salt, encodePassword);
    }
}
