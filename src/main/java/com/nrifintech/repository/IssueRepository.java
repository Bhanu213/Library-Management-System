package com.nrifintech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.nrifintech.model.Issue;

public interface IssueRepository extends CrudRepository<Issue,Integer>{
	@Query(value = "select * from issue where status = :status",nativeQuery = true)
	public List<Issue> findIssueAllByStatus(@Param("status") String issue);
	
	@Query(value = "select * from user u,issue i where u.name = :name and i.status = :status and i.user_id = u.user_id",nativeQuery = true)
	public List<Issue> findIssueAllByUserNameAndStatus(@Param("name") String name,@Param("status") String status);
	
	@Query(value = "select * from book b,issue i where b.title = :title and i.status = :status and b.book_id = i.book_id",nativeQuery = true)
	public List<Issue> findIssueAllByTitleAndUserNameAndStatus(@Param("title") String title,@Param("status") String status);
}
