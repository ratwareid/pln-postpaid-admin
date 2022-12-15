package com.ratwareid.webapp.controller;

import com.ratwareid.webapp.model.Tarif;
import com.ratwareid.webapp.repository.TarifRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/***********************************************************************
 * Module:  com.ratwareid.webapp.controller.TarifControllerTest
 * Author:  Ratwareid
 * Created: 09/12/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TarifControllerTest {

    @Autowired
    private TarifRepository tarifRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void save() {
        Tarif tarif = new Tarif();
        tarif.setId(1L);
        tarif.setDaya(900L);
        tarif.setTarifperkwh(1500L);
        tarifRepository.save(tarif);
    }
}