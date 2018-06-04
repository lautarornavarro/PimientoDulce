package com.example.model;

public class Libro {
	private int id;
	private String titulo;
	private String descripcion;
	private String resenia;
	
	public Libro(int id, String titulo, String descripcion, String resenia) {
		this.id = id;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.resenia = resenia;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getResenia() {
		return resenia;
	}

	public void setResenia(String resenia) {
		this.resenia = resenia;
	}

	
	
	
}
