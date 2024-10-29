package com.plantler.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.plantler.docs.KhBoardControllerDoc;
import com.plantler.dto.CommentDTO;
import com.plantler.dto.ResultDTO;
import com.plantler.service.KhBoardService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class KhBoardController implements KhBoardControllerDoc {
	
	private final KhBoardService khBoardService;
	
//	@GetMapping("/view")
    public ResponseEntity<?> view(@RequestParam("file_no") int file_no) {
		return khBoardService.view(file_no);
    }
	
	// 게시판 목록 + 랭킹 포함
//	@PostMapping("/khboardlist")
	public ResultDTO list(@RequestBody Map<String, Object> paramMap) {
		return khBoardService.list(paramMap);
	}
	
	// 게시글 상세페이지
//	@PostMapping("/khboarddetail/{board_no}")
	public ResultDTO khBoardDetail(@PathVariable("board_no") int board_no, HttpServletRequest request) {
		return khBoardService.khBoardDetail(board_no, request);
	}
	
	// 댓글 -------------------------------------------------------------------------------------------------	

	//댓글 추가
//	@PostMapping("/khboarddetail/comment/add")
	public ResultDTO addComment(@RequestBody CommentDTO commentDTO, HttpServletRequest req) {
		return khBoardService.addComment(commentDTO, req);			
	}
		
	// 댓글 수정
//	@PostMapping("/khboarddetail/comment/update")
	public ResultDTO updateComment(@RequestBody CommentDTO commentDTO, HttpServletRequest req) {
		return khBoardService.updateComment(commentDTO, req);
		
	}
		
	// 댓글 삭제
//	@PostMapping("/khboarddetail/comment/delete")
	public ResultDTO deleteComment(@RequestBody CommentDTO commentDTO, HttpServletRequest req) {
		return khBoardService.deleteComment(commentDTO, req);
	}
	
	// 게시판 글 등록 (파일 포함) -----------------------------------------------------------------	
//	@PostMapping("/khboardregister")
	public ResultDTO khboardRegister(@RequestParam(name="file", required = false) MultipartFile multipartFile, 
			@RequestParam("board_title") String board_title, 
			@RequestParam("board_content") String board_content, 
			@RequestParam("category_id") int category_id,
			HttpServletRequest request) {
		return khBoardService.khboardRegister(multipartFile, board_title, board_content, category_id, request);
	}

	// 게시판 글 수정
//	@PostMapping("/khboardupdate")
	public ResultDTO khBoardUpdate(
			@RequestParam("board_no") int board_no,
	        @RequestParam("board_title") String board_title,
	        @RequestParam("board_content") String board_content,
	        @RequestParam("category_id") int category_id,
	        @RequestParam(name="file", required = false) MultipartFile multipartFile,
	        HttpServletRequest request) {
		return khBoardService.khBoardUpdate(board_no, board_title, board_content, category_id, multipartFile, request);
	}

	// 게시글 삭제
//	@PostMapping("/khboarddelete/{board_no}")
	public ResultDTO deleteBoard(@PathVariable("board_no") int board_no, HttpServletRequest request) {
		return khBoardService.deleteBoard(board_no, request);
	}
		
	// 게시글 좋아요 추가
//	@PostMapping("/khboardaddlike/{board_no}")
	public ResultDTO khboardaddlike(@PathVariable("board_no") int board_no, HttpServletRequest request) {
		return khBoardService.khboardaddlike(board_no, request);
	}
		
	// 조회수 증가
//	@PostMapping("/khboardaddcount/{board_no}")
	public ResultDTO khboardaddcount(@PathVariable("board_no") int board_no) {
		return khBoardService.khboardaddcount(board_no);
	}
		
		
//		@GetMapping("/khTest")
//		public ResultDTO test(@RequestParam("comment_no") int comment_no) {
//			
//			boolean state = true;
//			
//			CommentDTO commentDTO = khbMapper.findByNo(comment_no);
//			if(commentDTO == null) { // 값이 없을때 
//				state = false;
//				//commentDTO = new CommentDTO();
//			}
//			
//			return ResultDTO.builder()
//					.state(state)
//					.result(commentDTO)
//					.build();
//		}
		
//		@Value("/upload + ${plantler.file.path}") String dir;
//		private String getRootPath() {return new File("").getAbsolutePath();}
		
		
		
		
	}


