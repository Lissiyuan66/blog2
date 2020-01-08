package com.lsy.util;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
    /**
     * 将获取到的前端参数转为string类型
     * @param request
     * @param key
     * @return
     */
    public static String getString(HttpServletRequest request, String key) {
        try {
            String result = request.getParameter(key);
            if(result != null) {
                result = result.trim();
            }
            if("".equals(result)) {
                result = null;
            }
            return result;
        }catch(Exception e) {
            return null;
        }
    }
    /**
     * 验证码校验
     * @param request
     * @return
     */
    public static boolean checkVerifyCode(HttpServletRequest request) {
        //获取生成的验证码
        String verifyCodeExpected = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        //生成小写
        String lowve = verifyCodeExpected.toLowerCase();
        //获取用户输入的验证码
        String verifyCodeActual = CodeUtil.getString(request, "verifyCodeActual");
        //System.out.println(verifyCodeActual);
        if (verifyCodeActual == null) {
            return false;
        }
        if (verifyCodeActual.equals(verifyCodeExpected) || verifyCodeActual.equals(lowve)) {
            return true;
        }
        return false;

    }
    public static boolean checkVerifyCode(String str,HttpServletRequest request) {
        //获取生成的验证码
        String verifyCodeExpected = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        //生成小写
        String lowve = verifyCodeExpected.toLowerCase();
        //获取用户输入的验证码
        //System.out.println("生成"+lowve);
        //System.out.println("输入"+str);
        if (str == null) {
            return false;
        }
        if (str.equals(verifyCodeExpected) || str.equals(lowve)) {
            return true;
        }
        return false;

    }
}