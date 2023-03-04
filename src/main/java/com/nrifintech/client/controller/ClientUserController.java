package com.nrifintech.client.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.nrifintech.model.Book;
import com.nrifintech.model.Issue;
import com.nrifintech.model.User;
import com.nrifintech.service.BookService;
import com.nrifintech.service.IssueService;
import com.nrifintech.service.UserService;

@Controller
@RequestMapping("/user")
public class ClientUserController {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private IssueService issueService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		List<Book> books=bookService.getAllBooks();
		model.addAttribute("books", books);
		return "dashboard";
	}
	
	@GetMapping("/createIssue/{bookId}")
	public RedirectView createIssue(@PathVariable("bookId") Integer bookId) {
		try {
			Book book=bookService.getBookById(bookId).getBody();
			book.setQty(book.getQty()-1);
			bookService.updateBook(bookId, book);
			
			User user=userService.getUserById(1).getBody();
			Issue issue=new Issue("2023/03/02",book,"Granted",user);
			issueService.addIssue(issue);
			return new RedirectView("/user/dashboard");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new RedirectView("/error");
		}
	}
	
	@GetMapping("/granted")
	public String issuePage(Model model) {
		List<Issue> issues=issueService.getAllIssues();
		model.addAttribute("issues", issues);
		return "granted";
	}
	
	@GetMapping("/deleteIssue/{issueId}")
	public RedirectView deleteIssue(@PathVariable("issueId") Integer issueId) {
		try {
			Issue issue=new Issue();
			issue=issueService.getIssueByIssueId(issueId).getBody();
			Book book=new Book();
			book=issue.getBook();
			book.setQty(book.getQty()+1);
			bookService.updateBook(book.getBookId(), book);
			issueService.deleteIssue(issueId);
			return new RedirectView("/user/granted");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new RedirectView("/error");
		}
	}
}
