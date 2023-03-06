package com.nrifintech.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nrifintech.exception.ResourceNotFoundException;
import com.nrifintech.model.Issue;
import com.nrifintech.repository.IssueRepository;

@Service
public class IssueService 
{
	@Autowired
	private IssueRepository issueRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BookService bookService;
	
    //get all issues list
	public List<Issue> getAllIssues()
	{

		List<Issue> issues = new ArrayList<>();
		for (Issue i : issueRepo.findAll())
			issues.add(i);
		return issues;
	}
    
	// add a new issue
	public ResponseEntity<Issue> addIssue(Issue issue) throws ResourceNotFoundException
	{
		issue.setUser(userService.getUserByusername(issue.getUser().getUsername()).getBody());
		issue.setBook(bookService.getBookByTitle(issue.getBook().getTitle()).getBody());
		issueRepo.save(issue);
		return ResponseEntity.ok().body(issue);
	}
    
	
	//delete a issue
	public ResponseEntity<Issue> deleteIssue(int issueId) throws ResourceNotFoundException
	{
		Issue issue = issueRepo.findById(issueId)
				.orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id " + issueId));
		issueRepo.deleteById(issueId);
		return ResponseEntity.ok().body(issue);
	}
    
	//get issue by a issueId
	public ResponseEntity<Issue> getIssueByIssueId(int issueId) throws ResourceNotFoundException
	{
		Issue issue = issueRepo.findById(issueId)
				.orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id " + issueId));
		return ResponseEntity.ok().body(issue);
	}
    
	//update issue by IssueId
	public ResponseEntity<Issue> updateIssue(Issue inew, int issueId) throws ResourceNotFoundException 
	{
		Issue issue = issueRepo.findById(issueId)
				.orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id " + issueId));
		issue.setBook(bookService.getBookByTitle(inew.getBook().getTitle()).getBody());
		issue.setStatus(inew.getStatus());
		issue.setIssueDate(inew.getIssueDate());
		issue.setUser(userService.getUserByusername(inew.getUser().getUsername()).getBody());
		issueRepo.save(issue);
		return ResponseEntity.ok().body(issue);
	}
	
	public ResponseEntity<List<Issue>> getIssueByBook(String bookTitle)
	{
		List<Issue> issuelist=new ArrayList<Issue>();
		for(Issue is:issueRepo.findAll() )
		{
			if(is.getBook().getTitle().equalsIgnoreCase(bookTitle))
			{
				issuelist.add(is);
			}
		}
		return ResponseEntity.ok().body(issuelist);
	}
	
	public ResponseEntity<List<Issue>> getIssueByUser(String username)
	{
		List<Issue> issuelist=new ArrayList<Issue>();
		
		for(Issue is:issueRepo.findAll())
		{
			if(is.getUser().getUsername().equals(username))
			{
				issuelist.add(is);
			}
		}
		return ResponseEntity.ok().body(issuelist);
	}
	
	public ResponseEntity<List<Issue>> getIssueByDate(String Date)
	{
		List<Issue> issuelist=new ArrayList<Issue>();
		
		for(Issue is:issueRepo.findAll())
		{
			if(is.getIssueDate().equals(Date))
			{
				issuelist.add(is);
			}
		}
		return ResponseEntity.ok().body(issuelist);
	}
	
	public ByteArrayOutputStream generateReport()
	{
		ByteArrayOutputStream bs=new ByteArrayOutputStream();
		XSSFWorkbook workbook=new XSSFWorkbook();
		XSSFSheet sheet =workbook.createSheet("Issues");
		int rownum=1;
		Row row=sheet.createRow(rownum++);
		int cellnum=1;
		Cell cellidName=row.createCell(cellnum++);
		cellidName.setCellValue("IssueId");
		Cell celltitleName=row.createCell(cellnum++);
		celltitleName.setCellValue("IssueDate");
		Cell cellIsbn=row.createCell(cellnum++);
		cellIsbn.setCellValue("Book");
		Cell cellquantityName=row.createCell(cellnum++);
		cellquantityName.setCellValue("Status");
		Cell celldateName=row.createCell(cellnum++);
		celldateName.setCellValue("User");
		for(Issue is:issueRepo.findAll())
		{
			cellnum=1;
			Row rowvalues=sheet.createRow(rownum++);
			Cell cellid=rowvalues.createCell(cellnum++);
			cellid.setCellValue(is.getIssueId());
			Cell celltitle=rowvalues.createCell(cellnum++);
			celltitle.setCellValue(is.getIssueDate());
			Cell cellIs=rowvalues.createCell(cellnum++);
			cellIs.setCellValue(is.getBook().getTitle());
			Cell cellquantity=rowvalues.createCell(cellnum++);
			cellquantity.setCellValue(is.getStatus());
			Cell celldate=rowvalues.createCell(cellnum++);
			celldate.setCellValue(is.getUser().getUsername());
		}
		try 
		{
			workbook.write(bs);
			bs.close();
			workbook.close();
		} 
		catch (IOException e) 
		{
			System.out.println(e.getMessage());
		}
		
		return bs;
	}
	
}
	//get issue by status
	public List<Issue> getIssueByStatus(String status){
		List<Issue> issues=new ArrayList<>();
		issues=issueRepo.findIssueAllByStatus(status);
		return issues;
	}
	
	//get issue by userName and status
	public List<Issue> getIssueByUserNameAndStatus(String userName,String status){
		List<Issue> issues=new ArrayList<>();
		issues=issueRepo.findIssueAllByUserNameAndStatus(userName,status);
		System.out.println(issues);
		return issues;
	}
	
	//get issue by bookTitle and userName
	public List<Issue> getIssueByTitleAndUserNameAndStatus(String title,String status){
		List<Issue> issues=new ArrayList<>();
		issues=issueRepo.findIssueAllByTitleAndUserNameAndStatus(title, status);
		return issues;
	}
}

