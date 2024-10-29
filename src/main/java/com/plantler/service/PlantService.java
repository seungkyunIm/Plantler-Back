package com.plantler.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.plantler.dto.KeywordDTO;
import com.plantler.dto.PlantDetailDTO;
import com.plantler.mapper.PlantlMapper;

@Service
public class PlantService {

    @Autowired
    private PlantlMapper plantMapper;
    
	public List<PlantDetailDTO> plantRec() {
	    List<PlantDetailDTO> plantsRec = plantMapper.getRandomKeyword();

	    return plantsRec;
	}
    
    public PlantDetailDTO getPlantDetail(Long id) {
        return plantMapper.getPlantDetailById(id);
    }
    
    public List<PlantDetailDTO> getPlantAll() {
    	List<PlantDetailDTO> plantsAll = plantMapper.getPlantDetail();
        return plantsAll;
    }
    
    public List<KeywordDTO> getPlantKeyword(){
    	return plantMapper.getPlantKeyword();
    }
    
    public Integer getQuestionNo(boolean question1, boolean question2, boolean question3, boolean question4) {
        // 데이터베이스에서 조건에 맞는 레코드를 찾는 로직
        return plantMapper.findQuestionNo(question1, question2, question3, question4);
    }
    
    public List<PlantDetailDTO> getRandomPlants(int questionNo) {
        return plantMapper.getRandomPlants(questionNo);
    }
    
    
}
