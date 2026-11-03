package com.fintech.financial_assistant.repository;

import com.fintech.financial_assistant.model.FinancialAdvice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialAdviceRepository extends JpaRepository<FinancialAdvice, Long> {
}
