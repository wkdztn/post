package com.example.post.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.post.dao.PostDao;
import com.example.post.dto.Post;

@Service
public class PostServiceImpl implements PostService {
	@Autowired
	PostDao postMapper;

	@Override
	public int maxNum() throws Exception {
		return postMapper.maxNum();
	}

	@Override
	public void insertData(Post post) throws Exception {
		postMapper.insertData(post);
	}

	@Override
	public int getDataCount(String searchKey, String searchValue) throws Exception {
		return postMapper.getDataCount(searchKey, searchValue);
	}

	@Override
	public List<Post> getLists(String searchKey, String searchValue, int start, int end) throws Exception {
		return postMapper.getLists(searchKey, searchValue, start, end);
	}

	@Override
	public void updateViews(int num) throws Exception {
		postMapper.updateViews(num);
	}

	@Override
	public Post getReadData(int num) throws Exception {
		return postMapper.getReadData(num);
	}

	@Override
	public void updateData(Post post) throws Exception {
		postMapper.updateData(post);
	}

	@Override
	public void deleteData(int num) throws Exception {
		postMapper.deleteData(num);
	}

	@Override
	public List<Post> getTop3Data() {
		return postMapper.getTop3Data();
	}

}
