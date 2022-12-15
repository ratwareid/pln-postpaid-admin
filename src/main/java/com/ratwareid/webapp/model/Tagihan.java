package com.ratwareid.webapp.model;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Data
@Table(name = "tagihan")
public class Tagihan {
	@Id
	@Column(name="id_tagihan")
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Long id;

	@Column(name="id_penggunaan")
	private Long idPenggunaan;

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "id_pelanggan", referencedColumnName = "id_pelanggan")
	private Pelanggan pelanggan;

	@Column(name="jumlah_meter")
	private Long jumlahMeter;

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "bulan", referencedColumnName = "id")
	private Bulan bulan;

	private Long tahun;
	private String status;

}
