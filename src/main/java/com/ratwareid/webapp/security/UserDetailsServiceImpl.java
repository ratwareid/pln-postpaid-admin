package com.ratwareid.webapp.security;


import com.ratwareid.webapp.model.Pelanggan;
import com.ratwareid.webapp.model.User;
import com.ratwareid.webapp.repository.PelangganRepository;
import com.ratwareid.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Komentar atau dokumentasi diketik disini
 */
public class UserDetailsServiceImpl implements UserDetailsService {

	//Atau bisa juga dengan seperti ini
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PelangganRepository pelangganRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User administrator = userRepository.getUserByUsername(username);
		if(administrator!=null) {
			return new MyAdminDetails(administrator);
		}else{
			Pelanggan user = pelangganRepository.getUserByUsername(username);
			if (user != null) {
				return new MyPelangganDetail(user);
			}else {
				throw new UsernameNotFoundException("Pengguna dengan username "+username+" tidak ditemukan!");
			}
		}
	}

}
