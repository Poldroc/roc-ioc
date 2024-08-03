package com.github.poldroc.ioc.exception;

import com.github.houbb.heaven.response.respcode.AdviceRespCode;
/**
 * IoC 包含建议的异常信息
 * @author Poldroc
 * @date 2024/8/3
 */

public class IocAdviceRuntimeException extends RuntimeException {

    /**
     * 包含建议的响应编码
     */
    private final AdviceRespCode adviceRespCode;

    public IocAdviceRuntimeException(AdviceRespCode adviceRespCode) {
        this.adviceRespCode = adviceRespCode;
    }

    public IocAdviceRuntimeException(String message, AdviceRespCode adviceRespCode) {
        super(message);
        this.adviceRespCode = adviceRespCode;
    }

    public IocAdviceRuntimeException(String message, Throwable cause, AdviceRespCode adviceRespCode) {
        super(message, cause);
        this.adviceRespCode = adviceRespCode;
    }

    public IocAdviceRuntimeException(Throwable cause, AdviceRespCode adviceRespCode) {
        super(cause);
        this.adviceRespCode = adviceRespCode;
    }

    public IocAdviceRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, AdviceRespCode adviceRespCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.adviceRespCode = adviceRespCode;
    }

}
