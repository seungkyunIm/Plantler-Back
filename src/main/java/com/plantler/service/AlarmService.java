package com.plantler.service;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import com.plantler.dto.ResultDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface AlarmService {

	public ResultDTO list(HttpServletRequest req);
	public ResultDTO addAlarm(Map<String, String> map, HttpServletRequest request);
	public ResultDTO alarmedit(@RequestBody Map<String, String> map, HttpServletRequest request);
	public ResultDTO alarmDelete(@RequestBody Map<String, String> map, HttpServletRequest request);
	
}
