package com.desafio.hierarquia.controllers;

import com.desafio.hierarquia.services.HierarchyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/hierarchy")
public class HierarchyController {

    @Autowired
    private HierarchyService hierarchyService;

    @GetMapping("/get")
    public ResponseEntity<Map<String, Object>> getAll() {
        Map<String, Object> response = hierarchyService.getHierarchy();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Map<String, Object> hierarchy) {
        hierarchyService.saveHierarchy(hierarchy);
        return ResponseEntity.ok("Saved");
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download() {
        byte[] jsonFile = hierarchyService.generateJsonFile();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=hierarquia.json");
        return new ResponseEntity<>(jsonFile, headers, HttpStatus.OK);
    }
}
