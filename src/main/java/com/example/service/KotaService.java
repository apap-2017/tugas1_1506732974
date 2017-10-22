package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.KotaModel;

@Service
public interface KotaService {
	KotaModel selectKota(String id);
	List<KotaModel> selectAllKota();
}
