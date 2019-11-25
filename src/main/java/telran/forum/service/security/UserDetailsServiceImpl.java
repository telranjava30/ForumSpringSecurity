package telran.forum.service.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import telran.forum.dao.UserAccountRepository;
import telran.forum.model.UserAccount;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	UserAccountRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount userAccount = repository.findById(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));
		String password = userAccount.getPassword();
		Set<String> setRoles = userAccount.getRoles()
				.stream()
				.map(r -> "ROLE_"+r.toUpperCase())
				.collect(Collectors.toSet());
		return new User(username, password,
				AuthorityUtils.createAuthorityList(setRoles.toArray(new String[setRoles.size()])));
	}

}
