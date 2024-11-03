package com.be.controller;


import com.be.config.ymlprop.RazorpayConfig;
import com.be.dto.response.PaymentLinkResponse;
import com.be.entity.User;
import com.be.service.UserService;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController
{
    @Autowired
    private RazorpayConfig razorpayConfig;

    @Autowired
    private UserService userService;


    @GetMapping("/getkeyandsecret")
    public void getKeyAndSecret()
    {
        System.out.println(razorpayConfig.getApiKey());
        System.out.println(razorpayConfig.getApiSecret());
    }

    @PostMapping("/payments/{planType}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink
            (@PathVariable String planType, @RequestHeader("Authorization") String jwt)
            throws Exception
    {
        User user = userService.findUserProfileByJwt(jwt);
        int amount = 799 * 100;

        // Adjust amount based on plan type
        if (planType.equals("ANNUALLY")) {
            // Apply 30% discount for annual plan
            amount = (int) (amount * 0.7 * 12);
        }


        try {
            // Instantiate a Razorpay client with your key ID and secret
            RazorpayClient razorpay = new RazorpayClient(razorpayConfig.getApiKey(), razorpayConfig.getApiSecret());


            // Create a JSON object with the payment link request parameters
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amount);
            paymentLinkRequest.put("currency", "INR");
//		      paymentLinkRequest.put("expire_by",1691097057);
//		      paymentLinkRequest.put("reference_id",order.getId().toString());

            // Create a JSON object with the customer details
            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());

            customer.put("email", user.getEmail());
            paymentLinkRequest.put("customer", customer);

            // Create a JSON object with the notification settings
            JSONObject notify = new JSONObject();
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            // Set the reminder settings
            paymentLinkRequest.put("reminder_enable", true);
            //TODO MOVE TO OUTSIDE
            // Set the callback URL and method
            paymentLinkRequest.put("callback_url", "http://localhost:5173/upgrade_plan/success?planType=" + planType);
            paymentLinkRequest.put("callback_method", "get");

            // Create the payment link using the paymentLink.create() method
            PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = payment.get("id");
            String paymentLinkUrl = payment.get("short_url");

            PaymentLinkResponse res = new PaymentLinkResponse(paymentLinkUrl, paymentLinkId);

            return new ResponseEntity<PaymentLinkResponse>(res, HttpStatus.ACCEPTED);

        } catch (RazorpayException e) {

            //TODO REMOVE SOPLN
            System.out.println("Error creating payment link: " + e.getMessage());
            throw new RazorpayException(e.getMessage());
        }


    }



    //  @GetMapping("/payments")
//  public ResponseEntity<ApiResponse> redirect(@RequestParam(name="payment_id") String paymentId,@RequestParam("order_id")Long orderId) throws RazorpayException, OrderException {
//	  RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
//	  Order order =orderService.findOrderById(orderId);
//
//	  try {
//
//
//		Payment payment = razorpay.payments.fetch(paymentId);
//		System.out.println("payment details --- "+payment+payment.get("status"));
//
//		if(payment.get("status").equals("captured")) {
//			System.out.println("payment details --- "+payment+payment.get("status"));
//
//			order.getPaymentDetails().setPaymentId(paymentId);
//			order.getPaymentDetails().setStatus(PaymentStatus.COMPLETED);
//			order.setOrderStatus(OrderStatus.PLACED);
////			order.setOrderItems(order.getOrderItems());
//			System.out.println(order.getPaymentDetails().getStatus()+"payment status ");
//			orderRepository.save(order);
//		}
//		ApiResponse res=new ApiResponse("your order get placed", true);
//	      return new ResponseEntity<ApiResponse>(res,HttpStatus.OK);
//
//	} catch (Exception e) {
//		System.out.println("errrr payment -------- ");
//		new RedirectView("https://shopwithzosh.vercel.app/payment/failed");
//		throw new RazorpayException(e.getMessage());
//	}
//
//  }



}
