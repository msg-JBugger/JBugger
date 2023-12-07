package com.example.demo.bug_api_calls;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BugAddAttachmentRequest {
    String attachmentFilename;
    byte[] attachmentContent;
}
