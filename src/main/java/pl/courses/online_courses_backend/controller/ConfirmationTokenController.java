package pl.courses.online_courses_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.courses.online_courses_backend.service.ConfirmationTokenService;

@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
public class ConfirmationTokenController {

    private final ConfirmationTokenService confirmationTokenService;

    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam String token) {
        return ResponseEntity.ok(confirmationTokenService.confirmAccount(token));
    }
}
