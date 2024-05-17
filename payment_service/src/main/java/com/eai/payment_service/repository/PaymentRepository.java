package com.eai.payment_service.repository;

import com.eai.payment_service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    Payment findPaymentByIdClient(Integer id);
}
