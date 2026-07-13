package com.example.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.Reservation;
import com.example.entity.Reservation.ReservationStatus;
import com.example.repository.ReservationRepository;
import com.example.service.ReservationService;

@Controller
@RequestMapping("/admin/reservations")
public class AdminReservationController {

	private final ReservationRepository reservationRepository;
	private final ReservationService reservationService;

	public AdminReservationController(ReservationRepository reservationRepository, ReservationService reservationService) {
		this.reservationRepository = reservationRepository;
		this.reservationService = reservationService;
		}

	//一覧表示
	@GetMapping
	public String index(
			@RequestParam(name = "page", defaultValue = "0") int page,
			Model model) {

		Pageable pageable = PageRequest.of(page, 10, Sort.by("reservationDatetime").descending());

		Page<Reservation> reservationPage = reservationRepository.findAll(pageable);

		model.addAttribute("reservationPage", reservationPage);

		return "admin/reservations/index";
	}

	//詳細表示
	@GetMapping("/{id}")
	public String show(
			@PathVariable Integer id,
			Model model) {

		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("予約が存在しません。"));

		model.addAttribute("reservation", reservation);

		return "admin/reservations/show";
	}

//ステータス変更
	@PostMapping("/{id}/visit")
	public String visit(
	        @PathVariable Integer id,
	        RedirectAttributes redirectAttributes) {

	    reservationService.changeStatus(id, ReservationStatus.VISITED);

	    redirectAttributes.addFlashAttribute(
	            "successMessage",
	            "来店済みに変更しました。");

	    return "redirect:/admin/reservations/" + id;
	}
	
	@PostMapping("/{id}/cancel")
	public String cancel(
	        @PathVariable Integer id,
	        RedirectAttributes redirectAttributes) {

	    reservationService.changeStatus(id, ReservationStatus.CANCELLED);

	    redirectAttributes.addFlashAttribute(
	            "successMessage",
	            "キャンセルしました。");

	    return "redirect:/admin/reservations/" + id;
	}	
	
	
}