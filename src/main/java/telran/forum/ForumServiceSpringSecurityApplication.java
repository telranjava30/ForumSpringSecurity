package telran.forum;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import telran.forum.dao.UserAccountRepository;
import telran.forum.model.UserAccount;

@SpringBootApplication
public class ForumServiceSpringSecurityApplication implements CommandLineRunner{

	@Autowired
	UserAccountRepository accountRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(ForumServiceSpringSecurityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(!accountRepository.existsById("admin")) {
			String hashPassword = passwordEncoder.encode("admin"); 
			UserAccount admin = UserAccount.builder()
					.login("admin")
					.password(hashPassword)
					.firstName("Super")
					.lastName("Admin")
					.role("User")
					.role("Moderator")
					.role("Administrator")
					.expDate(LocalDateTime.now().plusYears(25))
					.build();
			accountRepository.save(admin);
		}
	}

}
