package com.ratwareid.webapp.controller;

import com.ratwareid.webapp.model.*;
import com.ratwareid.webapp.repository.BulanRepository;
import com.ratwareid.webapp.repository.PelangganRepository;
import com.ratwareid.webapp.repository.PenggunaanRepository;

import com.ratwareid.webapp.repository.UserRepository;
import com.ratwareid.webapp.security.Constant;
import com.ratwareid.webapp.security.MyPelangganDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/***********************************************************************
 * Module:  com.ratwareid.webapp.controller.PenggunaanController
 * Author:  Ratwareid
 * Created: 26/11/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/

@Controller
@RequestMapping("/transaksi/penggunaan")
public class PenggunaanController extends BaseController{

    @Autowired
    PenggunaanRepository penggunaanRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PelangganRepository pelangganRepository;

    @Autowired
    BulanRepository bulanRepository;

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
            paging = PageRequest.of(pageint, size.orElse(10),Sort.by(sort_by.orElse("id_penggunaan")).descending());
        }else{
            paging = PageRequest.of(pageint, size.orElse(10),Sort.by(sort_by.orElse("id_penggunaan")).ascending());
        }
        Page<Penggunaan> list;
        if (hasAuthority("USER")){
            list = penggunaanRepository.findAllPrivateByKeyword(key,getCurrentUsername(), paging);
        }else {
            list = penggunaanRepository.findAllByKeyword(key, paging);
        }
        mvc = new ModelAndView("penggunaan");
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
        Penggunaan penggunaan = new Penggunaan();

        if (hasAuthority("USER")){
            Pelanggan pelanggan = pelangganRepository.getUserByUsername(getCurrentUsername());
            penggunaan.setPelanggan(pelanggan);
        }

        mvc.addObject("penggunaan",penggunaan);
        mvc.addObject("mode","edit");
        mvc.addObject("pelanggans",this.getPelanggans());
        mvc.addObject("bulans",this.getBulans());
        mvc.addObject("tahuns",this.getTahuns());
        return mvc;
    }

    @RequestMapping(value = "/save", method =RequestMethod.POST)
    public String save(@ModelAttribute Penggunaan penggunaan) throws Exception {
        if (getNumber(penggunaan.getMeterAkhir()) < getNumber(penggunaan.getMeterAwal())){
            throw new Exception("Meter Akhir harus lebih besar dari Meter Awal !");
        }

        penggunaanRepository.save(penggunaan);
        return "redirect:/transaksi/penggunaan/";
    }

    @RequestMapping("/edit")
    public ModelAndView edit(@RequestParam(name="id") Long id) throws Exception{
        Optional<Penggunaan> penggunaan = penggunaanRepository.findById(id);

        if (penggunaan.isEmpty()) throw new Exception("Penggunaan dengan id "+id+" tidak ditemukan !");

        if (hasAuthority("USER")){
            Pelanggan pelanggan = pelangganRepository.getUserByUsername(getCurrentUsername());
            penggunaan.get().setPelanggan(pelanggan);
        }
        mvc.addObject("penggunaan",penggunaan.get());
        mvc.addObject("mode","edit");
        mvc.addObject("pelanggans",this.getPelanggans());
        mvc.addObject("bulans",this.getBulans());
        mvc.addObject("tahuns",this.getTahuns());
        return mvc;
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam (name="id") Long id) {
        penggunaanRepository.deleteById(id);
        return "redirect:/transaksi/penggunaan/";
    }

    public Iterable<Pelanggan> getPelanggans(){
        if (hasAuthority("USER")){
            Pelanggan pelanggan = pelangganRepository.getUserByUsername(getCurrentUsername());
            ArrayList<Pelanggan> list = new ArrayList<>();
            list.add(pelanggan);
            return list;
        }else {
            return pelangganRepository.findAll();
        }
    }

    public List<Bulan> getBulans(){
        return bulanRepository.findAll();
    }

    public List<Long> getTahuns(){
        LocalDate nowDate = LocalDate.now();
        int tahun = nowDate.getYear();
        ArrayList<Long> tahuns = new ArrayList<>();
        for (int i = tahun-20;i<tahun+20;i++){
            //cetak rentang 20 tahun keatas kebawah tahun ini
            tahuns.add((long) i);
        }
        return tahuns;
    }

    public Long getNumber(Long val){
        return Objects.requireNonNullElse(val, 0L);
    }

    public ArrayList<Sortby> getSortby() {
        if (sortby == null){
            sortby = new ArrayList<>();
            sortby.add(new Sortby("id_penggunaan","ID"));
            sortby.add(new Sortby("b.nama_pelanggan","Nama Pelanggan"));
            sortby.add(new Sortby("c.id","Bulan"));
            sortby.add(new Sortby("tahun","Tahun"));
            sortby.add(new Sortby("meter_awal","Meter Awal"));
            sortby.add(new Sortby("meter_akhir","Meter Akhir"));
        }
        return sortby;
    }
}
