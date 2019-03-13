package com.xuyun.platform.dsfcenterdata.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.UUID;
/**
 * @ClassName: GenerationSequenceUtil
 * @Author: R.M.I
 * @CreateTime: 2019年02月26日 11:14:00
 * @Description: TODO
 * @Version 1.0.0
 */
public class GenerationSequenceUtil {
    private static final Integer RANDOM_NUM_DEFAULT_SIZE=8;//默认的随机数大小
    private static DateFormat sdf_yyMMdd=new SimpleDateFormat("yyMMdd");

    /**
     * 生成订单号形式的号码
     * 生成规则：
     * 时间戳+4位
     * @Title: generateOrderNo
     * @return String
     * @throws
     */
    public static Long genOrderNo(){
        StringBuilder orderNoBuilder=new StringBuilder();
        //获取当前时间戳
        orderNoBuilder.append(DateUtil.currentTimestamp());
        orderNoBuilder.append("0000");
        return Long.valueOf(orderNoBuilder.toString());
    }

    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
            "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z" };

    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

    /**
     * 生成uuid
     * @Title: generateUUID
     * @Description:
     *  生成UUID，可以作为batchNo来使用
     * @return String    返回类型
     * @throws
     */
    public static String generateUUID(){
        String uuid = UUID.randomUUID().toString(); //获取UUID并转化为String对象
        uuid = uuid.replace("-", "");               //因为UUID本身为32位,去掉"-"
        return uuid;
    }

    /**
     * 随机生成一个N位的随机数
     * @Title: generateRandomNum
     * @Description:
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
    public static String generateRandomNum(int n){
        if(n<=0){
            n=RANDOM_NUM_DEFAULT_SIZE;
        }
        StringBuilder str=new StringBuilder();//定义变长字符串
        Random random=new Random();
        for(int i=0;i<n;i++){//随机生成数字，并添加到字符串
            str.append(random.nextInt(10));
        }
        return str.toString();
    }



}
