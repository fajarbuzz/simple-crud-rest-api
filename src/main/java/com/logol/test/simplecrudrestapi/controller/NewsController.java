package com.logol.test.simplecrudrestapi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.logol.test.simplecrudrestapi.model.News;
import com.logol.test.simplecrudrestapi.service.NewsService;

@RestController
@RequestMapping(path="/news")
public class NewsController {
	
	@Autowired
	NewsService newsService;
	
	@PostMapping(value = "/create")
	public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> params) {
		Map<String, Object> paramsMap = new HashMap<>();
		boolean status = false;
		HttpStatus statusCode = HttpStatus.BAD_REQUEST;
		String id = null;
		JSONArray message = new JSONArray();
		if (params.containsKey("params")) {
			if (params.get("params") != null) {
				if (!params.get("params").toString().isEmpty()) {
					paramsMap = (Map<String, Object>) params.get("params");
				} else {
					message.put("params data is empty.");
				}
			} else {
				message.put("params data is null.");
			}
		} else {
			message.put("params data does not exist.");
		}
		if (paramsMap != null) {
			String title = null;
			String description = null;
			if (paramsMap.containsKey("title")) {
				if (paramsMap.get("title") != null) {
					if (!paramsMap.get("title").toString().isEmpty()) {
						title = paramsMap.get("title").toString();
					} else {
						message.put("field title is empty.");
					}
				} else {
					message.put("field title is null.");
				}
			} else {
				message.put("field title does not exist.");
			}
			if (paramsMap.containsKey("description")) {
				if (paramsMap.get("description") != null) {
					if (!paramsMap.get("description").toString().isEmpty()) {
						description = paramsMap.get("description").toString();
					} else {
						message.put("field description is empty.");
					}
				} else {
					message.put("field description is null.");
				}
			} else {
				message.put("field description does not exist.");
			}
			if (title != null && description != null) {
				id = newsService.insertNews(title, description, message, status);
				if (id != null) {
					statusCode = HttpStatus.OK;
					status = true;
					message.put("success creating news : "+title);
				} else {
					statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
				}
			}
		}
		Map<String, Object> response = new HashMap<>();
		response.put("status", status);
		response.put("id", id);
		response.put("message", message.toString());
		return new ResponseEntity<Map<String, Object>>(response, statusCode);
	}
	
	@GetMapping(value = "/read")
	public ResponseEntity<Map<String, Object>> read(@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "description", required = false) String description) {
		boolean status = false;
		HttpStatus statusCode = HttpStatus.BAD_REQUEST;
		JSONArray message = new JSONArray();
		String titleParam = null;
		if (title == null) {
			titleParam = "";
		} else {
			titleParam = title;
		}
		String descriptionParam = null;
		if (description == null) {
			descriptionParam = "";
		} else {
			descriptionParam =  description;
		}
		List<News> newsList = newsService.getNewsList(titleParam, descriptionParam);
		status = true;
		if (newsList.size() > 0) {
			statusCode = HttpStatus.OK;
		} else {
			message.put("no data available.");
			statusCode = HttpStatus.NO_CONTENT;
		}
		Map<String, Object> response = new HashMap<>();
		response.put("data", newsList);
		response.put("status", status);
		response.put("message", message.toString());
		return new ResponseEntity<Map<String, Object>>(response, statusCode);
	}
	
	@PostMapping(value = "/update")
	public ResponseEntity<Map<String, Object>> update(@RequestBody Map<String, Object> params) {
		Map<String, Object> paramsMap = new HashMap<>();
		boolean status = false;
		HttpStatus statusCode = HttpStatus.BAD_REQUEST;
		JSONArray message = new JSONArray();
		if (params.containsKey("params")) {
			if (params.get("params") != null) {
				if (!params.get("params").toString().isEmpty()) {
					paramsMap = (Map<String, Object>) params.get("params");
				} else {
					message.put("params data is empty.");
				}
			} else {
				message.put("params data is null.");
			}
		} else {
			message.put("params data does not exist.");
		}
		if (paramsMap != null) {
			String title = null;
			String description = null;
			String id = null;
			if (paramsMap.containsKey("title")) {
				if (paramsMap.get("title") != null) {
					if (!paramsMap.get("title").toString().isEmpty()) {
						title = paramsMap.get("title").toString();
					} else {
						message.put("field title is empty.");
					}
				} else {
					message.put("field title is null.");
				}
			} else {
				message.put("field title does not exist.");
			}
			if (paramsMap.containsKey("description")) {
				if (paramsMap.get("description") != null) {
					if (!paramsMap.get("description").toString().isEmpty()) {
						description = paramsMap.get("description").toString();
					} else {
						message.put("field description is empty.");
					}
				} else {
					message.put("field description is null.");
				}
			} else {
				message.put("field description does not exist.");
			}
			if (paramsMap.containsKey("id")) {
				if (paramsMap.get("id") != null) {
					if (!paramsMap.get("id").toString().isEmpty()) {
						id = paramsMap.get("id").toString();
					} else {
						message.put("field id is empty.");
					}
				} else {
					message.put("field id is null.");
				}
			} else {
				message.put("field id does not exist.");
			}
			if (title != null && description != null && id != null) {
				status = newsService.updateNews(title, description, id, message);
				if (status) {
					statusCode = HttpStatus.OK;
					message.put("success updating news with id : "+id);
				} else {
					statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
				}
			}
		}
		Map<String, Object> response = new HashMap<>();
		response.put("status", status);
		response.put("message", message.toString());
		return new ResponseEntity<Map<String, Object>>(response, statusCode);
	}
	
	@PostMapping(value = "/delete")
	public ResponseEntity<Map<String, Object>> delete(@RequestBody Map<String, Object> params) {
		String id = null;
		boolean status = false;
		HttpStatus statusCode = HttpStatus.BAD_REQUEST;
		JSONArray message = new JSONArray();
		if (params.containsKey("id")) {
			if (params.get("id") != null) {
				if (!params.get("id").toString().isEmpty()) {
					id = (String) params.get("id");
				} else {
					message.put("field id is empty.");
				}
			} else {
				message.put("field id is null.");
			}
		} else {
			message.put("field id does not exist.");
		}
		if (id != null) {
			status = newsService.deleteNews(id, message);
			if (status) {
				statusCode = HttpStatus.OK;
				message.put("success deleting news with id : "+id);
			} else {
				statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		}
		Map<String, Object> response = new HashMap<>();
		response.put("status", status);
		response.put("message", message.toString());
		return new ResponseEntity<Map<String, Object>>(response, statusCode);
	}
}
