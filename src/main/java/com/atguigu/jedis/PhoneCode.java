package com.atguigu.jedis;

import redis.clients.jedis.Jedis;

import java.util.Random;

public class PhoneCode {
    public static void main(String[] args) {
        VerifyCodeGenerate verifyCodeGenerate = new VerifyCodeGenerate("15171284192");
        //模拟验证码发送
        verifyCodeGenerate.verifyCode();
        //校验
        //verifyCodeGenerate.getRedisCode("688653");
    }
}
class VerifyCodeGenerate{
    private String codeKey;//验证码key
    private String countKey;//验证码次数
    public VerifyCodeGenerate(String phone){
        //设置验证码key
        this.codeKey = "VerifyCode:" + phone + ":code";
        //设置验证码次数
        this.countKey = "VerifyCode:" + phone + ":count";
    }

    //1.生成随机6位验证码
    public static String getCode() {
        Random random = new Random();
        String code = "";
        for (int i = 0; i < 6; i++) {
            code += random.nextInt(10);
        }
        return code;
    }

    //2.每个手机每天只能发送三次，验证码放到redis中，设置过期时间
    public void verifyCode() {
        Jedis jedis = new Jedis("192.168.2.130", 6379);
        String count = jedis.get(countKey);

        if (count == null) {
            //如果该手机没用发送过验证码，就发送一次，并设置过期时间为一天（24*60*60）
            jedis.setex(countKey, 24 * 60 * 60, "1");
            jedis.setex(codeKey, 120, getCode());
            System.out.println("验证码已发送");
        } else if (Integer.parseInt(count) <= 2) {
            jedis.incr(countKey);//次数加1
            jedis.setex(codeKey, 120, getCode());
            System.out.println("验证码已发送");
        } else if (Integer.parseInt(count) > 2) {
            System.out.println("今日验证次数已用完！");
            jedis.close();
        }
    }

    //3.验证码校验
    public void getRedisCode(String code) {
        //从redis获取验证码
        Jedis jedis = new Jedis("192.168.2.130", 6379);
        String redisCode = jedis.get(codeKey);
        //判断验证码是否相同
        if (redisCode.equals(code)) {
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }
        jedis.close();
    }

}
