package telran.forum.controller;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.forum.dto.UserEditDto;
import telran.forum.dto.UserProfileDto;
import telran.forum.dto.UserRegisterDto;
import telran.forum.service.UserAccountService;

@RestController
@RequestMapping("/account")
public class UserAccountController {
	@Autowired
	UserAccountService userAccountService;
	
	@PostMapping("/user")
	public UserProfileDto register(@RequestBody UserRegisterDto userRegisterDto) {
		return userAccountService.register(userRegisterDto);
	}
	
	@PostMapping("/login")
	public UserProfileDto login(Authentication authentication) {
		return userAccountService.login(authentication.getName());
	}
	
	@DeleteMapping("/user")
	public UserProfileDto removeUser(Authentication authentication) {
		return userAccountService.removeUser(authentication.getName());
	}
	
	@PutMapping("/user")
	public UserProfileDto editUser(@RequestBody UserEditDto userEditDto, Authentication authentication) {
		return userAccountService.editUser(authentication.getName(), userEditDto);
	}
	
	@PostMapping("/user/{login}/role/{role}")
	public Set<String> addRole(@PathVariable String login, @PathVariable String role){
		return userAccountService.addRole(login, role);
	}
	
	@DeleteMapping("/user/{login}/role/{role}")
	public Set<String> removeRole(@PathVariable String login, @PathVariable String role){
		return userAccountService.removeRole(login, role);
	}
	
	@PutMapping("/user/password")
	public void changePassword(Authentication authentication, @RequestHeader("X-Password") String password) {
		userAccountService.changePassword(authentication.getName(), password);
	}
	


}
