package com.example.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entity.Reservation;
import com.example.entity.Shop;
import com.example.entity.User;
import com.example.form.ReservationInputForm;
import com.example.repository.ReservationRepository;
import com.example.security.UserDetailsImpl;
import com.example.service.ReservationService;
import com.example.service.ShopService;

@Controller
@RequestMapping("/shops")
public class ReservationController {

	private final ShopService shopService;
	private final ReservationService reservationService;
	private final ReservationRepository reservationRepository;

	public ReservationController(ShopService shopService,
			ReservationService reservationService,
			ReservationRepository reservationRepository) {
		this.shopService = shopService;
		this.reservationService = reservationService;
		this.reservationRepository = reservationRepository;
	}

	//予約一覧
	@GetMapping("/reservations")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model) {
		User user = userDetailsImpl.getUser();
		Page<Reservation> reservationPage = reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable);

		model.addAttribute("reservationPage", reservationPage);
		model.addAttribute("user", user);

		return "reservations/index";
	}

	// 予約作成
	@PostMapping("/{shopId}/reservations/confirm")
	public String reserve(@PathVariable Integer shopId,
			@Valid @ModelAttribute ReservationInputForm reservationInputForm, BindingResult bindingResult,
			@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {

		if (bindingResult.hasErrors()) {
			Shop shop = shopService.findById(shopId);

			model.addAttribute("shop", shop);
			return "shops/show";
		}

		User user = userDetails.getUser();
		Shop shop = shopService.findById(shopId);

		if (user.getMembershipType() == 0) {
			throw new AccessDeniedException("有料会員のみ予約可能です");
		}

		model.addAttribute("shop", shop);
		model.addAttribute("reservationInputForm", reservationInputForm);
		return "reservations/confirm";
	}

	//予約確定
	@PostMapping("/{shopId}/reservations/create")
	public String create(
			@PathVariable Integer shopId,
			@Valid @ModelAttribute ReservationInputForm reservationInputForm,
			BindingResult bindingResult,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {

		User user = userDetails.getUser();

		Shop shop = shopService.findById(shopId);

		reservationService.createReservation(
				user,
				shop,
				reservationInputForm.getReservationDatetime(),
				reservationInputForm.getNumberOfPeople());

		return "redirect:/shops/" + shopId + "/reservations/complete";
	}

	@GetMapping("/{shopId}/reservations/complete")
	public String complete() {
		return "reservations/complete";
	}

	//予約キャンセル
	@PostMapping("/reservations/{reservationId}/cancel")
	public String cancel(
			@PathVariable Integer reservationId,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {

		User user = userDetails.getUser();

		reservationService.cancelReservation(reservationId, user);

		return "redirect:/shops/reservations";
	}
}