package com.nrifintech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

	@RequestMapping(method = RequestMethod.GET, value = "/showissue")
	public List<Issue> getAllIssues()
	{
		return is.getAllIssues();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/addissue")
	public ResponseEntity<Issue> addIssue(@RequestBody Issue issue) throws ResourceNotFoundException
	{
		return is.addIssue(issue);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/deleteissue/{issueId}")
	public ResponseEntity<Issue> deleteIssue(@PathVariable int issueId) throws ResourceNotFoundException
	{
		return is.deleteIssue(issueId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/showissuebyid/{issueId}")
	public ResponseEntity<Issue> getIssueById(@PathVariable int issueId) throws ResourceNotFoundException
	{
		return is.getIssueByIssueId(issueId);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/updateIssue/{issueId}")
	public ResponseEntity<Issue> updateIssue(@RequestBody Issue issue,@PathVariable int issueId) throws ResourceNotFoundException
	{
		return is.updateIssue(issue, issueId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/showissuebyuser/{username}")
	public ResponseEntity<List<Issue>> getIssueByUser(@PathVariable String username) throws ResourceNotFoundException
	{
		return is.getIssueByUser(username);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/showissuebydate/{date}")
	public ResponseEntity<List<Issue>> getIssueByDate(@PathVariable String date) throws ResourceNotFoundException
	{
		return is.getIssueByDate(date);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/showissuebybook/{bookname}")
	public ResponseEntity<List<Issue>> getIssueByBook(@PathVariable String bookname) throws ResourceNotFoundException
	{
		return is.getIssueByBook(bookname);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/generateissuesreport")
	public ResponseEntity<ByteArrayResource> getIssuesReport() throws ResourceNotFoundException
	{
		HttpHeaders header=new HttpHeaders();
		header.setContentType(new MediaType("application","force-download"));
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=IssueReports.xlsx");
		return new ResponseEntity<>(new ByteArrayResource(is.generateReport().toByteArray()),header,HttpStatus.CREATED);
	}
	
	
	@RequestMapping(method=RequestMethod.GET,value="/generateissuesreportbyuserid/{userId}")
	public ResponseEntity<ByteArrayResource> getIssuesReportByUser(@PathVariable int userId) throws ResourceNotFoundException
	{
		HttpHeaders header=new HttpHeaders();
		header.setContentType(new MediaType("application","force-download"));
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=IssueReports.xlsx");
		return new ResponseEntity<>(new ByteArrayResource(is.generateReportByUser(userId).toByteArray()),header,HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="showuserfinedetails")
	public ResponseEntity<List<Issue>> getUserFineDetails() throws ResourceNotFoundException
	{
		return is.getFineDetails();
	}
	
	
	@RequestMapping(method=RequestMethod.GET,value="showuserfinedetailsbyusername/{Username}")
	public ResponseEntity<List<Issue>> getUserFineDetailsByUsername(@PathVariable String Username) throws ResourceNotFoundException
	{
		return is.getfineDetailsByUsername(Username);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="showusertotalfinebyusername/{Username}")
	public ResponseEntity<Double> getUserTotalFineByUsername(@PathVariable String Username) throws ResourceNotFoundException
	{
		return is.getTotalFineByUsername(Username);
	}
	
	
}
