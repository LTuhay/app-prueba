package app.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import app.entities.Note;
import app.entities.User;
import app.services.IUserService;
import app.dto.RequestUserDTO;
import app.dto.ResponseNoteDTO;
import app.dto.ResponseUserDTO;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private IUserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody RequestUserDTO requestUserDTO) {
		try {
			if (userService.existsByUsername(requestUserDTO.getUsername())) {
				return ResponseEntity.badRequest().body("Username is already taken!");
			}
			User user = new User();
			user.setUsername(requestUserDTO.getUsername());
			user.setPassword(passwordEncoder.encode(requestUserDTO.getPassword()));
			userService.save(user);
			
			return new ResponseEntity<>(new ResponseUserDTO(user), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		
		try {
			Optional<User> userOptional = userService.findById(id);

			if (!userOptional.isPresent()) {
				return ResponseEntity.notFound().build();
			}

			User user = userOptional.get();
			userService.delete(user);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
