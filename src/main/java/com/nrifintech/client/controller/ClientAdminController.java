package com.nrifintech.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.nrifintech.model.Book;
import com.nrifintech.model.Issue;
import com.nrifintech.service.BookService;
import com.nrifintech.service.IssueService;

@Controller
@RequestMapping("/admin")
public class ClientAdminController {
	
	@Autowired
	private IssueService issueService;
	
	@Autowired
	private BookService bookService;
	
	@GetMapping("/granted")
	public String granted(Model model) {
		List<Issue> issues=issueService.getIssueByStatus("Granted");
		System.out.println(issues);
		model.addAttribute("issues", issues);
		return "admin/granted";
	}
	
	@GetMapping("/grantedToIssue/{issueId}")
	public RedirectView gratedToIssue(@PathVariable Integer issueId) {
		try {
			Issue issue=new Issue();
			issue=issueService.getIssueByIssueId(issueId).getBody();
			issue.setStatus("Issued");
			issueService.updateIssue(issue, issueId);
			return new RedirectView("/admin/granted");			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new RedirectView("/error");
		}
		
	}
	@GetMapping("/issue")
	public String issue(Model model) {
		List<Issue> issues=issueService.getIssueByStatus("Issued");
		model.addAttribute("issues", issues);
		return "admin/issued";
	}
	@GetMapping("/issuedToReturn/{issueId}")
	public RedirectView issuedToReturn(@PathVariable Integer issueId) {
		try {
			Issue issue=new Issue();
			issue=issueService.getIssueByIssueId(issueId).getBody();
			
			//adding book
			Book book=new Book();
			book=issue.getBook();
			book.setQty(book.getQty()+1);
			bookService.updateBook(book.getBookId(), book);
			
			
			issue.setStatus("Returned");
			issueService.updateIssue(issue, issueId);
			return new RedirectView("/admin/issue");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new RedirectView("/error");
		}
	}
	
	@PostMapping("/granted/issueUserSearch")
	public String searchByNameAndStatusGranted(@RequestParam("searchText") String userName,Model model) {
//		System.out.println(textboxSelect);
		List<Issue> issues=new ArrayList<>();
		System.out.println(userName);
		issues=issueService.getIssueByUserNameAndStatus(userName,"Granted");
		System.out.println(issues);
		model.addAttribute("issues", issues);
		return "admin/granted";
	}
	
	@PostMapping("/issued/issueUserSearch")
	public String searchByNameAndStatusIssued(@RequestParam("searchText") String userName,Model model) {
//		System.out.println(textboxSelect);
		List<Issue> issues=new ArrayList<>();
		issues=issueService.getIssueByUserNameAndStatus(userName,"Issued");
		model.addAttribute("issues", issues);
		return "admin/issued";
	}
	@GetMapping("/addBook")
	public String addBook(Model model) {
		Book book=new Book();
		model.addAttribute("book", book);
		return "admin/addBook";
	}
	@PostMapping("/postBook")
	public RedirectView postBook(@ModelAttribute("book") Book book) {
		System.out.println(book);
		bookService.addBook(book);
		return new RedirectView("/admin/addBook");
	}
}
