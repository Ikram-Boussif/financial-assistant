package com.fintech.financial_assistant.Controller;

import com.fintech.financial_assistant.model.FinancialAdvice;
import com.fintech.financial_assistant.service.AiAdvisorService;
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
    private final AiAdvisorService aiAdvisorService;

    public FinancialController(FinancialService financialService, AiAdvisorService aiAdvisorService) {
        this.financialService = financialService;
        this.aiAdvisorService = aiAdvisorService;
    }

    public record AskRequest(String question) {}

    public record AskResponse(String answer) {}

    @PostMapping("/ask")
    public AskResponse ask(@RequestBody AskRequest request) {
        return new AskResponse(this.aiAdvisorService.ask(request.question()));
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
