package com.nrifintech.client.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.nrifintech.model.Issue;
import com.nrifintech.service.IssueService;

@Controller
@RequestMapping("/admin")
public class ClientAdminController {
	
	@Autowired
	private IssueService issueService;
	
	@GetMapping("/granted")
	public String granted(Model model) {
		List<Issue> issues=issueService.getIssueByStatus("Granted");
		model.addAttribute("issues", issues);
		return "admin/granted";
	}
	
	@GetMapping("/gratedToIssue/{issueId}")
	public RedirectView gratedToIssue(@PathVariable Integer issueID) {
		try {
			Issue issue=new Issue();
			issue=issueService.getIssueByIssueId(issueID).getBody();
			issue.setStatus("Issued");
			issueService.updateIssue(issue, issueID);
			return new RedirectView("/admin/granted");			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new RedirectView("/error");
		}
		
	}
}
