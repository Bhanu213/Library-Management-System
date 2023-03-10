package com.nrifintech.client.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder.In;

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

import com.nrifintech.exception.ResourceNotFoundException;
import com.nrifintech.model.Book;
import com.nrifintech.model.Issue;
import com.nrifintech.model.User;
import com.nrifintech.service.BookService;
import com.nrifintech.service.IssueService;
import com.nrifintech.service.UserService;

@Controller
@RequestMapping("/admin")
public class ClientAdminController {

	@Autowired
	private IssueService issueService;

	@Autowired
	private BookService bookService;

	@Autowired
	private UserService userService;

	@GetMapping("/dashboard")
	public String dashboard(Model model, Principal principal) {
		try {
			List<Book> books = bookService.getAllBooks();
			model.addAttribute("books", books);
			User user = userService.getUserByusername(principal.getName()).getBody();
			model.addAttribute("user", user);
			return "admin/dashboard";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "error";
		}
	}
	@PostMapping("/dashboard/performSearch")
	public String search(@RequestParam("drop") String dropdownSelect,@RequestParam("searchText") String textboxSelect,Model model, Principal principal) throws ResourceNotFoundException {
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
		User user = userService.getUserByusername(principal.getName()).getBody();
		model.addAttribute("user", user);
		return "dashboard";
	}

	@GetMapping("/granted")
	public String granted(Model model) {
		List<Issue> issues = issueService.getIssueByStatus("Granted");
		System.out.println(issues);
		model.addAttribute("issues", issues);
		return "admin/granted";
	}

	@GetMapping("/grantedToIssue/{issueId}")
	public RedirectView gratedToIssue(@PathVariable Integer issueId) {
		try {
			Issue issue = new Issue();
			issue = issueService.getIssueByIssueId(issueId).getBody();
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
		List<Issue> issues = issueService.getIssueByStatus("Issued");
		model.addAttribute("issues", issues);
		return "admin/issued";
	}

	@GetMapping("/issuedToReturn/{issueId}")
	public RedirectView issuedToReturn(@PathVariable Integer issueId) {
		try {
			Issue issue = new Issue();
			issue = issueService.getIssueByIssueId(issueId).getBody();

			// adding book
			Book book = new Book();
			book = issue.getBook();
			book.setQty(book.getQty() + 1);
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
	public String searchByNameAndStatusGranted(@RequestParam("searchText") String userName, Model model) {
//		System.out.println(textboxSelect);
		List<Issue> issues = new ArrayList<>();
		System.out.println(userName);
		issues = issueService.getIssueByUserNameAndStatus(userName, "Granted");
		System.out.println(issues);
		model.addAttribute("issues", issues);
		return "admin/granted";
	}

	@PostMapping("/issued/issueUserSearch")
	public String searchByNameAndStatusIssued(@RequestParam("searchText") String userName, Model model) {
//		System.out.println(textboxSelect);
		List<Issue> issues = new ArrayList<>();
		issues = issueService.getIssueByUserNameAndStatus(userName, "Issued");
		model.addAttribute("issues", issues);
		return "admin/issued";
	}

	@GetMapping("/addBook")
	public String addBook(Model model) {
		Book book = new Book();
		model.addAttribute("book", book);
		return "admin/addBook";
	}

	@PostMapping("/postBook")
	public RedirectView postBook(@ModelAttribute("book") Book book) {
		System.out.println(book);
		bookService.addBook(book);
		return new RedirectView("/admin/addBook");
	}

	@PostMapping("/updateBook/{bookId}")
	public RedirectView updateBook(@PathVariable Integer bookId, @RequestParam("title") String title,
			@RequestParam("author") String author, @RequestParam("genre") String genre,
			@RequestParam("qty") Integer qty, @RequestParam("description") String description) {
		try {
			Book book = bookService.getBookById(bookId).getBody();
			book.getAuthor().setAuthorName(author);
			book.getGenre().setGenreName(genre);
			book.setQty(qty);
			book.setTitle(title);
			book.setDescription(description);
			bookService.updateBook(bookId, book);
			return new RedirectView("/admin/dashboard");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new RedirectView("/error");
		}

	}
}
