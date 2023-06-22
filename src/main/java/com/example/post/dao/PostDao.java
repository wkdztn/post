package com.example.post.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.example.post.dto.Post;

@Mapper
public interface PostDao {
	public int maxNum() throws Exception;

	public void insertData(Post post) throws Exception;

	public int getDataCount(String searchKey, String searchValue) throws Exception;

	public List<Post> getLists(String searchKey, String searchValue, int start, int end) throws Exception;

	public List<Post> getTop3Data();

	public void updateViews(int num) throws Exception;

	public Post getReadData(int num) throws Exception;

	public void updateData(Post post) throws Exception;

	public void deleteData(int num) throws Exception;
}
