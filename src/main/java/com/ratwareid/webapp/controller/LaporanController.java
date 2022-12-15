package com.ratwareid.webapp.controller;

import com.ratwareid.webapp.model.LaporanDetail;
import com.ratwareid.webapp.model.LaporanSummary;
import com.ratwareid.webapp.model.Level;
import com.ratwareid.webapp.repository.EntityRepository;
import com.ratwareid.webapp.repository.LevelRepository;
import com.ratwareid.webapp.security.Constant;
import com.ratwareid.webapp.security.SQLAssembler;
import lombok.Getter;
import lombok.Setter;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
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
@RequestMapping("/laporan")
@Getter
@Setter
public class LaporanController extends BaseController{

    @Autowired
    EntityRepository entityRepository;

    public String fltNoKWH,fltNama,fltBulan,fltTahun,fltStatus;
    public Long fltDayaKWH,fltBiaya,fltKWHStart,fltKWHEnd;

    public Long fltUsage;

    @RequestMapping(value = "/tagihan-summary",method = RequestMethod.GET)
    public ModelAndView summary(@RequestParam(value = "fltNoKWH",required = false) String fltNoKWH,
                               @RequestParam(value = "fltNama",required = false) String fltNama,
                               @RequestParam(value = "fltUsage",required = false) Long fltUsage,
                                @RequestParam(value = "fltStatus",required = false) String fltStatus,
                                @RequestParam(value = "page",required = false) Optional<Integer> page,
                               @RequestParam(value = "size",required = false) Optional<Integer> size) throws Exception {

        Integer pageint = page.orElse(1);

        SQLAssembler sql = new SQLAssembler();
        sql.addSelect("*");
        sql.addQuery("from get_total_invoice()");
        if (fltNoKWH != null){
            sql.addClause("nomor_kwh ilike ?");
            sql.addParKeyword(fltNoKWH);
        }
        if (fltNama != null){
            sql.addClause("nama_pelanggan ilike ?");
            sql.addParKeyword(fltNama);
        }
        if (fltUsage != null){
            sql.addClause("penggunaan_daya = ?");
            sql.addPar(fltUsage);
        }
        if (fltStatus != null){
            sql.addClause("keterangan = ?");
            sql.addPar(fltStatus);
        }
        sql.addOrder("id_pelanggan");
        sql.setPagination(size.orElse(10),pageint);

        List<LaporanSummary> list = (List<LaporanSummary>) sql.getList(entityRepository, LaporanSummary.class);
        mvc = new ModelAndView("laporan");

        mvc.addObject("fltNoKWH",fltNoKWH);
        mvc.addObject("fltNama",fltNama);
        mvc.addObject("fltStatus",fltStatus);
        mvc.addObject("fltUsage",fltUsage);

        mvc.addObject("mode","summary");
        mvc.addObject("list",list);
        mvc.addObject("currentPage",pageint);

        int totalPages = 0;
        mvc.addObject("totalPages",totalPages);

        if (list.size() > 0) {
            totalPages = sql.dataSize.divide(new BigDecimal(sql.getDataSize().intValue()), RoundingMode.UP).intValue();
            mvc.addObject("totalPages",totalPages);

            List<Integer> pageNum = new ArrayList<>();
            for (int x=pageint;x<=totalPages;x++){
                pageNum.add(x);
                if (pageNum.size() >= 10){
                    break;
                }
            }
            mvc.addObject("pageNumbers", pageNum);
        }
        return mvc;
    }

    @RequestMapping(value = "/tagihan-summary/print",method = RequestMethod.GET)
    public ModelAndView summaryPrint(@RequestParam(value = "fltNoKWH",required = false) String fltNoKWH,
                                @RequestParam(value = "fltNama",required = false) String fltNama,
                                @RequestParam(value = "fltUsage",required = false) Long fltUsage,
                                @RequestParam(value = "fltStatus",required = false) String fltStatus,
                                @RequestParam(value = "xls",required = true) String xls) throws Exception {

        SQLAssembler sql = new SQLAssembler();
        sql.addSelect("*");
        sql.addQuery("from get_total_invoice()");
        if (fltNoKWH != null){
            sql.addClause("nomor_kwh ilike ?");
            sql.addParKeyword(fltNoKWH);
        }
        if (fltNama != null){
            sql.addClause("nama_pelanggan ilike ?");
            sql.addParKeyword(fltNama);
        }
        if (fltUsage != null){
            sql.addClause("penggunaan_daya = ?");
            sql.addPar(fltUsage);
        }
        if (fltStatus != null){
            sql.addClause("keterangan ilike ?");
            sql.addParKeyword(fltStatus);
        }
        sql.addOrder("id_pelanggan");

        List<LaporanSummary> list = (List<LaporanSummary>) sql.getList(entityRepository, LaporanSummary.class);
        mvc = new ModelAndView("print_summary");
        mvc.addObject("list",list);
        mvc.addObject("xls",xls);

        return mvc;
    }

    @RequestMapping(value = "/tagihan-detail",method = RequestMethod.GET)
    public ModelAndView detail(@RequestParam(value = "fltNoKWH",required = false) String fltNoKWH,
                                @RequestParam(value = "fltNama",required = false) String fltNama,
                                @RequestParam(value = "fltBulan",required = false) String fltBulan,
                                @RequestParam(value = "fltTahun",required = false) Long fltTahun,
                                @RequestParam(value = "fltStatus",required = false) String fltStatus,
                                @RequestParam(value = "fltDayaKWH",required = false) Long fltDayaKWH,
                                @RequestParam(value = "fltBiaya",required = false) Long fltBiaya,
                                @RequestParam(value = "fltKWHStart",required = false) Long fltKWHStart,
                                @RequestParam(value = "fltKWHEnd",required = false) Long fltKWHEnd,
                                @RequestParam(value = "page",required = false) Optional<Integer> page,
                                @RequestParam(value = "size",required = false) Optional<Integer> size) throws Exception {

        Integer pageint = page.orElse(1);

        SQLAssembler sql = new SQLAssembler();
        sql.addSelect("d.id_tagihan,a.nomor_kwh,a.nama_pelanggan,b.daya,b.tarifperkwh,c.meter_awal,c.meter_akhir,\n" +
                "d.jumlah_meter,e.nama as bulan,c.tahun,COALESCE(b.tarifperkwh,0) * COALESCE(d.jumlah_meter,0) as total_tagihan,d.status");
        sql.addQuery("from pelanggan a \n" +
                "inner join tarif b on a.id_tarif = b.id_tarif\n" +
                "inner join penggunaan c on a.id_pelanggan = c.id_pelanggan\n" +
                "inner join tagihan d on c.id_penggunaan = d.id_penggunaan\n" +
                "inner join bulan e on c.bulan = e.id");

        if (fltNoKWH != null){
            sql.addClause("a.nomor_kwh ilike ?");
            sql.addParKeyword(fltNoKWH);
        }
        if (fltNama != null){
            sql.addClause("a.nama_pelanggan ilike ?");
            sql.addParKeyword(fltNama);
        }
        if (fltBulan != null){
            sql.addClause("e.nama ilike ?");
            sql.addParKeyword(fltBulan);
        }
        if (fltTahun != null){
            sql.addClause("c.tahun = ?");
            sql.addPar(fltTahun);
        }
        if (fltStatus != null){
            sql.addClause("d.status ilike ?");
            sql.addParKeyword(fltStatus);
        }
        if (fltDayaKWH != null){
            sql.addClause("b.daya = ?");
            sql.addPar(fltDayaKWH);
        }
        if (fltBiaya != null){
            sql.addClause("b.tarifperkwh = ?");
            sql.addPar(fltBiaya);
        }
        if (fltKWHStart != null){
            sql.addClause("c.meter_awal = ?");
            sql.addPar(fltKWHStart);
        }
        if (fltKWHEnd != null){
            sql.addClause("c.meter_akhir = ?");
            sql.addPar(fltKWHEnd);
        }
        sql.addOrder("c.tahun,c.bulan,a.id_pelanggan");
        sql.setPagination(size.orElse(10),pageint);

        List<LaporanDetail> list = (List<LaporanDetail>) sql.getList(entityRepository,LaporanDetail.class);
        mvc = new ModelAndView("laporan");

        mvc.addObject("fltNoKWH",fltNoKWH);
        mvc.addObject("fltNama",fltNama);
        mvc.addObject("fltBulan",fltBulan);
        mvc.addObject("fltTahun",fltTahun);
        mvc.addObject("fltStatus",fltStatus);
        mvc.addObject("fltDayaKWH",fltDayaKWH);
        mvc.addObject("fltBiaya",fltBiaya);
        mvc.addObject("fltKWHStart",fltKWHStart);
        mvc.addObject("fltKWHEnd",fltKWHEnd);

        mvc.addObject("mode","detail");
        mvc.addObject("list",list);
        mvc.addObject("currentPage",pageint);

        int totalPages = 0;
        mvc.addObject("totalPages",totalPages);

        if (list.size() > 0) {
            totalPages = sql.dataSize.divide(new BigDecimal(sql.getDataSize().intValue()), RoundingMode.UP).intValue();
            mvc.addObject("totalPages",totalPages);

            List<Integer> pageNum = new ArrayList<>();
            for (int x=pageint;x<=totalPages;x++){
                pageNum.add(x);
                if (pageNum.size() >= 10){
                    break;
                }
            }
            mvc.addObject("pageNumbers", pageNum);
        }
        return mvc;
    }

    @RequestMapping(value = "/tagihan-detail/print",method = RequestMethod.GET)
    public ModelAndView printDetail(@RequestParam(value = "fltNoKWH",required = false) String fltNoKWH,
                               @RequestParam(value = "fltNama",required = false) String fltNama,
                               @RequestParam(value = "fltBulan",required = false) String fltBulan,
                               @RequestParam(value = "fltTahun",required = false) Long fltTahun,
                               @RequestParam(value = "fltStatus",required = false) String fltStatus,
                               @RequestParam(value = "fltDayaKWH",required = false) Long fltDayaKWH,
                               @RequestParam(value = "fltBiaya",required = false) Long fltBiaya,
                               @RequestParam(value = "fltKWHStart",required = false) Long fltKWHStart,
                               @RequestParam(value = "fltKWHEnd",required = false) Long fltKWHEnd,
                               @RequestParam(value = "xls",required = true) String xls) throws Exception {

        SQLAssembler sql = new SQLAssembler();
        sql.addSelect("d.id_tagihan,a.nomor_kwh,a.nama_pelanggan,b.daya,b.tarifperkwh,c.meter_awal,c.meter_akhir,\n" +
                "d.jumlah_meter,e.nama as bulan,c.tahun,COALESCE(b.tarifperkwh,0) * COALESCE(d.jumlah_meter,0) as total_tagihan,d.status");
        sql.addQuery("from pelanggan a \n" +
                "inner join tarif b on a.id_tarif = b.id_tarif\n" +
                "inner join penggunaan c on a.id_pelanggan = c.id_pelanggan\n" +
                "inner join tagihan d on c.id_penggunaan = d.id_penggunaan\n" +
                "inner join bulan e on c.bulan = e.id");

        if (fltNoKWH != null){
            sql.addClause("a.nomor_kwh ilike ?");
            sql.addParKeyword(fltNoKWH);
        }
        if (fltNama != null){
            sql.addClause("a.nama_pelanggan ilike ?");
            sql.addParKeyword(fltNama);
        }
        if (fltBulan != null){
            sql.addClause("e.nama ilike ?");
            sql.addParKeyword(fltBulan);
        }
        if (fltTahun != null){
            sql.addClause("c.tahun = ?");
            sql.addPar(fltTahun);
        }
        if (fltStatus != null){
            sql.addClause("d.status ilike ?");
            sql.addParKeyword(fltStatus);
        }
        if (fltDayaKWH != null){
            sql.addClause("b.daya = ?");
            sql.addPar(fltDayaKWH);
        }
        if (fltBiaya != null){
            sql.addClause("b.tarifperkwh = ?");
            sql.addPar(fltBiaya);
        }
        if (fltKWHStart != null){
            sql.addClause("c.meter_awal = ?");
            sql.addPar(fltKWHStart);
        }
        if (fltKWHEnd != null){
            sql.addClause("c.meter_akhir = ?");
            sql.addPar(fltKWHEnd);
        }
        sql.addOrder("c.tahun,c.bulan,a.id_pelanggan");

        List<LaporanDetail> list = (List<LaporanDetail>) sql.getList(entityRepository,LaporanDetail.class);
        mvc = new ModelAndView("print_detail");
        mvc.addObject("list",list);
        mvc.addObject("xls",xls);

        return mvc;
    }
}
