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
@Table(name = "penggunaan")
public class Penggunaan {

    @Id
    @Column(name="id_penggunaan")
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_pelanggan", referencedColumnName = "id_pelanggan")
    private Pelanggan pelanggan;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "bulan", referencedColumnName = "id")
    private Bulan bulan;

    private Long tahun;

    @Column(name="meter_awal")
    private Long meterAwal;

    @Column(name="meter_akhir")
    private Long meterAkhir;
}
