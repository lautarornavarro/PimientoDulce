package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PimientoController {
	
		@GetMapping("/")
		public String main() {
			return "main";
		}
		
		@GetMapping("/libros")
		public String libros() {
			return "libros";
		}
		
		@GetMapping("/registrarse")
		public String registrarse() {
			return "registrarse";
		}
		
		@PostMapping("/insertar-usuario")
		public String insertarUsuario(@RequestParam String nombre, @RequestParam String password, @RequestParam String email) throws SQLException {
			Connection connection;
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Ejemplo","postgres","admin");
			
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
	

}
