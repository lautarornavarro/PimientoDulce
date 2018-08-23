package com.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Libro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String titulo;
	private String descripcion;
	private String resenia;
	private String imagen;
	
	
	
	public Libro(int id, String titulo, String descripcion, String resenia, String imagen) {
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.resenia = resenia;
		this.imagen = imagen;
		this.id = id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getImagen() {
		return imagen;
	}



	public void setImagen(String imagen) {
		this.imagen = imagen;
	}



	public long getId() {
		return id;
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



	@Override
	public String toString() {
		return "Libro [id=" + id + ", titulo=" + titulo + ", descripcion=" + descripcion + ", resenia=" + resenia
				+ ", imagen=" + imagen + "]";
	}


	
	
	
}
