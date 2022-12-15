package com.ratwareid.webapp.model;

import lombok.Data;

import javax.persistence.*;

/***********************************************************************
 * Module:  com.ratwareid.webapp.model.Tarif
 * Author:  Ratwareid
 * Created: 26/11/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/

@Entity
@Data
@Table(name = "tarif")
public class Tarif {

    @Id
    @Column(name="id_tarif")
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    private Long daya;
    private Long tarifperkwh;

}
