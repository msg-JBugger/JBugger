package com.example.demo.service;

import com.example.demo.bug_api_calls.*;
import com.example.demo.entity.Attachment;
import com.example.demo.entity.Bug;
import com.example.demo.entity.History;
import com.example.demo.enums.BugStatus;
import com.example.demo.repo.AttachmentRepositoryInterface;
import com.example.demo.repo.BugRepositoryInterface;
import com.example.demo.repo.HistoryRepositoryInterface;
import com.example.demo.repo.UserRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
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

        // Get all bugs from the database, sort them by target date, and filter them by the search criteria
        // todo: optimize the search by using database capabilities
        // todo: check if sort order is ok (ascending/descending)
        List<Bug> sortedAndFilteredBugs = bugRepository.findAll().stream()
                .sorted(Comparator.comparing(Bug::getTargetDate))
                .sorted(Comparator.comparing(Bug::getSeverity))
                .filter(bug -> request.getTitle() == null || bug.getTitle().contains(request.getTitle()))
                .filter(bug -> request.getDescription() == null || bug.getDescription().contains(request.getDescription()))
                .filter(bug -> request.getVersion() == null || bug.getDetectedInVersion().contains(request.getVersion()))
                .filter(bug -> request.getFixedRevision() == null || bug.getFixedInVersion().contains(request.getFixedRevision()))
                .filter(bug -> request.getTargetDate() == null || bug.getTargetDate().equals(request.getTargetDate()))
                .filter(bug -> request.getStatus() == null || bug.getStatus().equals(request.getStatus()))
                .filter(bug -> request.getSeverity() == null || bug.getSeverity() == request.getSeverity())
                .filter(bug -> request.getReporterUsername() == null || bug.getCreatedByUser().getUsername().equals(request.getReporterUsername()))
                .filter(bug -> request.getAssigneeUsername() == null || bug.getAssignedToUser().getUsername().equals(request.getAssigneeUsername()))
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
                .items(resultingItems)
                .build();
    }

    public BugContent addBug(String username, BugAddRequest request) {

        // todo: verify transactional behavior (if the bug is not saved, the attachment should not be saved either)
        // todo: validation and error handling

        // Save the bug and attachment to the database
        var bug = saveBug(username, request);
        saveAttachment(bug.getBugId(), request.getAttachmentFilename(), request.getAttachmentContent());

        // Return the bug content
        return BugContent.fromBug(bug);
    }

    public BugContent updateBug(long bugId, BugUpdateRequest request) {
        // Update the bug in the database
        var previousBugStatus = bugRepository.findById(bugId).orElseThrow().getStatus();
        Bug bug = overrideBug(bugId, request);

        // Add history entry
        saveHistory(previousBugStatus, bug);

        // Return the bug content
        return BugContent.fromBug(bug);
    }

    public BugContent closeBug(long bugId) {
        // We can close the bug by updating the bug status to CLOSED
        return updateBugStatus(bugId, new BugStatusUpdateRequest(BugStatus.CLOSED));
    }

    public BugContent updateBugStatus(long bugId, BugStatusUpdateRequest request) {
        // We can update the bug status by updating the whole bug and changing only the status
        var bugUpdateRequest = BugUpdateRequest.builder()
                .status(request.getStatus())
                .build();
        return updateBug(bugId, bugUpdateRequest);
    }

    public BugContentAttachment addAttachment(long bugId, BugAddAttachmentRequest request) {
        // Save the attachment to the database
        var attachment = saveAttachment(bugId, request.getAttachmentFilename(), request.getAttachmentContent());

        // Return the attachment content
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

    Bug saveBug(String username, BugAddRequest request) {
        // Create a new bug and set its independent attributes
        var bug = new Bug();
        bug.setTitle(request.getTitle());
        bug.setDescription(request.getDescription());
        bug.setDetectedInVersion(request.getDetectedInVersion());
        bug.setFixedInVersion(request.getFixedInVersion());
        bug.setTargetDate(request.getTargetDate());
        bug.setStatus(BugStatus.OPEN);
        bug.setSeverity(request.getSeverity());

        // Search for the reporter and assignee in the database
        var reporter = userRepository.findByUsername(username).orElseThrow();
        var assignee = userRepository.findByUsername(request.getAssigneeUsername()).orElseThrow();

        bug.setCreatedByUser(reporter);
        bug.setAssignedToUser(assignee);

        // Save the bug to the database
        bug = bugRepository.save(bug);
        return bug;
    }

    Attachment saveAttachment(long bugId, String attachmentFilename, byte[] attachmentContent) {
        // Get the bug from the database
        var bug = bugRepository.findById(bugId).orElseThrow();

        // Create a new attachment
        var attachment = new Attachment();
        attachment.setAttFilename(attachmentFilename);
        attachment.setAttContent(attachmentContent);
        attachment.setBug(bug);
        // todo: validate file size (applies when adding a bug with an attachment as well)

        // Save the attachment to the database
        attachment = attachmentRepository.save(attachment);
        return attachment;
    }

    void saveHistory(BugStatus beforeStatus, Bug bug) {
        // If the status has not changed, do not create a history entry
        if (bug.getStatus() == beforeStatus)
            return;

        // Get current date
        Date currentDate = java.sql.Timestamp.valueOf(LocalDateTime.now());
        // todo: look into more flexible date classes (LocalDate) and how to handle timezones

        // Create a new history entry
        var historyEntry = new History();
        historyEntry.setModifiedDate(currentDate);
        historyEntry.setBeforeStatus(beforeStatus);
        historyEntry.setAfterStatus(bug.getStatus());
        historyEntry.setBug(bug);

        // Save the history entry to the database
        historyRepository.save(historyEntry);
    }

    Bug overrideBug(long bugId, BugUpdateRequest request) {

        // Find the assignee in the database
        var assignee = request.getAssigneeUsername() == null ? null :
                userRepository.findByUsername(request.getAssigneeUsername()).orElseThrow();

        // Edit the bug
        var bug = bugRepository.findById(bugId).orElseThrow();

        if (request.getTitle() != null)
            bug.setTitle(request.getTitle());
        if (request.getDescription() != null)
            bug.setDescription(request.getDescription());
        if (request.getDetectedInVersion() != null)
            bug.setDetectedInVersion(request.getDetectedInVersion());
        if (request.getFixedInVersion() != null)
            bug.setFixedInVersion(request.getFixedInVersion());
        if (request.getTargetDate() != null)
            bug.setTargetDate(request.getTargetDate());
        if (request.getSeverity() != null)
            bug.setSeverity(request.getSeverity());
        if (request.getStatus() != null)  {
            validateStatusChange(bug.getStatus(), request.getStatus());
            bug.setStatus(request.getStatus());
        }
        if (assignee != null)
            bug.setAssignedToUser(assignee);

        bug = bugRepository.save(bug);
        return bug;
    }

    void validateStatusChange(BugStatus prevStatus, BugStatus newStatus) {
        if (prevStatus == newStatus || prevStatus == null)
            return;

        if (prevStatus == BugStatus.OPEN && (newStatus == BugStatus.IN_PROGRESS || newStatus == BugStatus.REJECTED))
            return;

        if (prevStatus == BugStatus.IN_PROGRESS &&
                (newStatus == BugStatus.INFO_NEEDED || newStatus == BugStatus.FIXED || newStatus == BugStatus.REJECTED))
            return;

        if (prevStatus == BugStatus.INFO_NEEDED && newStatus == BugStatus.IN_PROGRESS)
            return;

        if (prevStatus == BugStatus.FIXED && (newStatus == BugStatus.CLOSED || newStatus == BugStatus.OPEN))
            return;

        if (prevStatus == BugStatus.REJECTED && newStatus == BugStatus.CLOSED)
            return;

        throw new RuntimeException("Invalid status change.");
    }
}
