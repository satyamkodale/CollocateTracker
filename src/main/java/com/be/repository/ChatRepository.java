package com.be.repository;

import com.be.entity.Chat;
import com.be.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository  extends JpaRepository<Chat,Long> {
    Chat findByProject(Project projectById);

//	List<Chat> findByProjectNameContainingIgnoreCase(String projectName);
}
