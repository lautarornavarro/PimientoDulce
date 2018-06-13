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
import com.example.model.Pager;
import com.example.model.Usuario;

@Controller
public class PimientoController {
	
	private static final int BUTTONS_TO_SHOW = 5;
    
  
	
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
		return "redirect:/admin";
	}
	
	@GetMapping("/")
	public String main() {
		return "fragments/main";
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
			int logued = 1;
			
			template.addAttribute("logued", logued);
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
	
	

}
