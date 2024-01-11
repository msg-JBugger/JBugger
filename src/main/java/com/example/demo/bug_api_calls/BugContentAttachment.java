package com.example.demo.bug_api_calls;

import com.example.demo.entity.Attachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BugContentAttachment {
    long attachmentId;
    String attachmentFilename;
    String attachmentContent;

    public static BugContentAttachment fromAttachment(Attachment attachment) {

        // Convert attachment content from String to byte[] by interpreting the String as Base64 encoded data.
//        byte[] attachmentContent = Base64.getDecoder().decode(attachment.getAttContent());
        String content = Base64.getEncoder().encodeToString(attachment.getAttContent());
        return BugContentAttachment.builder()
                .attachmentId(attachment.getAttachmentId())
                .attachmentFilename(attachment.getAttFilename())
                .attachmentContent(content)
                .build();
    }
}
