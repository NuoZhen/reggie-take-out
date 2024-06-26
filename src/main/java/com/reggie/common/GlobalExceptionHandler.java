package com.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 异常处理方法
     *
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException) {
        log.info(sqlIntegrityConstraintViolationException.getMessage());

        if (sqlIntegrityConstraintViolationException.getMessage().contains("Duplicate entry")) {
            String[] split = sqlIntegrityConstraintViolationException.getMessage().split(" ");
            String msg = split[2] + "已存在！";
            return R.error(msg);
        }

        return R.error("未知错误！");
    }

    /**
     * 异常处理方法
     *
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException customException) {
        log.info(customException.getMessage());

        return R.error(customException.getMessage());
    }
}
