package com.example.demo;

import com.example.demo.bug_api_calls.BugContentHistory;
import com.example.demo.entity.History;
import com.example.demo.enums.BugStatus;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BugContentHistoryTest {

    @Test
    void testConstructorAndGetterMethods() {
        long historyId = 1L;
        Date modifiedDate = new Date();
        BugStatus beforeStatus = BugStatus.OPEN;
        BugStatus afterStatus = BugStatus.IN_PROGRESS;

        BugContentHistory bugContentHistory = new BugContentHistory(historyId, modifiedDate, beforeStatus, afterStatus);

        assertEquals(historyId, bugContentHistory.getHistoryId());
        assertEquals(modifiedDate, bugContentHistory.getModifiedDate());
        assertEquals(beforeStatus, bugContentHistory.getBeforeStatus());
        assertEquals(afterStatus, bugContentHistory.getAfterStatus());
    }

    @Test
    void testFromHistory() {
        History history = new History();
        Date mockDate = new Date();
        history.setHistoryId(1L);
        history.setModifiedDate(mockDate);
        history.setBeforeStatus(BugStatus.OPEN);
        history.setAfterStatus(BugStatus.IN_PROGRESS);

        BugContentHistory expectedResult = new BugContentHistory();

        expectedResult.setHistoryId(1L);
        expectedResult.setModifiedDate(mockDate);
        expectedResult.setBeforeStatus(BugStatus.OPEN);
        expectedResult.setAfterStatus(BugStatus.IN_PROGRESS);

        BugContentHistory actualResult = BugContentHistory.fromHistory(history);

        assertEquals(expectedResult, actualResult);

    }

}
