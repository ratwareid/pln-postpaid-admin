package com.ratwareid.webapp.repository;

import com.ratwareid.webapp.model.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/***********************************************************************
 * Module:  com.ratwareid.webapp.repository.RoleRepository
 * Author:  Ratwareid
 * Created: 26/11/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/
public interface LevelRepository extends JpaRepository<Level,Long> {

    public Page<Level> findAllByNameIsLikeIgnoreCase(String name, Pageable paging);

}
