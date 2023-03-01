package com.nrifintech.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.model.Status;
import com.nrifintech.repository.StatusRepository;

@Service
public class StatusService {
	@Autowired
	StatusRepository statusRepo;

	public List<Status> showall() {
		List<Status> status = new ArrayList<>();
		for (Status s : statusRepo.findAll()) {
			status.add(s);
		}
		return status;

	}
	
	public int getServiceId(String statusDescription)
	{
		for(Status s: statusRepo.findAll())
		{
			if(statusDescription.equals(s.getDescription()))
				return s.getStatusId();
		}
		
		return -1;
		
	}
}
