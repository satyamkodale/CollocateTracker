package com.be.service;

import com.be.dto.request.IssueRequest;
import com.be.entity.Issue;
import com.be.entity.User;

import java.util.*;

public interface IssueService {
    //	 List<Issue> getAllIssues() throws IssueException;

    Optional<Issue> getIssueById(Long issueId) throws Exception;

    List<Issue> getIssueByProjectId(Long projectId) throws Exception;

    Issue createIssue(IssueRequest issue, Long userid) throws  Exception;

    Optional<Issue> updateIssue(Long issueid,IssueRequest updatedIssue,Long userid ) throws Exception;

    String deleteIssue(Long issueId,Long userid) throws Exception;

    List<Issue> getIssuesByAssigneeId(Long assigneeId) throws Exception;

    List<Issue> searchIssues(String title, String status, String priority, Long assigneeId) throws Exception;

    List<User> getAssigneeForIssue(Long issueId) throws Exception;

    Issue addUserToIssue(Long issueId, Long userId) throws Exception;

    Issue updateStatus(Long issueId, String status) throws Exception;

}
