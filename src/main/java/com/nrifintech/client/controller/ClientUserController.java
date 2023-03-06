package com.nrifintech.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
	
//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private BookService bookService;
	
	@Autowired
	private IssueService issueService;
	
	@Autowired
	private UserService userService;
	
	
	
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		List<Book> books=bookService.getAllBooks();
//		System.out.println(books.get(0).getAuthor().getAuthorName());
		model.addAttribute("books", books);
		System.out.println(books);
		return "dashboard";
	}
	@PostMapping("/dashboard/performSearch")
	public String search(@RequestParam("drop") String dropdownSelect,@RequestParam("searchText") String textboxSelect,Model model) {
		List<Book> books=new ArrayList<>();
		if(dropdownSelect.equalsIgnoreCase("author")) {
			books=bookService.getBookByAuthor(textboxSelect).getBody();
		}
		else if(dropdownSelect.equalsIgnoreCase("genre")) {
			books=bookService.getBookByGenre(textboxSelect).getBody();
		}
		else if(dropdownSelect.equalsIgnoreCase("title")) {
			books=List.of(bookService.getBookByTitle(textboxSelect).getBody());
		}
		else {
			books=bookService.getAllBooks();
		}
		model.addAttribute("books", books);
		return "dashboard";
	}
	@GetMapping("/createIssue/{bookId}")
	public RedirectView createIssue(@PathVariable("bookId") Integer bookId, RedirectAttributes redirAttrs) {
		try {
			Book book=bookService.getBookById(bookId).getBody();
			book.setQty(book.getQty()-1);
			bookService.updateBook(bookId, book);
			
			User user=userService.getUserById(1).getBody();
			Issue issue=new Issue("2023/03/02",book,"Granted",user);
			issueService.addIssue(issue);
			redirAttrs.addFlashAttribute("msg", "Added successfully.");
			return new RedirectView("/user/dashboard");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			redirAttrs.addFlashAttribute("msg", "Error occured.");
			return new RedirectView("/error");
		}
	}
	
	@GetMapping("/granted")
	public String issuePage(Model model) {
		List<Issue> issues=issueService.getIssueByStatus("Granted");
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
	@GetMapping("/issue")
	public String issue(Model model) {
		List<Issue> issues=new ArrayList<>();
		issues=issueService.getIssueByStatus("Issued");
		model.addAttribute("issues", issues);
		return "issue";
	}
	@GetMapping("/return")
	public String returnPage(Model model) {
		List<Issue> issues=new ArrayList<>();
		issues=issueService.getIssueByStatus("Returned");
		model.addAttribute("issues", issues);
		return "return";
	}
	@PostMapping("/granted/titleBasedSearch")
	public String grantedTitleSearch(@RequestParam("searchText") String title,Model model) {
//		System.out.println(textboxSelect);
		List<Issue> issues=new ArrayList<>();
		issues=issueService.getIssueByTitleAndUserNameAndStatus(title, "Granted");
		model.addAttribute("issues", issues);
		return "granted";
	}
	@PostMapping("/issued/titleBasedSearch")
	public String issuedTitleSearch(@RequestParam("searchText") String title,Model model) {
//		System.out.println(textboxSelect);
		List<Issue> issues=new ArrayList<>();
		issues=issueService.getIssueByTitleAndUserNameAndStatus(title, "Issued");
		model.addAttribute("issues", issues);
		return "issue";
	}
	@PostMapping("/returned/titleBasedSearch")
	public String returnedTitleSearch(@RequestParam("searchText") String title,Model model) {
//		System.out.println(textboxSelect);
		List<Issue> issues=new ArrayList<>();
		issues=issueService.getIssueByTitleAndUserNameAndStatus(title, "Returned");
		model.addAttribute("issues", issues);
		return "return";
	}
}
