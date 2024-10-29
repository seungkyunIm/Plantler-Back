package com.plantler.docs;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.plantler.dto.CommentDTO;
import com.plantler.dto.ResultDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name="윤신이가 만든 컨트롤러", description = "보조는 윤은이가 했으니 물어봐요!")
public interface KhBoardControllerDoc {
	
	/***************************************************
	 * @Operation 메소드 정의
	 * operationId: Endpoint 아이디
	 * summary: 간단한 설명으로 Swagger-ui의 Endpoint 상단에 노출
	 * description: 엔드포인트 상세 설명
	 * tags: 현재 Endpoint가 어떠한 tag 그룹에 속한지 알려주는 속성
	 * response: 응답코드, 응답 타입
	 * security: 보안방법에 대한 설정
	 * *************************************************/
	
	/***************************************************
	 * @Parameter 파라메터 정의
	 * name : 파라미터 이름
	 * description : 파라미터 설명
	 * required : 필수/선택 여부
	 * in : 파라미터의 타입 설정
	 * ParameterIn.QUERY : 요청 쿼리 파라미터
	 * ParameterIn.HEADER : 요청 헤더에 전달되는 파라미터
	 * ParameterIn.PATH: PathVariable 에 속하는 파라미터
	 * 값없음: RequestBody에 해당하는 객체 타입의 파라미터
	 * *************************************************/
	
	/***************************************************
	 * @ApiResponse 응답 정의
	 * responseCode: HTTP 상태코드
	 * description : 응답 결과 구조에 대한 설명
	 * content : 응답 페이로드 구조
	 * schema : 페이로드에서 사용하는 객체 스키마
	 * *************************************************/

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
	
	@Operation(summary = "윤신용", description = "게시판 수정 하는 URL")
	@ApiResponse(responseCode = "200", description = "게시판 수정 후 성공시 알려주는 값", content = @Content(schema = @Schema(implementation = ResultDTO.class)))
	public ResultDTO khBoardUpdate(
			@Parameter(name="board_no", description = "게시글 번호(고유번호)") int board_no,
	        String board_title,
	        String board_content,
	        int category_id,
	        MultipartFile multipartFile,
	        HttpServletRequest request);
	public ResultDTO deleteBoard(int board_no, HttpServletRequest request);
	public ResultDTO khboardaddlike(int board_no, HttpServletRequest request);
	public ResultDTO khboardaddcount(int board_no);
	
}
