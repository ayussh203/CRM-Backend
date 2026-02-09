package com.crm.server.controller;

import com.crm.server.dto.LeadDTO;
import com.crm.server.entity.Lead;
import com.crm.server.service.LeadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    @PostMapping
    public ResponseEntity<Lead> createLead(@RequestBody LeadDTO leadDTO) {
        return ResponseEntity.ok(leadService.createLead(leadDTO));
    }

    @GetMapping
    public ResponseEntity<List<Lead>> getLeads() {
        return ResponseEntity.ok(leadService.getMyLeads());
    }
}