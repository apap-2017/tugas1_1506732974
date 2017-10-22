package com.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.example.model.KeluargaModel;

@Mapper
public interface KeluargaMapper {
	
	@Select("select * from keluarga where nomor_kk = #{nkk}")	
	KeluargaModel selectKeluarga(@Param("nkk") String nkk);

	@Select("select * from keluarga where id=#{id}")
	KeluargaModel selectKeluargaById(@Param("id") String id);
	
	@Select("select * from keluarga where nomor_kk like #{nkk}")
	List<KeluargaModel> listNkkSama(String nkk);
	
	@Insert("insert into keluarga (nomor_kk, alamat, rt, rw, id_kelurahan, is_tidak_berlaku) values (#{nomor_kk}, #{alamat}, #{RT}, #{RW}, #{id_kelurahan}, #{is_tidak_berlaku})")
	void addKeluarga(KeluargaModel keluarga);
	
	@Update("update keluarga set nomor_kk = #{nomor_kk}, alamat = #{alamat}, rt = #{RT}, rw = #{RW}, id_kelurahan = #{id_kelurahan}, is_tidak_berlaku = #{is_tidak_berlaku} where id = #{id}")
    void updateKeluarga(KeluargaModel keluarga);
}