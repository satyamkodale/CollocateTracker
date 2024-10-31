package com.be.service.impl;

import com.be.entity.Chat;
import com.be.entity.Project;
import com.be.entity.User;
import com.be.repository.ChatRepository;
import com.be.repository.ProjectRepository;
import com.be.repository.UserRepository;
import com.be.service.ChatService;
import com.be.service.ProjectService;
import com.be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;


    @Override
    public Project createProject(Project project, Long userId) throws Exception {

        User user = userService.findUserById(userId);

        Project createdProject=new Project();

        createdProject.setOwner(user);
        createdProject.setTags(project.getTags());
        createdProject.setName(project.getName());
        createdProject.setCategory(project.getCategory());
        createdProject.setDescription(project.getDescription());
        createdProject.getTeam().add(user);
        //TODO SOPLN
        System.out.println(createdProject);
        Project savedProject=projectRepository.save(project);
        //TODO CHECK CAN WE REMOVE
        savedProject.getTeam().add(user);

        Chat chat = new Chat();
        chat.setProject(savedProject);
        Chat projectChat = chatService.createChat(chat);
        savedProject.setChat(projectChat);

        return savedProject;
    }


    //GET PROJECTS BASED ON FILTER AND CATEGORY
    @Override
    public List<Project> getProjectsByTeam(User user, String category, String tag) throws Exception {
        List<Project> projects= projectRepository.findByTeamContainingOrOwner(user,user);
        if (category != null) {
            projects = projects.stream()
                    .filter(project -> project.getCategory().equals(category))
                    .collect(Collectors.toList());
        }
        if (tag != null) {
            projects = projects.stream()
                    .filter(project -> project.getTags().contains(tag))
                    .collect(Collectors.toList());
        }
        return projects;
    }

    @Override
    public String deleteProject(Long projectId,Long id) throws Exception {
        User user = userService.findUserById(id);
        System.out.println("user ____>"+user);
        if(user!=null) {
            projectRepository.deleteById(projectId);
            return "project deleted";
        }
        //TODO CHANGE
        throw new Exception("User doesnot exists");
    }

    @Override
    public Project updateProject(Project updatedProject, Long id) throws Exception {
        Project project = getProjectById(id);

        if (project != null) {
            // Update the existing project with the fields from updatedProject
            if (updatedProject.getName() != null) {
                project.setName(updatedProject.getName());
            }

            if (updatedProject.getDescription() != null) {
                project.setDescription(updatedProject.getDescription());
            }

            if (updatedProject.getTags() != null) {
                project.setTags(updatedProject.getTags());
            }

            // Save the updated project once
            return projectRepository.save(project);
        }

        throw new Exception("Project does not exist");
    }

    @Override
    public List<Project> searchProjects(String keyword, User user) throws Exception {
        String partialName = "%" + keyword + "%";
//			projectRepository.findByPartialNameAndTeamIn(partialName, user);
        List<Project> list = projectRepository.findByNameContainingAndTeamContains(keyword,user);
        if(list!=null) {
            return list;
        }
        throw new Exception("No Projects available");
    }

    @Override
    public Project getProjectById(Long projectId) throws Exception {
        Optional<Project> project = projectRepository.findById(projectId);
        if(project.isPresent()) {
            return project.get();
        }
        //TODO CHANGE
        throw new Exception("No project exists with the id "+projectId);
    }

    @Override
    public void addUserToProject(Long projectId, Long userId) throws Exception {

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new Exception("project not found"));
        User user = userService.findUserById(userId);

        if (!project.getTeam().contains(user)) {
            project.getChat().getUsers().add(user);
            project.getTeam().add(user);
            projectRepository.save(project);
        }

    }

    @Override
    public void removeUserFromProject(Long projectId, Long userId) throws Exception {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new Exception("project not found"));
        User user = userService.findUserById(userId);

        if (project.getTeam().contains(user)) {
            project.getTeam().remove(user);
            project.getChat().getUsers().remove(user);
        }
    }

    @Override
    public Chat getChatByProjectId(Long projectId) throws Exception {
        Project project = projectRepository.findById(projectId).orElseThrow(()-> new Exception("Project not found"));
        if( project != null ) return project.getChat() ;


        //TODO CHANGE
        throw new Exception("no chats found");
    }

    public List<User> getUsersByProjectId(Long projectId) throws Exception {
        Project project = projectRepository.findById(projectId).orElse(null);
        if( project != null) return project.getChat().getUsers();

        throw new Exception("no project found with id "+projectId);
    }


}
