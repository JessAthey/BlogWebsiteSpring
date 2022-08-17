package com.techtalentsouth.techtalentblog.blogpost;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

//You use @RestController if you want a controller for a REST API.
//Anything you return, is typically displayed directly.
//If you return a string, that will become your web page.
//if you return an object, it will JSONify that object and return JSON

@Controller // Make this BlogPostController class into a controller
            // class that is scanned for web mappings/endpoints.
public class BlogPostController {
	@Autowired // tell springboot to setup the blogPostRepository.
	BlogPostRepository blogPostRepository;
	
	@GetMapping(path="/")
	public String index(Model model) {
		List<BlogPost> posts = blogPostRepository.findAll();
			
		model.addAttribute("posts", posts);				
		return "blogpost/index";				
	}
	
	@GetMapping(value="/blogposts/new")
	public String newBlog(Model model) {
		BlogPost blogPost = new BlogPost();	
		//blogPost.setAuthor("Scott Dossey"); //This would set a default form value
		
		model.addAttribute("blogPost", blogPost);
		return "blogpost/new";
	}
	
	
	@PostMapping(path="/blogposts")
	public String addNewBlogPost(BlogPost blogPost, Model model) {
		BlogPost dbBlogPost = blogPostRepository.save(blogPost);		
				
		model.addAttribute("blogPost", dbBlogPost);		
		return "blogpost/result";					
	}
	
	//In order to edit a post, we are  going to add a controller
	//method to edit a single post.... and we are going to identify
	//which post is to be edited via specifying the id of the post
	//in the url...and the id will specify the primary key
	//that we will look up in the database....
	//"/blogposts/{id}"
	@GetMapping(path="/blogposts/{id}")
	public String editPostWithId(@PathVariable Long id, Model model) {
		Optional<BlogPost> post = blogPostRepository.findById(id);
		if (post.isPresent()) {
			BlogPost actualPost = post.get();
			model.addAttribute("blogPost", actualPost);			
		}	
		return "blogpost/edit";
	}
	
	@PostMapping(path="/blogposts/{id}")
	public String updateExistingPost(@PathVariable Long id,
			                         BlogPost blogPost,
			                         Model model) {
	
		Optional<BlogPost> post = blogPostRepository.findById(id);
		if (post.isPresent()) {
			BlogPost actualPost = post.get();
			actualPost.setTitle(blogPost.getTitle());
			actualPost.setAuthor(blogPost.getAuthor());
			actualPost.setBlogEntry(blogPost.getBlogEntry());
			blogPostRepository.save(actualPost);
			model.addAttribute("blogPost", actualPost);			
		}
		return "blogpost/result";
	}
	
	
	@GetMapping(path="/blogposts/delete/{id}")
	public String deleteByPostById(@PathVariable Long id) {
		blogPostRepository.deleteById(id);
		return "blogpost/delete";
	}	

}
