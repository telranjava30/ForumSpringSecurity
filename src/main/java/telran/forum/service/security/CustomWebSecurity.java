package telran.forum.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import telran.forum.dao.ForumRepository;
import telran.forum.model.Post;

@Component("customSecurity")
public class CustomWebSecurity {
	
	@Autowired
	ForumRepository forumRepository;
	
	public boolean checkAuthorityForPost(String id, Authentication authentication) {
		Post post = forumRepository.findById(id).orElse(null);
		if(post == null) {
			return false;
		}
		return post.getAuthor().equals(authentication.getName()) 
				&& !authentication.getAuthorities().isEmpty();
	}

}
