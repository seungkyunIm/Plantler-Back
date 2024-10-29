package com.plantler.service;

import org.springframework.http.ResponseEntity;

import com.plantler.dto.CommentDTO;
import com.plantler.dto.RequestBoardDTO;
import com.plantler.dto.RequestBoardFileDTO;
import com.plantler.dto.ResultDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface FreeBoardService {

	public ResponseEntity<?> freeview(int file_no);
	public ResultDTO list(RequestBoardDTO requestBoardDTO);
	public ResultDTO freeBoardDetail(int board_no, HttpServletRequest request);
	public ResultDTO addComment(CommentDTO commentDTO, HttpServletRequest req);
	public ResultDTO updateComment(CommentDTO commentDTO, HttpServletRequest req);
	public ResultDTO deleteComment(CommentDTO commentDTO, HttpServletRequest req);
	public ResultDTO freeboardadd(RequestBoardFileDTO requestBoardFileDTO, HttpServletRequest request);
	public ResultDTO freeBoardUpdate(RequestBoardFileDTO requestBoardFileDTO, HttpServletRequest request);
	public ResultDTO deleteBoard(int board_no, HttpServletRequest request);
	public ResultDTO freeboardaddlike(int board_no, HttpServletRequest request);
	public ResultDTO freeboardaddcount(int board_no);
	
}
