package com.plantler.docs;

import org.springframework.http.ResponseEntity;

import com.plantler.dto.CommentDTO;
import com.plantler.dto.RequestBoardDTO;
import com.plantler.dto.RequestBoardFileDTO;
import com.plantler.dto.ResultDTO;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name="식집사 게시판 컨트롤러", description = "키우기 노하우 & 식물 무료 나눔 게시판 (Dev: 박윤신, 장윤은)")
public interface BoardControllerDoc {

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
