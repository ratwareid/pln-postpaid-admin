package com.ratwareid.webapp.controller;

import com.ratwareid.webapp.model.Pelanggan;
import com.ratwareid.webapp.model.Sortby;
import com.ratwareid.webapp.model.Tarif;
import com.ratwareid.webapp.model.User;
import com.ratwareid.webapp.repository.PelangganRepository;
import com.ratwareid.webapp.repository.TarifRepository;
import com.ratwareid.webapp.repository.UserRepository;
import com.ratwareid.webapp.security.Constant;
import com.ratwareid.webapp.security.PassGen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/***********************************************************************
 * Module:  com.ratwareid.webapp.controller.pelangganController
 * Author:  Ratwareid
 * Created: 26/11/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/

@Controller
@RequestMapping("/master/pelanggan")
public class PelangganController extends BaseController{

    @Autowired
    PelangganRepository pelangganRepository;

    @Autowired
    TarifRepository tarifRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public ModelAndView init(@RequestParam(value = "keyword",required = false) Optional<String> keyword, @RequestParam(value = "page",required = false) Optional<Integer> page,
                             @RequestParam(value = "size",required = false) Optional<Integer> size,
                             @RequestParam(value = "sort_by",required = false) Optional<String> sort_by,
                             @RequestParam(value = "sort_method",required = false) String sort_method) throws Exception {

        String key = keyword.isEmpty() ? "%%" : "%" + keyword.get()+"%";
        Integer pageint = page.map(integer -> (integer - 1)).orElse(0);
        Pageable paging;
        if (sort_method != null && sort_method.equalsIgnoreCase(Constant.SORTMETHOD.DESCENDING)){
            paging = PageRequest.of(pageint, size.orElse(10),Sort.by(sort_by.orElse("id_pelanggan")).descending());
        }else{
            paging = PageRequest.of(pageint, size.orElse(10),Sort.by(sort_by.orElse("id_pelanggan")).ascending());
        }
        Page<Pelanggan> list = pelangganRepository.findAllByKeyword(key,paging);
        mvc = new ModelAndView("pelanggan");
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
        Pelanggan pelanggan = new Pelanggan();
        mvc.addObject("pelanggan",pelanggan);
        mvc.addObject("mode","edit");
        mvc.addObject("tarifs",this.getTarifs());
        return mvc;
    }

    @RequestMapping(value = "/save", method =RequestMethod.POST)
    public String save(@ModelAttribute Pelanggan pelanggan) throws Exception {

        //Cek pelanggan dengan username yang sama
        User user = userRepository.getUserByUsername(pelanggan.getUsername());
        if (user != null){
            throw new Exception("Username "+user.getUsername()+" telah digunakan di data user !");
        }

        if (pelanggan.getNewPassword()!=null){
            pelanggan.setPassword(PassGen.generatePassword(pelanggan.getNewPassword()));
        }
        pelangganRepository.save(pelanggan);
        return "redirect:/master/pelanggan/";
    }

    @RequestMapping("/edit")
    public ModelAndView edit(@RequestParam(name="id") Long id) throws Exception{
        Optional<Pelanggan> pelanggan = pelangganRepository.findById(id);
        if (pelanggan.isEmpty()) throw new Exception("pelanggan dengan id "+id+" tidak ditemukan !");
        mvc.addObject("pelanggan",pelanggan.get());
        mvc.addObject("mode","edit");
        mvc.addObject("tarifs",this.getTarifs());
        return mvc;
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam (name="id") Long id) {
        pelangganRepository.deleteById(id);
        return "redirect:/master/pelanggan/";
    }

    public List<Tarif> getTarifs() throws Exception{
        return tarifRepository.findAll();
    }

    public ArrayList<Sortby> getSortby() {
        if (sortby == null){
            sortby = new ArrayList<>();
            sortby.add(new Sortby("id_pelanggan","ID"));
            sortby.add(new Sortby("username","Username"));
            sortby.add(new Sortby("nomor_kwh","Nomor KWH"));
            sortby.add(new Sortby("nama_pelanggan","Nama Pelanggan"));
            sortby.add(new Sortby("alamat","Alamat"));
            sortby.add(new Sortby("b.daya","Tarif"));
        }
        return sortby;
    }
}
