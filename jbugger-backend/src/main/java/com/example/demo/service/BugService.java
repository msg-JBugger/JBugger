package com.example.demo.service;

import com.example.demo.bug_api_calls.*;
import com.example.demo.entity.Attachment;
import com.example.demo.entity.Bug;
import com.example.demo.entity.History;
import com.example.demo.enums.BugStatusEnum;
import com.example.demo.repo.AttachmentRepositoryInterface;
import com.example.demo.repo.BugRepositoryInterface;
import com.example.demo.repo.HistoryRepositoryInterface;
import com.example.demo.repo.UserRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BugService {
    private final BugRepositoryInterface bugRepository;
    private final UserRepositoryInterface userRepository;
    private final AttachmentRepositoryInterface attachmentRepository;
    private final HistoryRepositoryInterface historyRepository;

    @Autowired
    public BugService(BugRepositoryInterface bugRepository,
                      UserRepositoryInterface userRepository,
                      AttachmentRepositoryInterface attachmentRepository,
                      HistoryRepositoryInterface historyRepository) {
        this.bugRepository = bugRepository;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
        this.historyRepository = historyRepository;
    }

    public Optional<BugContent> getBugById(long bugId) {
        return bugRepository.findById(bugId)
                .map(BugContent::fromBug);
    }

    public BugSearchResponse searchBugs(BugSearchRequest request) {

        // Convert the severity enum to an integer
        int severity = switch (request.getSeverity()) {
            case LOW -> 0;
            case MEDIUM -> 1;
            case HIGH -> 2;
            case CRITICAL -> 3;
        };

        // Get all bugs from the database, sort them by target date, and filter them by the search criteria
        // todo: optimize the search by using the database's search capabilities
        // todo: sort also by severity (second priority)
        List<Bug> sortedAndFilteredBugs = bugRepository.findAll().stream()
                .sorted(Comparator.comparing(Bug::getTargetDate))
                .filter(bug -> request.getTitle() == null || bug.getTitle().contains(request.getTitle()))
                .filter(bug -> request.getDescription() == null || bug.getDescription().contains(request.getDescription()))
                .filter(bug -> request.getVersion() == null || bug.getVersion().contains(request.getVersion()))
                .filter(bug -> request.getFixedRevision() == null || bug.getFixedRevision().contains(request.getFixedRevision()))
                .filter(bug -> request.getTargetDate() == null || bug.getTargetDate().equals(request.getTargetDate()))
                .filter(bug -> request.getStatus() == null || bug.getStatus().equals(request.getStatus()))
                .filter(bug -> request.getSeverity() == null || bug.getSeverity() == severity)
                .filter(bug -> request.getReporterUsername() == null || bug.getCreatedByUser().getUsername().equals(request.getReporterUsername()))
                .filter(bug -> request.getAssigneeUsername() == null || bug.getAssignedTo().getUsername().equals(request.getAssigneeUsername()))
                .toList();

        // Get the bugs that are on the requested page
        int offset = request.getPageNumber() * request.getPageSize();
        int totalNumberOfBugs = sortedAndFilteredBugs.size();
        int totalPages = (int) Math.ceil((double) totalNumberOfBugs / request.getPageSize());

        List<Bug> bugsOnPage = sortedAndFilteredBugs.stream()
                .skip(offset)
                .limit(request.getPageSize())
                .toList();

        // Convert the bugs to BugAttributes objects
        List<BugAttributes> resultingItems = bugsOnPage.stream()
                .map(BugAttributes::fromBug)
                .toList();

        // Create and return the response
        return BugSearchResponse.builder()
                .pageNumber(request.getPageNumber())
                .pageSize(request.getPageSize())
                .totalPagesCount(totalPages)
                .totalResultsCount(totalNumberOfBugs)
                .items(resultingItems)
                .build();
    }

    public BugContent addBug(String username, BugAddRequest request) {

        // todo: initiate a transaction, so that if any of the following steps fail, the entire operation is rolled back
        //       (possible implementation: add @Transactional annotation to this method)
        // todo: split the code into multiple methods
        // todo: validation and error handling

        // ADD BUG TO THE DATABASE

        // Convert the severity enum to an integer
        int severity = switch (request.getSeverity()) {
            case LOW -> 0;
            case MEDIUM -> 1;
            case HIGH -> 2;
            case CRITICAL -> 3;
        };

        // Create a new bug and set its independent attributes
        var bug = new Bug();
        bug.setTitle(request.getTitle());
        bug.setDescription(request.getDescription());
        bug.setVersion(request.getDetectedInVersion());
        bug.setFixedRevision(request.getFixedInVersion());
        bug.setTargetDate(request.getTargetDate());
        bug.setStatus(BugStatusEnum.NEW);
        bug.setSeverity(severity);

        // Search for the reporter and assignee in the database
        var reporter = userRepository.findByUsername(username).orElseThrow();
        var assignee = userRepository.findByUsername(request.getAssigneeUsername()).orElseThrow();

        bug.setCreatedByUser(reporter);
        bug.setAssignedTo(assignee);

        // Save the bug to the database
        bug = bugRepository.save(bug);


        // ADD BUG ATTACHMENT TO THE DATABASE

        // Convert attachment content from byte[] to String by encoding the byte[] as Base64 data.
        String attachmentContentString = Base64.getEncoder().encodeToString(request.getAttachmentContent());

        // Create a new attachment
        var attachment = new Attachment();
        attachment.setAttContent(attachmentContentString);
        attachment.setBug(bug);

        // Save the attachment to the database
        attachmentRepository.save(attachment);


        // RETURN THE BUG CONTENT
        return BugContent.fromBug(bug);
    }

    public BugContent updateBug(long bugId, BugUpdateRequest request) {

        // todo: initiate a transaction
        // todo: split the code into multiple methods

        // Convert the severity enum to an integer
        int severity = switch (request.getSeverity()) {
            case LOW -> 0;
            case MEDIUM -> 1;
            case HIGH -> 2;
            case CRITICAL -> 3;
        };

        // Find the assignee in the database
        var assignee = request.getAssigneeUsername() == null ? null :
                userRepository.findByUsername(request.getAssigneeUsername()).orElseThrow();

        // Edit the bug
        var bug = bugRepository.findById(bugId).orElseThrow();
        var beforeStatus = bug.getStatus();

        if (request.getTitle() != null)
            bug.setTitle(request.getTitle());
        if (request.getDescription() != null)
            bug.setDescription(request.getDescription());
        if (request.getDetectedInVersion() != null)
            bug.setVersion(request.getDetectedInVersion());
        if (request.getFixedInVersion() != null)
            bug.setFixedRevision(request.getFixedInVersion());
        if (request.getSeverity() != null)
            bug.setSeverity(severity);
        if (request.getStatus() != null)
            bug.setStatus(request.getStatus());
        if (assignee != null)
            bug.setAssignedTo(assignee);

        // todo: validate status change before saving the bug (in separate validator or here)

        bug = bugRepository.save(bug);

        // Add a history entry if the status has changed
        if (request.getStatus() != null && request.getStatus() != beforeStatus) {

            // todo: look into more flexible date classes (LocalDate) and how to handle timezones
            Date currentDate = java.sql.Timestamp.valueOf(LocalDateTime.now());

            var historyEntry = new History();
            historyEntry.setModifiedDate(currentDate);
            historyEntry.setBeforeStatus(beforeStatus);
            historyEntry.setAfterStatus(request.getStatus());
            //historyEntry.setBug(bug);
            // todo: set the bug

            historyRepository.save(historyEntry);
        }

        // Return the bug content
        return BugContent.fromBug(bug);
    }

    public BugContent closeBug(long bugId) {
        return updateBugStatus(bugId, new BugStatusUpdateRequest(BugStatusEnum.CLOSED));
    }

    public BugContent updateBugStatus(long bugId, BugStatusUpdateRequest request) {
        var bugUpdateRequest = BugUpdateRequest.builder()
                .status(request.getStatus())
                .build();
        return updateBug(bugId, bugUpdateRequest);
    }

    public BugContentAttachment addAttachment(long bugId, BugAddAttachmentRequest request) {

        // Transform the attachment content from byte[] to String by encoding the byte[] as Base64 data.
        String attachmentContentString = Base64.getEncoder().encodeToString(request.getAttachmentContent());

        // Get the bug from the database
        var bug = bugRepository.findById(bugId).orElseThrow();

        // Create a new attachment and save it to the database
        var attachment = new Attachment();
        attachment.setAttContent(attachmentContentString);
        attachment.setBug(bug);
        // todo: set the filename (we need to add it in the db first)
        // todo: validate file size (applies when adding a bug with an attachment as well)

        attachment = attachmentRepository.save(attachment);

        // Return the bug content
        return BugContentAttachment.fromAttachment(attachment);
    }

    public void deleteAttachment(long bugId, long attachmentId) {
        // Get the attachment from the database
        var attachment = attachmentRepository.findById(attachmentId).orElseThrow();

        // Check if the attachment belongs to the bug
        if (attachment.getBug().getBugId() != bugId)
            throw new RuntimeException("The attachment does not belong to the bug.");

        // Delete the attachment from the database
        attachmentRepository.delete(attachment);
    }
}
