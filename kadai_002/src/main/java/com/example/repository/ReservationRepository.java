package com.example.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Reservation;
import com.example.entity.User;

public interface ReservationRepository extends JpaRepository<Reservation, Integer>{

	public Page<Reservation> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
	
	List<Reservation> findByUser_Id(Integer userId);
	
	List<Reservation> findByShop_Id(Integer shopId);
	
	List<Reservation> findByUser_IdOrderByReservationDatetimeDesc(Integer userId);
	List<Reservation> findByShop_IdOrderByReservationDatetimeAsc(Integer shopId);
	
	List<Reservation> findByUser_IdAndStatus(Integer userId, Reservation.ReservationStatus status);
	
	boolean existsByShop_IdAndReservationDatetime(Integer shopId,
            LocalDateTime reservationDatetime);
	
	//会員削除用
		void deleteByUser(User user);

}
