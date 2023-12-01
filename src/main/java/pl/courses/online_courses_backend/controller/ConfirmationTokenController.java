package pl.courses.online_courses_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.courses.online_courses_backend.service.ConfirmationTokenService;

@RestController
@RequestMapping("/api/tokens")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ConfirmationTokenController {

    private final ConfirmationTokenService confirmationTokenService;

    @GetMapping("confirm")
    public ResponseEntity<String> confirm(@RequestParam String token) {
        confirmationTokenService.confirmAccount(token);
        return ResponseEntity.ok("Account confirmed");
    }
}
