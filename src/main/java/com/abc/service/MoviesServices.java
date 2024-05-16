package com.abc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.abc.dto.SearchDto;
import com.abc.entity.MoviesList;
import com.abc.repository.MoviesRepository;
import com.abc.utils.ImageUtils;

@Service
public class MoviesServices {
	
	
	@Autowired
	private MoviesRepository moviesRepository;

	public MoviesList saveMovies(MoviesList movie, MultipartFile file) {
		byte[] compressImage = extracted(file);
		if(compressImage==null)return null;
	    movie.setImage(compressImage);
        movie.setImagename(file.getOriginalFilename() + "_" + System.currentTimeMillis());
		MoviesList save = moviesRepository.save(movie);
		return save;
		
	}


	public List<MoviesList> getAllMovies() {
		return moviesRepository.findAll();
	}

	
	public MoviesList getMoviesById(Long id) {
		 Optional<MoviesList> byId = moviesRepository.findById(id);
		 MoviesList moviesList = byId.get();
		 return moviesList;
	}

	
	public MoviesList updateMoviesById(Long id, MoviesList movie) {
		Optional<MoviesList> byId = moviesRepository.findById(id);   
		if(!byId.isPresent()) {
			return null;
		}
		MoviesList moviesList = byId.get();
		
		
		if(movie.getActor()!=null) {
			moviesList.setActor(movie.getActor());
		}
		if(movie.getLanguage()!=null) {
			moviesList.setLanguage(movie.getLanguage());
		}
		if(movie.getMoviename()!=null) {
			moviesList.setMoviename(movie.getMoviename());
		}
		if(movie.getRating()>0) {
			moviesList.setRating(movie.getRating());
		}
		if(movie.getYear()>1000) {
			moviesList.setYear(movie.getYear());
		}
		if(movie.getMovietype()!=null) {
			moviesList.setMovietype(movie.getMovietype());
		}
		if(movie.getImagename()!=null) {
			moviesList.setImagename(movie.getImagename());
		}
		
		
		return moviesRepository.save(moviesList);
	}

	public String deleteMoviesById(Long id)
	{
		if(moviesRepository.existsById(id)) {
			moviesRepository.deleteById(id);
			return "movie deleted sucessfully";
		}else {
			return "movie not found";
		}
		
	}

	  public List<MoviesList> searchMoviesByField(SearchDto searchDto) {
	        MoviesList moviesList = new MoviesList();
	        ExampleMatcher matcher = ExampleMatcher.matching()
	                .withIgnoreCase()
	                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
	                .withIgnorePaths("id");

	        if (StringUtils.hasLength(searchDto.getActor())) {
	            moviesList.setActor(searchDto.getActor());
	            matcher = matcher.withMatcher("actor", ExampleMatcher.GenericPropertyMatchers.contains());
	        }

	        if (StringUtils.hasLength(searchDto.getLanguage())) {
	            moviesList.setLanguage(searchDto.getLanguage());
	        }

	        if (StringUtils.hasLength(searchDto.getMovietype())) {
	            moviesList.setMovietype(searchDto.getMovietype());
	        }

	        if (searchDto.getRating() > 0) {
	            moviesList.setRating(searchDto.getRating());
	            matcher = matcher.withMatcher("rating", ExampleMatcher.GenericPropertyMatchers.exact());
	        } else {
	            matcher = matcher.withIgnorePaths("rating");
	        }

	        if (searchDto.getYear() > 0) {
	            moviesList.setYear(searchDto.getYear());
	            matcher = matcher.withMatcher("year", ExampleMatcher.GenericPropertyMatchers.exact());
	        } else {
	            matcher = matcher.withIgnorePaths("year");
	        }

	        Example<MoviesList> example = Example.of(moviesList, matcher);

	        return moviesRepository.findAll(example);
	    }

	  
	public ResponseEntity<?> updateDoctorImage(Long movieid, MultipartFile file) {
		Optional<MoviesList> movie = moviesRepository.findById(movieid);
		if(!movie.isPresent()) {
			return null;
		}
		byte[] compressImage = extracted(file);
		if(compressImage==null)  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user.");;
	   MoviesList moviesList = movie.get();	
	    moviesList.setImage(compressImage);
        moviesList.setImagename(file.getOriginalFilename() + "_" + System.currentTimeMillis());
	     moviesRepository.save(moviesList);
		 return ResponseEntity.status(HttpStatus.OK).body("Image Uploaded Sucessfully");
	}
		

	private byte[] extracted(MultipartFile file) {
		byte[] compressImage = null;
		if (file.isEmpty()) {
          return null;
		}
		try {
			byte[] fileBytes = file.getBytes();
			compressImage = ImageUtils.compressImage(fileBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return compressImage;
	}


	public byte[] downloadImage(String imagename) {
			Optional<MoviesList> findByImagename = moviesRepository.findByImagename(imagename);
			byte[] images = ImageUtils.decompressImage(findByImagename.get().getImage());
			return images;
	}
	
	public List<MoviesList> searchByMovieName(String name){
		return moviesRepository.findByMovienameContaining(name);
	}



	    public Page<MoviesList> getAllMovies(Pageable pageable) {
	        return moviesRepository.findAll(pageable);
	    }
}
