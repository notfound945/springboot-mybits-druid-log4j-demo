package cn.notfound945.demo.pojo;

import io.swagger.annotations.ApiModel;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 *
 * 生成随机图片验证码
 */
@ApiModel("图片验证码生成类")
public class ValidateCode {

    private static Random random = new Random();
    private int width = 160;// 宽
    private int height = 40;// 高
    private int lineSize = 30;// 干扰线数量
    private int stringNum = 4;//随机产生字符的个数

    private String randomString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final String sessionKey = "sessionKey";


    /*
     *  获取字体
     */
    private Font getFont() {
        return new Font("Times New Roman", Font.ROMAN_BASELINE, 40);
    }

    /*
     *  获取颜色
     */
    private static Color getRandomColor(int fc, int bc) {
        fc = Math.min(fc, 255);
        bc = Math.min(bc, 255);
        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 12);
        return new Color(r, g, b);
    }

    /*
     *  绘制干扰线
     */
    private void drawLine(Graphics g) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(20);
        int yl = random.nextInt(10);
        g.drawLine(x, y, x + xl, y + yl);
    }

    /*
     *  获取随机字符
     */
    private String getRandomString(int num) {
        num = num > 0 ? num : randomString.length();
        return String.valueOf(randomString.charAt(random.nextInt(num)));
    }

    /*
     *  绘制字符串
     */
    private String drawString(Graphics g, String randomStr, int i) {
        g.setFont(getFont());
        g.setColor(getRandomColor(108, 190));
//        System.out.println(random.nextInt(randomString.length()));
        String rand = getRandomString(random.nextInt(randomString.length()));
        randomStr += rand;
        g.translate(random.nextInt(3), random.nextInt(6));
        g.drawString(rand, 40 * i + 10, 25);
        return randomStr;
    }

    /*
     *  生成随机图片
     */
    public void getRandomCodeImage(HttpServletRequest request, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.fillRect(0, 0, width, height);
        g.setColor(getRandomColor(105, 189));
        g.setFont(getFont());

        // 绘制干扰线
        for (int i = 0; i < lineSize; i++) {
            drawLine(g);
        }

        // 绘制随机字符
        String randomString = "";
        for (int i = 0; i < stringNum; i++) {
            randomString = drawString(g, randomString, i);
        }
//        保存 Session Key 到 Shiro session 会话中
        System.out.println("Shiro Session Key: " + randomString);
        g.dispose();

        session.removeAttribute(sessionKey);
        session.setAttribute(sessionKey, randomString);
        try {
            //  直接返回图片
            ImageIO.write(image, "PNG", response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     *  生成随机图片，返回 base64 字符串
    /*
    public String getRandomCodeBase64(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics graphics = image.getGraphics();
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(getRandomColor(105, 189));
        graphics.setFont(getFont());

        // 绘制干扰线
        for (int i = 0; i < lineSize; i++) {
            drawLine(g);
        }

        // 绘制随机字符
        String randomString = "";
        for (int i = 0; i < stringNum; i++) {
            randomString = drawString(graphics, randomString, i);
        }

        System.out.println(randomString);

        graphics.dispose();
        session.removeAttribute(sessionKey);
        session.setAttribute(sessionKey, randomString);
        String base64String = "";
        try {
            //  直接返回图片
            //  ImageIO.write(image, "PNG", response.getOutputStream());
            //返回 base64
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", bos);

            byte[] bytes = bos.toByteArray();
            Base64.Encoder encoder = Base64.getEncoder();
            base64String = encoder.encodeToString(bytes);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return base64String;
    }*/

}

