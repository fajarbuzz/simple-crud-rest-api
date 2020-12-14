package com.logol.test.simplecrudrestapi.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.logol.test.simplecrudrestapi.model.News;
import com.logol.test.simplecrudrestapi.repository.NewsRepository;

@Service
public class NewsService {
	
	@Autowired
	NewsRepository newsRepository;
	
	public String insertNews(String title, String description, JSONArray message, boolean status) {
		String id = null;
		try {
			News news = new News();
			news.setTitle(title);
			news.setDescription(description);
			id = UUID.randomUUID().toString();
			news.setId(id);
			Date createdDate = new Date();
			news.setCreatedDate(createdDate);
			newsRepository.save(news);
			status = true;
		} catch (Exception e) {
			message.put(e.getMessage());
		}
		return id;
	}
	
	public List<News> getNewsList(String title, String description) {
		List<News> newsList = null;
		try {
			newsList = newsRepository.newsList(title, description);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newsList;
	}
	
	public boolean updateNews(String title, String description, String id, JSONArray message) {
		boolean status = false;
		try {
			News news = newsRepository.findNewsById(id);
			if (news != null) {
				news.setTitle(title);
				news.setDescription(description);
				newsRepository.save(news);
				status = true;
			} else {
				message.put("news data not found.");
			}
		} catch (Exception e) {
			message.put(e.getMessage());
		}
		return status;
	}
	
	public boolean deleteNews(String id, JSONArray message) {
		boolean status = false;
		try {
			News news = newsRepository.findNewsById(id);
			if (news != null) {
				newsRepository.delete(news);
				status = true;
			} else {
				message.put("news data not found.");
			}
		} catch (Exception e) {
			message.put(e.getMessage());
		}
		return status;
	}
}
