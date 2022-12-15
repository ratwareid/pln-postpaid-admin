package com.ratwareid.webapp.repository;

import com.ratwareid.webapp.model.Pelanggan;
import com.ratwareid.webapp.model.Pembayaran;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/***********************************************************************
 * Module:  com.ratwareid.webapp.repository.PembayaranRepository
 * Author:  Ratwareid
 * Created: 27/11/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/
public interface PembayaranRepository extends PagingAndSortingRepository<Pembayaran,Long> {

    @Query(value = "select a.* from pembayaran a " +
            "inner join tagihan b on a.id_tagihan = b.id_tagihan  " +
            "inner join pelanggan c on a.id_pelanggan = c.id_pelanggan " +
            "inner join users d on a.id_user = d.user_id " +
            "where c.nama_pelanggan ilike ?1 or d.nama_admin ilike ?1 or CAST(a.tanggal_pembayaran AS VARCHAR) ilike ?1 " +
            "or CAST(COALESCE(a.bulan_bayar,0) AS VARCHAR) ilike ?1 or CAST(COALESCE(a.tahun_bayar,0) AS VARCHAR) ilike ?1 " +
            "or CAST(COALESCE(a.biaya_admin,0) AS VARCHAR) ilike ?1 or CAST(COALESCE(a.total_bayar,0) AS VARCHAR) ilike ?1",
            nativeQuery = true)
    Page<Pembayaran> findAllByKeyword(String keyword, Pageable paging);

}
