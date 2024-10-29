package com.plantler.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plantler.docs.BoardControllerDoc;
import com.plantler.dto.CommentDTO;
import com.plantler.dto.RequestBoardDTO;
import com.plantler.dto.RequestBoardFileDTO;
import com.plantler.dto.ResultDTO;
import com.plantler.service.BoardService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BoardController implements BoardControllerDoc {

	private final BoardService boardService;
	
	@GetMapping("/view")
	public ResponseEntity<?> view(@RequestParam("file_no") int file_no) {
		return boardService.view(file_no);
	}

	// 게시판 목록 + 랭킹 포함
	@PostMapping("/boardlist")
	public ResultDTO boardList(@RequestBody RequestBoardDTO requestBoardDTO) {
		return boardService.boardList(requestBoardDTO);
	}

	// 게시판 글 등록 (파일 포함) -----------------------------------------------------------------	
	@PostMapping("/boardadd")
	public ResultDTO boardAdd(@ModelAttribute RequestBoardFileDTO requestBoardFileDTO, HttpServletRequest request) {
		return boardService.boardAdd(requestBoardFileDTO, request);
	}

	// 게시판 글 수정
	@PostMapping("/boardupdate")
	public ResultDTO boardUpdate(@ModelAttribute RequestBoardFileDTO requestBoardFileDTO, HttpServletRequest request) {
		return boardService.boardUpdate(requestBoardFileDTO, request);
	}

	// 게시글 상세페이지
	@PostMapping("/boarddetail/{board_no}")
	public ResultDTO boardDetail(@PathVariable("board_no") int board_no, HttpServletRequest request) {
		return boardService.boardDetail(board_no, request);
	}

	// 게시글 삭제
	@PostMapping("/boarddelete/{board_no}")
	public ResultDTO boardDelete(@PathVariable("board_no") int board_no, HttpServletRequest request) {
		return boardService.boardDelete(board_no, request);
	}

	// 게시글 좋아요 추가
	@PostMapping("/boardaddlike/{board_no}")
	public ResultDTO boardAddLike(@PathVariable("board_no") int board_no, HttpServletRequest request) {
		return boardService.boardAddLike(board_no, request);
	}

	// 조회수 증가
	@PostMapping("/boardaddcount/{board_no}")
	public ResultDTO boardAddCount(@PathVariable("board_no") int board_no) {
		return boardService.boardAddCount(board_no);
	}

	//댓글 추가
	@PostMapping("/boarddetail/comment/add")
	public ResultDTO commentAdd(@RequestBody CommentDTO commentDTO, HttpServletRequest req) {
		return boardService.commentAdd(commentDTO, req);
	}

	// 댓글 수정
	@PostMapping("/boarddetail/comment/update")
	public ResultDTO commentUpdate(@RequestBody CommentDTO commentDTO, HttpServletRequest req) {
		return boardService.commentUpdate(commentDTO, req);
	}

	// 댓글 삭제
	@PostMapping("/boarddetail/comment/delete")
	public ResultDTO commentDelete(@RequestBody CommentDTO commentDTO, HttpServletRequest req) {
		return boardService.commentDelete(commentDTO, req);
	}

}
