package com.plantler.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.plantler.dto.CommentDTO;
import com.plantler.dto.ResultDTO;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

public interface KhBoardService {
	
	public ResponseEntity<?> view(int file_no);
	public ResultDTO list(Map<String, Object> paramMap);
	public ResultDTO khBoardDetail(int board_no, HttpServletRequest request);
	
	public ResultDTO addComment(CommentDTO commentDTO, HttpServletRequest req);
	public ResultDTO updateComment(CommentDTO commentDTO, HttpServletRequest req);
	public ResultDTO deleteComment(CommentDTO commentDTO, HttpServletRequest req);
	
	public ResultDTO khboardRegister(
			MultipartFile multipartFile, 
			String board_title, 
			String board_content, 
			int category_id,
			HttpServletRequest request);
	public ResultDTO khBoardUpdate(
			int board_no,
	        String board_title,
	        String board_content,
	        int category_id,
	        MultipartFile multipartFile,
	        HttpServletRequest request);
	public ResultDTO deleteBoard(int board_no, HttpServletRequest request);
	public ResultDTO khboardaddlike(int board_no, HttpServletRequest request);
	public ResultDTO khboardaddcount(int board_no);
	
}
