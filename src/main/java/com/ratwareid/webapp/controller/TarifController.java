package com.ratwareid.webapp.controller;

import com.ratwareid.webapp.model.Sortby;
import com.ratwareid.webapp.model.Tarif;
import com.ratwareid.webapp.repository.TarifRepository;
import com.ratwareid.webapp.security.Constant;
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
 * Module:  com.ratwareid.webapp.controller.tarifController
 * Author:  Ratwareid
 * Created: 26/11/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/

@Controller
@RequestMapping("/master/tarif")
public class TarifController extends BaseController{

    @Autowired
    TarifRepository tarifRepository;

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
            paging = PageRequest.of(pageint, size.orElse(10),Sort.by(sort_by.orElse("id_tarif")).descending());
        }else{
            paging = PageRequest.of(pageint, size.orElse(10),Sort.by(sort_by.orElse("id_tarif")).ascending());
        }
        Page<Tarif> list = tarifRepository.findAllByKeyword(key,paging);
        mvc = new ModelAndView("tarif");
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
        Tarif tarif = new Tarif();
        mvc.addObject("tarif",tarif);
        mvc.addObject("supporteddaya", Constant.getListDaya());
        mvc.addObject("mode","edit");
        return mvc;
    }

    @RequestMapping(value = "/save", method =RequestMethod.POST)
    public String save(@ModelAttribute Tarif tarif) {
        tarifRepository.save(tarif);
        return "redirect:/master/tarif/";
    }

    @RequestMapping("/edit")
    public ModelAndView edit(@RequestParam(name="id") Long id) throws Exception{
        Optional<Tarif> tarif = tarifRepository.findById(id);
        if (tarif.isEmpty()) throw new Exception("tarif dengan id "+id+" tidak ditemukan !");
        mvc.addObject("tarif",tarif.get());
        mvc.addObject("supporteddaya", Constant.getListDaya());
        mvc.addObject("mode","edit");
        return mvc;
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam (name="id") Long id) {
        tarifRepository.deleteById(id);
        return "redirect:/master/tarif/";
    }

    public ArrayList<Sortby> getSortby() {
        if (sortby == null){
            sortby = new ArrayList<>();
            sortby.add(new Sortby("id_tarif","ID"));
            sortby.add(new Sortby("daya","Daya"));
            sortby.add(new Sortby("tarifperkwh","Tarif Per KWH"));
        }
        return sortby;
    }
}
