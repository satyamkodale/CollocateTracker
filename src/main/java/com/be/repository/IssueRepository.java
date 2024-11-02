package com.be.repository;

import com.be.entity.Issue;
import com.be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue,Long> {
    List<Issue> findByAssigneeId(Long assigneeId);

    @Query("SELECT i FROM Issue i " +
            "LEFT JOIN i.assignee a " +
            "WHERE (:title IS NULL OR LOWER(i.title) LIKE %:title%) " +
            "AND (:status IS NULL OR i.status = :status) " +
            "AND (:priority IS NULL OR i.priority = :priority) " +
            "AND (:assigneeId IS NULL OR a.id = :assigneeId)")
    List<Issue> searchIssues(
            @Param("title") String title,
            @Param("status") String status,
            @Param("priority") String priority,
            @Param("assigneeId") Long assigneeId
    );

    @Query("SELECT i.assignee FROM Issue i WHERE i.id = :issueId")
    List<User> findAssigneeByIssueId(@Param("issueId") Long issueId);

    @Query("SELECT i FROM Issue i LEFT JOIN FETCH i.comments WHERE i.id = :issueId")
    Issue findIssueWithComments(@Param("issueId") Long issueId);

    List<Issue> findByProjectId(Long projectId);
}
