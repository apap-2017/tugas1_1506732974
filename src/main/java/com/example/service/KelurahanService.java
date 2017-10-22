package com.example.service;

import java.util.List;

import com.example.model.KecamatanModel;
import com.example.model.KelurahanModel;

public interface KelurahanService {
	KecamatanModel selectKecamatan(String id_kecamatan);
	KelurahanModel selectKelurahan(String id);
    List<KelurahanModel> selectAllKelurahan(String kecamatan);
    List<KelurahanModel> selectAllKelurahan();
}
