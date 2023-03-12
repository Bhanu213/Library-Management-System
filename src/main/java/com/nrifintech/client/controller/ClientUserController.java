package com.nrifintech.client.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.nrifintech.exception.ResourceNotFoundException;
import com.nrifintech.model.Book;
import com.nrifintech.model.Issue;
import com.nrifintech.model.User;
import com.nrifintech.repository.UserRepository;
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
	public String dashboard(Model model, Principal principal) throws ResourceNotFoundException {
		List<Book> books = bookService.getAllBooks();
//		System.out.println(books.get(0).getAuthor().getAuthorName());
		model.addAttribute("books", books);
		System.out.println(books);
		User user = userService.getUserByusername(principal.getName()).getBody();
		model.addAttribute("user", user);
		return "dashboard";
	}

	@PostMapping("/dashboard/performSearch")
	public String search(@RequestParam("drop") String dropdownSelect, @RequestParam("searchText") String textboxSelect,
			Model model, Principal principal) throws ResourceNotFoundException {
		List<Book> books = new ArrayList<>();
		if (dropdownSelect.equalsIgnoreCase("author")) {
			books = bookService.getBookByAuthor(textboxSelect).getBody();
		} else if (dropdownSelect.equalsIgnoreCase("genre")) {
			books = bookService.getBookByGenre(textboxSelect).getBody();
		} else if (dropdownSelect.equalsIgnoreCase("title")) {
			books = List.of(bookService.getBookByTitle(textboxSelect).getBody());
		} else {
			books = bookService.getAllBooks();
		}
		model.addAttribute("books", books);
		User user = userService.getUserByusername(principal.getName()).getBody();
		model.addAttribute("user", user);
		return "dashboard";
	}

	@GetMapping("/createIssue/{bookId}")
	public RedirectView createIssue(@PathVariable("bookId") Integer bookId, RedirectAttributes redirAttrs,
			Principal principal) {
		try {
			Book book = bookService.getBookById(bookId).getBody();
			book.setQty(book.getQty() - 1);
			bookService.updateBook(bookId, book);
//			User user=userService.getUserById(1).getBody();
			Issue issue = new Issue();
			issue.setUser(userService.getUserByusername(principal.getName()).getBody());
			issue.setBook(book);
			issue.setStatus("Granted");
			issue.setFine(0);
			String date=LocalDate.now().toString();
			issue.setIssueDate(date);
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
	public String issuePage(Model model, Principal principal) throws ResourceNotFoundException {
		List<Issue> issues = issueService.getIssueByUserNameAndStatus(principal.getName(), "Granted");
		model.addAttribute("issues", issues);
		User user = userService.getUserByusername(principal.getName()).getBody();
		model.addAttribute("user", user);
		return "granted";
	}

	@GetMapping("/deleteIssue/{issueId}")
	public RedirectView deleteIssue(@PathVariable("issueId") Integer issueId) {
		try {
			Issue issue = new Issue();
			issue = issueService.getIssueByIssueId(issueId).getBody();
			Book book = new Book();
			book = issue.getBook();
			book.setQty(book.getQty() + 1);
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
	public String issue(Model model, Principal principal) throws ResourceNotFoundException {
		List<Issue> issues = new ArrayList<>();
		issues = issueService.getIssueByUserNameAndStatus(principal.getName(), "Issued");
		model.addAttribute("issues", issues);
		User user = userService.getUserByusername(principal.getName()).getBody();
		model.addAttribute("user", user);
		return "issue";
	}

	@GetMapping("/return")
	public String returnPage(Model model, Principal principal) throws ResourceNotFoundException {
		List<Issue> issues = new ArrayList<>();
		issues = issueService.getIssueByUserNameAndStatus(principal.getName(), "Returned");
		model.addAttribute("issues", issues);
		User user = userService.getUserByusername(principal.getName()).getBody();
		model.addAttribute("user", user);
		return "return";
	}

	@PostMapping("/granted/titleBasedSearch")
	public String grantedTitleSearch(@RequestParam("searchText") String title, Model model, Principal principal)
			throws ResourceNotFoundException {
//		System.out.println(textboxSelect);
		List<Issue> issues = new ArrayList<>();
		issues = issueService.getIssueByTitleAndUserNameAndStatus(title, "Granted", principal.getName());
		model.addAttribute("issues", issues);
		User user = userService.getUserByusername(principal.getName()).getBody();
		model.addAttribute("user", user);
		return "granted";
	}

	@PostMapping("/issued/titleBasedSearch")
	public String issuedTitleSearch(@RequestParam("searchText") String title, Model model, Principal principal)
			throws ResourceNotFoundException {
//		System.out.println(textboxSelect);
		List<Issue> issues = new ArrayList<>();
		issues = issueService.getIssueByTitleAndUserNameAndStatus(title, "Issued", principal.getName());
		model.addAttribute("issues", issues);
		User user = userService.getUserByusername(principal.getName()).getBody();
		model.addAttribute("user", user);
		return "issue";
	}

	@PostMapping("/returned/titleBasedSearch")
	public String returnedTitleSearch(@RequestParam("searchText") String title, Model model, Principal principal)
			throws ResourceNotFoundException {
//		System.out.println(textboxSelect);
		List<Issue> issues = new ArrayList<>();
		issues = issueService.getIssueByTitleAndUserNameAndStatus(title, "Returned", principal.getName());
		model.addAttribute("issues", issues);
		User user = userService.getUserByusername(principal.getName()).getBody();
		model.addAttribute("user", user);
		return "return";
	}

	@GetMapping("/profile")
	public String profile(Model model, Principal principal) throws ResourceNotFoundException {
		User user = userService.getUserByusername(principal.getName()).getBody();
		model.addAttribute("user", user);
		System.out.println(user);
		return "profile";
	}

	@PostMapping("/updateProfile")
	public RedirectView updateProfile(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("username") String username) throws ResourceNotFoundException {
		User user=userService.getUserByusername(username).getBody();
		user.setEmail(email);
		user.setName(name);
		user.setUsername(username);
		userService.updateUser(user.getId(), user);
		return new RedirectView("/user/profile");
	}
}
