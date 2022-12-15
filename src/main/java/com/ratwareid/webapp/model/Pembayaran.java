package com.ratwareid.webapp.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "pembayaran")
public class Pembayaran {
	@Id
	@Column(name="id_pembayaran")
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "id_tagihan", referencedColumnName = "id_tagihan")
	private Tagihan tagihan;

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "id_pelanggan", referencedColumnName = "id_pelanggan")
	private Pelanggan pelanggan;

	@Column(name="tanggal_pembayaran")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tanggalPembayaran;

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "bulan_bayar", referencedColumnName = "id")
	private Bulan bulan;

	@Column(name="tahun_bayar")
	private Long tahun;

	@Column(name="biaya_admin")
	private Long biayaAdmin;

	@Column(name="total_bayar")
	private Long totalBayar;

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "id_user", referencedColumnName = "user_id")
	private User petugas;
}
