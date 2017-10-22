package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.KeluargaMapper;
import com.example.model.KeluargaModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KeluargaServiceDatabase implements KeluargaService {
	@Autowired
	private KeluargaMapper keluargaMapper;

	@Override
	public KeluargaModel selectKeluarga(String nomor_kk) {
		log.info ("select keluarga with nomor_kk {}", nomor_kk);
		return keluargaMapper.selectKeluarga(nomor_kk);
	}
	
	@Override
	public List<KeluargaModel> listNkkSama(String nkk) {
		return keluargaMapper.listNkkSama(nkk);
	}

	@Override
	public void addKeluarga(KeluargaModel keluarga) {
		keluargaMapper.addKeluarga(keluarga);
	}

	@Override
	public KeluargaModel selectKeluargaById(String id) {
		return keluargaMapper.selectKeluargaById(id);
	}



	@Override
	public void updateKeluarga(KeluargaModel keluarga) {
		keluargaMapper.updateKeluarga(keluarga);
		
	}
}