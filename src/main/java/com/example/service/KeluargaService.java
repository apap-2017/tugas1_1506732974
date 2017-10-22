package com.example.service;

import java.util.List;

import com.example.model.KeluargaModel;

public interface KeluargaService {
	
	KeluargaModel selectKeluarga (String nomor_kk);
	
	void addKeluarga(KeluargaModel keluarga);
	
	KeluargaModel selectKeluargaById(String id);
	
	List<KeluargaModel> listNkkSama(String nkk);
	
	void updateKeluarga(KeluargaModel keluarga);
}