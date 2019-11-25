package telran.forum.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import telran.forum.configuration.AccountConfiguration;
import telran.forum.dao.UserAccountRepository;
import telran.forum.dto.UserEditDto;
import telran.forum.dto.UserProfileDto;
import telran.forum.dto.UserRegisterDto;
import telran.forum.exceptions.UserExistsException;
import telran.forum.exceptions.UserNotFoundException;
import telran.forum.model.UserAccount;

@Service
public class UserAccountServiceImpl implements UserAccountService {
	
	@Autowired
	UserAccountRepository accountRepository;
	
	@Autowired
	AccountConfiguration accountConfiguration;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public UserProfileDto register(UserRegisterDto userRegisterDto) {
		if (accountRepository.existsById(userRegisterDto.getLogin())) {
			throw new UserExistsException();
		}
		String hashPassword = passwordEncoder.encode(userRegisterDto.getPassword());
		UserAccount userAccount = UserAccount.builder()
									.login(userRegisterDto.getLogin())
									.password(hashPassword)
									.firstName(userRegisterDto.getFirstName())
									.lastName(userRegisterDto.getLastName())
									.role("User")
									.expDate(LocalDateTime.now().plusDays(accountConfiguration.getExpPeriod()))
									.build();
		accountRepository.save(userAccount);
		return userAccountToUserProfileDto(userAccount);
	}
	
	private UserProfileDto userAccountToUserProfileDto(UserAccount userAccount) {
		return UserProfileDto.builder()
				.login(userAccount.getLogin())
				.firstName(userAccount.getFirstName())
				.lastName(userAccount.getLastName())
				.roles(userAccount.getRoles())
				.build();
	}

	@Override
	public UserProfileDto login(String login) {
		UserAccount userAccount = accountRepository.findById(login).get();		
		return userAccountToUserProfileDto(userAccount);
	}

	@Override
	public UserProfileDto editUser(String login, UserEditDto userEditDto) {
		UserAccount userAccount = accountRepository.findById(login).get();	
		if (userEditDto.getFirstName() != null) {
			userAccount.setFirstName(userEditDto.getFirstName());
		}
		if (userEditDto.getLastName() != null) {
			userAccount.setLastName(userEditDto.getLastName());
		}
		accountRepository.save(userAccount);
		return userAccountToUserProfileDto(userAccount);
	}

	@Override
	public UserProfileDto removeUser(String login) {
		UserAccount userAccount = accountRepository.findById(login).get();
		accountRepository.deleteById(login);
		return userAccountToUserProfileDto(userAccount);
	}

	@Override
	public void changePassword(String login, String password) {
		UserAccount userAccount = accountRepository.findById(login).get();
		String hashPassword = passwordEncoder.encode(password);
		userAccount.setPassword(hashPassword);
		userAccount.setExpDate(LocalDateTime.now().plusDays(accountConfiguration.getExpPeriod()));
		accountRepository.save(userAccount);
		
	}

	@Override
	public Set<String> addRole(String login, String role) {
		UserAccount userAccount = accountRepository.findById(login)
				.orElseThrow(() -> new UserNotFoundException(login));
		userAccount.addRole(role);
		accountRepository.save(userAccount);
		return userAccount.getRoles();
	}

	@Override
	public Set<String> removeRole(String login, String role) {
		UserAccount userAccount = accountRepository.findById(login)
				.orElseThrow(() -> new UserNotFoundException(login));
		userAccount.removeRole(role);
		accountRepository.save(userAccount);
		return userAccount.getRoles();
	}
	
}
