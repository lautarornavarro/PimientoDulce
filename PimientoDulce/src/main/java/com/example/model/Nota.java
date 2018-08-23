package com.example.model;

public class Nota {
	private int id;
	private String titulo;
	private String texto;
	private String imagennota;
	
	
	
	
	public Nota(String titulo, String texto, String imagennota) {
		this.titulo = titulo;
		this.texto = texto;
		this.imagennota = imagennota;
	}




	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public String getTitulo() {
		return titulo;
	}




	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}




	public String getTexto() {
		return texto;
	}




	public void setTexto(String texto) {
		this.texto = texto;
	}




	public String getImagennota() {
		return imagennota;
	}




	public void setImagennota(String imagennota) {
		this.imagennota = imagennota;
	}




	@Override
	public String toString() {
		return "Nota [id=" + id + ", titulo=" + titulo + ", texto=" + texto + ", imagennota=" + imagennota + "]";
	}

}

