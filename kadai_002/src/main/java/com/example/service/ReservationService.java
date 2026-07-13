package com.example.service;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.entity.Reservation;
import com.example.entity.Reservation.ReservationStatus;
import com.example.entity.Shop;
import com.example.entity.User;
import com.example.repository.ReservationRepository;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final MailService mailService;
	

	public ReservationService(ReservationRepository reservationRepository, MailService mailService) {
		this.reservationRepository = reservationRepository;
		this.mailService = mailService;
	}

	//予約作成
	@Transactional
	public void createReservation(User user, Shop shop, LocalDateTime datetime, Integer numberOfPeople) {

		// 重複チェック
		boolean exists = reservationRepository
				.findByShop_Id(shop.getId())
				.stream()
				.anyMatch(r -> r.getReservationDatetime().equals(datetime));

		if (exists) {
			throw new IllegalStateException("その時間はすでに予約されています");
		}

		// 保存

		Reservation reservation = new Reservation();

		reservation.setUser(user);
		reservation.setShop(shop);
		reservation.setReservationDatetime(datetime);
		reservation.setNumberOfPeople(numberOfPeople);
		reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);

		reservationRepository.save(reservation);
		mailService.sendReservationMail(reservation);
	}

	//ユーザーの予約一覧
	public List<Reservation> findByUser(User user) {
		return reservationRepository.findByUser_IdOrderByReservationDatetimeDesc(user.getId());

	}

	//予約キャンセル
	@Transactional
	public void cancelReservation(Integer reservationId, User user) {
		Reservation reservation = reservationRepository
				.findById(reservationId)
				.orElseThrow();

		//自分の予約だけキャンセル可能
		if (!reservation.getUser().getId().equals(user.getId())) {
			throw new IllegalStateException("不正な操作です");
		}
		reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
		reservationRepository.save(reservation);
		mailService.sendReservationCancelMail(reservation);
	}
	
	//予約ステータス変更
	@Transactional
	public void changeStatus(
	        Integer reservationId,
	        ReservationStatus status) {

	    Reservation reservation =
	            reservationRepository.getReferenceById(reservationId);

	    reservation.setStatus(status);

	    reservationRepository.save(reservation);
	}
}