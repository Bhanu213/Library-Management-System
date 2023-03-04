package com.nrifintech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.nrifintech.model.Issue;

public interface IssueRepository extends CrudRepository<Issue,Integer>{
	@Query(value = "select * from issue where status = :status",nativeQuery = true)
	public List<Issue> findIssueAllByStatus(@Param("status") String issue);
}
