package org.example.adds.Users;

import lombok.AllArgsConstructor;
import org.example.adds.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @DeleteMapping("/delete/{phone}")
    public ResponseEntity<Response> deleteUser(@PathVariable String phone){
        try {
            return ResponseEntity.ok(usersService.deleteByPhone(phone));
        }catch (NoSuchElementException ex){
            return ResponseEntity.status(404).body(new Response(ex.getMessage(), false));
        }
    }
}
