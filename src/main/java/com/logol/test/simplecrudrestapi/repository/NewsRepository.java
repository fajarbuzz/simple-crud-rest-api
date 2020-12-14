package com.logol.test.simplecrudrestapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.logol.test.simplecrudrestapi.model.News;

public interface NewsRepository extends JpaRepository<News, String>{
	News findNewsById(String id);
	
	String query = "select n from News n where lower(n.title) like %:title% and "
			+ "lower(n.description) like %:description% order by createdDate desc";
	@Query(query)
	List<News> newsList(@Param("title") String title, @Param("description") String description);
}
