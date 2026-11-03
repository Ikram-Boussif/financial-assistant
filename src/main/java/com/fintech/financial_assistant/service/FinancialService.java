package com.fintech.financial_assistant.service;

import com.fintech.financial_assistant.model.FinancialAdvice;
import com.fintech.financial_assistant.repository.FinancialAdviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialService {
    private final FinancialAdviceRepository financialAdviceRepository;

    public FinancialService(FinancialAdviceRepository financialAdviceRepository) {
        this.financialAdviceRepository = financialAdviceRepository;
    }

    public List<FinancialAdvice> getAdvices() {
        return this.financialAdviceRepository.findAll();
    }

    public FinancialAdvice addAdvice(FinancialAdvice advice) {
        return this.financialAdviceRepository.save(advice);
    }
}
