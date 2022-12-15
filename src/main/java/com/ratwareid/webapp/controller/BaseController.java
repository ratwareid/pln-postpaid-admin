package com.ratwareid.webapp.controller;

import com.ratwareid.webapp.model.Sortby;
import com.ratwareid.webapp.security.MyPelangganDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/***********************************************************************
 * Module:  com.ratwareid.webapp.controller.BaseController
 * Author:  Ratwareid
 * Created: 26/11/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/

@Component
@ControllerAdvice
public class BaseController {

    @Autowired
    ServletContext servletContext;
    public ModelAndView mvc;
    protected ArrayList<Sortby> sortby;

    public void buildPaging(ModelAndView model, List<Object> list, BigDecimal totalDataAll, int currentPage, int pageSize){
        model.addObject("currentPage",currentPage);
        model.addObject("lists",list);
        int totalPages = totalDataAll.divide(new BigDecimal(pageSize), RoundingMode.UP).intValue();
        if (totalPages > 0) {
            List<Integer> pageNum = new ArrayList<>();
            for (int x=currentPage;x<=totalPages;x++){
                pageNum.add(x);
                if (pageNum.size() >= 10){
                    break;
                }
            }
            model.addObject("pageNumbers", pageNum);
        }
    }

    public void showSuccess(String message){
        servletContext.setAttribute("message_type","success");
        servletContext.setAttribute("message_message",message);
    }


    public void showError(String message){
        servletContext.setAttribute("message_type","error");
        servletContext.setAttribute("message_message",message);
    }

    public void showInfo(String message){
        servletContext.setAttribute("message_type","info");
        servletContext.setAttribute("message_message",message);
    }

    public void showWarning(String message){
        servletContext.setAttribute("message_type","warning");
        servletContext.setAttribute("message_message",message);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView exception(@ModelAttribute Model model, final Throwable throwable) {
        throwable.printStackTrace();
        this.showError(throwable.getMessage());
        return mvc;
    }

    public boolean hasAuthority(String authority){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase(authority))) {
            return true;
        }
        return false;
    }

    public String getCurrentUsername(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

}
