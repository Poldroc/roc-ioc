package com.github.poldroc.ioc.exception.respcode;

import com.github.houbb.heaven.response.respcode.AdviceRespCode;
import com.github.houbb.heaven.response.respcode.RespCode;

/**
 * IoC 响应编码
 * @author Poldroc
 * @date 2024/8/3
 */

public enum IocRespCode implements AdviceRespCode {;

    /**
     * 编码
     */
    private final String code;

    /**
     * 消息
     */
    private final String msg;

    /**
     * 建议
     */
    private final String advice;

    IocRespCode(String code, String msg, String advice) {
        this.code = code;
        this.msg = msg;
        this.advice = advice;
    }


    IocRespCode(final RespCode respCode, String advice) {
        this.code = respCode.getCode();
        this.msg = respCode.getMsg();
        this.advice = advice;
    }

    @Override
    public String getAdvice() {
        return this.advice;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    @Override
    public String toString() {
        return "IocRespCode{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", advice='" + advice + '\'' +
                '}';
    }

}
