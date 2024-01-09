package com.example.demo.repo;

import com.example.demo.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepositoryInterface extends JpaRepository<Attachment, Long> {
}
