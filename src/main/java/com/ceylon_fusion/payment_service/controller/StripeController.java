//package com.ceylon_fusion.payment_service.controller;
//
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.PaymentIntent;
//import com.stripe.model.checkout.Session;
//import com.stripe.param.PaymentIntentCreateParams;
//import com.stripe.param.checkout.SessionCreateParams;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//public class StripeController {
//
//    @RestController
//    @RequestMapping("/api/payment-checkout")
//    public class PaymentController {
//
//        @PostMapping("/create-checkout-session")
//        public ResponseEntity<Map<String, String>> createCheckoutSession() throws StripeException {
//            Stripe.apiKey = "sk_test_51QixwiByjVSKb..."; // keep secret key safe
//
//            List<Object> lineItems = new ArrayList<>();
//            Map<String, Object> item = new HashMap<>();
//            item.put("price_data", Map.of(
//                    "currency", "usd",
//                    "product_data", Map.of("name", "T-shirt"),
//                    "unit_amount", 2000 // amount in cents
//            ));
//            item.put("quantity", 1);
//            lineItems.add(item);
//
//            Map<String, Object> params = new HashMap<>();
//            params.put("payment_method_types", List.of("card"));
//            params.put("line_items", lineItems);
//            params.put("mode", "payment");
//            params.put("success_url", "http://localhost:5173/success");
//            params.put("cancel_url", "http://localhost:5173/cancel");
//
//            Session session = Session.create(params);
//            Map<String, String> responseData = new HashMap<>();
//            responseData.put("id", session.getId());
//
//            return ResponseEntity.ok(responseData);
//        }
//    }
//
//}
