package com.fintech.financial_assistant.model;

import jakarta.persistence.*;

@Entity
@Table(name = "financial_advice")
public class FinancialAdvice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String advice;
    private String priority;


    public FinancialAdvice() {
    }

    public FinancialAdvice(String category, String advice, String priority) {
        this.category = category;
        this.advice = advice;
        this.priority = priority;
    }


    public Long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
