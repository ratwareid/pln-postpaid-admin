package com.ratwareid.webapp.model;

import lombok.Data;

import javax.persistence.*;

/***********************************************************************
 * Module:  com.ratwareid.webapp.model.Pelanggan
 * Author:  Ratwareid
 * Created: 26/11/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/

@Entity
@Data
@Table(name = "pelanggan")
public class Pelanggan {

    @Id
    @Column(name="id_pelanggan")
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String alamat;

    @Column(name="nomor_kwh")
    private String nomorKwh;

    @Column(name="nama_pelanggan")
    private String namaPelanggan;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_tarif", referencedColumnName = "id_tarif")
    private Tarif tarif;

    @Transient
    private String newPassword;
}
