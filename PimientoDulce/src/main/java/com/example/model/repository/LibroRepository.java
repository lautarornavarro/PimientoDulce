package com.example.model.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.example.model.Libro;

@Repository
public interface LibroRepository extends PagingAndSortingRepository<Libro, Long>{

}
