package com.example.post.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.post.dto.Post;
import com.example.post.service.PostService;
import com.example.post.util.MyUtil;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PostController {
	@Autowired
	private PostService postService;

	@Autowired
	MyUtil myUtil;

	@RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(Post post, HttpServletRequest request, Model model) {

		try {
			List<Post> top3 = postService.getTop3Data();
			model.addAttribute("top3", top3);
			System.out.println(top3);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "index";
	}

	@RequestMapping(value = "/created", method = RequestMethod.GET)
	public String created() {
		return "bbs/created";
	}

}
