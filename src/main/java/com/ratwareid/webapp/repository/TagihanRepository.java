package com.ratwareid.webapp.repository;

import com.ratwareid.webapp.model.Pelanggan;
import com.ratwareid.webapp.model.Penggunaan;
import com.ratwareid.webapp.model.Tagihan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/***********************************************************************
 * Module:  com.ratwareid.webapp.repository.TagihanRepository
 * Author:  Ratwareid
 * Created: 27/11/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/
public interface TagihanRepository extends PagingAndSortingRepository<Tagihan,Long> {

    @Query(value = "select a.* from tagihan a " +
            "left join pelanggan b on a.id_pelanggan = b.id_pelanggan " +
            "left join bulan c on a.bulan = c.id " +
            "where COALESCE(b.nama_pelanggan,'') ilike ?1 or CAST(COALESCE(a.bulan,0) AS VARCHAR) ilike ?1 " +
            "or CAST(COALESCE(a.tahun,0) AS VARCHAR) ilike ?1 or CAST(COALESCE(a.jumlah_meter,0) AS VARCHAR) ilike ?1 or COALESCE(a.status,'') ilike ?1",
            nativeQuery = true)
    Page<Tagihan> findAllByKeyword(String keyword, Pageable paging);

    @Query(value = "select a.* from tagihan a " +
            "inner join pelanggan b on a.id_pelanggan = b.id_pelanggan and b.username = ?2 " +
            "left join bulan c on a.bulan = c.id " +
            "where COALESCE(b.nama_pelanggan,'') ilike ?1 or CAST(COALESCE(a.bulan,0) AS VARCHAR) ilike ?1 " +
            "or CAST(COALESCE(a.tahun,0) AS VARCHAR) ilike ?1 or CAST(COALESCE(a.jumlah_meter,0) AS VARCHAR) ilike ?1 or COALESCE(a.status,'') ilike ?1",
            nativeQuery = true)
    Page<Tagihan> findAllPrivateByKeyword(String keyword,String unamePelanggan, Pageable paging);

    @Query(value = "select t from Tagihan t where UPPER(t.status) = 'BELUM DIBAYAR' and t.pelanggan = ?1")
    List<Tagihan> findNotPaidInvoice(Pelanggan pelanggan);
}
