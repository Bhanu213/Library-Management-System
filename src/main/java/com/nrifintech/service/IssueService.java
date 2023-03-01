package com.nrifintech.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nrifintech.exception.ResourceNotFoundException;

import com.nrifintech.model.Issue;
import com.nrifintech.model.Status;
import com.nrifintech.repository.IssueRepository;

@Service
public class IssueService {
	@Autowired
	private IssueRepository issueRepo;
	
	@Autowired
	private StatusService statusService;
    //get all issues list
	public List<Issue> getAllIssues() {

		List<Issue> issues = new ArrayList<>();
		for (Issue i : issueRepo.findAll())
			issues.add(i);
		return issues;
	}
    // add a new issue
	public ResponseEntity<Issue> addIssue(Issue issue) {
		Status status = issue.getStatus();
		status.setStatusId(statusService.getServiceId(status.getDescription()));
		issue.setStatus(status);
		issueRepo.save(issue);
		return ResponseEntity.ok().body(issue);
	}
    
	//delete a issue
	public ResponseEntity<Issue> deleteIssue(int issueId) throws ResourceNotFoundException {
		Issue issue = issueRepo.findById(issueId)
				.orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id " + issueId));
		issueRepo.delete(issue);
		return ResponseEntity.ok().body(issue);
	}
    //get issue by a issueId
	public ResponseEntity<Issue> getIssueByIssueId(int issueId) throws ResourceNotFoundException {
		Issue issue = issueRepo.findById(issueId)
				.orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id " + issueId));
		return ResponseEntity.ok().body(issue);
	}
    //update issue by IssueId
	public ResponseEntity<Issue> updateIssue(Issue inew, int issueId) throws ResourceNotFoundException {
		Issue issue = issueRepo.findById(issueId)
				.orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id " + issueId));
		issue.setBook(inew.getBook());
		issue.setStatus(inew.getStatus());
		issue.setIssueDate(inew.getIssueDate());
		issue.setUser(inew.getUser());
		issueRepo.save(issue);
		return ResponseEntity.ok().body(issue);
	}
}
