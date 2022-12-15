package com.ratwareid.webapp.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

/***********************************************************************
 * Module:  com.ratwareid.webapp.model.Laporan
 * Author:  Ratwareid
 * Created: 03/12/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/

@Getter
@Setter
public class LaporanSummary {
    public String nomor_kwh,nama_pelanggan,keterangan;
    public BigInteger id_pelanggan,penggunaan_daya;
    public BigDecimal total_tagihan,total_pembayaran,sisa_tagihan;
}
