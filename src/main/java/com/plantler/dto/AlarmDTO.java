package com.plantler.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDTO {
		
	private int user_no;
	private int alarm_no;
	private String alarm_plantname;
	private String alarm_memo;
	
	private LocalDateTime alarm_scheduled;
	private String alarm_scheduled2;
	private LocalDateTime alarm_repot;
	private String alarm_repot2;
	private LocalDateTime alarm_nutrients;
	private String alarm_nutrients2;
	
	private LocalDateTime alarm_regdate;
	private LocalDateTime alarm_moddate;
	
}
