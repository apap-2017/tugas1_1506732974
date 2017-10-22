package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.KecamatanMapper;
import com.example.model.KecamatanModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KecamatanServiceDatabase implements KecamatanService{

	@Autowired
    KecamatanMapper kecamatanMapper;
	
	@Override
	public KecamatanModel selectKecamatan(String id) {
		return kecamatanMapper.selectKecamatan(id);
	}

	@Override
	public List<KecamatanModel> selectAllKecamatan(String kota) {
		return kecamatanMapper.selectAllKecamatan(kota);
	}

	@Override
	public List<KecamatanModel> selectAllKecamatan() {
		return kecamatanMapper.selectAllKecamatans();
	}

	@Override
	public List<KecamatanModel> selectKecamatanByIdKota(String kt) {
		log.info("select kecamatan by id kota {}", kt);
		return kecamatanMapper.selectKecamatanByIdKota(kt);
	}
}
