package com.plantler.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import com.plantler.dto.BoardDTO;
import com.plantler.dto.BoardLikeDTO;
import com.plantler.dto.CategoryDTO;
import com.plantler.dto.CategoryParamDTO;
import com.plantler.dto.CommentDTO;
import com.plantler.dto.FileDTO;
import com.plantler.dto.RequestBoardDTO;

@Mapper
public interface BoardMapper {

	// 전체 게시글 리스트
	@Select({"  <script>"
			+ "SELECT pb.*, pu.user_nick, pc.category_name, pbl.`active` AS board_like, DATE_FORMAT(pb.board_regdate, '%Y-%m-%d %H:%i:%s') AS board_regdate2 "
			+ "  FROM pl_board AS pb "
			+ " INNER JOIN pl_user AS pu "
			+ "    ON (pb.user_no = pu.user_no) "
			+ " INNER JOIN pl_category AS pc "
			+ "    ON (pb.category_id = pc.category_id) "
			+ "  LEFT OUTER JOIN (SELECT board_no, COUNT(*) AS ACTIVE FROM pl_board_likes GROUP BY board_no) AS pbl "
			+ "    ON (pbl.board_no = pb.board_no) "
			+ " WHERE pb.board_title like '%${query}%' "
			+ " AND pb.board_type = #{board_type} "
			+ "	<if test='category_id != 0'> AND pb.category_id = ${category_id} </if> "
			+ " ORDER BY 1 DESC LIMIT ${page}, ${size}"
			+ "</script>"})
	public List<BoardDTO> findAll(RequestBoardDTO requestBoardDTO);
	
	@Select("<script> "
			+ "	SELECT COUNT(*) AS cnt "
			+ "	FROM pl_board AS pb "
			+ "	INNER JOIN pl_category AS pc "
			+ "	ON (pb.category_id = pc.category_id) "
			+ "	WHERE board_title like '%${query}%' "
			+ " AND pb.board_type = #{board_type} "
			+ "	 <if test= 'category_id != 0'> AND pb.category_id = ${category_id} </if> "
//			+ "	ORDER BY 1 DESC LIMIT ${page}, ${size} "
			+ "	</script>")
	public int findAllTotal(RequestBoardDTO requestBoardDTO);

	@Select("SELECT * FROM pl_comment WHERE comment_no = #{comment_no}")
	public CommentDTO findByNo(int comment_no);
	
	// 아래의 코드는 추후에 회원쪽으로 가야하는 코드 아닌가???
	@Select("SELECT user_no FROM pl_user WHERE user_id = #{user_id}")
	public int findByUserId(String user_id);
	
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "board_no", before = false, resultType = Integer.class)
	@Insert("INSERT INTO pl_board (board_title, board_content, category_id, user_no, board_type) VALUE (#{board_title}, #{board_content}, #{category_id}, #{user_no}, #{board_type})")
	public int saveBoard(BoardDTO boardDTO);
	
	// pl_file테이블에는 user_no이 없어서 뺌
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "file_no", before = false, resultType = Integer.class)
	@Insert("INSERT INTO pl_file (board_no, file_server_name, file_name, file_type, file_url, file_extension, file_sort) VALUE (#{board_no}, #{file_server_name}, #{file_name}, #{file_type}, #{file_url}, #{file_extension}, #{file_sort})")
	public int saveFile(FileDTO fileDTO);
	
	// 게시글 상세
	@Select("SELECT * FROM pl_file WHERE file_no = #{file_no}")
	public FileDTO findByFileNo(int file_no);

	// 게시글 상세
	@Select("SELECT * FROM pl_file WHERE board_no = #{board_no}")
	public FileDTO findByFileBoardNO(int board_no);
	
	// 게시글 상세
	@Select("SELECT pb.*, pu.user_nick, pc.category_name, pbl.`active` AS board_like, "
			+ " DATE_FORMAT(pb.board_regdate, '%Y-%m-%d %H:%i:%s') AS board_regdate2 "
			+ "	FROM pl_board AS pb "
			+ "	INNER JOIN pl_user AS pu "
			+ "	    ON (pb.user_no = pu.user_no) "
			+ "	INNER JOIN pl_category AS pc "
			+ "	   ON (pb.category_id = pc.category_id) "
			+ " LEFT OUTER JOIN (SELECT board_no, COUNT(*) AS ACTIVE FROM pl_board_likes GROUP BY board_no) AS pbl "
			+ "    ON (pbl.board_no = pb.board_no) "
			+ " WHERE pb.board_no = #{board_no}")
	public BoardDTO findByBoardNo(int board_no);
	
	//게시판 상단 랭킹 1 to 10(조회수가 많은 순으로)
	@Select("  SELECT pb.*, pc.category_name "
			+ "  FROM pl_board AS pb "
			+ " INNER JOIN pl_category AS pc "
			+ "    ON (pb.category_id = pc.category_id) "
			+ " WHERE pc.category_id < 100 "
			+ " ORDER BY pb.board_count DESC LIMIT 10 ")
	public List<BoardDTO> top10ByBoardLikes(int limit);	//like컬럼이 없어져서 임의로 board_count
		
	// 게시글 수정
	@Update("UPDATE pl_board SET board_title = #{board_title}, board_content = #{board_content}, category_id = #{category_id}, board_moddate = now() WHERE board_no = #{board_no}")
	public int updateBoard(BoardDTO boardDTO);

	// 게시글 파일 수정
	@Update("UPDATE pl_file SET file_server_name = #{file_server_name}, file_name = #{file_name}, file_type = #{file_type}, file_url = #{file_url}, file_extension = #{file_extension}, file_sort = #{file_sort} WHERE file_no = #{file_no}")
	public int updateFile(FileDTO fileDTO);
	
	// 게시글 삭제
	@Delete("DELETE FROM pl_board WHERE board_no = #{board_no}")
	public int deleteBoard(int board_no);
	
	// 게시글 파일 삭제
	@Delete("DELETE FROM pl_file WHERE file_no = #{file_no}")
	public int deleteFile(int file_no);

	// 게시글 조회수
	@Update("UPDATE pl_board SET board_count = board_count + 1 WHERE board_no = #{board_no}")
	public int boardCount(int board_no);
	
	// 게시글 좋아요 수 추가
	@Select("SELECT * FROM pl_board_likes WHERE board_no = #{board_no} AND user_no = #{user_no}")
	public BoardLikeDTO findByActive(BoardLikeDTO boardLikeDTO);
	
	@Insert("INSERT INTO pl_board_likes (board_no, user_no) VALUE (#{board_no}, #{user_no})")
	public int addActive(BoardLikeDTO boardLikeDTO);
	
	@Delete("DELETE FROM pl_board_likes WHERE board_no = #{board_no} AND user_no = #{user_no}")
    public int deleteActive(BoardLikeDTO boardLikeDTO);
    
    @Select("SELECT COUNT(*) AS cnt FROM pl_board_likes WHERE board_no = #{board_no}")
    public int findByCount(BoardLikeDTO boardLikeDTO);

    //댓글-----------------------------------------------------------------------------------------------
  	//댓글 목록 조회
  	@Select("SELECT pc.* , pu.user_nick "
  			+ "	FROM pl_comment AS pc "
  			+ "INNER JOIN pl_user AS pu "
  			+ "	ON (pc.user_no = pu.user_no) "
  			+ "WHERE pc.board_no = #{board_no} ")
  	public List<CommentDTO> findCommentByBoardNo(@Param("board_no") int board_no);
  	
  	//댓글 추가
  	@Insert("INSERT INTO pl_comment (board_no, user_no, comment_content) VALUES (#{board_no}, #{user_no}, #{comment_content}) ")
  	public int addComment(CommentDTO commentDTO);
  	
  	//댓글 수정
  	@Update("UPDATE pl_comment SET comment_content = #{comment_content}, comment_moddate = now() WHERE comment_no = #{comment_no} AND user_no = #{user_no}")
  	public int updateComment(CommentDTO commentDTO);
  	
  	//댓글 삭제
  	@Delete("DELETE FROM pl_comment WHERE comment_no = #{comment_no} AND user_no = #{user_no}")
  	public int deleteComment(CommentDTO commentDTO);
  	
  	@Select("  SELECT pc.* , pu.user_nick "
  			+ "  FROM pl_comment AS pc "
  			+ " INNER JOIN pl_user AS pu "
  			+ "    ON (pc.user_no = pu.user_no) "
  			+ " WHERE pc.comment_no = #{comment_no}")
  	public CommentDTO findByCommentNo(CommentDTO commentDTO);
  	
  	// 카테고리별 게시글 목록 불러오기
  	@Select("SELECT * FROM pl_category WHERE category_type = #{board_type}")
  	public List<CategoryDTO> findByCategory(int board_type);
	
}
