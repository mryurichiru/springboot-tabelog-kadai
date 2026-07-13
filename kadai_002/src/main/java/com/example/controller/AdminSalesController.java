package com.example.controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dto.SalesSummary;
import com.example.form.SalesSearchForm;
import com.example.service.StripeService;
import com.stripe.exception.StripeException;

@Controller
@RequestMapping("/admin/sales")
public class AdminSalesController {

    private final StripeService stripeService;

    public AdminSalesController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @GetMapping
    public String index(Model model) {

        model.addAttribute("salesSearchForm", new SalesSearchForm());

        return "admin/sales/index";
    }
    
    @PostMapping
    public String search(
            @ModelAttribute SalesSearchForm salesSearchForm,
            Model model) throws StripeException {

        SalesSummary summary =
                stripeService.getSalesSummary(
                        salesSearchForm.getStartDate(),
                        salesSearchForm.getEndDate());

        model.addAttribute("salesSearchForm", salesSearchForm);
        model.addAttribute("summary", summary);

        return "admin/sales/index";
    }
    
    //CSV出力用
    @GetMapping("/csv")
    public ResponseEntity<byte[]> downloadCsv(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) throws StripeException {

        SalesSummary summary =
                stripeService.getSalesSummary(startDate, endDate);

        StringBuilder csv = new StringBuilder();

        csv.append("開始日,終了日,売上金額,決済件数\n");

        csv.append(startDate)
           .append(",")
           .append(endDate)
           .append(",")
           .append(summary.getTotalAmount())
           .append(",")
           .append(summary.getPaymentCount());

        byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byte[] data = csv.toString().getBytes(StandardCharsets.UTF_8);

        byte[] csvBytes = new byte[bom.length + data.length];

        System.arraycopy(bom, 0, csvBytes, 0, bom.length);
        System.arraycopy(data, 0, csvBytes, bom.length, data.length);

        HttpHeaders headers = new HttpHeaders();

        headers.add(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=sales_summary.csv");

        headers.setContentType(
                new MediaType("text", "csv", StandardCharsets.UTF_8));

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvBytes);
    }
}