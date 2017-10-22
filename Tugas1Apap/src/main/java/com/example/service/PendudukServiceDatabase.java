package com.example.service;

import com.example.model.KeluargaModel;
import com.example.model.PendudukModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.PendudukMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PendudukServiceDatabase implements PendudukService {

	@Autowired
	private PendudukMapper pendudukMapper;

	@Override
	public PendudukModel selectPenduduk(String nik) {
		log.info("select penduduk with nik {}", nik);
        return pendudukMapper.selectPenduduk(nik);
	}

	@Override
	public void addPenduduk(PendudukModel penduduk) {
		pendudukMapper.addPenduduk(penduduk);
	}
	
	@Override
	public KeluargaModel selectKeluarga(String id_keluarga) {
		return pendudukMapper.selectKeluarga(id_keluarga);
	}

	@Override
	public List<PendudukModel> listNikSama(String nik) {
		return pendudukMapper.listNikSama(nik);
	}

	@Override
	public List<PendudukModel> selectAnggotaKeluarga(String id) {
		return pendudukMapper.selectAnggotaKeluarga(id);
	}

	@Override
	public void updatePenduduk(PendudukModel penduduk) {
		pendudukMapper.updatePenduduk(penduduk);
		
	}

	@Override
	public void nonAktifkanPenduduk(String nik) {
		pendudukMapper.nonAktifkanPenduduk(nik);
	}

	@Override
	public List<PendudukModel> selectPendudukByKelurahan(String id_kelurahan) {
		return pendudukMapper.selectPendudukByKelurahan(id_kelurahan);
	}
}