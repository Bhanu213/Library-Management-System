package com.nrifintech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.nrifintech.model.Issue;

public interface IssueRepository extends CrudRepository<Issue,Integer>{
	
	@Query(value = "select * from issue where status = :status",nativeQuery = true)
	public List<Issue> findIssueAllByStatus(@Param("status") String issue);
	
	@Query(value = "select * from user u,issue i where i.user_id = u.user_id and u.username = :username and i.status = :status",nativeQuery = true)
	public List<Issue> findIssueAllByUserNameAndStatus(@Param("username") String userName,@Param("status") String status);
	
	@Query(value = "select * from book b,issue i,user u where u.user_id = i.user_id and b.book_id = i.book_id and b.title = :title and i.status = :status and u.username = :username",nativeQuery = true)
	public List<Issue> findIssueAllByTitleAndUserNameAndStatus(@Param("title") String title,@Param("status") String status,@Param("username") String userName);
}
