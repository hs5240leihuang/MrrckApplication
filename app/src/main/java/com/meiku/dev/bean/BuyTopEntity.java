package com.meiku.dev.bean;

public class BuyTopEntity {
	
	private String daysString;
	private int days;
	private String discountString;
	private double discount;
	
	public String getDaysString() {
		return daysString;
	}
	public void setDaysString(String daysString) {
		this.daysString = daysString;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public String getDiscountString() {
		return discountString;
	}
	public void setDiscountString(String discountString) {
		this.discountString = discountString;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public BuyTopEntity(String daysString, int days, String discountString,
			double discount) {
		super();
		this.daysString = daysString;
		this.days = days;
		this.discountString = discountString;
		this.discount = discount;
	}
	
}
