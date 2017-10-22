package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KecamatanModel {
	private String id;
	private String id_kota;
	private String kode_kecamatan;
	private String nama_kecamatan;
	private KotaModel kota;
}