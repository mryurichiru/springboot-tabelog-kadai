package com.example.dto;

public class SalesSummary {

    // 売上金額（円）
    private Long totalAmount;

    // 決済件数
    private Integer paymentCount;

    public SalesSummary() {
    }

    public SalesSummary(Long totalAmount, Integer paymentCount) {
        this.totalAmount = totalAmount;
        this.paymentCount = paymentCount;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getPaymentCount() {
        return paymentCount;
    }

    public void setPaymentCount(Integer paymentCount) {
        this.paymentCount = paymentCount;
    }
}