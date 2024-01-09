package com.example.demo.controller;

import com.example.demo.bug_api_calls.*;
import com.example.demo.enums.BugSeverity;
import com.example.demo.enums.BugStatus;
import com.example.demo.service.BugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEST_MANAGER', 'ROLE_DEVELOPER')")
@RequestMapping("/api/bug")
public class BugController {
    private final BugService bugService;

    @Autowired
    public BugController(BugService bugService) {
        this.bugService = bugService;
    }


    @GetMapping("/{bugId}")
    public ResponseEntity<BugContent> getBugById(@PathVariable long bugId) {
        var bugGetByIdResponse = bugService.getBugById(bugId);
        return bugGetByIdResponse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<BugSearchResponse> searchBugs(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String version,
            @RequestParam String fixedRevision,
            @RequestParam String targetDate,
            @RequestParam String status,
            @RequestParam String severity,
            @RequestParam String reporterUsername,
            @RequestParam String assigneeUsername
    ) {
        var request = BugSearchRequest
                .builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .title(Objects.equals(title, "") ? null : title)
                .description(Objects.equals(description, "") ? null : description)
                .version(Objects.equals(version, "") ? null : version)
                .fixedRevision(Objects.equals(fixedRevision, "") ? null : fixedRevision)
                //.targetDate(new SimpleDateFormat("MM-dd-yyyy").parse(targetDate))
                .targetDate(null)
                //.status(BugStatus.valueOf(status))
                .status(null)
                //.severity(BugSeverity.valueOf(severity))
                .severity(null)
                .reporterUsername(Objects.equals(reporterUsername, "") ? null : reporterUsername)
                .assigneeUsername(Objects.equals(assigneeUsername, "") ? null : assigneeUsername)
                .build();

        var bugSearchResponse = bugService.searchBugs(request);
        return ResponseEntity.ok(bugSearchResponse);
    }

    @PostMapping("/add")
    public ResponseEntity<BugContent> addBug(@RequestParam String username, @RequestBody BugAddRequest request) {
        var bugAddResponse = bugService.addBug(username, request);
        return ResponseEntity.ok(bugAddResponse);
    }

    @PutMapping("/update/{bugId}")
    public ResponseEntity<BugContent> updateBug(@PathVariable long bugId, @RequestBody BugUpdateRequest request) {
        var bugUpdateResponse = bugService.updateBug(bugId, request);
        return ResponseEntity.ok(bugUpdateResponse);
    }

    @PatchMapping("/close/{bugId}")
    public ResponseEntity<BugContent> closeBug(@PathVariable long bugId) {
        var bugCloseResponse = bugService.closeBug(bugId);
        return ResponseEntity.ok(bugCloseResponse);
    }

    @PatchMapping("/statusUpdate/{bugId}")
    public ResponseEntity<BugContent> updateBugStatus(@PathVariable long bugId, @RequestBody BugStatusUpdateRequest request) {
        var bugStatusUpdateResponse = bugService.updateBugStatus(bugId, request);
        return ResponseEntity.ok(bugStatusUpdateResponse);
    }

    @PostMapping("/addAttachment/{bugId}")
    public ResponseEntity<BugContentAttachment> addAttachment(@PathVariable long bugId, @RequestBody BugAddAttachmentRequest request) {
        var bugAddAttachmentResponse = bugService.addAttachment(bugId, request);
        return ResponseEntity.ok(bugAddAttachmentResponse);
    }


    @DeleteMapping("/deleteAttachment/{bugId}/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable long bugId, @PathVariable long attachmentId) {
        bugService.deleteAttachment(bugId, attachmentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
