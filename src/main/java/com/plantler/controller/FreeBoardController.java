package com.plantler.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.plantler.docs.FreeBoardControllerDoc;
import com.plantler.dto.CommentDTO;
import com.plantler.dto.RequestBoardDTO;
import com.plantler.dto.RequestBoardFileDTO;
import com.plantler.dto.ResultDTO;
import com.plantler.service.FreeBoardService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
public class FreeBoardController implements FreeBoardControllerDoc {
	
	private final FreeBoardService freeBoardService;
	
	// 노하우 게시판에서는 "/view"였음
//	@GetMapping("/freeview")
    public ResponseEntity<?> freeview(@RequestParam("file_no") int file_no) {
		return freeBoardService.freeview(file_no);
    }
	
	// 게시판 목록 + 랭킹 포함
//	@PostMapping("/freeboardlist")
	public ResultDTO list(@RequestBody RequestBoardDTO requestBoardDTO) {
		return freeBoardService.list(requestBoardDTO);
	}
	
//	@PostMapping("/freeboarddetail/{board_no}")
	public ResultDTO freeBoardDetail(@PathVariable("board_no") int board_no, HttpServletRequest request) {
		return freeBoardService.freeBoardDetail(board_no, request);		
	}

	//댓글 추가
//	@PostMapping("/freeboarddetail/comment/add")
	public ResultDTO addComment(@RequestBody CommentDTO commentDTO, HttpServletRequest req) {
		return freeBoardService.addComment(commentDTO, req);
	}
		
	// 댓글 수정
//	@PostMapping("/freeboarddetail/comment/update")
	public ResultDTO updateComment(@RequestBody CommentDTO commentDTO, HttpServletRequest req) {
		return freeBoardService.updateComment(commentDTO, req);		
	}
		
	// 댓글 삭제
//	@PostMapping("/freeboarddetail/comment/delete")
	public ResultDTO deleteComment(@RequestBody CommentDTO commentDTO, HttpServletRequest req) {
		return freeBoardService.deleteComment(commentDTO, req);		
	}
	
	// 게시판 글 등록 (파일 포함) -----------------------------------------------------------------	
//	@PostMapping("/freeboardadd")
	public ResultDTO freeboardadd(@ModelAttribute RequestBoardFileDTO requestBoardFileDTO, HttpServletRequest request) {
		return freeBoardService.freeboardadd(requestBoardFileDTO, request);
	}

	// 게시판 글 수정
//	@PostMapping("/freeboardupdate")
	public ResultDTO freeBoardUpdate(@ModelAttribute RequestBoardFileDTO requestBoardFileDTO, HttpServletRequest request) {
		return freeBoardService.freeBoardUpdate(requestBoardFileDTO, request);
	}

	// 게시글 삭제
//	@PostMapping("/freeboarddelete/{board_no}")
	public ResultDTO deleteBoard(@PathVariable("board_no") int board_no, HttpServletRequest request) {
		return freeBoardService.deleteBoard(board_no, request);
	}
		
	// 게시글 좋아요 추가
//	@PostMapping("/freeboardaddlike/{board_no}")
	public ResultDTO freeboardaddlike(@PathVariable("board_no") int board_no, HttpServletRequest request) {
		return freeBoardService.freeboardaddlike(board_no, request);
	}
	
	// 조회수 증가
//	@PostMapping("/freeboardaddcount/{board_no}")
	public ResultDTO freeboardaddcount(@PathVariable("board_no") int board_no) {
		return freeBoardService.freeboardaddcount(board_no);
	}

}


