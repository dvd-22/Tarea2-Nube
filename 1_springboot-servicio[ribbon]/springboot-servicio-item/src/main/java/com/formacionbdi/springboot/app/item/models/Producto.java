package com.formacionbdi.springboot.app.item.models;

import java.util.Date;

public class Producto {
	
	private Long id;
	private String marca;
	private int año;
	private double precio ;
	private Date createAt;
	private String modelo;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public int getAño() {
		return año;
	}
	public void setAño(int año) {
		this.año = año;
	}
	public double getPrecio(){
		return precio;
	}
	public void setPrecio(double precio){
		this.precio = precio;
	}
	public String getModelo(){
		return modelo;
	}
	public void setModelo(String modelo){
		this.modelo=modelo;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	

}
