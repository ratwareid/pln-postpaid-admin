package com.ratwareid.webapp.repository;

import com.ratwareid.webapp.model.Pelanggan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/***********************************************************************
 * Module:  com.ratwareid.webapp.repository.PelangganRepository
 * Author:  Ratwareid
 * Created: 26/11/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/
public interface PelangganRepository extends PagingAndSortingRepository<Pelanggan,Long> {

    @Query("SELECT u from Pelanggan u Where u.username = :username")
    public Pelanggan getUserByUsername(@Param("username") String username);

    @Query(value = "select a.* from pelanggan a " +
            "inner join tarif b on a.id_tarif = b.id_tarif  " +
            "where username ilike ?1 or nomor_kwh ilike ?1 or nama_pelanggan ilike ?1 " +
            "or alamat ilike ?1 or CAST(COALESCE(b.daya,0) AS VARCHAR) ilike ?1 ",
    nativeQuery = true)
    public Page<Pelanggan> findAllByKeyword(String keyword, Pageable paging);

}
