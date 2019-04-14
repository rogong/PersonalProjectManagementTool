package com.rogong.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rogong.ppmtool.domain.Backlog;
import com.rogong.ppmtool.domain.ProjectTask;
import com.rogong.ppmtool.repositories.BacklogRepository;
import com.rogong.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
		
		//PTs to be added to a specific project, project != null, BL exists
		Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
		//set the bl to pt
		projectTask.setBacklog(backlog);
		
		//we want our project sequence to be like this: IDPRO-1 IDPRO-2 ...100 101
		Integer BacklogSequence = backlog.getPTSequence();
		
		//Update the BL SEQUENCE
		BacklogSequence++;
		
		backlog.setPTSequence(BacklogSequence);
		
		//Add Sequence to Project Task
		projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
		projectTask.setProjectIdentifier(projectIdentifier);
		
		//INITIAL priority when priority null
		if(projectTask.getPriority()==null) { 
			projectTask.setPriority(3);
		}
		//In the future we need this
//		if( projectTask.getPriority()==0||projectTask.getPriority()==null) { 
//			projectTask.setPriority(3);
//		}
		
		//INITIAL status when status is null
		if(projectTask.getStatus() == ""||projectTask.getStatus()==null) {
			projectTask.setStatus("TO_DO");
		}
		
		return projectTaskRepository.save(projectTask);

	}

}
