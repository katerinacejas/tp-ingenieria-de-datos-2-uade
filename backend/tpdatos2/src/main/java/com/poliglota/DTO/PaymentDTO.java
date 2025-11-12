package com.poliglota.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PaymentDTO {
	private String paymentId;
	private String invoiceId;
	private LocalDateTime paymentDate;
	private double amount;
	private String paymentMethod;
}
