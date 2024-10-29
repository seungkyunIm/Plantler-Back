package com.plantler.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.plantler.auth.JwtToken;
import com.plantler.dto.AlarmDTO;
import com.plantler.dto.RequestTokenDTO;
import com.plantler.dto.ResultDTO;
import com.plantler.mapper.AlarmMapper;
import com.plantler.util.TokenComponent;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlarmServiceImp implements AlarmService {
	
	@Autowired
	private AlarmMapper am;
	
	@Autowired
	private JwtToken jwtToken;
	
	@Autowired
	private TokenComponent tokenComponent;
	
	// 목록
	public ResultDTO list(HttpServletRequest req) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("{}", authentication.getPrincipal());
		boolean state = false;
		Object result = null;		
		String user_id = tokenComponent.getUserID(req);
		if(user_id != null) {
			int user_no = am.findByUserId(user_id);
			state = true;
			result = am.findAll(user_no);
		}
		return ResultDTO.builder().state(state).result(result).build();
	}
	
	// 등록
	public ResultDTO addAlarm(Map<String, String> map, HttpServletRequest request) {
        boolean state = false;
        Object result = null;

        try {
        	String user_id = tokenComponent.getUserID(request);
    		if(user_id != null) {
                int user_no = am.findByUserId(user_id);

                AlarmDTO alarmDTO = AlarmDTO.builder()
                        .alarm_plantname(map.get("alarm_plantname"))
                        .alarm_memo(map.get("alarm_memo"))
                        .alarm_scheduled2(map.get("alarm_scheduled"))
                        .alarm_repot2(map.get("alarm_repot"))
                        .alarm_nutrients2(map.get("alarm_nutrients"))
                        .user_no(user_no)
                        .build();

                if (am.saveAlarm(alarmDTO) == 1) {
                    state = true;
                    result = alarmDTO.getAlarm_no();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDTO.builder().state(false).msg("ERROR").build();
        }
        return ResultDTO.builder().state(state).result(result).build();
    }
	
	// 수정
	public ResultDTO alarmedit(@RequestBody Map<String, String> map, HttpServletRequest request) {
		// 서비스 이동하는 메소드 호출 >>	
		try {
			// Token 가져오기
			String user_id = tokenComponent.getUserID(request);
    		if(user_id != null) {
				Object result = null;
				int user_no = am.findByUserId(user_id);
				// 작성자와 동일 한 사람인지 확인!!
				if(user_no == Integer.parseInt(map.get("user_no"))) {
					
					AlarmDTO alarmDTO = AlarmDTO.builder()
							.alarm_plantname(map.get("alarm_plantname"))
							.alarm_memo(map.get("alarm_memo"))
							.alarm_scheduled2(map.get("alarm_scheduled"))
							.alarm_repot2(map.get("alarm_repot"))
							.alarm_nutrients2(map.get("alarm_nutrients"))
							.alarm_no(Integer.parseInt(map.get("alarm_no")))
							.build();
					
					if(am.editAlarm(alarmDTO) == 1) {
//						System.out.println("게시판 수정 되니?" + alarmDTO);
						int alarm_no = alarmDTO.getAlarm_no();
						result = alarm_no;
					}
//					System.out.println("result:  " + result);
					return ResultDTO.builder().state(true).result(result).build();
					
				} 							
			} 
				
		} catch (NumberFormatException e) {
			e.printStackTrace();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultDTO.builder().state(false).msg("ERRROR").build();
	}
	
	// 삭제
	public ResultDTO alarmDelete(@RequestBody Map<String, String> map, HttpServletRequest request) {
		try {
			// Token 가져오기
			String user_id = tokenComponent.getUserID(request);
    		if(user_id != null) {
				Object result = null;
				int user_no = am.findByUserId(user_id);
				
				// 작성자와 동일 한 사람인지 확인!!
				if(user_no == Integer.parseInt(map.get("user_no"))) {					
					
					int alarm_no = Integer.parseInt(map.get("alarm_no"));
					if(am.deleteAlarm(alarm_no) == 1) {//												
						result = alarm_no;
					}
					return ResultDTO.builder().state(true).result(result).build();
				} 							
			} 
				
		} catch (NumberFormatException e) {
			e.printStackTrace();			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return ResultDTO.builder().state(false).msg("ERRROR").build();
    }
}
