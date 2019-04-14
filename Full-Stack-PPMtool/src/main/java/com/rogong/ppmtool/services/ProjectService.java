package com.rogong.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rogong.ppmtool.domain.Backlog;
import com.rogong.ppmtool.domain.Project;
import com.rogong.ppmtool.exceptions.ProjectIdException;
import com.rogong.ppmtool.repositories.BacklogRepository;
import com.rogong.ppmtool.repositories.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	public Project saveOrUpdateProject(Project project) {
		
	try {
		project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
		
		if(project.getId()==null) {
			Backlog backlog = new Backlog();
			project.setBacklog(backlog);
			backlog.setProject(project);
			backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			
		}
		
		if(project.getId()!=null) {
			project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
		}
		
		return projectRepository.save(project);
	} catch(Exception ex){
		throw new ProjectIdException("Project ID '"+project.getProjectIdentifier().toUpperCase()+"' already exists");
	}
		
	}
	
	public Project findProjectByIdentifier(String projectId) {
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if(project == null) {
			throw new ProjectIdException("Project ID '"+ projectId+"' doesn't exist");
		}
		
		return project;
	}
	
	public Iterable<Project> findAllProjects() {
		return projectRepository.findAll();
	}
	
	public void deleteProjectByIdentifier(String projectId) {
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if(project == null) {
			throw new ProjectIdException("Cannot Delete Project with ID '"+projectId+"'. doesn't exist");
		}
		
		projectRepository.delete(project);
	}
}
