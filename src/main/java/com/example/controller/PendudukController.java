package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.KecamatanModel;
import com.example.model.KeluargaModel;
import com.example.model.KelurahanModel;
import com.example.model.KotaModel;
import com.example.model.PendudukModel;
import com.example.service.KecamatanService;
import com.example.service.KeluargaService;
import com.example.service.KelurahanService;
import com.example.service.KotaService;
import com.example.service.PendudukService;

@Controller
public class PendudukController {
	
	@Autowired
	PendudukService pendudukDAO;
	@Autowired
	KeluargaService keluargaDAO;
	@Autowired
	KelurahanService kelurahanDAO;
	@Autowired
	KecamatanService kecamatanDAO;
	@Autowired
	KotaService kotaDAO;
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/penduduk")
	public String pendudukViewNIK(Model model, @RequestParam(value="nik", required=true) String nik) {
		PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);
		if (penduduk == null) {
			model.addAttribute("errorMessage", "Penduduk dengan NIK " + nik + " tidak ditemukan");
			return "error/error404";	
		} else {
			KeluargaModel keluarga = keluargaDAO.selectKeluargaById(penduduk.getId_keluarga());
	        KelurahanModel kelurahan = kelurahanDAO.selectKelurahan(keluarga.getId_kelurahan());
	        KecamatanModel kecamatan = kecamatanDAO.selectKecamatan(kelurahan.getId_kecamatan());
	        KotaModel kota = kotaDAO.selectKota(kecamatan.getId_kota());
			
			model.addAttribute("penduduk", penduduk);
			model.addAttribute("keluarga", keluarga);
			model.addAttribute("kelurahan", kelurahan);
			model.addAttribute("kecamatan", kecamatan);
			model.addAttribute("kota", kota);
			return "penduduk-detail";
		}
	}
	
	@RequestMapping("/penduduk/tambah")
    public String add (Model model)
    {
        PendudukModel penduduk = new PendudukModel();
        model.addAttribute("penduduk", penduduk);
		return "penduduk-add";
    }
	
	@RequestMapping("/penduduk/tambah/submit")
	public String addPendudukSubmit (
			@ModelAttribute PendudukModel penduduk, Model model)   
    {
        KeluargaModel keluarga = keluargaDAO.selectKeluargaById(penduduk.getId_keluarga());
        KelurahanModel kelurahan = kelurahanDAO.selectKelurahan(keluarga.getId_kelurahan());
        KecamatanModel kecamatan = kecamatanDAO.selectKecamatan(kelurahan.getId_kecamatan());
        
        String kode = kecamatan.getKode_kecamatan();
        kode = kode.substring(0, 6);
        String nik = kode;
        
        String tanggal_lahir = penduduk.getTanggal_lahir();
        String[] tanggal_nik = tanggal_lahir.split("-");
        String tahun = tanggal_nik[0];
        String bulan = tanggal_nik[1];
        String tanggal = tanggal_nik[2];
        
        if(penduduk.getJenis_kelamin().equals("1")) {
        		int temp = Integer.parseInt(tanggal);
        		temp += 40;
        		tanggal = "" + temp;
        }
        
        nik += tanggal + bulan + tahun.substring(2);
        
        List<PendudukModel> listNikSama = pendudukDAO.listNikSama(nik + "%");
        if(listNikSama.isEmpty()) {
        		nik += "0001";
        }else {
        		PendudukModel temp = listNikSama.get(listNikSama.size()-1);
        		String nikTambahan = temp.getNik().substring(12);
        		int temp2 = Integer.parseInt(nikTambahan);
        		temp2++;
        		
        		String temp3 = "" + temp2;
        		for(int i = 0; i < 4-temp3.length(); i++) {
        			nik += "0";
        		}
        		nik += temp3;
        }
        
        penduduk.setNik(nik);
        pendudukDAO.addPenduduk(penduduk);
        model.addAttribute("nik", nik);
        return "penduduk-add-success";
    }

	@RequestMapping("/penduduk/ubah/{nik}")
	public String ubah (Model model, @PathVariable(value="nik") String nik) {
		PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);
		
		if(penduduk == null) {
			model.addAttribute("errorMessage", "Penduduk dengan NIK " + nik + " tidak ditemukan, "
					+ "mohon cek kembali Nomor Induk Kependudukan Anda.");
			return "error/error404";
		}else {
			model.addAttribute("penduduk", penduduk);
			return "penduduk-update";
		}
	}
	
	@RequestMapping(value="/penduduk/ubah/{nik}", method = RequestMethod.POST)
	public String ubahPendudukSubmit(Model model, @ModelAttribute PendudukModel penduduk) {
		PendudukModel pendudukLama = pendudukDAO.selectPenduduk(penduduk.getNik());
		String nikBaru = pendudukLama.getNik();
		
		//mengganti tanggal lahir
		if(!pendudukLama.getTanggal_lahir().equals(penduduk.getTanggal_lahir())) {
			String tanggal_lahir = penduduk.getTanggal_lahir();
	        String[] tgl_lahir = tanggal_lahir.split("-");
	        String tahun = tgl_lahir[0];
	        String bulan = tgl_lahir[1];
	        String tanggal = tgl_lahir[2];
	        int temp;
	        
	        //jenis kelamin perempuan
	        if(penduduk.getJenis_kelamin().equals("1")) {
	        		temp = Integer.parseInt(tanggal) + 40;
	        		tanggal = "" + temp;
	        }
	        nikBaru = nikBaru.substring(0,6) + tanggal + bulan + tahun + nikBaru.substring(12);
	        
		}else {
			//jenis kelamin di update
			if(!pendudukLama.getJenis_kelamin().equals(penduduk.getJenis_kelamin())) {
				int tanggal = Integer.parseInt(nikBaru.substring(6, 8));
				//mengubah menjadi laki-laki
				if(penduduk.getJenis_kelamin().equals("0")) {
					tanggal = tanggal-40;
				}else {
					tanggal += 40;
				}
				nikBaru = nikBaru.substring(0, 6) + tanggal + nikBaru.substring(8);
			}
		}
		
		if(!pendudukLama.getId_keluarga().equals(penduduk.getId_keluarga())) {
			KeluargaModel keluargaBaru = keluargaDAO.selectKeluargaById(penduduk.getId_keluarga());
	        KelurahanModel kelurahanBaru = kelurahanDAO.selectKelurahan(keluargaBaru.getId_kelurahan());
	        KecamatanModel kecamatanBaru = kecamatanDAO.selectKecamatan(kelurahanBaru.getId_kecamatan());
	        
	        String kodeLokasiBaru = kecamatanBaru.getKode_kecamatan().substring(0,6);
	        nikBaru = kodeLokasiBaru + nikBaru.substring(6);
		}
		
		if(!nikBaru.equals(pendudukLama.getNik())) {
			String nikDepan = nikBaru.substring(0, 12) + "%";
			List<PendudukModel> listNikSama = pendudukDAO.listNikSama(nikDepan + "%");
	        
			if(listNikSama.isEmpty()) {
	        		nikBaru = nikBaru.substring(0, 12) + "0001";
	        }else {
	        		PendudukModel temp = listNikSama.get(listNikSama.size()-1);
	        		String nikTambahan = temp.getNik().substring(12);
	        		int temp2 = Integer.parseInt(nikTambahan) + 1;
	        		String nol = "";
	        		String temp3 = "" + temp2;
	        		int counter = 4-temp3.length();
	        		for(int i = 0; i < counter; i++) {
	        			nol += "0";
	        		}
	        		nikBaru = nikBaru.substring(0, 12) + nol + temp3;
	        }
		}else {
			
		}
		penduduk.setNik(nikBaru);
		penduduk.setId(pendudukLama.getId());
		
		model.addAttribute("nikLama", pendudukLama.getNik());
		pendudukDAO.updatePenduduk(penduduk);
		return "penduduk-update-success";
		
	}
	
	@PostMapping("/penduduk/mati")
	public String nonAktifPenduduk(@RequestParam(value="nik", required = true) String nik, Model model){
		if(nik == null) {
			model.addAttribute("errorMessage", "Masukkan NIK");
			return "error/error404";
		}
		
		PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);
		
		if(penduduk == null) {
			model.addAttribute("errorMessage", "Nik tidak ditemukan");
			return "error/error404";
		}
		
		pendudukDAO.nonAktifkanPenduduk(nik);
		
		String id_keluarga = penduduk.getId_keluarga();
		KeluargaModel keluarga = keluargaDAO.selectKeluargaById(id_keluarga);
		List<PendudukModel> anggotaKeluarga = pendudukDAO.selectAnggotaKeluarga(id_keluarga);
		
		if(!anggotaKeluarga.isEmpty()) {
			int counter = 0;
			
			//hitung anggota keluarga yang sudah wafat
			for (int i = 0; i < anggotaKeluarga.size(); i++) {
				if(anggotaKeluarga.get(i).getIs_wafat().equals("1")) {
					counter++;
				}
			}
			
			//jika semua anggota keluarga sudah wafat, maka tidak berlaku
			if(anggotaKeluarga.size() == counter) {
				keluarga.setIs_tidak_berlaku("1");
			}
		}
		keluargaDAO.updateKeluarga(keluarga);
		model.addAttribute("nik", nik);
		return "penduduk-nonaktif-success";
	}
	
	@GetMapping("/penduduk/cari")
	public String cariPenduduk(@RequestParam(value="kt", required=false) String kt,
			@RequestParam(value="kc", required=false) String kc,
			@RequestParam(value="kl", required=false) String kl,
			Model model) {
		
		Boolean flag = false;
		
		//jika kelurahan tidak kosong
		if(kl != null) {
			model.addAttribute("kt_nama", kotaDAO.selectKota(kt).getNama_kota());
			model.addAttribute("kc_nama", kecamatanDAO.selectKecamatan(kc).getNama_kecamatan());
			model.addAttribute("kl_nama", kelurahanDAO.selectKelurahan(kl).getNama_kelurahan());
			model.addAttribute("flag_kelurahan", true);
			model.addAttribute("kl", kl);
			
			flag = true;
			List<PendudukModel> listPenduduk = pendudukDAO.selectPendudukByKelurahan(kl);
			
			PendudukModel termuda = listPenduduk.get(0);
			PendudukModel tertua = listPenduduk.get(0);
			
			for(int i = 0; i < listPenduduk.size(); i++) {
				if(termuda.getTanggal_lahir().compareTo(listPenduduk.get(i).getTanggal_lahir()) < 0) {
					termuda = listPenduduk.get(i);
				}
				if(tertua.getTanggal_lahir().compareTo(listPenduduk.get(i).getTanggal_lahir()) > 0) {
					tertua = listPenduduk.get(i);
				}
			}
			model.addAttribute("listPenduduk", listPenduduk);
			model.addAttribute("tertua", tertua);
			model.addAttribute("termuda", termuda);
			return "cari-penduduk-hasil";
		}
		
		//buat list kota
		List<KotaModel> listKota = kotaDAO.selectAllKota();
		model.addAttribute("listKota", listKota);
		
		//jika kota tidak kosong
		if(kt != null) {
			model.addAttribute("kt_nama", kotaDAO.selectKota(kt).getNama_kota());
			model.addAttribute("flag_kota", true);
			model.addAttribute("kt", kt);
			
			if(kc == null) {
				List<KecamatanModel> listKecamatan = kecamatanDAO.selectKecamatanByIdKota(kt);
				model.addAttribute("listKecamatan", listKecamatan);
			}
			
			if(kc != null) {
				model.addAttribute("kc_nama", kecamatanDAO.selectKecamatan(kc).getNama_kecamatan());
				model.addAttribute("flag_kecamatan", true);
				model.addAttribute("kc", kc);
				if(kl == null) {
					List<KelurahanModel> listKelurahan = kelurahanDAO.selectAllKelurahan(kc);
					model.addAttribute("listKelurahan", listKelurahan);
				}
			}
		}
		
		
		return "form-cari-penduduk";
	}
}