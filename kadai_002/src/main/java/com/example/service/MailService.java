package com.example.service;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.entity.PasswordResetToken;
import com.example.entity.Reservation;
import com.example.entity.User;

@Service
public class MailService {

	private final JavaMailSender javaMailSender;
	private final VerificationTokenService verificationTokenService;
	private final PasswordResetService passwordResetService;

	public MailService(
			JavaMailSender javaMailSender,
			VerificationTokenService verificationTokenService,
			PasswordResetService passwordResetService) {

		this.javaMailSender = javaMailSender;
		this.verificationTokenService = verificationTokenService;
		this.passwordResetService = passwordResetService;
	}

	//会員登録時の認証メール
	public void sendVerificationMail(User user, String requestUrl) {

		String token = UUID.randomUUID().toString();

		verificationTokenService.create(user, token);

		String senderAddress = "mryurichiru@gmail.com";
		String recipientAddress = user.getEmail();
		String subject = "【NAGOYAMESHI】メール認証";

		String confirmationUrl = requestUrl + "/verify?token=" + token;

		String message = "以下のリンクをクリックして会員登録を完了してください。";

		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setFrom(senderAddress);
		mailMessage.setTo(recipientAddress);
		mailMessage.setSubject(subject);
		mailMessage.setText(message + "\n" + confirmationUrl);

		javaMailSender.send(mailMessage);
	}

	//予約完了メール
	public void sendReservationMail(Reservation reservation) {

		String subject = "【NAGOYAMESHI】ご予約ありがとうございます";

		String text = """
				%s 様

				この度はNAGOYAMESHIをご利用いただき、
				誠にありがとうございます。

				以下の内容でご予約を承りました。

				━━━━━━━━━━━━━━
				店舗名：%s
				予約日時：%s
				人数：%d名
				━━━━━━━━━━━━━━

				ご来店を心よりお待ちしております。

				※このメールは自動送信されています。
				"""
				.formatted(
						reservation.getUser().getName(),
						reservation.getShop().getName(),
						reservation.getReservationDatetime()
								.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")),
						reservation.getNumberOfPeople());

		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setTo(reservation.getUser().getEmail());
		mailMessage.setSubject(subject);
		mailMessage.setText(text);

		javaMailSender.send(mailMessage);
	}

	//予約キャンセルメール
	public void sendReservationCancelMail(Reservation reservation) {

		String subject = "【NAGOYAMESHI】ご予約をキャンセルしました";

		String text = """
				%s 様

				ご予約のキャンセルを承りました。

				キャンセル内容は以下の通りです。

				━━━━━━━━━━━━━━
				店舗名：%s
				予約日時：%s
				人数：%d名
				━━━━━━━━━━━━━━

				またのご利用を心よりお待ちしております。

				※このメールは自動送信されています。
				"""
				.formatted(
						reservation.getUser().getName(),
						reservation.getShop().getName(),
						reservation.getReservationDatetime()
								.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")),
						reservation.getNumberOfPeople());

		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setTo(reservation.getUser().getEmail());
		mailMessage.setSubject(subject);
		mailMessage.setText(text);

		javaMailSender.send(mailMessage);
	}

	//パスワードリセット
	public void sendPasswordResetMail(
			User user,
			String requestUrl) {

		PasswordResetToken passwordResetToken = passwordResetService.create(user);

		String senderAddress = "mryurichiru@gmail.com";
		String recipientAddress = user.getEmail();
		String subject = "【NAGOYAMESHI】パスワード再設定";

		String resetUrl = requestUrl + "/password/reset/" +
				passwordResetToken.getToken();

		String message = user.getName() + " 様\n\n" +
				"パスワード再設定の申請を受け付けました。\n\n" +
				"30分以内に下記URLへアクセスし、新しいパスワードを設定してください。\n\n" +
				resetUrl +
				"\n\nこのメールに心当たりがない場合は、このメールを破棄してください。\n\n" +
				"NAGOYAMESHI";

		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setFrom(senderAddress);
		mailMessage.setTo(recipientAddress);
		mailMessage.setSubject(subject);
		mailMessage.setText(message);

		javaMailSender.send(mailMessage);
	}
}
