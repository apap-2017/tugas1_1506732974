package com.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.model.KecamatanModel;
import com.example.model.KelurahanModel;

@Mapper
public interface KelurahanMapper {
	@Select("select * from kecamatan where id = #{id_kecamatan}")
	KecamatanModel selectKecamatan(@Param("id_kecamatan") String id);
	
	@Select("select * from kelurahan where id = #{id}")
    KelurahanModel selectKelurahan(String id);

    @Select("select * from kelurahan where id_kecamatan = #{kecamatan}")
    List<KelurahanModel> selectAllKelurahan(String kecamatan);

    @Select("select * from kelurahan")
    List<KelurahanModel> selectAllKelurahans();
	
}