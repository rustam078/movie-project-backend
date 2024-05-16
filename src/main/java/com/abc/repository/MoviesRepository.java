package com.abc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abc.entity.MoviesList;

@Repository
public interface MoviesRepository extends JpaRepository<MoviesList,Long>{

	Optional<MoviesList> findByImagename(String imagename);
	
	List<MoviesList> findByMovienameContaining(String name);

	
}
