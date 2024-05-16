package com.abc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.abc.dto.SearchDto;
import com.abc.entity.MoviesList;
import com.abc.service.MoviesServices;

@RestController
@CrossOrigin
public class MoviesController {

	@Autowired
	private MoviesServices moviesServices;
	
	@Autowired
    private SimpMessagingTemplate messagingTemplate;
	
	@GetMapping("/")
	public String welcome() {
		return "Welcome to first movies project";
	}
	
	
	@PostMapping("/save")
	public String saveMovies(@ModelAttribute MoviesList movie,@RequestParam("file") MultipartFile file) {
	   MoviesList saveMovies = moviesServices.saveMovies(movie,file);
	    if(saveMovies!=null) {
	   messagingTemplate.convertAndSend("/topic/uploadNotification", "Hi "+movie.getMoviename()+" movie is uploaded on platform Watch now and enjoy!^"+saveMovies.getImagename());  

	    }
		return "movie added sucessfully "+saveMovies.getId();
		
	}
	
	 @GetMapping("/all")
	    public Page<MoviesList> getAllMovies(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size) {
	        return moviesServices.getAllMovies(PageRequest.of(page, size));
	    }
	
	
	@GetMapping("/getbyid/{id}")
	public MoviesList getMoviesById(@PathVariable Long id){	
		return moviesServices.getMoviesById(id);
	}
	
	@PutMapping("/update/{id}")
	public MoviesList updateMoviesById(@RequestBody MoviesList movie, @PathVariable Long id){	
		return moviesServices.updateMoviesById(id,movie);
	}

	@DeleteMapping("/deteteMoviesById/{id}")
	public String deleteMoviesById(@PathVariable Long id){	
		return moviesServices.deleteMoviesById(id);
	}
	
	@PostMapping("/search")
	public List<MoviesList> searchMoviesByField(@RequestBody SearchDto searchDto){	
		return moviesServices.searchMoviesByField(searchDto);
	}
	
	@PutMapping("/images/{movieid}")
	public ResponseEntity<?> updateDoctorImage(@PathVariable Long movieid, @RequestParam("file") MultipartFile file) {
		return moviesServices.updateDoctorImage(movieid, file);
	}
	
	@GetMapping("/images/{imagename}")
	public ResponseEntity<?> downloadImage(@PathVariable String imagename) {
		byte[] imageData = moviesServices.downloadImage(imagename);
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);

	}
	
	
	@GetMapping("/search")
	public List<MoviesList> searchByMovieName(@RequestParam String name){
		return moviesServices.searchByMovieName(name);
	}
	
	

}
