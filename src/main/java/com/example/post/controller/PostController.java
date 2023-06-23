package com.example.post.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "index";
	}

	@RequestMapping(value = "/created", method = RequestMethod.GET)
	public String created() {
		return "bbs/created";
	}

	@RequestMapping(value = "/created", method = RequestMethod.POST)
	public String createdOK(Post post, HttpServletRequest request, Model model) {
		try {
			int maxNum = postService.maxNum();

			post.setNum(maxNum + 1);
			postService.insertData(post);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/list";
	}

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(Post post, HttpServletRequest request, Model model) {
		try {
			String pageNum = request.getParameter("pageNum");
			int currentPage = 1;

			if (pageNum != null)
				currentPage = Integer.parseInt(pageNum);
			String searchKey = request.getParameter("searchKey");
			String searchValue = request.getParameter("searchValue");

			if (searchValue == null) {
				searchKey = "category";
				searchValue = "";
			} else {
				if (request.getMethod().equalsIgnoreCase("GET")) {
					searchValue = URLDecoder.decode(searchValue, "UTF-8");
				}
			}

			int dataCount = postService.getDataCount(searchKey, searchValue);

			int numPerPage = 5;
			int totalPage = myUtil.getPageCount(numPerPage, dataCount);

			if (currentPage > totalPage) {
				currentPage = totalPage;
			}

			int start = (currentPage - 1) * numPerPage + 1;
			int end = currentPage * numPerPage;

			List<Post> lists = postService.getLists(searchKey, searchValue, start, end);

			String param = "";

			if (searchValue != null && !searchValue.equals("")) {
				param = "searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			}

			String listUrl = "/list";

			if (!param.equals(""))
				listUrl += "?" + param;

			String pageIndexList = myUtil.pageIndexList(currentPage, totalPage, listUrl);

			String articleUrl = "/article?pageNum=" + currentPage;

			if (!param.equals("")) {
				articleUrl += "&" + param;
			}

			model.addAttribute("lists", lists);
			model.addAttribute("articleUrl", articleUrl);
			model.addAttribute("pageIndexList", pageIndexList);
			model.addAttribute("dataCount", dataCount);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "bbs/list";

	}

	@RequestMapping(value = "/article", method = { RequestMethod.GET, RequestMethod.POST })
	public String article(HttpServletRequest request, Model model) {
		try {
			int num = Integer.parseInt(request.getParameter("num"));
			String pageNum = request.getParameter("pageNum");

			String searchKey = request.getParameter("searchKey");
			String searchValue = request.getParameter("searchValue");

			if (searchValue != null) {
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}

			postService.updateViews(num);

			Post post = postService.getReadData(num);

			if (post == null) {
				return "redirect:/list?pageNum=" + pageNum;
			}

			String param = "pageNum=" + pageNum;
			if (searchValue != null && !searchValue.equals("")) {

				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			}

			model.addAttribute("post", post);
			model.addAttribute("params", param);
			model.addAttribute("pageNum", pageNum);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "bbs/article";

	}

	@RequestMapping(value = "/updated", method = { RequestMethod.GET, RequestMethod.POST })
	public String updated(HttpServletRequest request, Model model) throws Exception {
		int num = Integer.parseInt(request.getParameter("num"));

		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");

		if (searchValue != null) {
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		}

		Post post = postService.getReadData(num);

		if (post == null) {
			return "redirect:/list?pageNum=" + pageNum;
		}

		String param = "pageNum=" + pageNum;

		if (searchValue != null && !searchValue.equals("")) {
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}

		model.addAttribute("post", post);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("params", param);
		model.addAttribute("searchKey", searchKey);
		model.addAttribute("searchValue", searchValue);

		return "bbs/updated";

	}

	@RequestMapping(value = "/updated_ok", method = { RequestMethod.GET, RequestMethod.POST })
	public String updatedOK(Post post, HttpServletRequest request, Model model) {
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		String param = "?pageNum=" + pageNum;

		try {
			post.setContent(post.getContent().replaceAll("<br/>", "\r\n"));
			postService.updateData(post);

			if (searchValue != null && !searchValue.equals("")) {
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/list" + param;

	}

	@RequestMapping(value = "/deleted_ok", method = { RequestMethod.GET, RequestMethod.POST })
	public String deleteOK(HttpServletRequest request, Model model) {
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		String param = "?pageNum=" + pageNum;

		try {
			String searchKey = request.getParameter("searchKey");
			String searchValue = request.getParameter("searchValue");
			postService.deleteData(num);

			if (searchValue != null && !searchValue.equals("")) {
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/list" + param;

	}
}
