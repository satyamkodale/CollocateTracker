package com.be.service;

import com.be.entity.Chat;
import com.be.entity.Project;
import com.be.entity.User;

import java.util.List;

public interface ProjectService {


    //TODO THROW CUSTOM EXCEPTION
    Project createProject(Project project, Long userId) throws Exception;

    //TODO THROW CUSTOM EXCEPTION -> TODO UNDERSTAND
    //FOR FILTER PURPOSE BASED ON CATEGORY AND TAGS
    List<Project> getProjectsByTeam(User user, String category, String tag) throws Exception;

    //TODO THROW CUSTOM EXCEPTION
    //WHY USER ID _> OWNER AND REQUESTING PERSON SHOULD BE SAME
    String deleteProject(Long projectId,Long userId) throws Exception;

    //TODO THROW CUSTOM EXCEPTION
    Project updateProject(Project updatedProject, Long id) throws Exception;

    Project getProjectById(Long projectId) throws Exception;

    void addUserToProject(Long projectId, Long userId) throws Exception;

    void removeUserFromProject(Long projectId, Long userId) throws Exception;


    Chat getChatByProjectId(Long projectId) throws Exception;
    public List<Project> searchProjects(String keyword, User user) throws Exception;
}
