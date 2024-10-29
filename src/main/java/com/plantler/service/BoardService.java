package com.plantler.service;

import org.springframework.http.ResponseEntity;

import com.plantler.dto.CommentDTO;
import com.plantler.dto.RequestBoardDTO;
import com.plantler.dto.RequestBoardFileDTO;
import com.plantler.dto.ResultDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface BoardService {

	public ResponseEntity<?> view(int file_no);
	
	public ResultDTO boardList(RequestBoardDTO requestBoardDTO);
	public ResultDTO boardAdd(RequestBoardFileDTO requestBoardFileDTO, HttpServletRequest request);
	
	public ResultDTO boardUpdate(RequestBoardFileDTO requestBoardFileDTO, HttpServletRequest request);
	public ResultDTO boardDetail(int board_no, HttpServletRequest request);
	public ResultDTO boardDelete(int board_no, HttpServletRequest request);
	public ResultDTO boardAddLike(int board_no, HttpServletRequest request);
	public ResultDTO boardAddCount(int board_no);
	public ResultDTO commentAdd(CommentDTO commentDTO, HttpServletRequest req);
	public ResultDTO commentUpdate(CommentDTO commentDTO, HttpServletRequest req);
	public ResultDTO commentDelete(CommentDTO commentDTO, HttpServletRequest req);
	
}
