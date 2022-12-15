package com.ratwareid.webapp.controller;

import com.ratwareid.webapp.model.*;
import com.ratwareid.webapp.repository.PelangganRepository;
import com.ratwareid.webapp.repository.PembayaranRepository;
import com.ratwareid.webapp.repository.TagihanRepository;
import com.ratwareid.webapp.repository.UserRepository;
import com.ratwareid.webapp.security.Constant;
import com.ratwareid.webapp.security.MyAdminDetails;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/***********************************************************************
 * Module:  com.ratwareid.webapp.controller.pembayaranController
 * Author:  Ratwareid
 * Created: 26/11/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/

@Controller
@RequestMapping("/transaksi/pembayaran")
public class PembayaranController extends BaseController{

    @Autowired
    PembayaranRepository pembayaranRepository;

    @Autowired
    TagihanRepository tagihanRepository;

    @Autowired
    PelangganRepository pelangganRepository;

    @Autowired
    UserRepository userRepository;

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
            paging = PageRequest.of(pageint, size.orElse(10),Sort.by(sort_by.orElse("id_pembayaran")).descending());
        }else{
            paging = PageRequest.of(pageint, size.orElse(10),Sort.by(sort_by.orElse("id_pembayaran")).ascending());
        }
        Page<Pembayaran> list = pembayaranRepository.findAllByKeyword(key,paging);
        mvc = new ModelAndView("pembayaran");
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

    public User getPetugas(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyAdminDetails userDetails = (MyAdminDetails) authentication.getPrincipal();
        return userRepository.getUserByUsername(userDetails.getUsername());
    }

    @RequestMapping(value="/add",method = RequestMethod.GET)
    public ModelAndView add() throws Exception{
        Pembayaran pembayaran = new Pembayaran();
        pembayaran.setBiayaAdmin(2500L);

        pembayaran.setPetugas(getPetugas());

        mvc.addObject("pembayaran",pembayaran);
        mvc.addObject("pelanggans",getPelanggan());
        mvc.addObject("tagihans",getTagihan(pembayaran));
        mvc.addObject("mode","edit");
        return mvc;
    }

    @RequestMapping(value = "/save", method =RequestMethod.POST)
    public String save(@ModelAttribute Pembayaran pembayaran) {
        pembayaranRepository.save(pembayaran);
        return "redirect:/transaksi/pembayaran/";
    }


    @RequestMapping("/delete")
    public String delete(@RequestParam (name="id") Long id) {
        pembayaranRepository.deleteById(id);
        return "redirect:/transaksi/pembayaran/";
    }

    public List<Tagihan> getTagihan(Pembayaran pembayaran){
        if (pembayaran.getPelanggan() != null) {
            return tagihanRepository.findNotPaidInvoice(pembayaran.getPelanggan());
        }
        return new ArrayList<>();
    }

    public Iterable<Pelanggan> getPelanggan(){
        return pelangganRepository.findAll();
    }

    @RequestMapping(value="/pelanggan-selected",method = RequestMethod.POST)
    public ModelAndView onPelangganChanged(@ModelAttribute Pembayaran pembayaran) throws Exception {

        if (pembayaran.getPelanggan() == null){
            resetData(pembayaran);
        }

        mvc.addObject("pembayaran",pembayaran);
        mvc.addObject("pelanggans",getPelanggan());
        mvc.addObject("tagihans",getTagihan(pembayaran));
        mvc.addObject("mode","edit");

        return mvc;
    }

    public void resetData(Pembayaran pembayaran){
        pembayaran.setTagihan(null);
        pembayaran.setBulan(null);
        pembayaran.setTahun(null);
        pembayaran.setBiayaAdmin(null);
        pembayaran.setTotalBayar(null);
    }

    public void loadData(Pembayaran pembayaran){
        Tagihan tagihan = pembayaran.getTagihan();
        Tarif tarif = pembayaran.getPelanggan().getTarif();
        pembayaran.setBulan(tagihan.getBulan());
        pembayaran.setTahun(tagihan.getTahun());
        long biayaPenggunaan = Math.multiplyExact(tagihan.getJumlahMeter(),tarif.getTarifperkwh());
        pembayaran.setTotalBayar(Math.addExact(biayaPenggunaan,pembayaran.getBiayaAdmin()));
    }

    @RequestMapping(value="/tagihan-selected",method = RequestMethod.POST)
    public ModelAndView onTagihanChanged(@ModelAttribute Pembayaran pembayaran) throws Exception {

        if (pembayaran.getTagihan() == null){
            resetData(pembayaran);
        }else{
            loadData(pembayaran);
        }

        mvc.addObject("pembayaran",pembayaran);
        mvc.addObject("pelanggans",getPelanggan());
        mvc.addObject("tagihans",getTagihan(pembayaran));
        mvc.addObject("mode","edit");

        return mvc;
    }

    public ArrayList<Sortby> getSortby() {
        if (sortby == null){
            sortby = new ArrayList<>();
            sortby.add(new Sortby("id_pembayaran","ID"));
            sortby.add(new Sortby("id_tagihan","ID Tagihan"));
            sortby.add(new Sortby("c.nama_pelanggan","Nama Pelanggan"));
            sortby.add(new Sortby("tanggal_pembayaran","Tanggal Pembayaran"));
            sortby.add(new Sortby("bulan_bayar","Bulan"));
            sortby.add(new Sortby("tahun_bayar","Tahun"));
            sortby.add(new Sortby("total_bayar","Total Bayar"));
            sortby.add(new Sortby("d.nama_admin","Nama Petugas"));
        }
        return sortby;
    }
}
