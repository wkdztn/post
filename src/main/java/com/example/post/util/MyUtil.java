package com.example.post.util;

import org.springframework.stereotype.Service;

@Service
public class MyUtil {
	public int getPageCount(int numPerPage, int dataCount) {

		int pageCount = 0;

		pageCount = dataCount / numPerPage;

		if (dataCount % numPerPage != 0) {
			pageCount++;

		}
		return pageCount;
	}

	public String pageIndexList(int currentPage, int totalPage, String listUrl) {
		// 문자열 데이터를 자주 추가하거나 삭제할때는 메모리 낭비의 방지를 위해 StringBuffer를 사용한다
		int numPerBlock = 5; // ◀이전 6 7 8 9 10 다음▶ 이전과 다음 사이의 숫자를 몇개를 표시할지
		int currentPageSetup; // ◀이전 버튼에 들어갈 값
		int page; // 그냥 페이지 숫자를 클릭했을때 들어갈 값

		StringBuffer sb = new StringBuffer();

		if (currentPage == 0 || totalPage == 0)
			return ""; // 0이면 데이터가 없다는 말. 빈값을 리턴한다.

		// 검색어가 있을 때: /list?searchKey=name&searchValue=춘식
		if (listUrl.indexOf("?") != -1) {
			// '?'가 들어있다면(쿼리스트링이 있다면)
			listUrl = listUrl + "&";
		} else { // 쿼리스트링이 없으면
			listUrl = listUrl + "?";
		}

		// 1. ◀이전 버튼 만들기
		// currentPage가(1~4) (5~9) (10~14) (15~19) 5 10 15.. 일때 currentPageSetup 5 10
		// 15..
		currentPageSetup = (currentPage / numPerBlock) * numPerBlock;

		// currentPage가 5 10 15 20.. 일때 currentPageSetup 0 5 10 15
		if (currentPage % numPerBlock == 0) {
			currentPageSetup = currentPageSetup - numPerBlock;
		}

		if(totalPage > numPerBlock && currentPageSetup > 0) {
			sb.append("<a href=\"" + listUrl + "pageNum=" + currentPageSetup + "\">◀이전</a>&nbsp;");
		}

		// 2. 그냥 페이지(6 7 8 9 10) 이동 버튼 만들기
		page = currentPageSetup + 1; // page는 1 6 11 16..

		while(page <= totalPage && page <= (currentPageSetup + numPerBlock)) {
			if (page == currentPage) {
				// 현재 내가 선택한 페이지라면
				sb.append("<font color=\"yellowgreen\">" + page + "</font>&nbsp;");
				// <font color = "green>9</font>&nbsp;
			} else {
				// 현재 선택한 페이지가 아니면
				sb.append("<a href=\"" + listUrl + "pageNum=" + page + "\">" + page + "</a>&nbsp;");
				// <a href="list?pageNum=7">7</a>&nbsp;
			}
			page++;
		}

		// 3. 다음▶ 버튼 만들기
		if (totalPage - currentPageSetup > numPerBlock) {
			sb.append("<a href=\"" + listUrl + "pageNum=" + page + "\">다음▶</a>&nbsp;");
		}
		// 4. 버튼 합쳐서 문자열로 리턴
		return sb.toString();
	}
}