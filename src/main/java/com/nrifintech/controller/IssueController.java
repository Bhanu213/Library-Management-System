package com.nrifintech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.exception.ResourceNotFoundException;
import com.nrifintech.model.Issue;
import com.nrifintech.service.IssueService;

@RestController
@RequestMapping("/issue")
public class IssueController {
	@Autowired
	IssueService is;

	@RequestMapping(method = RequestMethod.GET, value = "/showIssueList")
	public List<Issue> getAllIssues() {
		return is.getAllIssues();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/addIssue")
	public ResponseEntity<Issue> addIssue(@RequestBody Issue issue) throws ResourceNotFoundException {
		return is.addIssue(issue);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/deleteIssue/{issueId}")
	public ResponseEntity<Issue> deleteIssue(@PathVariable int issueId) throws ResourceNotFoundException {
		return is.deleteIssue(issueId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/showIssue/{issueId}")
	public ResponseEntity<Issue> getIssueById(@PathVariable int issueId) throws ResourceNotFoundException {
		return is.getIssueByIssueId(issueId);
	}
	@RequestMapping(method = RequestMethod.PUT, value = "/updateIssue/{issueId}")
	public ResponseEntity<Issue> updateIssue(@RequestBody Issue issue,@PathVariable int issueId) throws ResourceNotFoundException {
		return is.updateIssue(issue, issueId);
	}
}
