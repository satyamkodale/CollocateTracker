package com.be.repository;

import com.be.entity.Project;
import com.be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    List<Project> findByOwner(User owner);

    //TODO UNDERSTANDS ALL METHODS
    // to get projects based on partial name and team member
    List<Project> findByNameContainingAndTeamContains(String partialName, User user);
    List<Project> findByNameContainingAndTeamContaining(String partialName, User user);

    //if you find user in team field return user
    @Query("SELECT p FROM Project p JOIN p.team t WHERE t = :user")
    List<Project> findProjectsByTeam(@Param("user") User user);

    //both owner project and invited project
    List<Project> findByTeamContainingOrOwner(User user,User owner);
}
