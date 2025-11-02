package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import entity.User;
import repository.UserRepository;

public class CustomUserDetailsService implements UserDetailsService{
	@Autowired
	private UserRepository userRepositoryy;
	

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		
			User user;
			try {
				user = UserRepository.findByUsername(username)
						.orElseThrow(()-> new UsernameNotFoundException("User not found: "+ username));
			} catch (UsernameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return user;
	}
	
	
	
}
