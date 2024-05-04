package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation=reservationRepository2.findById(reservationId).get();

        int TotHour=reservation.getNumberOfHours();
        int pricePerHour=reservation.getSpot().getPricePerHour();

        int bill=TotHour*pricePerHour;

        if(amountSent<bill){
            throw new Exception("Insufficient Amount");
        }

        if(!mode.equalsIgnoreCase("cash") && !mode.equalsIgnoreCase("card") && !mode.equalsIgnoreCase("upi")){
            throw new Exception("Payment mode not detected");
        }

        Payment payment=new Payment();
        payment.setPaymentCompleted(true);

        if(mode.equalsIgnoreCase("cash")){
            payment.setPaymentMode(PaymentMode.CASH);
        }else if(mode.equalsIgnoreCase("card")){
            payment.setPaymentMode(PaymentMode.CARD);
        }else{
            payment.setPaymentMode(PaymentMode.UPI);
        }
        payment.setReservation(reservation);
        reservation.setPayment(payment);

//        payment=paymentRepository2.save(payment);
        reservationRepository2.save(reservation);

        return payment;
    }
}
