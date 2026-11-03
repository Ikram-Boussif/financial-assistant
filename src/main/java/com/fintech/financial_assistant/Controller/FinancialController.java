package com.fintech.financial_assistant.Controller;

import com.fintech.financial_assistant.model.FinancialAdvice;
import com.fintech.financial_assistant.service.FinancialService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/financial")
public class FinancialController {

    private final FinancialService financialService;

    public FinancialController(FinancialService financialService) {
        this.financialService = financialService;
    }

    @GetMapping("/advice")
    public List<FinancialAdvice> getAdvice() {
        return this.financialService.getAdvices();
    }

    @PostMapping("/advice")
    public FinancialAdvice addAdvice(@RequestBody FinancialAdvice financialAdvice) {
        return this.financialService.addAdvice(financialAdvice);
    }
}
