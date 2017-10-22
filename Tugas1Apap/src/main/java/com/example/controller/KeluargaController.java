package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.KeluargaModel;
import com.example.model.KelurahanModel;
import com.example.model.KotaModel;
import com.example.model.PendudukModel;
import com.example.model.KecamatanModel;
import com.example.service.KeluargaService;
import com.example.service.KelurahanService;
import com.example.service.KotaService;
import com.example.service.PendudukService;


import com.example.service.KecamatanService;

@Controller
public class KeluargaController {
	@Autowired
	KeluargaService keluargaDAO;
	@Autowired
	KelurahanService kelurahanDAO;
	@Autowired
	KecamatanService kecamatanDAO;
	@Autowired
	KotaService kotaDAO;
	@Autowired 
	PendudukService pendudukDAO;
	
	@RequestMapping("/keluarga")
	public String viewKeluargaSubmit(Model model, @RequestParam(value="nkk", required=false) String nkk) {
		KeluargaModel keluarga = keluargaDAO.selectKeluarga(nkk);
		if (keluarga == null) {
			model.addAttribute("errorMessage", "Keluarga dengan NKK " + nkk + " tidak ditemukan");
			return "error/error404";
			
		} else {
			List<PendudukModel> anggotaKeluarga = pendudukDAO.selectAnggotaKeluarga(keluarga.getId());
			KelurahanModel kelurahan = kelurahanDAO.selectKelurahan(keluarga.getId_kelurahan());
	        KecamatanModel kecamatan = kecamatanDAO.selectKecamatan(kelurahan.getId_kecamatan());
	        KotaModel kota = kotaDAO.selectKota(kecamatan.getId_kota());
	        
	        model.addAttribute("keluarga", keluarga);
	        model.addAttribute("anggotaKeluarga", anggotaKeluarga);
	        model.addAttribute("kelurahan", kelurahan);
	        model.addAttribute("kecamatan", kecamatan);
	        model.addAttribute("kota", kota);
	        
			return "keluarga-detail";
		}
	}
	
	@RequestMapping("/keluarga/tambah")
    public String add(Model model)
    {
		KeluargaModel keluarga = new KeluargaModel();
        keluarga.setId_kelurahan("");
        model.addAttribute("keluarga", keluarga);
        
        List<KelurahanModel> kelurahan = kelurahanDAO.selectAllKelurahan();
        List<KecamatanModel> kecamatan = kecamatanDAO.selectAllKecamatan();
        List<KotaModel> kota = kotaDAO.selectAllKota();
        for(int i = 0; i < kelurahan.size(); i++){
            for(int j = 0; j < kecamatan.size(); j++){
                for(int k = 0 ; k < kota.size(); k++){
                    if(kelurahan.get(i).getId_kecamatan().equals(kecamatan.get(j).getId())){
                        if(kecamatan.get(j).getId_kota().equals(kota.get(k).getId())){
                            String namaKelurahan = kota.get(k).getNama_kota()+
                                    " / " + kecamatan.get(j).getNama_kecamatan() +
                                    " / " + kelurahan.get(i).getNama_kelurahan();
                            kelurahan.get(i).setNama_kelurahan(namaKelurahan);
                        }
                    }
                }
            }
        }
        model.addAttribute("kelurahan", kelurahan);
		return "keluarga-add";
    }
	
	@RequestMapping("/keluarga/tambah/submit")
	public String addKeluargaSubmit (
			@ModelAttribute KeluargaModel keluarga, Model model)   
    {
		KelurahanModel kelurahan = kelurahanDAO.selectKelurahan(keluarga.getId_kelurahan());
        
        String nkk = kelurahan.getKode_kelurahan().substring(0, 6);
        
        String[] tanggal_isi = java.time.LocalDate.now().toString().split("-");
        nkk += tanggal_isi[2] + tanggal_isi[1] + tanggal_isi[0].substring(2);
        
        List<KeluargaModel> listNkkSama = keluargaDAO.listNkkSama(nkk + "%");
        if(listNkkSama.isEmpty()) {
        		nkk += "0001";
        }else {
	        	KeluargaModel temp = listNkkSama.get(listNkkSama.size()-1);
	    		String nkkTambahan = temp.getNomor_kk().substring(12);
	    		int temp2 = Integer.parseInt(nkkTambahan);
	    		temp2++;
	    		
	    		String temp3 = "" + temp2;
	    		for(int i = 0; i < 4-temp3.length(); i++) {
	    			nkk += "0";
	    		}
	    		nkk += temp3;
	    }
        
        keluarga.setNomor_kk(nkk);
        keluarga.setIs_tidak_berlaku("0");
        
        String rt = keluarga.getRT();
        String temp = "";
        for(int i = 0; i < 3-rt.length(); i++) {
			temp += "0";
		}
        rt = temp + rt;
        
        String rw = keluarga.getRW();
        String temp2 = "";
        for(int i = 0; i < 3-rw.length(); i++) {
			temp2 += "0";
		}
        rw = temp2 + rw;
        
        keluarga.setRT(rt);
        keluarga.setRW(rw);
        
        keluargaDAO.addKeluarga(keluarga);
        model.addAttribute("nkk", nkk);
        return "keluarga-add-success";
    }

	@RequestMapping("/keluarga/ubah/{nomor_kk}")
	public String ubah (Model model, @PathVariable(value="nomor_kk") String nomor_kk) {
		KeluargaModel keluarga = keluargaDAO.selectKeluarga(nomor_kk);
		
		if(keluarga == null) {
			model.addAttribute("errorMessage", "Keluarga dengan NKK " + nomor_kk + " tidak ditemukan, "
					+ "mohon cek kembali Nomor Kartu Keluarga Anda.");
			return "error/error404";
		}
		model.addAttribute("keluarga", keluarga);
		List<KelurahanModel> kelurahan = kelurahanDAO.selectAllKelurahan();
        List<KecamatanModel> kecamatan = kecamatanDAO.selectAllKecamatan();
        List<KotaModel> kota = kotaDAO.selectAllKota();
        for(int i = 0; i < kelurahan.size(); i++){
            for(int j = 0; j < kecamatan.size(); j++){
                for(int k = 0 ; k < kota.size(); k++){
                    if(kelurahan.get(i).getId_kecamatan().equals(kecamatan.get(j).getId())){
                        if(kecamatan.get(j).getId_kota().equals(kota.get(k).getId())){
                            String namaKelurahan = kota.get(k).getNama_kota()+
                                    " / " + kecamatan.get(j).getNama_kecamatan() +
                                    " / " + kelurahan.get(i).getNama_kelurahan();
                            kelurahan.get(i).setNama_kelurahan(namaKelurahan);
                        }
                    }
                }
            }
        }
        model.addAttribute("kelurahan", kelurahan);
        return "keluarga-update";
	}
	
	@RequestMapping(value="/keluarga/ubah/{nomor_kk}", method = RequestMethod.POST)
	public String ubahKeluargaSubmit(Model model, @ModelAttribute KeluargaModel keluarga) {
		
		KeluargaModel keluargaLama = keluargaDAO.selectKeluarga(keluarga.getNomor_kk());
		KelurahanModel kelurahan = kelurahanDAO.selectKelurahan(keluarga.getId_kelurahan());
		String nkkBaru = keluargaLama.getNomor_kk();
		
		//cek rt
		if(!keluarga.getRT().equals(keluargaLama.getRT())) {
			String rt = keluarga.getRT();
	        String temp = "";
	        for(int i = 0; i < 3-rt.length(); i++) {
				temp += "0";
			}
	        rt = temp + rt;
	        keluarga.setRT(rt);
		}
		
		//cek rw
		if(!keluarga.getRW().equals(keluargaLama.getRW())) {
	    		String rw = keluarga.getRW();
	    		String temp2 = "";
	        for(int i = 0; i < 3-rw.length(); i++) {
				temp2 += "0";
			}
	        rw = temp2 + rw;
	        keluarga.setRW(rw);
		}
		
		nkkBaru = kelurahan.getKode_kelurahan().substring(0,6);
		String[] tanggal_isi = java.time.LocalDate.now().toString().split("-");
        nkkBaru += tanggal_isi[2] + tanggal_isi[1] + tanggal_isi[0].substring(2);
        
        List<KeluargaModel> listNkkSama = keluargaDAO.listNkkSama(nkkBaru + "%");
        if(listNkkSama.isEmpty()) {
        		nkkBaru += "0001";
        }else {
	        	KeluargaModel temp = listNkkSama.get(listNkkSama.size()-1);
	    		String nkkTambahan = temp.getNomor_kk().substring(12);
	    		int temp2 = Integer.parseInt(nkkTambahan);
	    		temp2++;
	    		
	    		String temp3 = "" + temp2;
	    		for(int i = 0; i < 4-temp3.length(); i++) {
	    			nkkBaru += "0";
	    		}
	    		nkkBaru += temp3;
	    }
		keluarga.setNomor_kk(nkkBaru);
		keluarga.setId(keluargaLama.getId());
		keluarga.setIs_tidak_berlaku("0");
		
		List<PendudukModel> anggotaKeluarga = pendudukDAO.selectAnggotaKeluarga(keluarga.getId());
		if(!anggotaKeluarga.isEmpty()) {
			for(int i = 0; i < anggotaKeluarga.size(); i++) {
				PendudukModel penduduk = anggotaKeluarga.get(i);
				
				String nikTengah = penduduk.getNik().substring(6, 12);
				String nikBaru = "";
				String nikDepan = kelurahan.getKode_kelurahan().substring(0, 6);
				nikBaru = "" + nikDepan + nikTengah;
				
				List<PendudukModel> listNikSama = pendudukDAO.listNikSama(nikBaru + "%");
				if(listNikSama.isEmpty()) {
					nikBaru = nikBaru.substring(0, 12) + "0001";
				}else {
					PendudukModel temp = listNikSama.get(listNikSama.size()-1);
		        		String nikTambahan = temp.getNik().substring(12);
		        		int temp2 = Integer.parseInt(nikTambahan) + 1;
		        		String nol = "";
		        		String temp3 = "" + temp2;
		        		int counter = 4-temp3.length();
		        		for(int j = 0; j < counter; j++) {
		        			nol += "0";
		        		}
		        		nikBaru = nikBaru.substring(0, 12) + nol + temp3;
		        		
				}
				penduduk.setNik(nikBaru);
				pendudukDAO.updatePenduduk(penduduk);
			}
		}
		model.addAttribute("nkkLama", keluargaLama.getNomor_kk());
		keluargaDAO.updateKeluarga(keluarga);
		return "keluarga-update-success";
		
	}
}