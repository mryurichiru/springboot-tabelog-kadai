package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	@Query("""
			SELECT u
			FROM User u
			WHERE
			(
			    :name IS NULL
			    OR :name = ''
			    OR u.name LIKE %:name%
			    OR u.furigana LIKE %:name%
			)
			AND (
			    :email IS NULL
			    OR :email = ''
			    OR u.email LIKE %:email%
			)
			AND (
			    :phoneNumber IS NULL
			    OR :phoneNumber = ''
			    OR u.phoneNumber LIKE %:phoneNumber%
			)
			AND (
			    :membershipType IS NULL
			    OR u.membershipType = :membershipType
			)
			""")
			Page<User> search(
			        @Param("name") String name,
			        @Param("email") String email,
			        @Param("phoneNumber") String phoneNumber,
			        @Param("membershipType") Integer membershipType,
			        Pageable pageable);
	
	//csv出力用
	@Query("""
			SELECT u
			FROM User u
			WHERE
			(
			    :name IS NULL
			    OR :name = ''
			    OR u.name LIKE %:name%
			    OR u.furigana LIKE %:name%
			)
			AND (
			    :email IS NULL
			    OR :email = ''
			    OR u.email LIKE %:email%
			)
			AND (
			    :phoneNumber IS NULL
			    OR :phoneNumber = ''
			    OR u.phoneNumber LIKE %:phoneNumber%
			)
			AND (
			    :membershipType IS NULL
			    OR u.membershipType = :membershipType
			)
			ORDER BY u.id
			""")
			List<User> searchForCsv(
			        @Param("name") String name,
			        @Param("email") String email,
			        @Param("phoneNumber") String phoneNumber,
			        @Param("membershipType") Integer membershipType);
	
	public User findByEmail(String email);
	public Page<User> findByNameLikeOrFuriganaLike(String nameKeyword, String furiganaKeyword, Pageable pageable);
	
	//集計用
	long countByMembershipType(Integer membershipType);

	long countByEnabled(Boolean enabled);
	
	Optional<User> findByStripeCustomerId(String stripeCustomerId);
	
	}