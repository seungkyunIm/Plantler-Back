package com.plantler.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plantler.auth.JwtToken;
import com.plantler.docs.MainControllerDoc;
import com.plantler.dto.FreeBoardDTO;
import com.plantler.dto.KhBoardDTO;
import com.plantler.dto.ResultDTO;
import com.plantler.mapper.FreeBoardMapper;
import com.plantler.mapper.KhBoardMapper;
import com.plantler.service.KhBoardServiceImp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MainController implements MainControllerDoc {

	private final KhBoardMapper khbMapper;
	private final JwtToken jwtToken;
	private final FreeBoardMapper fbMapper;
	
//	@GetMapping("/")
//	public String home() {
//		return "서버 스타트~~~~~~~~~~";
//	}
	
	//메인페이지 랭킹용
	@GetMapping("/")
	public ResultDTO getKhRanks() {
		
		boolean state = false;
		
		Map<String, Object> khRanksResult = new HashMap<>();
		Map<String, Object> freeRankResult = new HashMap<>();

		List<KhBoardDTO> khBoardRanks = khbMapper.KhTop10ByBoardLikes(10);
		List<FreeBoardDTO> freeBoardRanks = fbMapper.FreeTop10ByBoardLikes(10);
		
		if (khBoardRanks != null && freeBoardRanks != null) {
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("ranks", khBoardRanks);
			resultMap.put("freeranks", freeBoardRanks);
		
			khRanksResult.put("result", resultMap);
			freeRankResult.put("result", resultMap);
			
			log.info("resultMap {} ", resultMap);
		
			return ResultDTO.builder().state(true).result(resultMap).build();
		} else {
			return ResultDTO.builder().state(false).msg("데이터가 없습니다").build();
		}
	}
	
}
