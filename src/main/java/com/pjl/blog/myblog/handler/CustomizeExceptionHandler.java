package com.pjl.blog.myblog.handler;

import com.pjl.blog.myblog.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handleControllerException(Throwable e, Model model, HttpServletRequest request) {
        if (e instanceof CustomizeException){
            model.addAttribute("errorMessage",e.getMessage());
        }else{
            model.addAttribute("errorMessage","肯定是服务器的锅，要不等下试试？" + e.getMessage() + request.getRequestURL());
        }
        return new ModelAndView("error");
    }

}
