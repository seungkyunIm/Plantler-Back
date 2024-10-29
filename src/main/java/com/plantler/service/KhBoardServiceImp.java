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
import com.plantler.dto.CategoryDTO;
import com.plantler.dto.CategoryParamDTO;
import com.plantler.dto.CommentDTO;
import com.plantler.dto.FileDTO;
import com.plantler.dto.KhBoardDTO;
import com.plantler.dto.KhBoardLikeDTO;
import com.plantler.dto.RequestTokenDTO;
import com.plantler.dto.ResultDTO;
import com.plantler.mapper.KhBoardMapper;
import com.plantler.util.FileComponent;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class KhBoardServiceImp implements KhBoardService {

	private final KhBoardMapper khbMapper;
	private final JwtToken jwtToken;
	private final FileComponent fileComponent;
	
	@Override
	public ResponseEntity<?> view(int file_no) {
		try {
			  FileDTO fileDTO = khbMapper.findByFileNo(file_no);
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
	public ResultDTO list(Map<String, Object> paramMap) {
		log.info("paramMap : {}", paramMap);
		boolean state = false;
		//노하우 게시판 상단 랭킹
		List<KhBoardDTO> khBoardRanks = khbMapper.KhTop10ByBoardLikes(10); 
		
		//파일 정보 추가 (게시판 랭킹용)
		if(khBoardRanks != null && !khBoardRanks.isEmpty()) {
			for (KhBoardDTO board : khBoardRanks) {
				try {
					FileDTO fileDTO = khbMapper.findByFileNo(board.getBoard_no());
					if (fileDTO != null) {
						//board.setFile_no(fileDTO.findByFileNoOne(1));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		
		int total = 0;
		int page = 0;
		int size = 0;
		List<Integer> paging = new ArrayList<>();
		
		size = Integer.parseInt(paramMap.get("size").toString());
		page = Integer.parseInt(paramMap.get("page").toString());
		paramMap.put("page", page * size);
		
		if(paramMap.get("category_id") == null) paramMap.put("category_id", 0);
		
		List<KhBoardDTO> list = khbMapper.findAll(paramMap);
		total = khbMapper.findAllTotal(paramMap);
		
		int totalPaging = (int) Math.floor(total / size);
		
		int endPage = totalPaging + 1;
		int start = (endPage - size) >= page ? page : (endPage - size);
		if(page < size) start = 0;
		for(int i = start; i < (start + size); i++) {
			if(i < endPage) {
				paging.add(i);				
			}
		}
		
		boolean rankstate = (khBoardRanks != null && !khBoardRanks.isEmpty());
		state = rankstate || (list != null && !list.isEmpty());
		
		List<CategoryDTO> categories = khbMapper.findByCategory(CategoryParamDTO.builder().start(1).end(99).build());
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("list", list);
		resultMap.put("total", total);
		resultMap.put("paging", paging);
		resultMap.put("ranks", khBoardRanks);
		resultMap.put("categories", categories);
		
		
		// 조건에 따라서 true;
		state = true;
		
		return ResultDTO.builder()
				.state(state)
				.result(resultMap)
				.build();
	}

	@Override
	public ResultDTO khBoardDetail(int board_no, HttpServletRequest request) {
		KhBoardDTO khBoardDTO = khbMapper.findByBoardNo(board_no);
		if(khBoardDTO != null) {
			FileDTO fileDTO = khbMapper.findByFileBoardNO(khBoardDTO.getBoard_no());
			Map<String, Object> resultMap = new HashMap<>();
			if(fileDTO != null) {				
				khBoardDTO.setFile_no(fileDTO.getFile_no());
			}
			resultMap.put("board", khBoardDTO);
			
			//댓글 목록 조회
			List<CommentDTO> comments = khbMapper.findCommentByBoardNo(board_no);
			
			//게시글 세부 내용 및 댓글 내용
//			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("khBoard", khBoardDTO);
			resultMap.put("comments", comments);
			
			// Token 가져오기
	        String token = request.getHeader("Authorization");
	        if(token != null) {
		        if (jwtToken.isValidToken(token)) {
		            RequestTokenDTO requestTokenDTO = jwtToken.getUser(token);
		            int user_no = khbMapper.findByUserId(requestTokenDTO.getId());
		            
		            if(khBoardDTO.getUser_no() == user_no) {
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
	public ResultDTO addComment(CommentDTO commentDTO, HttpServletRequest req) {
		//토큰에서 사용자 정보 가져오기
		String token = req.getHeader("Authorization");
		
		if(token != null) {
			if(jwtToken.isValidToken(token)) {
				RequestTokenDTO requestTokenDTO = jwtToken.getUser(token);
				int user_no = khbMapper.findByUserId(requestTokenDTO.getId());
				log.info("user_no: {}", user_no);
				
				commentDTO.setUser_no(user_no);
				if (khbMapper.addComment(commentDTO) > 0) {
					List<CommentDTO> comments = khbMapper.findCommentByBoardNo(commentDTO.getBoard_no());
					return ResultDTO.builder().state(true).msg("댓글이 등록되었습니다.").result(comments).build();
				}
			}
		}

		return ResultDTO.builder().state(false).result("댓글 등록 중 실패하였습니다.").build();
	}

	@Override
	public ResultDTO updateComment(CommentDTO commentDTO, HttpServletRequest req) {
		//토큰에서 사용자 정보 가져오기
		String token = req.getHeader("Authorization");
		
		if(token != null) {
			if(jwtToken.isValidToken(token)) {
				RequestTokenDTO requestTokenDTO = jwtToken.getUser(token);
				int user_no = khbMapper.findByUserId(requestTokenDTO.getId());
				log.info("user_no: {}", user_no);
				
				// 댓글 테이블 업데이트 로직 추가 ..
				commentDTO.setUser_no(user_no);
				if(khbMapper.updateComment(commentDTO) == 1) {
					commentDTO = khbMapper.findByCommentNo(commentDTO);
					return ResultDTO.builder().state(true).result(commentDTO).build();
				}
			}
		}

		return ResultDTO.builder().state(false).result("댓글 수정 중 실패하였습니다.").build();
	}

	@Override
	public ResultDTO deleteComment(CommentDTO commentDTO, HttpServletRequest req) {
		//토큰에서 사용자 정보 가져오기
		String token = req.getHeader("Authorization");
		
		if(token != null) {
			if(jwtToken.isValidToken(token)) {
				RequestTokenDTO requestTokenDTO = jwtToken.getUser(token);
				int user_no = khbMapper.findByUserId(requestTokenDTO.getId());
				log.info("user_no: {}", user_no);
				
				// 댓글 테이블 삭제 로직 추가 ..
				commentDTO.setUser_no(user_no);
				if(khbMapper.deleteComment(commentDTO) == 1) {
					return ResultDTO.builder().state(true).build();
				}
			}
		}

		return ResultDTO.builder().state(false).result("댓글 삭제 오류").build();
	}

	@Override
	public ResultDTO khboardRegister(MultipartFile multipartFile, String board_title, String board_content,
			int category_id, HttpServletRequest request) {
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
				int user_no = khbMapper.findByUserId(user_id);
				System.out.println("user no: " + user_no);
				
				// 1단 게시판 등록 도전!! >> 게시판 번호를 이용하여 파일 업로드쪽으로 가봅시다..
				KhBoardDTO khBoardDTO = KhBoardDTO.builder()
						.board_title(board_title)
						.board_content(board_content)
						.category_id(category_id)
						.user_no(user_no)
						.build();
				System.out.println("게시판 등록 되니?" + khBoardDTO);
				if(khbMapper.saveBoard(khBoardDTO) == 1) {
					// 게시글 저장 완료
					int board_no = khBoardDTO.getBoard_no();
					state = true;
					result = board_no;
					
					// 2단계 파일 업로드 도전!!
					if(multipartFile == null || multipartFile.isEmpty()) {
						// 파일이 없다..
						System.out.println("NO FILE UPLOADED");
					} else {
						FileDTO fileDTO = fileComponent.addFile(multipartFile);
						fileDTO.setBoard_no(board_no);
						
						if( khbMapper.saveFile(fileDTO) == 1) {
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
	public ResultDTO khBoardUpdate(int board_no, String board_title, String board_content, int category_id,
			MultipartFile multipartFile, HttpServletRequest request) {
		boolean state = false;
	    Object result = null;

	    try {
	        // Token 가져오기
	        String token = request.getHeader("Authorization");
	        if (jwtToken.isValidToken(token)) {
	            RequestTokenDTO requestTokenDTO = jwtToken.getUser(token);
	            int user_no = khbMapper.findByUserId(requestTokenDTO.getId());

	            KhBoardDTO khBoardDTO = khbMapper.findByBoardNo(board_no);
	            
	            // 작성자와 로그인 사용자 동일 인물인지 확인
	            if(user_no == khBoardDTO.getUser_no()) {
	            	// 동일!!
	            	
	            	// 게시글 수정 DTO 생성
		            khBoardDTO.setBoard_title(board_title);
		            khBoardDTO.setBoard_content(board_content);
		            khBoardDTO.setCategory_id(category_id);
	            	
	            	// 게시글 업데이트
		            if (khbMapper.updateBoard(khBoardDTO) == 1) {
		                state = true;

		                // 파일 수정 로직
		                if (multipartFile != null && !multipartFile.isEmpty()) {
		                    // 파일 정보 생성
		                    FileDTO fileDTO = khbMapper.findByFileBoardNO(board_no);
		                    if(fileDTO == null) {
		                    	fileDTO = new FileDTO();
		                    }
		                    
		                    fileDTO.setBoard_no(board_no);		                    
		                    // Util File
		                    fileDTO = fileComponent.editFile(fileDTO, multipartFile);

		                    int status = 0;
		                    if(fileDTO.getFile_no() == 0) {
		                    	// 파일 추가
		                    	status = khbMapper.saveFile(fileDTO);
		                    } else {
		                    	// 파일 수정
		                    	status = khbMapper.updateFile(fileDTO);
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
	public ResultDTO deleteBoard(int board_no, HttpServletRequest request) {
		boolean state = false;
	    try {
	        // Token 가져오기
	        String token = request.getHeader("Authorization");
	        if (jwtToken.isValidToken(token)) {
	        	RequestTokenDTO requestTokenDTO = jwtToken.getUser(token);
	            int user_no = khbMapper.findByUserId(requestTokenDTO.getId());
	        	
	            KhBoardDTO khBoardDTO = khbMapper.findByBoardNo(board_no);
	            
	            if(user_no == khBoardDTO.getUser_no()) {
	            	// 작성자와 동일 
	            	// 파일 정보 조회
		            FileDTO fileDTO = khbMapper.findByFileBoardNO(board_no);
		            if (fileDTO != null) {
		            	fileDTO = fileComponent.deleteFile(fileDTO);
		                // 데이터베이스에서 파일 정보 삭제
		                khbMapper.deleteFile(fileDTO.getFile_no());
		            }
		        	
		            // 권한 검증 후 게시글 삭제 수행
		            if (khbMapper.deleteBoard(board_no) == 1) {
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
	public ResultDTO khboardaddlike(int board_no, HttpServletRequest request) {
		boolean state = false;
	    int total = 0;
	    try {
	        String token = request.getHeader("Authorization");
	        if (jwtToken.isValidToken(token)) {
	            RequestTokenDTO requestTokenDTO = jwtToken.getUser(token);
	            int user_no = khbMapper.findByUserId(requestTokenDTO.getId());

	            // 해당 게시글에 좋아요 상태값 확인
	            
	            KhBoardLikeDTO khBoardLikeDTO = KhBoardLikeDTO.builder()
	            									.board_no(board_no)
	            									.user_no(user_no)
	            									.build();
	            
	            khBoardLikeDTO = khbMapper.khboardFindByActive(khBoardLikeDTO);
	            log.info("like : {}", khBoardLikeDTO);
	            if(khBoardLikeDTO == null) {
	            	// 입력
	            	khBoardLikeDTO = KhBoardLikeDTO.builder().board_no(board_no).user_no(user_no).build();
	            	state = (khbMapper.khboardAddActive(khBoardLikeDTO) == 1) ? true : false;
	            } else {
	            	// 삭제
	            	state = (khbMapper.khboardDeleteActive(khBoardLikeDTO) == 1) ? true : false;
	            }
	            
	            // 해당 게시글의 좋아요 확인
	            total = khbMapper.khboardFindByCount(khBoardLikeDTO);
	            
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResultDTO.builder().state(false).msg("ERROR").build();
	    }
	    return ResultDTO.builder().state(state).result(total).build();
	}

	@Override
	public ResultDTO khboardaddcount(int board_no) {
		int countResult = khbMapper.boardCount(board_no);
		log.info("게시글 번호: {} 조회수 증가 결과: {}", board_no, countResult);
		boolean state = (countResult == 1) ? true : false;
		return ResultDTO.builder().state(state).build();
	}
	
}
