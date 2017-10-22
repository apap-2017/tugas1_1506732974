package com.example.service;

import java.util.List;

import com.example.model.KeluargaModel;
import com.example.model.PendudukModel;

public interface PendudukService {
	PendudukModel selectPenduduk(String nik);
	List<PendudukModel> selectAnggotaKeluarga(String id);
	void addPenduduk (PendudukModel penduduk);
	KeluargaModel selectKeluarga(String id_keluarga);
	List<PendudukModel> listNikSama(String nik);
	void updatePenduduk(PendudukModel penduduk);
	void nonAktifkanPenduduk (String nik);
	List<PendudukModel> selectPendudukByKelurahan(String id_kelurahan);
}