//package com.zit.stock.security.handler;
//
//
//import com.zit.stock.vo.resp.R;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.sql.SQLIntegrityConstraintViolationException;
//
///**
// * 全局异常处理器，处理项目中抛出的业务异常
// */
//@RestControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler {
//
//    /**
//     * 捕获业务异常
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler
//    public R exceptionHandler(Exception ex){
//        log.error("异常信息：{}", ex.getMessage());
//        return R.error(ex.getMessage());
//    }
//
//    @ExceptionHandler
//    public R<Object> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e){
//        //1.使用异常对象获取异常的信息
//        String message = e.getMessage();
//        //2.截取重复的信息
//        if (message.contains("Duplicate entry")) {
//            String val = message.split(" ")[2];
//            return R.error((val + "已存在"));
//        }
//
//        return R.error("未知错误");
//    }
//}
