package com.example.service;

import java.time.LocalDate;
import java.time.ZoneId;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.dto.SalesSummary;
import com.example.entity.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceCollection;
import com.stripe.model.checkout.Session;
import com.stripe.param.InvoiceListParams;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripeService {

    @Value("${stripe.api-key}")
    private String stripeApiKey;

    @Value("${stripe.price-id}")
    private String priceId;
    
    @Value("${app.base-url}")
    private String baseUrl;

    // Stripe Checkout Session作成
    public String createStripeSession(User user, HttpServletRequest httpServletRequest) {
        Stripe.apiKey = stripeApiKey;
        String requestUrl = new String(httpServletRequest.getRequestURL());
        SessionCreateParams params = SessionCreateParams.builder()

                // カード決済
                .addPaymentMethodType(
                    SessionCreateParams.PaymentMethodType.CARD)

                // サブスクモード
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)

                // 商品
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPrice(priceId)
                        .setQuantity(1L)
                        .build())
                .putMetadata(
                        "userId",
                        user.getId().toString())


                // 成功時
                .setSuccessUrl(
                    requestUrl.replace(
                        "/subscription/checkout",
                        "/subscription/success"))

                // キャンセル時
                .setCancelUrl(
                    requestUrl.replace(
                        "/subscription/checkout",
                        "/subscription/create"))

                // メタデータ
                .setSubscriptionData(
                    SessionCreateParams.SubscriptionData.builder()

                        .putMetadata(
                            "userId",
                            user.getId().toString())
                        .build())
                .build();

        try {
            Session session = Session.create(params);
            return session.getUrl();
        } catch (StripeException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    //StripePortalSessionを作成
    public String createCustomerPortal(User user) {

        Stripe.apiKey = stripeApiKey;

        try {

        	com.stripe.param.billingportal.SessionCreateParams params =
        	        com.stripe.param.billingportal.SessionCreateParams.builder()
        	                .setCustomer(user.getStripeCustomerId())
        	                .setReturnUrl(baseUrl + "/user?subscriptionUpdated")
        	                .build();

            com.stripe.model.billingportal.Session portalSession =
                    com.stripe.model.billingportal.Session.create(params);

            return portalSession.getUrl();

        } catch (StripeException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    //売上データ取得
    public SalesSummary getSalesSummary(
            LocalDate startDate,
            LocalDate endDate) throws StripeException {

        Stripe.apiKey = stripeApiKey;

        long startUnix = startDate
                .atStartOfDay(ZoneId.systemDefault())
                .toEpochSecond();

        long endUnix = endDate
                .plusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toEpochSecond();

        InvoiceListParams params =
                InvoiceListParams.builder()
                        .setStatus(InvoiceListParams.Status.PAID)
                        .setCreated(
                                InvoiceListParams.Created.builder()
                                        .setGte(startUnix)
                                        .setLt(endUnix)
                                        .build())
                        .setLimit(100L)
                        .build();

        InvoiceCollection invoices = Invoice.list(params);
        
        System.out.println("Invoice件数：" + invoices.getData().size());

        long totalAmount = 0;
        int paymentCount = 0;

        for (Invoice invoice : invoices.getData()) {

            System.out.println(invoice.getId());
            System.out.println(invoice.getAmountPaid());

            totalAmount += invoice.getAmountPaid();
            paymentCount++;
        }

        return new SalesSummary(totalAmount, paymentCount);
    }
}