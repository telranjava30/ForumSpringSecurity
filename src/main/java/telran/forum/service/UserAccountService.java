package telran.forum.service;

import java.util.Set;

import telran.forum.dto.UserEditDto;
import telran.forum.dto.UserProfileDto;
import telran.forum.dto.UserRegisterDto;

public interface UserAccountService {
	
	UserProfileDto register(UserRegisterDto userRegisterDto);
	
	UserProfileDto login(String login);
	
	UserProfileDto editUser(String login, UserEditDto userEditDto);
	
	UserProfileDto removeUser(String login);
	
	void changePassword(String login, String password);
	
	Set<String> addRole(String login, String role);
	
	Set<String> removeRole(String login, String role);

}
