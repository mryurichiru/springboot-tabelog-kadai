package com.example.form;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ReservationInputForm {
	 @NotNull(message = "予約日時を入力してください")
	    @Future(message = "現在より未来の日時を入力してください")
	    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	    private LocalDateTime reservationDatetime;

	    @NotNull(message = "人数を入力してください")
	    @Min(value = 1, message = "1人以上で入力してください")
	    private Integer numberOfPeople;
}
