package com.plantler.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.plantler.dto.KeywordDTO;
import com.plantler.dto.PlantDetailDTO;

@Mapper
public interface PlantlMapper {
	
	@Select("SELECT * FROM pl_plant;")
	public List<PlantDetailDTO> getRecPlant();
	
	// 선택 없이 랜덤으로 나오게 하는 키워드 
    @Select("SELECT * FROM pl_detail ORDER BY RAND() LIMIT 5;")
    public List<PlantDetailDTO> getRandomKeyword();
    
    @Select("SELECT * FROM pl_detail")
    public List<PlantDetailDTO> getPlantDetail();
    
    @Select("SELECT * FROM pl_detail WHERE plantNo = #{id}")
    public PlantDetailDTO getPlantDetailById(Long id);
    
    @Select("SELECT keyword_no AS keywordNo, keyword_name AS keywordName FROM pl_keyword")
    public List<KeywordDTO> getPlantKeyword();
    
    @Select("SELECT Question_no AS questionNo FROM pl_mine WHERE Question1 = #{question1} AND Question2 = #{question2} AND Question3 = #{question3} AND Question4 = #{question4}")
    public Integer findQuestionNo(@Param("question1") boolean question1,
                                   @Param("question2") boolean question2,
                                   @Param("question3") boolean question3,
                                   @Param("question4") boolean question4);
    
    @Select("SELECT DISTINCT plantNo, "
    		+ "       MAX(questionNo) AS questionNo, "
    		+ "       MAX(keywordNo) AS keywordNo, "
    		+ "       MAX(plantTitle) AS plantTitle, "
    		+ "       MAX(plantImage) AS plantImage, "
    		+ "       MAX(keyword1) AS keyword1, "
    		+ "       MAX(keyword2) AS keyword2, "
    		+ "       MAX(keyword3) AS keyword3 "
    		+ "FROM pl_mine_plant "
    		+ "WHERE questionNo = #{questionNo} "
    		+ "GROUP BY plantNo "
    		+ "ORDER BY RAND() "
    		+ "LIMIT 5;")
    List<PlantDetailDTO> getRandomPlants(int questionNo);
    
}
