package com.example.service;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.form.AdminProfileEditForm;
import com.example.form.AdminUserEditForm;
import com.example.form.SignupForm;
import com.example.form.UserEditForm;
import com.example.repository.FavoriteRepository;
import com.example.repository.ReservationRepository;
import com.example.repository.ReviewRepository;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.repository.VerificationTokenRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final VerificationTokenRepository verificationTokenRepository;
	private final ReservationRepository reservationRepository;
	private final FavoriteRepository favoriteRepository;
	private final ReviewRepository reviewRepository;
	private final MailService mailService;

	public UserService(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder, VerificationTokenRepository verificationTokenRepository,
			ReservationRepository reservationRepository,
			FavoriteRepository favoriteRepository,
			ReviewRepository reviewRepository,
			MailService mailService) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.verificationTokenRepository = verificationTokenRepository;
		this.reservationRepository = reservationRepository;
		this.favoriteRepository = favoriteRepository;
		this.reviewRepository = reviewRepository;
		this.mailService = mailService;
	}

	//サインアップ
	@Transactional
	public User create(SignupForm signupForm) {
		User user = new User();
		Role role = roleRepository.findByName("ROLE_GENERAL");

		user.setName(signupForm.getName());
		user.setFurigana(signupForm.getFurigana());
		user.setEmail(signupForm.getEmail());
		user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
		user.setPhoneNumber(signupForm.getPhoneNumber());
		user.setDateOfBirth(signupForm.getDateOfBirth());
		user.setOccupation(signupForm.getOccupation());

		user.setRole(role);
		user.setMembershipType(0);
		user.setEnabled(false);

		return userRepository.save(user);
	}

	//会員情報編集
	@Transactional
	public void update(UserEditForm userEditForm) {
		User user = userRepository.getReferenceById(userEditForm.getId());

		user.setName(userEditForm.getName());
		user.setFurigana(userEditForm.getFurigana());
		user.setEmail(userEditForm.getEmail());
		user.setPhoneNumber(userEditForm.getPhoneNumber());
		user.setDateOfBirth(userEditForm.getDateOfBirth());
		user.setOccupation(userEditForm.getOccupation());
		userRepository.save(user);
	}

	// メールアドレスが登録済みかどうかをチェックする
	public boolean isEmailRegistered(String email) {
		User user = userRepository.findByEmail(email);
		return user != null;
	}

	// パスワードとパスワード（確認用）の入力値が一致するかどうかをチェックする
	public boolean isSamePassword(String password, String passwordConfirmation) {
		return password.equals(passwordConfirmation);
	}

	// ユーザーを有効にする
	@Transactional
	public void enableUser(User user) {
		user.setEnabled(true);
		userRepository.save(user);
	}

	// メールアドレスが変更されたかどうかをチェックする
	public boolean isEmailChanged(UserEditForm userEditForm) {
		User currentUser = userRepository.getReferenceById(userEditForm.getId());
		return !userEditForm.getEmail().equals(currentUser.getEmail());
	}

	//退会
	@Transactional
	public void deleteUser(User user) {

		reservationRepository.deleteByUser(user);
		favoriteRepository.deleteByUser(user);
		reviewRepository.deleteByUser(user);

		// token削除
		verificationTokenRepository.deleteByUser(user);

		// ユーザー削除
		userRepository.delete(user);
	}

	//管理者の会員情報編集
	@Transactional
	public void update(AdminUserEditForm adminUserEditForm) {

		User user = userRepository.getReferenceById(adminUserEditForm.getId());

		user.setName(adminUserEditForm.getName());
		user.setFurigana(adminUserEditForm.getFurigana());
		user.setEmail(adminUserEditForm.getEmail());
		user.setPhoneNumber(adminUserEditForm.getPhoneNumber());
		user.setDateOfBirth(adminUserEditForm.getDateOfBirth());
		user.setOccupation(adminUserEditForm.getOccupation());

		user.setMembershipType(adminUserEditForm.getMembershipType());
		user.setEnabled(adminUserEditForm.getEnabled());

		Role role = roleRepository.getReferenceById(adminUserEditForm.getRoleId());
		user.setRole(role);

		userRepository.save(user);
	}

	//仮登録メール再送
	@Transactional
	public void resendVerificationMail(Integer userId, String requestUrl) {
		User user = userRepository.getReferenceById(userId);
		mailService.sendVerificationMail(user, requestUrl);
	}
	
	//csv出力
	public String createCsv(List<User> users) {

	    StringBuilder sb = new StringBuilder();

	    sb.append("ID,氏名,フリガナ,メールアドレス,電話番号,会員種別,ロール,登録日時\n");

	    for (User user : users) {

	        sb.append(user.getId()).append(",");
	        sb.append(user.getName()).append(",");
	        sb.append(user.getFurigana()).append(",");
	        sb.append(user.getEmail()).append(",");
	        sb.append(user.getPhoneNumber()).append(",");
	        sb.append(user.isPaidMember() ? "有料会員" : "無料会員").append(",");
	        sb.append(user.getRole().getName()).append(",");
	        sb.append(user.getCreatedAt()).append("\n");
	    }

	    return sb.toString();
	}
	
	// 管理者プロフィール編集
	@Transactional
	public void updateProfile(AdminProfileEditForm form) {

	    User user = userRepository.getReferenceById(form.getId());

	    user.setName(form.getName());
	    user.setFurigana(form.getFurigana());
	    user.setEmail(form.getEmail());
	    user.setPhoneNumber(form.getPhoneNumber());
	    user.setDateOfBirth(form.getDateOfBirth());
	    user.setOccupation(form.getOccupation());

	    userRepository.save(user);
	}
}