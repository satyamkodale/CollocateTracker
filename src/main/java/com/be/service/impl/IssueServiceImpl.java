package com.be.service.impl;

import com.be.dto.request.IssueRequest;
import com.be.entity.Issue;
import com.be.entity.Project;
import com.be.entity.User;
import com.be.repository.IssueRepository;
import com.be.service.IssueService;
import com.be.service.ProjectService;
import com.be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//TODO HANDLE EXCEPTIONS

@Service
public class IssueServiceImpl implements IssueService {
    @Autowired
    private IssueRepository issueRepository;
    //
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;

    @Autowired
    private NotificationServiceImpl notificationServiceImpl;

    @Override
    public Optional<Issue> getIssueById(Long issueId) throws Exception {
        Optional<Issue> issue = issueRepository.findById(issueId);
        if (issue.isPresent()) {
            return issue;
        }
        throw new Exception("No issues found with issueid" + issueId);
    }

    @Override
    public List<Issue> getIssueByProjectId(Long projectId) throws Exception {
        projectService.getProjectById(projectId);
        return issueRepository.findByProjectId(projectId);
    }

    @Override
    public Issue createIssue(IssueRequest issueRequest, Long userId)
            throws Exception {
        User user = getUserOrThrow(userId);

        // Check if the project exists
        Project project = projectService.getProjectById(issueRequest.getProjectId());
        //TODO REMOVE
        System.out.println("pro->id---------->"+issueRequest.getProjectId());
        if (project == null) {
            throw new Exception("Project not found with ID: " + issueRequest.getProjectId());
        }

        // Create a new issue
        Issue issue = new Issue();
        issue.setTitle(issueRequest.getTitle());
        issue.setDescription(issueRequest.getDescription());
        issue.setStatus(issueRequest.getStatus());
        issue.setProjectID(issueRequest.getProjectId());
        issue.setPriority(issueRequest.getPriority());
        issue.setDueDate(issueRequest.getDueDate());

        // Set the project for the issue
        issue.setProject(project);

        // Save the issue
        return issueRepository.save(issue);
    }
    @Override
    public Optional<Issue> updateIssue(Long issueId, IssueRequest updatedIssue, Long userId)
            throws Exception {
        User user = getUserOrThrow(userId);
        Optional<Issue> existingIssue = issueRepository.findById(issueId);

        if (existingIssue.isPresent()) {
            // Check if the project exists
            Project project = projectService.getProjectById(updatedIssue.getProjectId());
            if (project == null) {
                throw new Exception("Project not found with ID: " + updatedIssue.getProjectId());
            }
            User assignee=null;
            if(updatedIssue.getUserId()!=null) {
                assignee = userService.findUserById(updatedIssue.getUserId());
                if (assignee == null) {
                    throw new Exception("Assignee not found with ID: " + updatedIssue.getUserId());
                }
            }

            Issue issueToUpdate = existingIssue.get();


            if (updatedIssue.getDescription() != null) {
                issueToUpdate.setDescription(updatedIssue.getDescription());
            }

            if(updatedIssue.getUserId()!=null)
            {
               issueToUpdate.setAssignee(assignee);
            }

            if (updatedIssue.getDueDate() != null) {
                issueToUpdate.setDueDate(updatedIssue.getDueDate());
            }

            if (updatedIssue.getPriority() != null) {
                issueToUpdate.setPriority(updatedIssue.getPriority());
            }

            if (updatedIssue.getStatus() != null) {
                issueToUpdate.setStatus(updatedIssue.getStatus());
            }

            if (updatedIssue.getTitle() != null) {
                issueToUpdate.setTitle(updatedIssue.getTitle());
            }

            // Save the updated issue
            return Optional.of(issueRepository.save(issueToUpdate));
        }

        throw new Exception("Issue not found with issueid" + issueId);
    }

    @Override
    public String deleteIssue(Long issueId, Long userid) throws Exception {
        Optional<Issue> issueById = getIssueById(issueId);
        if (issueById.isPresent()) {
            issueRepository.deleteById(issueId);
            return "issue with the id" + issueId + "deleted";
        }
        throw new Exception("Issue not found with issueid" + issueId);
    }

    @Override
    public List<Issue> getIssuesByAssigneeId(Long assigneeId) throws Exception {
        List<Issue> issues = issueRepository.findByAssigneeId(assigneeId);
        if (issues != null) {
            return issues;
        }
        throw new Exception("Issues not found");
    }


    @Override
    public List<Issue> searchIssues(String title, String status, String priority, Long assigneeId) throws Exception {
        List<Issue> searchIssues = issueRepository.searchIssues(title, status, priority, assigneeId);
        if (searchIssues != null) {
            return searchIssues;
        }
        throw new Exception("No Issues found");
    }

    @Override
    public List<User> getAssigneeForIssue(Long issueId) throws Exception {
        return List.of();
    }

    @Override
    public Issue addUserToIssue(Long issueId, Long userId) throws Exception {
        User user = userService.findUserById(userId);
        Optional<Issue> issue=getIssueById(issueId);

        if(issue.isEmpty())throw new Exception("issue not exist");

        issue.get().setAssignee(user);
        notifyAssignee(user.getEmail(),"New Issue Assigned To You","New Issue Assign To You");
        return issueRepository.save(issue.get());
    }

    @Override
    public Issue updateStatus(Long issueId, String status) throws Exception {
        Optional<Issue> optionalIssue=issueRepository.findById(issueId);
        if(optionalIssue.isEmpty()){
            throw new Exception("issue not found");
        }
        Issue issue=optionalIssue.get();
        issue.setStatus(status);

        return issueRepository.save(issue);
    }

    private User getUserOrThrow(Long userId) throws Exception {
        User user = userService.findUserById(userId);

        if (user != null) {
            return user;
        } else {
            throw new Exception("User not found with id: " + userId);
        }
    }

    private void notifyAssignee(String email, String subject, String body) {
        System.out.println("IssueServiceImpl.notifyAssignee()");
        notificationServiceImpl.sendNotification(email, subject, body);
    }


//    @Override
//    public List<Issue> getAllIssues() throws IssueException {
//        List<Issue> issues = issueRepository.findAll();
//        if(issues!=null) {
//        	return issues;
//        }
//        throw new IssueException("No issues found");
//    }

}

