package com.poliglota.service;

import com.poliglota.model.mysql.*;
import com.poliglota.repository.AccountRepository;
import com.poliglota.repository.InvoiceRepository;
import com.poliglota.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillingService {

	private final InvoiceRepository invoiceRepository;
	private final PaymentRepository paymentRepository;
	private final AccountRepository accountRepository;

	/**
	 * Genera una factura nueva para un usuario.
	 */
	public Invoice generateInvoice(Long userId, double amount, String description) {
		Invoice invoice = new Invoice();
		invoice.setUser(userId);
		invoice.setIssueDate(LocalDateTime.now());
		invoice.setBilledProcesses(java.util.List.of(description));
		invoice.setStatus("pending");
		return invoiceRepository.save(invoice);
	}

	/**
	 * Registra un pago y acredita el monto a la cuenta del usuario.
	 */
	@Transactional
	public Payment registerPayment(Long userId, String invoiceId, double amount, String method) {
		// 1️⃣ Crear el pago
		Payment payment = new Payment();
		payment.setInvoiceId(invoiceId);
		payment.setPaymentDate(LocalDateTime.now());
		payment.setAmount(amount);
		payment.setPaymentMethod(method);
		paymentRepository.save(payment);

		// 2️⃣ Acreditar el monto en la cuenta del usuario
		Optional<Account> optAccount = accountRepository.findAll().stream()
				.filter(a -> a.getUserId().equals(userId))
				.findFirst();

		Account account;
		if (optAccount.isPresent()) {
			account = optAccount.get();
			account.setCurrentBalance(account.getCurrentBalance() + amount);
		} else {
			account = new Account();
			account.setUserId(userId);
			account.setCurrentBalance(amount);
			account.setTransactionHistory(java.util.List.of("Acreditación inicial: $" + amount));
		}

		accountRepository.save(account);

		// 3️⃣ Cambiar el estado de la factura a "paid"
		try {
			Long invId = Long.valueOf(invoiceId);
			invoiceRepository.findById(invId).ifPresent(inv -> {
				inv.setStatus("paid");
				invoiceRepository.save(inv);
			});
		} catch (NumberFormatException e) {
			// invoiceId no es un número válido, no se puede actualizar la factura
		}

		return payment;
	}

	/**
	 * Consulta el saldo de la cuenta corriente de un usuario.
	 */
	public double getAccountBalance(Long userId) {
		return accountRepository.findAll().stream()
				.filter(a -> a.getUserId().equals(userId))
				.map(Account::getCurrentBalance)
				.findFirst()
				.orElse(0.0);
	}
}
