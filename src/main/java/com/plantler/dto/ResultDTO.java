package com.plantler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "응답 DTO")
public class ResultDTO {

	@Schema(description = "응답 상태 값", defaultValue = "false")
	private boolean state;
	private Object result;
	private String msg;
	
	
//게시글 상세 페이지 관련 (KhBoardDetail)	
	public static ResultDTOBuilder builder() {
		return new ResultDTOBuilder();
	}
	
	public static class ResultDTOBuilder {
		private boolean state;
		private Object result;
		
		public ResultDTOBuilder state(boolean state) {
			this.state = state;
			return this;
		}
		
		public ResultDTOBuilder result(Object result) {
			this.result = result;
			return this;
		}
		
		public ResultDTO build() {
			ResultDTO resultDTO = new ResultDTO(state, result, msg);
			resultDTO.state = this.state;
			resultDTO.result = this.result;
			return resultDTO;
		}
	}
}
