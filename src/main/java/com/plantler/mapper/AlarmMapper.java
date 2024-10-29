package com.plantler.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import com.plantler.dto.AlarmDTO;
import com.plantler.dto.KhBoardDTO;
import com.plantler.dto.ResultDTO;

@Mapper
public interface AlarmMapper {
	
	// 목록 조회
	@Select(" SELECT pu.user_no, pu.user_no, pa.alarm_no, pa.alarm_plantname, pa.alarm_memo, "			
			+ "		 DATE_FORMAT(pa.alarm_scheduled, '%Y-%m-%d %H:%i:%s') as alarm_scheduled2, "
			+ "	 	 DATE_FORMAT(pa.alarm_repot, '%Y-%m-%d %H:%i:%s') as alarm_repot2, "
			+ "	 	 DATE_FORMAT(pa.alarm_nutrients, '%Y-%m-%d %H:%i:%s') as alarm_nutrients2 "
			+ " FROM pl_alarm as pa "
			+ "INNER JOIN pl_user AS pu "
			+ "   ON (pa.user_no = pu.user_no) AND pu.user_no = #{user_no} "
			+ "ORDER BY 2 DESC ")
	public List<AlarmDTO> findAll(int user_no);
	
	// 알림 등록	
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "alarm_no", before = false, resultType = Integer.class)
	@Insert("INSERT INTO pl_alarm "
			+ "					(alarm_plantname, "
			+ "					alarm_memo, "
			+ "					alarm_scheduled, "
			+ "					alarm_repot, "
			+ "					alarm_nutrients, "
			+ "					user_no) "
			+ "VALUE "
			+ "		(#{alarm_plantname}, "
			+ "		#{alarm_memo}, "
			+ "		STR_TO_DATE(#{alarm_scheduled2}, '%Y-%m-%d %H:%i:%s'), "
			+ "		STR_TO_DATE(#{alarm_repot2}, '%Y-%m-%d %H:%i:%s'), "
			+ "		STR_TO_DATE(#{alarm_nutrients2}, '%Y-%m-%d %H:%i:%s'), "
			+ "		#{user_no}) ")
	public int saveAlarm(AlarmDTO alarmDTO);
	
	// user no 조회
	@Select("SELECT user_no FROM pl_user WHERE user_id = #{user_id}")
	public int findByUserId(String user_id);
	
	// 수정 
	@Update("UPDATE pl_alarm SET "
			+ "		alarm_plantname = #{alarm_plantname}, "
			+ "		alarm_memo = #{alarm_memo}, "
			+ "		alarm_scheduled = STR_TO_DATE(#{alarm_scheduled2}, '%Y-%m-%d %H:%i:%s'), "
			+ "		alarm_repot = STR_TO_DATE(#{alarm_repot2}, '%Y-%m-%d %H:%i:%s'), "
			+ "		alarm_nutrients = STR_TO_DATE(#{alarm_nutrients2}, '%Y-%m-%d %H:%i:%s') "
			+ "WHERE alarm_no = #{alarm_no}")
	public int editAlarm(AlarmDTO alarmDTO);
	
	// 삭제
	@Delete("DELETE FROM pl_alarm WHERE alarm_no = #{alarm_no}")
	public int deleteAlarm(int alarm_no);
	
}
