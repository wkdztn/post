package com.example.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Post {
	private int num;
	private String category;
	private String tit;
	private String content;
	private String address;
	private String price;
	private String img;
	private String reg_date;
	private int views;
}
