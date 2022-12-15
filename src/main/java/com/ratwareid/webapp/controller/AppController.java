package com.ratwareid.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppController extends BaseController {

	@RequestMapping("/")
	public String viewHomePage(Model model) {
		if (hasAuthority("USER")){
			return "redirect:/transaksi/penggunaan/";
		}else {
			return "redirect:/master/users/";
		}
	}
}
