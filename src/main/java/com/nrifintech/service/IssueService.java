package com.nrifintech.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nrifintech.exception.ResourceNotFoundException;
import com.nrifintech.model.Issue;
import com.nrifintech.repository.IssueRepository;

@Service
public class IssueService {
	@Autowired
	private IssueRepository issueRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private BookService bookService;

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

	// Report of all Users
	public ResponseEntity<ByteArrayResource> generateReport() {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Issues");
		int rownum = 1;
		Row row = sheet.createRow(rownum++);
		int cellnum = 1;
		Cell cellidName = row.createCell(cellnum++);
		cellidName.setCellValue("IssueId");
		Cell celltitleName = row.createCell(cellnum++);
		celltitleName.setCellValue("IssueDate");
		Cell cellIsbn = row.createCell(cellnum++);
		cellIsbn.setCellValue("Book");
		Cell cellquantityName = row.createCell(cellnum++);
		cellquantityName.setCellValue("Status");
		Cell celldateName = row.createCell(cellnum++);
		celldateName.setCellValue("User");
		Cell cellfineName = row.createCell(cellnum++);
		cellfineName.setCellValue("Fine");
		for (Issue is : issueRepo.findAll()) {
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
		try {
			workbook.write(bs);
			bs.close();
			workbook.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		HttpHeaders header=new HttpHeaders();
		header.setContentType(new MediaType("application","force-download"));
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=IssueReports.xlsx");
		return new ResponseEntity<>(new ByteArrayResource(bs.toByteArray()),header,HttpStatus.CREATED);
	}

	// Report of Particular User
	public ResponseEntity<ByteArrayResource> generateReportByUser(int userId) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("User");
		int rownum = 1;
		Row row = sheet.createRow(rownum++);
		int cellnum = 1;
		Cell cellidName = row.createCell(cellnum++);
		cellidName.setCellValue("IssueId");
		Cell celltitleName = row.createCell(cellnum++);
		celltitleName.setCellValue("IssueDate");
		Cell cellIsbn = row.createCell(cellnum++);
		cellIsbn.setCellValue("Book");
		Cell cellquantityName = row.createCell(cellnum++);
		cellquantityName.setCellValue("Status");
		Cell cellfineName = row.createCell(cellnum++);
		cellfineName.setCellValue("Fine");
		for (Issue is : issueRepo.findAll()) {
			if (is.getUser().getId() == userId) {
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
				Cell cellfine = rowvalues.createCell(cellnum++);
				cellfine.setCellValue(is.getFine());
			}
		}
		try {
			workbook.write(bs);
			bs.close();
			workbook.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		HttpHeaders header=new HttpHeaders();
		header.setContentType(new MediaType("application","force-download"));
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=IssueReports.xlsx");
		return new ResponseEntity<>(new ByteArrayResource(bs.toByteArray()),header,HttpStatus.CREATED);
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

	public Double fineCalculation(String isDate, Double fine) {
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
		if (fine == null) {
			fine = 0.0;
		}
		if (days_diff > 10) {
			return (fine + (days_diff - 10) * 10);
		} else {
			return fine;
		}
	}
	
	public ResponseEntity<List<Issue>> getFineDetails() {
		List<Issue> fineList = new ArrayList<Issue>();

		for (Issue i : issueRepo.findAll()) {
			if (i.getFine() > 0.0) {
				fineList.add(i);
			}
		}
		return ResponseEntity.ok().body(fineList);
	}

	public ResponseEntity<List<Issue>> getfineDetailsByUsername(String username) throws ResourceNotFoundException {
		Double Totalfine = 0.0;
		List<Issue> userIssueList = new ArrayList<Issue>();
		for (Issue i : issueRepo.findAll()) {
			if (i.getUser().getUsername().equals(username)) {
				userIssueList.add(i);
				Totalfine += i.getFine();
			}
		}
		return ResponseEntity.ok().body(userIssueList);
	}

	public ResponseEntity<Double> getTotalFineByUsername(String username) throws ResourceNotFoundException {
		Double Totalfine = 0.0;
		for (Issue i : issueRepo.findAll()) {
			if (i.getUser().getUsername().equals(username)) {
				Totalfine += i.getFine();
			}
		}
		return ResponseEntity.ok().body(Totalfine);
	}
}