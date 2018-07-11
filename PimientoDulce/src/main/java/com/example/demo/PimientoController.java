package com.example.demo;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.Libro;
import com.example.model.Nota;
import com.example.model.Pager;
import com.example.model.Usuario;

@Controller
public class PimientoController {
	
	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
    
  
	
	@Autowired
	private Environment env;
	
	@Autowired
	private UsuariosHelper UsuariosHelper;

	// formulario vacio para loguearse
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
	
	@PostMapping("/procesar-login")
	public String procesarLogin(HttpSession session, @RequestParam String nombre, @RequestParam String contra) throws SQLException {
		
		boolean sePudo = UsuariosHelper.intentarLoguearse(session, nombre, contra);
		
		if (sePudo) {
			return "redirect:/";
		} else {
			return "admin";
		}
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) throws SQLException {
		UsuariosHelper.cerrarSesion(session);
		return "redirect:/";
	}
	
	@GetMapping("/")
	public String main(Model template) throws SQLException {
		
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),env.getProperty("spring.datasource.username"),env.getProperty("spring.datasource.password"));
		
		
		PreparedStatement consulta = 
				connection.prepareStatement("SELECT * FROM notas ORDER BY id DESC LIMIT 1 ;");
			
		ResultSet resultado = consulta.executeQuery();
			
		ArrayList<Nota> listadoNotas = new ArrayList<Nota>();
			
		while ( resultado.next() ) {
			int id = resultado.getInt("id");
			String titulo = resultado.getString("titulo");
			String texto = resultado.getString("texto");
				
			Nota x = new Nota(titulo, texto);
			listadoNotas.add(x);
		}
		
		template.addAttribute("listadoNotas", listadoNotas);
		
		
		PreparedStatement consulta2 = 
				connection.prepareStatement("SELECT * FROM libros ORDER BY id DESC LIMIT 1 ;");
			
			
		ResultSet resultado2 = consulta2.executeQuery();
			
		ArrayList<Libro> listadoLibros2 = new ArrayList<Libro>();
			
		while ( resultado2.next() ) {
			int id = resultado2.getInt("id");
			String titulo = resultado2.getString("titulo");
			String descripcion = resultado2.getString("descripcion");
			String resenia = resultado2.getString("resenia");
				
			Libro x = new Libro(titulo, descripcion, resenia);
			listadoLibros2.add(x);
		}
		
		template.addAttribute("listadoLibros2", listadoLibros2);
		
		PreparedStatement consulta3 = 
				connection.prepareStatement("SELECT * FROM libros ORDER BY id DESC LIMIT 1 offset 1;");
			
			
		ResultSet resultado3 = consulta3.executeQuery();
			
		ArrayList<Libro> listadoLibros3 = new ArrayList<Libro>();
			
		while ( resultado3.next() ) {
			int id = resultado3.getInt("id");
			String titulo = resultado3.getString("titulo");
			String descripcion = resultado3.getString("descripcion");
			String resenia = resultado3.getString("resenia");
				
			Libro x = new Libro(titulo, descripcion, resenia);
			listadoLibros3.add(x);
		}
		
		template.addAttribute("listadoLibros3", listadoLibros3);
		
		PreparedStatement consulta4 = 
				connection.prepareStatement("SELECT * FROM libros ORDER BY id DESC LIMIT 1 offset 2 ;");
			
			
		ResultSet resultado4 = consulta4.executeQuery();
			
		ArrayList<Libro> listadoLibros4 = new ArrayList<Libro>();
			
		while ( resultado4.next() ) {
			int id = resultado4.getInt("id");
			String titulo = resultado4.getString("titulo");
			String descripcion = resultado4.getString("descripcion");
			String resenia = resultado4.getString("resenia");
				
			Libro x = new Libro(titulo, descripcion, resenia);
			listadoLibros4.add(x);
		}
		
		template.addAttribute("listadoLibros4", listadoLibros4);
		
		return "novedades";
	}
		
	@GetMapping("/libros")
	public String libros(HttpSession session, Model template) throws SQLException {
			
		Usuario logueado = UsuariosHelper.usuarioLogueado(session);
			
		if (logueado != null) {
			return "redirect:agregarLibros";
		}
			
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),env.getProperty("spring.datasource.username"),env.getProperty("spring.datasource.password"));
			
		PreparedStatement consulta = 
				connection.prepareStatement("SELECT * FROM libros ORDER BY id DESC;");
			
		ResultSet resultado = consulta.executeQuery();
			
		ArrayList<Libro> listadoLibros = new ArrayList<Libro>();
			
		while ( resultado.next() ) {
			int id = resultado.getInt("id");
			String titulo = resultado.getString("titulo");
			String descripcion = resultado.getString("descripcion");
			String resenia = resultado.getString("resenia");
				
			Libro x = new Libro(titulo, descripcion, resenia);
			listadoLibros.add(x);
		}
		System.out.print(listadoLibros);
			
		template.addAttribute("listadoLibros", listadoLibros);
			
		return "listadoLibros";
	}
		
	@GetMapping("/libros/{titulo}")
	public String detalle(Model template, @PathVariable String titulo) throws SQLException {
			
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),env.getProperty("spring.datasource.username"),env.getProperty("spring.datasource.password"));
			
		PreparedStatement consulta = 
				connection.prepareStatement("SELECT * FROM libros WHERE titulo = ?;");
			
		consulta.setString(1, titulo);

		ResultSet resultado = consulta.executeQuery();
			
		if ( resultado.next() ) {
			String libro = resultado.getString("titulo");
			String descripcion = resultado.getString("descripcion");
			String resenia = resultado.getString("resenia");
				
			template.addAttribute("titulo", libro);
			template.addAttribute("descripcion", descripcion);
			template.addAttribute("resenia", resenia);
		}
			
		return "libro";
	}
		
	@GetMapping("/libros/pagina/{numeroPagina}")
	public String numeroPagina(HttpSession session, Model template, @PathVariable int numeroPagina, @RequestParam("pagina") Optional<Integer> pagina) throws SQLException {
		
		Usuario logueado = UsuariosHelper.usuarioLogueado(session);
		
		if (logueado != null) {
			int logged = 1;
			
			template.addAttribute("logged", logged);
		}
			
		
		if (numeroPagina == 0) {
			return "redirect:/libros/pagina/1";
		}
		
			
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),env.getProperty("spring.datasource.username"),env.getProperty("spring.datasource.password"));
		
		PreparedStatement consulta = 
				connection.prepareStatement("SELECT * FROM libros ORDER BY id DESC LIMIT 5 offset ?;");
			
		consulta.setInt(1, (numeroPagina-1)*5);
			
			
		ResultSet resultado = consulta.executeQuery();
			
		ArrayList<Libro> listadoLibros = new ArrayList<Libro>();
			
		while ( resultado.next() ) {
			int id = resultado.getInt("id");
			String titulo = resultado.getString("titulo");
			String descripcion = resultado.getString("descripcion");
			String resenia = resultado.getString("resenia");
				
			Libro x = new Libro(titulo, descripcion, resenia);
			listadoLibros.add(x);
		}
		
		consulta = connection.prepareStatement("SELECT COUNT (*) AS totalLibros FROM libros;");		

		ResultSet resultado1 = consulta.executeQuery();
		
		if (resultado1.next()) {
			int totalLibros = resultado1.getInt("totalLibros");
			
			template.addAttribute("totalLibros", totalLibros);
		}
		
		if (numeroPagina > resultado1.getInt("totalLibros")/5+1) {
			return "redirect:/libros/pagina/" + (resultado1.getInt("totalLibros")/5+1);
		}
		
		 Pager pager = new Pager(resultado1.getInt("totalLibros")/5+1, numeroPagina, BUTTONS_TO_SHOW);
			
		template.addAttribute("listadoLibros", listadoLibros);
        template.addAttribute("pager", pager);
			
		return "listadoLibros";
	}
	
	@GetMapping("/notas/pagina/{numeroPaginaNotas}")
	public String numeroPaginaNotas(HttpSession session, Model template, @PathVariable int numeroPaginaNotas, @RequestParam("pagina") Optional<Integer> pagina) throws SQLException {
		
		Usuario logueado = UsuariosHelper.usuarioLogueado(session);
		
		if (logueado != null) {
			int logged = 1;
			
			template.addAttribute("logged", logged);
		}
		
		if (numeroPaginaNotas == 0) {
			return "redirect:/notas/pagina/1";
		}
		
			
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),env.getProperty("spring.datasource.username"),env.getProperty("spring.datasource.password"));
		
		PreparedStatement consulta = 
				connection.prepareStatement("SELECT * FROM notas ORDER BY id DESC LIMIT 3 offset ?;");
			
		consulta.setInt(1, (numeroPaginaNotas-1)*3);
			
			
		ResultSet resultado = consulta.executeQuery();
			
		ArrayList<Nota> listadoNotas = new ArrayList<Nota>();
			
		while ( resultado.next() ) {
			int id = resultado.getInt("id");
			String titulo = resultado.getString("titulo");
			String texto = resultado.getString("texto");
				
			Nota x = new Nota(titulo, texto);
			listadoNotas.add(x);
		}
		
		consulta = connection.prepareStatement("SELECT COUNT (*) AS totalNotas FROM notas;");		

		ResultSet resultado1 = consulta.executeQuery();
		
		if (resultado1.next()) {
			int totalNotas = resultado1.getInt("totalNotas");
			
			template.addAttribute("totalNotas", totalNotas);
		}
		
		if (numeroPaginaNotas > resultado1.getInt("totalNotas")/3+1) {
			return "redirect:/notas/pagina/" + (resultado1.getInt("totalNotas")/3+1);
		}
		
		 Pager pager = new Pager(resultado1.getInt("totalNotas")/3+1, numeroPaginaNotas, BUTTONS_TO_SHOW);
			
		template.addAttribute("listadoNotas", listadoNotas);
        template.addAttribute("pager", pager);
			
		return "listadoNotas";
	}
		
		
	@GetMapping("/procesarLibros")
	public String procesarLibros(Model template, @RequestParam String palabraBuscada) throws SQLException {
			
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),env.getProperty("spring.datasource.username"),env.getProperty("spring.datasource.password"));
			
		PreparedStatement consulta = 
				connection.prepareStatement("SELECT * FROM libros WHERE nombre LIKE ?;");
		consulta.setString(1, "%" + palabraBuscada +  "%");
			
		ResultSet resultado = consulta.executeQuery();
			
		ArrayList<Libro> listadoLibros = new ArrayList<Libro>();
			
		while ( resultado.next() ) {
			int id = resultado.getInt("id");
			String titulo = resultado.getString("titulo");
			String descripcion = resultado.getString("descripcion");
			String resenia = resultado.getString("resenia");
				
			Libro x = new Libro(titulo, descripcion, resenia);
			listadoLibros.add(x);
		}
			
		template.addAttribute("listadoLibros", listadoLibros);
			
		return "listadoUsuarios";
	}
		
	@GetMapping("/registrarse")
	public String registrarse() {
		return "registrarse";
	}
		
	@PostMapping("/insertar-usuario")
	public String insertarUsuario(@RequestParam String nombre, @RequestParam String password, @RequestParam String email) throws SQLException {
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),env.getProperty("spring.datasource.username"),env.getProperty("spring.datasource.password"));
			
		PreparedStatement consulta = 
				connection.prepareStatement("INSERT INTO usuarios(nombre, contrasenia, email) VALUES(?, ?, ?);", PreparedStatement.RETURN_GENERATED_KEYS);
		consulta.setString(1, nombre);
		consulta.setString(2, password);
		consulta.setString(3, email);
			
		int affected = consulta.executeUpdate();
			
		if(affected == 1) {
			ResultSet gk = consulta.getGeneratedKeys();
				
				
			if (gk.next()) {
				System.out.println(gk.getInt(1));
			}
		} else {
			System.out.println( " hola");
		}
			
		connection.close();
		return "redirect:/registrarse";
	}
		
	@PostMapping("/insertar-libro")
	public String insertarLibro(@RequestParam String titulo, @RequestParam String descripcion, @RequestParam String resenia) throws SQLException {
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),env.getProperty("spring.datasource.username"),env.getProperty("spring.datasource.password"));
			
		PreparedStatement consulta = 
				connection.prepareStatement("INSERT INTO libros(titulo, descripcion, resenia) VALUES(?, ?, ?);", PreparedStatement.RETURN_GENERATED_KEYS);
		consulta.setString(1, titulo);
		consulta.setString(2, descripcion);
		consulta.setString(3, resenia);
			
		int affected = consulta.executeUpdate();
			
		if(affected == 1) {
			ResultSet gk = consulta.getGeneratedKeys();
				
				
			if (gk.next()) {
				System.out.println(gk.getInt(1));
			}
		} else {
			System.out.println( " hola");
		}
			
		connection.close();
		return "redirect:/libros/pagina/1";
	}
	
	@PostMapping("/insertar-nota")
	public String insertarNota(@RequestParam String titulo, @RequestParam String texto) throws SQLException {
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),env.getProperty("spring.datasource.username"),env.getProperty("spring.datasource.password"));
			
		PreparedStatement consulta = 
				connection.prepareStatement("INSERT INTO notas(titulo, texto) VALUES(?, ?);", PreparedStatement.RETURN_GENERATED_KEYS);
		consulta.setString(1, titulo);
		consulta.setString(2, texto);
			
		int affected = consulta.executeUpdate();
			
		if(affected == 1) {
			ResultSet gk = consulta.getGeneratedKeys();
				
				
			if (gk.next()) {
				System.out.println(gk.getInt(1));
			}
		} else {
			System.out.println( " hola");
		}
			
		connection.close();
		return "redirect:/notas/pagina/1";
	}
	

}
