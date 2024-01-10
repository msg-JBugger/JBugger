package com.example.demo;

import com.example.demo.bug_api_calls.BugAttributes;
import com.example.demo.entity.Bug;
import com.example.demo.entity.User;
import com.example.demo.enums.BugSeverity;
import com.example.demo.enums.BugStatus;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BugAttributesTest {

    @Test
    void testConstructorAndGetterMethods() {

        long bugId = 1L;
        String title = "Test Bug";
        String description = "Description of the bug";
        String detectedInVersion = "1.0";
        String fixedInVersion = "1.1";
        Date targetDate = new Date();
        BugStatus status = BugStatus.OPEN;
        BugSeverity severity = BugSeverity.LOW;
        String reporterUsername = "reporter";
        String assigneeUsername = "assignee";


        BugAttributes bugAttributes = new BugAttributes(bugId, title, description, detectedInVersion, fixedInVersion,
                targetDate, status, severity, reporterUsername, assigneeUsername);


        assertEquals(bugId, bugAttributes.getBugId());
        assertEquals(title, bugAttributes.getTitle());
        assertEquals(description, bugAttributes.getDescription());
        assertEquals(detectedInVersion, bugAttributes.getDetectedInVersion());
        assertEquals(fixedInVersion, bugAttributes.getFixedInVersion());
        assertEquals(targetDate, bugAttributes.getTargetDate());
        assertEquals(status, bugAttributes.getStatus());
        assertEquals(severity, bugAttributes.getSeverity());
        assertEquals(reporterUsername, bugAttributes.getReporterUsername());
        assertEquals(assigneeUsername, bugAttributes.getAssigneeUsername());
    }

    @Test
    void testFromBug() {

        User reporter = mock(User.class);
        when(reporter.getUsername()).thenReturn("reporter");

        User assignee = mock(User.class);
        when(assignee.getUsername()).thenReturn("assignee");

        Date mockDate = new Date();

        Bug bug = new Bug();
        bug.setBugId(1L);
        bug.setTitle("Test Bug");
        bug.setDescription("Description of the bug");
        bug.setDetectedInVersion("1.0");
        bug.setFixedInVersion("1.1");
        bug.setTargetDate(mockDate);
        bug.setStatus(BugStatus.OPEN);
        bug.setSeverity(BugSeverity.LOW);
        bug.setCreatedByUser(reporter);
        bug.setAssignedToUser(assignee);

        BugAttributes expectedResult = getBugAttributes(mockDate);

        BugAttributes actualResult = BugAttributes.fromBug(bug);

        assertEquals(expectedResult, actualResult);
        
    }

    private static BugAttributes getBugAttributes(Date mockDate) {
        BugAttributes expectedResult = new BugAttributes();

        expectedResult.setBugId(1L);
        expectedResult.setTitle("Test Bug");
        expectedResult.setDescription("Description of the bug");
        expectedResult.setDetectedInVersion("1.0");
        expectedResult.setFixedInVersion("1.1");
        expectedResult.setTargetDate(mockDate);
        expectedResult.setStatus(BugStatus.OPEN);
        expectedResult.setSeverity(BugSeverity.LOW);
        expectedResult.setReporterUsername("reporter");
        expectedResult.setAssigneeUsername("assignee");
        return expectedResult;
    }

}
