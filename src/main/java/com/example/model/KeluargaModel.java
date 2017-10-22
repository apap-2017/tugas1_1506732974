package com.example.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeluargaModel {
	private String id;
	private String nomor_kk;
	private String alamat;
	private String RT;
	private String RW;
	private String id_kelurahan;
	private String is_tidak_berlaku;
	private String kelurahan;
	private String kecamatan;
	private String kota;
	
	private List<PendudukModel> anggotaKeluarga;
}