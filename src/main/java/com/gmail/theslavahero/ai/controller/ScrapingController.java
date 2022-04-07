package com.gmail.theslavahero.ai.controller;

import com.gmail.theslavahero.ai.Runner;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScrapingController {
    Runner runner;
    @PostMapping("/start")
    public ResponseEntity<String> startInitialScraping() {
        runner.run();
        return ResponseEntity.ok("NICE");
    }
}
