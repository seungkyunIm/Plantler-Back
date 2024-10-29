package com.plantler.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plantler.docs.HotelControllerDoc;

@RestController
public class HotelController implements HotelControllerDoc {
	
	@GetMapping("/hotellist")
	public String hotellist() {
		return "hotel/hotellist";
	}
	@GetMapping("/hotelmaps")
	public String hotelmaps() {
		return "hotel/hotelmaps";
	}

}
