package com.plantler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestBoardDTO {

	private int board_type;
	private String query;
	private int category_id;
	private int page;
	private int size;
	
}
