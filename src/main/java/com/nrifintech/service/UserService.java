package com.nrifintech.service;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nrifintech.exception.ResourceNotFoundException;

import com.nrifintech.model.Issue;
import com.nrifintech.model.User;
import com.nrifintech.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	
	public User addUser(User user)
	{
		userRepository.save(user);
		return user;
	}
	
	public ResponseEntity<User> getUserById(int userId) throws ResourceNotFoundException
	{
		User user =userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found for this id "+userId));
		return ResponseEntity.ok().body(user);
	}

	public List<User> getAllUsers()
	{
		List<User> userList=new ArrayList<User>();
		
		for(User a:userRepository.findAll())
		{
			userList.add(a);
		}
		return userList;
	}
	
	public ResponseEntity<User> updateUser(Integer userId,User newUser) throws ResourceNotFoundException{
		User user=userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found for the id "+userId));
		user.setName(newUser.getName());
		user.setAge(newUser.getAge());
		user.setEmail(newUser.getEmail());
		user.setUsername(newUser.getUsername());
		user.setPassword(newUser.getPassword());
		user.setFine(newUser.getFine());
		userRepository.save(user);
		return ResponseEntity.ok().body(user);
	}
	
	public ResponseEntity<User> deleteUser(Integer userId) throws ResourceNotFoundException{
		User user =userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found for this id "+userId));
		userRepository.delete(user);
		return ResponseEntity.ok().body(user);
	}
	public ByteArrayOutputStream generateUserReport(Integer userId) throws ResourceNotFoundException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("User Issue Report");
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		int rownum = 1;
		Row rownames = sheet.createRow(rownum++);
		int cellnum = 1;
		Cell cellIssueidName = rownames.createCell(cellnum++);
		cellIssueidName.setCellValue("issueId");
		Cell cellBooktitleName = rownames.createCell(cellnum++);
		cellBooktitleName.setCellValue("bookTitle");
		Cell cellIssueStatus= rownames.createCell(cellnum++);
		cellIssueStatus.setCellValue("Current Status");
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id " + userId));
		

			for (Issue i : user.getIssues()) {
				Row rowvalues = sheet.createRow(rownum++);
				cellnum = 1;
				Cell cellIssueid = rowvalues.createCell(cellnum++);
				cellIssueid.setCellValue(i.getIssueId());
				Cell cellBooktitle = rowvalues.createCell(cellnum++);
				cellBooktitle.setCellValue(i.getBook().getTitle());
				Cell cellStatus = rownames.createCell(cellnum++);
				cellStatus.setCellValue(i.getStatus());

			}
		
		try {
			workbook.write(bs);
			bs.close();
			workbook.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return bs;
	}
}
