package com.nrifintech.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.mail.internet.MimeMessage;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nrifintech.exception.ResourceNotFoundException;
import com.nrifintech.model.Issue;
import com.nrifintech.model.User;
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
	
	@Autowired
	private JavaMailSender javamailsender;
	
	@Value("${spring.mail.username}")
	private String sendermail;
	
	private int rownum,cellnum;
	
	private XSSFWorkbook workbook;
	
	private XSSFSheet sheet;
	
	private HttpHeaders header;

	// get all issues list
	public List<Issue> getAllIssues() {

		List<Issue> issues = new ArrayList<>();
		for (Issue i : issueRepo.findAll())
			issues.add(i);
		return issues;
	}

	// add a new issue
	public ResponseEntity<Issue> addIssue(Issue issue) throws ResourceNotFoundException {
		issue.setUser(userService.getUserByusername(issue.getUser().getUsername()).getBody());
		issue.setBook(bookService.getBookByTitle(issue.getBook().getTitle()).getBody());
		issue.setFine(fineCalculation(issue.getIssueDate(), issue.getFine()));
		System.out.println(issue);
		issueRepo.save(issue);
		return ResponseEntity.ok().body(issue);
	}

	// delete a issue
	public ResponseEntity<Issue> deleteIssue(int issueId) throws ResourceNotFoundException {
		Issue issue = issueRepo.findById(issueId)
				.orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id " + issueId));
		issueRepo.deleteById(issueId);
		return ResponseEntity.ok().body(issue);
	}

	// get issue by a issueId
	public ResponseEntity<Issue> getIssueByIssueId(int issueId) throws ResourceNotFoundException {
		Issue issue = issueRepo.findById(issueId)
				.orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id " + issueId));
		return ResponseEntity.ok().body(issue);
	}

	// update issue by IssueId
	public ResponseEntity<Issue> updateIssue(Issue inew, int issueId) throws ResourceNotFoundException {
		Issue issue = issueRepo.findById(issueId)
				.orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id " + issueId));
		issue.setBook(bookService.getBookByTitle(inew.getBook().getTitle()).getBody());
		issue.setStatus(inew.getStatus());
		issue.setIssueDate(inew.getIssueDate());
		issue.setUser(userService.getUserByusername(inew.getUser().getUsername()).getBody());
		issue.setFine(fineCalculation(inew.getIssueDate(), inew.getFine()));
		issueRepo.save(issue);
		return ResponseEntity.ok().body(issue);
	}

	// Get Issues By Book
	public ResponseEntity<List<Issue>> getIssueByBook(String bookTitle) {
		List<Issue> issuelist = new ArrayList<Issue>();
		for (Issue is : issueRepo.findAll()) {
			if (is.getBook().getTitle().equalsIgnoreCase(bookTitle)) {
				issuelist.add(is);
			}
		}
		return ResponseEntity.ok().body(issuelist);
	}

	// Get Issues By Username
	public ResponseEntity<List<Issue>> getIssueByUser(String username) {
		List<Issue> issuelist = new ArrayList<Issue>();

		for (Issue is : issueRepo.findAll()) {
			if (is.getUser().getUsername().equals(username)) {
				issuelist.add(is);
			}
		}
		return ResponseEntity.ok().body(issuelist);
	}

	// Get issues By Date
	public ResponseEntity<List<Issue>> getIssueByDate(String Date) {
		List<Issue> issuelist = new ArrayList<Issue>();

		for (Issue is : issueRepo.findAll()) {
			if (is.getIssueDate().equals(Date)) {
				issuelist.add(is);
			}
		}
		return ResponseEntity.ok().body(issuelist);
	}

	

	// get issue by status
	public List<Issue> getIssueByStatus(String status) {
		List<Issue> issues = new ArrayList<>();
		issues = issueRepo.findIssueAllByStatus(status);
		return issues;
	}

	// get issue by userName and status
	public List<Issue> getIssueByUserNameAndStatus(String userName, String status) {
		List<Issue> issues = new ArrayList<>();
		issues = issueRepo.findIssueAllByUserNameAndStatus(userName, status);
//		System.out.println(issues);
		return issues;
	}

	// get issue by bookTitle and userName
	public List<Issue> getIssueByTitleAndUserNameAndStatus(String title, String status,String userName) {
		List<Issue> issues = new ArrayList<>();
		issues = issueRepo.findIssueAllByTitleAndUserNameAndStatus(title, status,userName);
		return issues;
	}

	public Integer fineCalculation(String isDate, Integer fine) 
	{
		long milliSeconds = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date issueDate = new Date();
		try {
			issueDate = sdf.parse(isDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date currentDate = new Date(milliSeconds);
		System.out.println(issueDate);
		System.out.println(currentDate);
		long time_diff = currentDate.getTime() - issueDate.getTime();
		long days_diff = TimeUnit.MILLISECONDS.toDays(time_diff) % 365;
		System.out.println(days_diff);
		Calendar cal=Calendar.getInstance();
		if (fine == null || cal.get(Calendar.DATE)==28) 
		{
			fine = 0;
		}
		else if (days_diff > 10) 
		{
			fine+= 10;
		}
		return fine;
	}
	
	@Scheduled(cron="${cron.expression.value}")
	public void updateFine()
	{
		for(Issue i:issueRepo.findAll())
		{
			if(i.getStatus().equalsIgnoreCase("Issued"))
			{
				i.setFine(fineCalculation(i.getIssueDate(),i.getFine()));
				issueRepo.save(i);
			}
		}
	}
	
	public ResponseEntity<List<Issue>> getFineDetails() 
	{
		List<Issue> fineList = new ArrayList<Issue>();

		for (Issue i : issueRepo.findAll()) 
		{
			if (i.getStatus().equalsIgnoreCase("Issued"))
			{
				fineList.add(i);
			}
		}
		return ResponseEntity.ok().body(fineList);
	}

	public ResponseEntity<List<Issue>> getfineDetailsByUsername(String username) throws ResourceNotFoundException 
	{
		List<Issue> userIssueList = new ArrayList<Issue>();
		for (Issue i : issueRepo.findAll()) 
		{
			if (i.getUser().getUsername().equals(username) && i.getStatus().equalsIgnoreCase("Issued")) 
			{
				userIssueList.add(i);
			}
		}
		return ResponseEntity.ok().body(userIssueList);
	}

	public ResponseEntity<Integer> getTotalFineByUsername(String username) throws ResourceNotFoundException 
	{
		Integer Totalfine = 0;
		for (Issue i : issueRepo.findAll()) 
		{
			if (i.getUser().getUsername().equals(username) && i.getStatus().equalsIgnoreCase("Issued"))
			{
				Totalfine += i.getFine();
			}
		}
		return ResponseEntity.ok().body(Totalfine);
	}
	
	public ResponseEntity<String> updateFineByIssueId(int IssueId)
	{
		Issue i=issueRepo.findById(IssueId).get();
		if ( i.getStatus().equalsIgnoreCase("Issued")) 
		{
			i.setFine(0);
			issueRepo.save(i);
			return ResponseEntity.ok().body("Fine Updated");
		}
		return ResponseEntity.ok().body("Invalid IssueId");
	}
	
	//Create Sheet
	public void createSheet()
	{
		header=new HttpHeaders();
		header.setContentType(new MediaType("application","force-download"));
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=IssuesReport.xlsx");
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("Issues");
		rownum = 1;
		Row row = sheet.createRow(rownum++);
		 cellnum = 1;
		Cell cellidName = row.createCell(cellnum++);
		cellidName.setCellValue("IssueId");
		Cell cellissueDateName = row.createCell(cellnum++);
		cellissueDateName.setCellValue("IssueDate");
		Cell cellBookName = row.createCell(cellnum++);
		cellBookName.setCellValue("Book");
		Cell cellUserName = row.createCell(cellnum++);
		cellUserName.setCellValue("User");
		Cell cellStatusName = row.createCell(cellnum++);
		cellStatusName.setCellValue("Status");
		Cell cellfineName = row.createCell(cellnum++);
		cellfineName.setCellValue("Fine");
	}
		
	//Create Data in sheet
	public void createDataInSheet(Issue is)
	{
		cellnum = 1;
		Row rowvalues = sheet.createRow(rownum++);
		Cell cellid = rowvalues.createCell(cellnum++);
		cellid.setCellValue(is.getIssueId());
		Cell celltitle = rowvalues.createCell(cellnum++);
		celltitle.setCellValue(is.getIssueDate());
		Cell cellIs = rowvalues.createCell(cellnum++);
		cellIs.setCellValue(is.getBook().getTitle());
		Cell cellquantity = rowvalues.createCell(cellnum++);
		cellquantity.setCellValue(is.getStatus());
		Cell celldate = rowvalues.createCell(cellnum++);
		celldate.setCellValue(is.getUser().getUsername());
		Cell cellfine = rowvalues.createCell(cellnum++);
		cellfine.setCellValue(is.getFine());
	}
		
	// Report of all Users
	public ResponseEntity<ByteArrayResource> generateReport() 
	{
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		createSheet();
		for (Issue is : issueRepo.findAll()) 
		{
			createDataInSheet(is);
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
		return new ResponseEntity<>(new ByteArrayResource(bs.toByteArray()),header,HttpStatus.CREATED);
	}

    // Report of Particular User
	public ResponseEntity<ByteArrayResource> generateReportByUser(int userId) 
    {
		ByteArrayOutputStream bs=new ByteArrayOutputStream();
		createSheet();
		for (Issue is : issueRepo.findAll()) 
		{
			if (is.getUser().getId() == userId) 
			{
				createDataInSheet(is);
			}
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
		return new ResponseEntity<>(new ByteArrayResource(bs.toByteArray()),header,HttpStatus.CREATED);
	}
	
	@Scheduled(cron="0 0 6 28 * ? ")
	public void reportToAccountsDept() throws ResourceNotFoundException
	{
		ByteArrayOutputStream bs=new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Issues");
		int rownum = 1;
		Row row = sheet.createRow(rownum++);
		int cellnum = 1;
		Cell cellidName = row.createCell(cellnum++);
		cellidName.setCellValue("UserId");
		Cell cellName = row.createCell(cellnum++);
		cellName.setCellValue("Name");
		Cell cellUsernameName = row.createCell(cellnum++);
		cellUsernameName.setCellValue("UserName");
		Cell cellEmailName = row.createCell(cellnum++);
		cellEmailName.setCellValue("Email");
		Cell cellfineName = row.createCell(cellnum++);
		cellfineName.setCellValue("Fine");
		for(User us:userService.getAllUsers())
		{
			cellnum = 1;
			Row rowvalues = sheet.createRow(rownum++);
			Cell cellid = rowvalues.createCell(cellnum++);
			cellid.setCellValue(us.getId());
			Cell cellname = rowvalues.createCell(cellnum++);
			cellname.setCellValue(us.getName());
			Cell cellUsername = rowvalues.createCell(cellnum++);
			cellUsername.setCellValue(us.getUsername());
			Cell cellEmail = rowvalues.createCell(cellnum++);
			cellEmail.setCellValue(us.getEmail());
			Cell cellfine = rowvalues.createCell(cellnum++);
			cellfine.setCellValue(getTotalFineByUsername(us.getUsername()).getBody());
		}
		try
		{
			workbook.write(bs);
			MimeMessage msg= javamailsender.createMimeMessage();
			MimeMessageHelper msghelp=new MimeMessageHelper(msg,true);
			msghelp.setFrom(sendermail);
			msghelp.setTo("maheshkambhampati159@gmail.com");
			msghelp.setSubject("Fine Details");
			msghelp.setText("These are the details");
			File excelFile=new File("Issues.xlsx");
			FileOutputStream fileout=new FileOutputStream(excelFile);
			fileout.write(bs.toByteArray());
			msghelp.addAttachment("Issues.xlsx", excelFile);
			javamailsender.send(msg);
			System.out.println("Mail Sent");
			fileout.close();
			bs.close();
			workbook.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}