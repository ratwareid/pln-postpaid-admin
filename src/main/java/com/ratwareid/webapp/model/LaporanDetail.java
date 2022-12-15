package com.ratwareid.webapp.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

/***********************************************************************
 * Module:  com.ratwareid.webapp.model.Laporan
 * Author:  Ratwareid
 * Created: 03/12/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/

@Getter
@Setter
public class LaporanDetail {
    public String nomor_kwh,nama_pelanggan,bulan,status;
    public BigInteger id_tagihan,daya,tarifperkwh,tahun,meter_awal,meter_akhir,jumlah_meter,total_tagihan;
}
