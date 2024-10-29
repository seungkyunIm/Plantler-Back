package com.plantler.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.plantler.dto.CommentDTO;
import com.plantler.dto.RequestBoardDTO;
import com.plantler.dto.RequestBoardFileDTO;
import com.plantler.dto.ResultDTO;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name="윤신2가 만든 컨트롤러", description = "사실 메인은 윤은이가 했으니 물어봐요!")
public interface FreeBoardControllerDoc {

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
