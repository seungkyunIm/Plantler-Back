package com.plantler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KhBoardLikeDTO {

	private int board_no;
	private int user_no;
	private boolean active;
	
}
