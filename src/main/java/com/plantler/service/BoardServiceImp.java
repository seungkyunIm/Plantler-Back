package com.plantler.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.plantler.auth.JwtToken;
import com.plantler.dto.BoardDTO;
import com.plantler.dto.BoardLikeDTO;
import com.plantler.dto.CategoryDTO;
import com.plantler.dto.CategoryParamDTO;
import com.plantler.dto.CommentDTO;
import com.plantler.dto.FileDTO;
import com.plantler.dto.RequestBoardDTO;
import com.plantler.dto.RequestBoardFileDTO;
import com.plantler.dto.RequestTokenDTO;
import com.plantler.dto.ResultDTO;
import com.plantler.mapper.BoardMapper;
import com.plantler.util.FileComponent;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardServiceImp implements BoardService {
	
	private final BoardMapper boardMapper;
	private final JwtToken jwtToken;
	private final FileComponent fileComponent;
	
//	private boolean 정수형비교(int a, int b) {
//		return a == b;
//	}
//	private boolean 문자열비교(String a, String b) {
//		return a.equals(b);
//	}
//	private boolean 뒤가크다(int a, int b) {
//		return a < b;
//	}
	
	@Override
	public ResponseEntity<?> view(int file_no) {
		try {
			  FileDTO fileDTO = boardMapper.findByFileNo(file_no);
			  String url = fileDTO.getFile_server_name(); // 데이터베이스에서 file_no로 파일 정보 가져와서 아래 로직 적용
			  File file = fileComponent.getFile(url);
		      return ResponseEntity.ok()
		        .contentLength(file.length())
		        .contentType(MediaType.parseMediaType(fileDTO.getFile_type()))
		        .body(new InputStreamResource(new FileInputStream(file)));
	    } catch (Exception e) {
	      e.printStackTrace();
	      return ResponseEntity.notFound().build();
	    }
	}

	@Override
	public ResultDTO boardList(RequestBoardDTO requestBoardDTO) {
		log.info("RequestBoardDTO : {}", requestBoardDTO);
		boolean state = false;
		int total = 0;
		int page = requestBoardDTO.getPage();
		int size = requestBoardDTO.getSize();
		List<Integer> paging = new ArrayList<>();
		Map<String, Object> resultMap = new HashMap<>();
		boolean rankstate = false;
		
		//노하우 게시판 상단 랭킹
		if(requestBoardDTO.getBoard_type() == 1) {
			List<BoardDTO> boardRanks = boardMapper.top10ByBoardLikes(10);
			rankstate = (boardRanks != null && !boardRanks.isEmpty());
			resultMap.put("ranks", boardRanks);
		} else {
			rankstate = true;
		}
		
		requestBoardDTO.setPage(page * size);
		
		List<BoardDTO> list = boardMapper.findAll(requestBoardDTO);
		total = boardMapper.findAllTotal(requestBoardDTO);
		log.info("윤신: {}", total);
		int totalPaging = (int) Math.floor(total / size);
		
		int endPage = totalPaging + 1;
		int start = (endPage - size) >= page ? page : (endPage - size);
		if(page < size) start = 0;
		for(int i = start; i < (start + size); i++) {
			if(i < endPage) {
				paging.add(i);				
			}
		}
		
		List<CategoryDTO> categories = boardMapper.findByCategory(requestBoardDTO.getBoard_type());
		log.info("{}", categories);
		resultMap.put("list", list);
		resultMap.put("total", total);
		resultMap.put("paging", paging);
		resultMap.put("categories", categories);
		
		// 조건에 따라서 true;
//		state = true;
		state = rankstate || (list != null && !list.isEmpty());
		
		return ResultDTO.builder()
				.state(state)
				.result(resultMap)
				.build();
	}

	@Override
	public ResultDTO boardAdd(RequestBoardFileDTO requestBoardFileDTO, HttpServletRequest request) {
		// 서비스 이동하는 메소드 호출 >> 
		boolean state = false;
		Object result = null;
	
		try {
			// Token 가져오기
			String token =  request.getHeader("Authorization");
			System.out.println(token);
			if(jwtToken.isValidToken(token)) {
				RequestTokenDTO requestTokenDTO = jwtToken.getUser(token);
				String user_id = requestTokenDTO.getId();
				System.out.println("user id: " + user_id);
				int user_no = boardMapper.findByUserId(user_id);
				System.out.println("user no: " + user_no);
				
				// 1단 게시판 등록 도전!! >> 게시판 번호를 이용하여 파일 업로드쪽으로 가봅시다..
				BoardDTO boardDTO = BoardDTO.builder()
						.board_title(requestBoardFileDTO.getBoard_title())
						.board_content(requestBoardFileDTO.getBoard_content())
						.category_id(requestBoardFileDTO.getCategory_id())
						.user_no(user_no)
						.board_type(requestBoardFileDTO.getBoard_type())
						.build();
				System.out.println("게시판 등록 되니?" + boardDTO);
				if(boardMapper.saveBoard(boardDTO) == 1) {
					// 게시글 저장 완료
					int board_no = boardDTO.getBoard_no();
					state = true;
					result = board_no;
					
					// 2단계 파일 업로드 도전!!
					MultipartFile multipartFile = requestBoardFileDTO.getFile();
					if(multipartFile == null || multipartFile.isEmpty()) {
						// 파일이 없다..
						System.out.println("NO FILE UPLOADED");
					} else {
						FileDTO fileDTO = fileComponent.addFile(multipartFile);
						fileDTO.setBoard_no(board_no);
						
						if( boardMapper.saveFile(fileDTO) == 1) {
							// 파일 테이블 정상 입력 완료!!
							System.out.println("FileDTO SAVED SUCCESSFULLY");
						} else {
							state = false;
						}
						
					}
				}
				
			} 
				
		}catch (Exception e) {
				e.printStackTrace();
				return ResultDTO.builder().state(false).msg("ERRROR").build();
		}
		
		return ResultDTO.builder().state(state).result(result).build();
	}

	@Override
	public ResultDTO boardUpdate(RequestBoardFileDTO requestBoardFileDTO, HttpServletRequest request) {
		boolean state = false;
	    Object result = null;

	    try {
	        // Token 가져오기
	        String token = request.getHeader("Authorization");
	        if (jwtToken.isValidToken(token)) {
	            RequestTokenDTO requestTokenDTO = jwtToken.getUser(token);
	            int user_no = boardMapper.findByUserId(requestTokenDTO.getId());
	            int board_no = requestBoardFileDTO.getBoard_no();
	            BoardDTO boardDTO = boardMapper.findByBoardNo(board_no);
	            
	            // 작성자와 로그인 사용자 동일 인물인지 확인
	            if(user_no == boardDTO.getUser_no()) {
	            	// 동일!!
	            	
	            	// 게시글 수정 DTO 생성
	            	boardDTO.setBoard_title(requestBoardFileDTO.getBoard_title());
	            	boardDTO.setBoard_content(requestBoardFileDTO.getBoard_content());
	            	boardDTO.setCategory_id(requestBoardFileDTO.getCategory_id());
	            	
	            	// 게시글 업데이트
		            if (boardMapper.updateBoard(boardDTO) == 1) {
		                state = true;

		                // 파일 수정 로직
		                MultipartFile multipartFile = requestBoardFileDTO.getFile();
		                if (multipartFile != null && !multipartFile.isEmpty()) {
		                    // 파일 정보 생성
		                    FileDTO fileDTO = boardMapper.findByFileBoardNO(board_no);
		                    if(fileDTO == null) {
		                    	fileDTO = new FileDTO();
		                    }
		                    
		                    fileDTO.setBoard_no(board_no);		                    
		                    // Util File
		                    fileDTO = fileComponent.editFile(fileDTO, multipartFile);
		                    
		                    int status = 0;
		                    if(fileDTO.getFile_no() == 0) {
		                    	// 파일 추가
		                    	status = boardMapper.saveFile(fileDTO);
		                    } else {
		                    	// 파일 수정
		                    	status = boardMapper.updateFile(fileDTO);
		                    }
		                    
		                    // 파일 관련 상태값 확인
		                    if (status == 1) {
		                        state = true;
		                    }
		                }
		                result = board_no; // 수정된 게시글 번호
		            }
	            } 		            
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResultDTO.builder().state(false).msg("ERROR").build();
	    }

	    return ResultDTO.builder().state(state).result(result).build();
	}

	@Override
	public ResultDTO boardDetail(int board_no, HttpServletRequest request) {
		BoardDTO boardDTO = boardMapper.findByBoardNo(board_no);
		if(boardDTO != null) {
			FileDTO fileDTO = boardMapper.findByFileBoardNO(boardDTO.getBoard_no());
			Map<String, Object> resultMap = new HashMap<>();
			if(fileDTO != null) {				
				boardDTO.setFile_no(fileDTO.getFile_no());
			}
			resultMap.put("board", boardDTO);
			
			//댓글 목록 조회
			List<CommentDTO> comments = boardMapper.findCommentByBoardNo(board_no);
			
			//게시글 세부 내용 및 댓글 내용
			resultMap.put("board", boardDTO);
			resultMap.put("comments", comments);
			
			// Token 가져오기
	        String token = request.getHeader("Authorization");
	        if(token != null) {
		        if (jwtToken.isValidToken(token)) {
		            RequestTokenDTO requestTokenDTO = jwtToken.getUser(token);
		            int user_no = boardMapper.findByUserId(requestTokenDTO.getId());
		            BoardLikeDTO boardLikeDTO = boardMapper.findByActive(BoardLikeDTO.builder().board_no(board_no).user_no(user_no).build());
		            log.info("{} : {} : {}", board_no, user_no, boardLikeDTO);
		            resultMap.put("like", boardLikeDTO);
		            
		            if(boardDTO.getUser_no() == user_no) {
		            	resultMap.put("grant", true);
		            } else {
		            	resultMap.put("grant", false);
		            }
		        }
	        }
			return ResultDTO.builder().state(true).result(resultMap).build();
		}
		return ResultDTO.builder().state(false).result("❌❌❌ 게시글을 찾을 수 없습니다. ❌❌❌").build();
	}

	@Override
	public ResultDTO boardDelete(int board_no, HttpServletRequest request) {
		boolean state = false;
	    try {
	        // Token 가져오기
	        String token = request.getHeader("Authorization");
	        if (jwtToken.isValidToken(token)) {
	        	RequestTokenDTO requestTokenDTO = jwtToken.getUser(token);
	            int user_no = boardMapper.findByUserId(requestTokenDTO.getId());
	        	
	            BoardDTO boardDTO = boardMapper.findByBoardNo(board_no);
	            
	            if(user_no == boardDTO.getUser_no()) {
	            	// 작성자와 동일 
	            	// 파일 정보 조회
		            FileDTO fileDTO = boardMapper.findByFileBoardNO(board_no);
		            if (fileDTO != null) {
		                fileDTO = fileComponent.deleteFile(fileDTO);
		                // 데이터베이스에서 파일 정보 삭제
		                boardMapper.deleteFile(fileDTO.getFile_no());
		            }
		        	
		            // 권한 검증 후 게시글 삭제 수행
		            if (boardMapper.deleteBoard(board_no) == 1) {
		                state = true; // 삭제 성공
		            }
	            }		        	
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResultDTO.builder().state(false).msg("ERROR").build();
	    }
	    return ResultDTO.builder().state(state).build();
	}

	@Override
	public ResultDTO boardAddLike(int board_no, HttpServletRequest request) {
		boolean state = false;
	    int total = 0;
	    try {
	        String token = request.getHeader("Authorization");
	        if (jwtToken.isValidToken(token)) {
	            RequestTokenDTO requestTokenDTO = jwtToken.getUser(token);
	            int user_no = boardMapper.findByUserId(requestTokenDTO.getId());

	            // 해당 게시글에 좋아요 상태값 확인
	            
	            BoardLikeDTO boardLikeDTO = BoardLikeDTO.builder()
	            									.board_no(board_no)
	            									.user_no(user_no)
	            									.build();
	            
	            boardLikeDTO = boardMapper.findByActive(boardLikeDTO);
	            log.info("like : {}", boardLikeDTO);
	            if(boardLikeDTO == null) {
	            	// 입력
	            	boardLikeDTO = BoardLikeDTO.builder().board_no(board_no).user_no(user_no).build();
	            	state = (boardMapper.addActive(boardLikeDTO) == 1) ? true : false;
	            } else {
	            	// 삭제
	            	state = (boardMapper.deleteActive(boardLikeDTO) == 1) ? true : false;
	            }
	            
	            // 해당 게시글의 좋아요 확인
	            total = boardMapper.findByCount(boardLikeDTO);
	            
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResultDTO.builder().state(false).msg("ERROR").build();
	    }
	    return ResultDTO.builder().state(state).result(total).build();
	}

	@Override
	public ResultDTO boardAddCount(int board_no) {
		int countResult = boardMapper.boardCount(board_no);
		log.info("게시글 번호: {} 조회수 증가 결과: {}", board_no, countResult);
		boolean state = (countResult == 1) ? true : false;
		return ResultDTO.builder().state(state).build();
	}

	@Override
	public ResultDTO commentAdd(CommentDTO commentDTO, HttpServletRequest req) {
		//토큰에서 사용자 정보 가져오기
		String token = req.getHeader("Authorization");
		
		if(token != null) {
			if(jwtToken.isValidToken(token)) {
				RequestTokenDTO requestTokenDTO = jwtToken.getUser(token);
				int user_no = boardMapper.findByUserId(requestTokenDTO.getId());
				log.info("user_no: {}", user_no);
				
				commentDTO.setUser_no(user_no);
				if (boardMapper.addComment(commentDTO) > 0) {
					List<CommentDTO> comments = boardMapper.findCommentByBoardNo(commentDTO.getBoard_no());
					return ResultDTO.builder().state(true).msg("댓글이 등록되었습니다.").result(comments).build();
				}
			}
		}

		return ResultDTO.builder().state(false).result("댓글 등록 중 실패하였습니다.").build();
	}

	@Override
	public ResultDTO commentUpdate(CommentDTO commentDTO, HttpServletRequest req) {
		//토큰에서 사용자 정보 가져오기
		String token = req.getHeader("Authorization");
		
		if(token != null) {
			if(jwtToken.isValidToken(token)) {
				RequestTokenDTO requestTokenDTO = jwtToken.getUser(token);
				int user_no = boardMapper.findByUserId(requestTokenDTO.getId());
				log.info("user_no: {}", user_no);
				
				// 댓글 테이블 업데이트 로직 추가 ..
				commentDTO.setUser_no(user_no);
				if(boardMapper.updateComment(commentDTO) == 1) {
					commentDTO = boardMapper.findByCommentNo(commentDTO);
					return ResultDTO.builder().state(true).result(commentDTO).build();
				}
			}
		}

		return ResultDTO.builder().state(false).result("댓글 수정 중 실패하였습니다.").build();
	}

	@Override
	public ResultDTO commentDelete(CommentDTO commentDTO, HttpServletRequest req) {
		//토큰에서 사용자 정보 가져오기
		String token = req.getHeader("Authorization");
		
		if(token != null) {
			if(jwtToken.isValidToken(token)) {
				RequestTokenDTO requestTokenDTO = jwtToken.getUser(token);
				int user_no = boardMapper.findByUserId(requestTokenDTO.getId());
				log.info("user_no: {}", user_no);
				
				// 댓글 테이블 삭제 로직 추가 ..
				commentDTO.setUser_no(user_no);
				if(boardMapper.deleteComment(commentDTO) == 1) {
					return ResultDTO.builder().state(true).build();
				}
			}
		}

		return ResultDTO.builder().state(false).result("댓글 삭제 오류").build();
	}

}
