package com.plantler.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.plantler.docs.AlarmControllerDoc;
import com.plantler.dto.ResultDTO;
import com.plantler.service.AlarmServiceImp;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AlarmController implements AlarmControllerDoc {
	
	@Autowired
	private AlarmServiceImp alarmService;
		
	// 알림 목록
	@PostMapping("/alarmlist")
	public ResultDTO list(HttpServletRequest req) {
		return alarmService.list(req);
	}
	
	// 알림 등록
	@PostMapping("/alarmadd")
	public ResultDTO alarmadd(@RequestBody Map<String, String> map, HttpServletRequest request) {
        return alarmService.addAlarm(map, request);
	}
		
	// 알림 수정
	@PostMapping("/alarmedit")
	public ResultDTO alarmedit(@RequestBody Map<String, String> map, HttpServletRequest request) throws IOException {
		return alarmService.alarmedit(map, request);
	}
	
	// 알림 삭제
	@PostMapping("/delete")
	public ResultDTO alarmDelete(@RequestBody Map<String, String> map, HttpServletRequest request) throws IOException {			
		return alarmService.alarmDelete(map, request);
    }
}
