package sjtusummerproject.codemicroservice.Service.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sjtusummerproject.codemicroservice.Service.GenerateCodeService;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

@Service
public class GenerateCodeServiceImpl implements GenerateCodeService {
    Random random = new Random();
    public HashMap<String,Object> GetCode() {
        // 在内存中创建图象
        BufferedImage image = new BufferedImage(imgWidth, imgHeight,
                BufferedImage.TYPE_INT_RGB);

        // 获取图形上下文
        Graphics2D g = image.createGraphics();

        // 生成随机类
        //Random random = new Random();

        // 设定背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, imgWidth, imgHeight);

        // 设定字体
        g.setFont(new Font(fontStyle, Font.PLAIN + Font.ITALIC, fontHeight));

        // 画边框
        g.setColor(new Color(55, 55, 12));
        g.drawRect(0, 0, imgWidth - 1, imgHeight - 1);

        // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 160; i++) {
            int x = random.nextInt(imgWidth);
            int y = random.nextInt(imgHeight);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        // 取随机产生的认证码(4位数字)
        String sRand = "";
        int red = 0, green = 0, blue = 0;
        for (int i = 0; i < codeCount; i++) {
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            int wordType = random.nextInt(3);
            char retWord = 0;
            switch (wordType) {
                case 0:
                    retWord = this.getSingleNumberChar();
                    break;
                case 1:
                    retWord = this.getLowerOrUpperChar(0);
                    break;
                case 2:
                    retWord = this.getLowerOrUpperChar(1);
                    break;
            }
            sRand += String.valueOf(retWord);
            g.setColor(new Color(red, green, blue));
            g.drawString(String.valueOf(retWord), (i) * x + 5, codeY + 7);//这个地方非常重要，关系到验证码是否居中显示。要根据大小进行调整。
        }
        // 将认证码存入SESSION
        sRand = sRand.toLowerCase();

        g.dispose();

        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("image",image);
        result.put("code-ans",sRand);

        System.out.println("in generate code the image "+image);
        System.out.println("in generate code the ans "+sRand);

        return result;
    }

    private int imgWidth = 0;//验证码图片的宽度
    private int imgHeight = 0;//验证码图片的高度
    private int codeCount = 0;//验证码的个数
    private int x = 0;
    private int fontHeight;
    private int codeY;
    private String fontStyle;
    private static final long serialVersionUID = 128554012633034503L;

    GenerateCodeServiceImpl(){
        init();
    }
    /**
     * 初始化配置参数
     */
    public void init() {
        // 宽度
        String strWidth = "120";
        // 高度
        String strHeight = "40";
        // 字符个数
        String strCodeCount = "4";

        fontStyle = "Times New Roman";

        // 将配置的信息转换成数值
        try {
            if (strWidth != null && strWidth.length() != 0) {
                imgWidth = Integer.parseInt(strWidth);
            }
            if (strHeight != null && strHeight.length() != 0) {
                imgHeight = Integer.parseInt(strHeight);
            }
            if (strCodeCount != null && strCodeCount.length() != 0) {
                codeCount = Integer.parseInt(strCodeCount);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        x = imgWidth / (codeCount + 1);
        fontHeight = imgHeight - 2;
        codeY = imgHeight - 12;
    }

    Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    char getSingleNumberChar() {
        int numberResult = random.nextInt(10);
        int ret = numberResult + 48;
        return (char) ret;
    }

    char getLowerOrUpperChar(int upper) {
        int numberResult = random.nextInt(26);
        int ret = 0;
        if (upper == 0) {// 小写
            ret = numberResult + 97;
        } else if (upper == 1) {// 大写
            ret = numberResult + 65;
        }
        return (char) ret;
    }
    public void setRandom(Random random){
        this.random = random;
    }
    public Random getRandom(){
        return this.random;
    }
}