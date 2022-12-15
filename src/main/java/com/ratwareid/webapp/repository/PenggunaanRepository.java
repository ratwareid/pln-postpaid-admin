package com.ratwareid.webapp.repository;

import com.ratwareid.webapp.model.Pelanggan;
import com.ratwareid.webapp.model.Penggunaan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/***********************************************************************
 * Module:  com.ratwareid.webapp.repository.PenggunaanRepository
 * Author:  Ratwareid
 * Created: 27/11/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/
public interface PenggunaanRepository extends PagingAndSortingRepository<Penggunaan,Long> {

    @Query(value = "select a.* from penggunaan a " +
            "inner join pelanggan b on a.id_pelanggan = b.id_pelanggan " +
            "left join bulan c on a.bulan = c.id " +
            "where b.nama_pelanggan ilike ?1 or COALESCE(c.nama,'') ilike ?1 or CAST(COALESCE(a.tahun,0) AS VARCHAR) ilike ?1 " +
            "or CAST(COALESCE(a.meter_awal,0) AS VARCHAR) ilike ?1 or CAST(COALESCE(a.meter_akhir,0) AS VARCHAR) ilike ?1",
            nativeQuery = true)
    Page<Penggunaan> findAllByKeyword(String keyword, Pageable paging);

    @Query(value = "select a.* from penggunaan a " +
            "inner join pelanggan b on a.id_pelanggan = b.id_pelanggan and b.username = ?2 " +
            "left join bulan c on a.bulan = c.id " +
            "where b.nama_pelanggan ilike ?1 or COALESCE(c.nama,'') ilike ?1 or CAST(COALESCE(a.tahun,0) AS VARCHAR) ilike ?1 " +
            "or CAST(COALESCE(a.meter_awal,0) AS VARCHAR) ilike ?1 or CAST(COALESCE(a.meter_akhir,0) AS VARCHAR) ilike ?1",
            nativeQuery = true)
    Page<Penggunaan> findAllPrivateByKeyword(String keyword,String unamePelanggan, Pageable paging);

}
