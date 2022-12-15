package com.ratwareid.webapp.repository;

import com.ratwareid.webapp.model.Tarif;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/***********************************************************************
 * Module:  com.ratwareid.webapp.repository.TarifRepository
 * Author:  Ratwareid
 * Created: 26/11/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/
public interface TarifRepository extends JpaRepository<Tarif,Long> {

    @Query(value = "select t.* from Tarif t where CAST(COALESCE(t.daya,0) AS varchar) like UPPER(?1) or CAST(COALESCE(t.tarifperkwh,0) AS varchar) like UPPER(?1) ",nativeQuery = true)
    public Page<Tarif> findAllByKeyword(String keyword, Pageable paging);
}
