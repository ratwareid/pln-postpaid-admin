package com.ratwareid.webapp.controller;

import com.ratwareid.webapp.model.Penggunaan;
import com.ratwareid.webapp.model.Sortby;
import com.ratwareid.webapp.model.Tagihan;
import com.ratwareid.webapp.repository.TagihanRepository;
import com.ratwareid.webapp.security.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
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
 * Module:  com.ratwareid.webapp.controller.levelController
 * Author:  Ratwareid
 * Created: 26/11/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/

@Controller
@RequestMapping("/transaksi/tagihan")
public class TagihanController extends BaseController{

    @Autowired
    TagihanRepository tagihanRepository;

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
            paging = PageRequest.of(pageint, size.orElse(10),Sort.by(sort_by.orElse("id_tagihan")).descending());
        }else{
            paging = PageRequest.of(pageint, size.orElse(10),Sort.by(sort_by.orElse("id_tagihan")).ascending());
        }

        Page<Tagihan> list;
        if (hasAuthority("USER")){
            list = tagihanRepository.findAllPrivateByKeyword(key,getCurrentUsername(), paging);
        }else {
            list = tagihanRepository.findAllByKeyword(key, paging);
        }
        mvc = new ModelAndView("tagihan");
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

    public ArrayList<Sortby> getSortby() {
        if (sortby == null){
            sortby = new ArrayList<>();
            sortby.add(new Sortby("id_tagihan","ID"));
            sortby.add(new Sortby("b.nama_pelanggan","Nama Pelanggan"));
            sortby.add(new Sortby("c.id","Bulan"));
            sortby.add(new Sortby("tahun","Tahun"));
            sortby.add(new Sortby("jumlah_meter","Jumlah Meter"));
            sortby.add(new Sortby("status","Status"));
        }
        return sortby;
    }
}
