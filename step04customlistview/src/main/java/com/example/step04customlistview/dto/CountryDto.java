package com.example.step04customlistview.dto;

import java.io.Serializable;

public class CountryDto implements Serializable {
	private int imageResId;
	private String name;

	public CountryDto() {}

	public CountryDto(int imageResId, String name) {
		this.imageResId = imageResId;
		this.name = name;
	}

	public int getImageResId() {
		return imageResId;
	}

	public void setImageResId(int imageResId) {
		this.imageResId = imageResId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
