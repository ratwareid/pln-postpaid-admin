package com.ratwareid.webapp.controller;

import com.ratwareid.webapp.model.Level;
import com.ratwareid.webapp.model.Pelanggan;
import com.ratwareid.webapp.model.Sortby;
import com.ratwareid.webapp.model.User;
import com.ratwareid.webapp.repository.LevelRepository;
import com.ratwareid.webapp.repository.PelangganRepository;
import com.ratwareid.webapp.repository.UserRepository;
import com.ratwareid.webapp.security.Constant;
import com.ratwareid.webapp.security.PassGen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/***********************************************************************
 * Module:  com.ratwareid.webapp.controller.UserController
 * Author:  Ratwareid
 * Created: 26/11/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/

@Controller
@RequestMapping("/master/users")
public class UserController extends BaseController{

    @Autowired
    UserRepository userRepository;

    @Autowired
    LevelRepository levelRepository;

    @Autowired
    PelangganRepository pelangganRepository;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public ModelAndView init(@RequestParam(value = "keyword",required = false) Optional<String> keyword,
                             @RequestParam(value = "page",required = false) Optional<Integer> page,
                             @RequestParam(value = "size",required = false) Optional<Integer> size,
                             @RequestParam(value = "sort_by",required = false) Optional<String> sort_by,
                             @RequestParam(value = "sort_method",required = false) String sort_method) throws Exception {

        String key = keyword.isEmpty() ? "%%" : "%" + keyword.get()+"%";
        Integer pageint = page.map(integer -> (integer - 1)).orElse(0);
        Pageable paging;
        if (sort_method != null && sort_method.equalsIgnoreCase(Constant.SORTMETHOD.DESCENDING)){
            paging = PageRequest.of(pageint, size.orElse(10),Sort.by(sort_by.orElse("id")).descending());
        }else{
            paging = PageRequest.of(pageint, size.orElse(10),Sort.by(sort_by.orElse("id")).ascending());
        }
        Page<User> list = userRepository.findAllByUsernameIsLikeIgnoreCase(key,paging);
        mvc = new ModelAndView("user");
        mvc.addObject("mode","list");
        mvc.addObject("list",list);
        mvc.addObject("sortby",getSortby());
        mvc.addObject("sort_by",sort_by.orElse("id_pelanggan"));
        mvc.addObject("sort_method",sort_method);
        mvc.addObject("keyword",keyword.orElse(null));

        int totalPages = list.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            mvc.addObject("pageNumbers", pageNumbers);
        }
        return mvc;
    }

    @RequestMapping(value="/add",method = RequestMethod.GET)
    public ModelAndView add() throws Exception{
        User user = new User();
        mvc.addObject("user",user);
        mvc.addObject("mode","edit");
        mvc.addObject("levels",this.getLevels());
        return mvc;
    }

    @RequestMapping(value = "/save", method =RequestMethod.POST)
    public String save(@ModelAttribute User user) throws Exception {

        //Cek pelanggan dengan username yang sama
        Pelanggan pelanggan = pelangganRepository.getUserByUsername(user.getUsername());
        if (pelanggan != null){
            throw new Exception("Username "+user.getUsername()+" telah digunakan di data pelanggan !");
        }

        if (user.getNewPassword()!=null){
            user.setPassword(PassGen.generatePassword(user.getNewPassword()));
        }
        userRepository.save(user);
        return "redirect:/master/users/";
    }

    @RequestMapping("/edit")
    public ModelAndView edit(@RequestParam(name="id") Long id) throws Exception{
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new Exception("User dengan id "+id+" tidak ditemukan !");
        mvc.addObject("user",user.get());
        mvc.addObject("mode","edit");
        mvc.addObject("levels",this.getLevels());
        return mvc;
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam (name="id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/master/users/";
    }

    public List<Level> getLevels(){
        return levelRepository.findAll();
    }

    public ArrayList<Sortby> getSortby() {
        if (sortby == null){
            sortby = new ArrayList<>();
            sortby.add(new Sortby("id","ID"));
            sortby.add(new Sortby("username","Username"));
            sortby.add(new Sortby("nama_admin","Nama Admin"));
            sortby.add(new Sortby("level.name","Level"));
        }
        return sortby;
    }
}
