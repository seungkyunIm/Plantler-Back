package com.plantler.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestBoardFileDTO {

	private int board_type;
	private int board_no;
	private String board_title;
	private String board_content; 
	private int category_id;
	private MultipartFile file;
	
}
