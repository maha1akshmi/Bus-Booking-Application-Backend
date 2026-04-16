package com.example.BBA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.math.BigDecimal;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendBookingConfirmation(String toEmail, Long bookingId, BigDecimal amount, int seatCount) {
        sendHtmlEmail(toEmail, "Booking Confirmed - #" + bookingId, """
            <html>
            <body style="font-family:Arial,sans-serif;background:#f4f4f4;padding:20px;">
              <div style="max-width:500px;margin:auto;background:#fff;border-radius:10px;overflow:hidden;box-shadow:0 2px 10px rgba(0,0,0,0.1);">
                <div style="background:#dc2626;color:#fff;padding:20px;text-align:center;">
                  <h1 style="margin:0;">Booking Confirmed ✓</h1>
                </div>
                <div style="padding:20px;">
                  <p>Your booking <strong>#%d</strong> has been confirmed.</p>
                  <table style="width:100%%;border-collapse:collapse;margin:15px 0;">
                    <tr><td style="padding:8px;border-bottom:1px solid #eee;color:#666;">Seats Booked</td><td style="padding:8px;border-bottom:1px solid #eee;font-weight:bold;">%d</td></tr>
                    <tr><td style="padding:8px;border-bottom:1px solid #eee;color:#666;">Total Amount</td><td style="padding:8px;border-bottom:1px solid #eee;font-weight:bold;">₹%s</td></tr>
                    <tr><td style="padding:8px;color:#666;">Status</td><td style="padding:8px;font-weight:bold;color:#16a34a;">CONFIRMED</td></tr>
                  </table>
                  <p style="color:#666;font-size:13px;">Thank you for choosing our bus service!</p>
                </div>
              </div>
            </body>
            </html>
            """.formatted(bookingId, seatCount, amount.toString()));
    }

    @Async
    public void sendBookingCancellation(String toEmail, Long bookingId) {
        sendHtmlEmail(toEmail, "Booking Cancelled - #" + bookingId, """
            <html>
            <body style="font-family:Arial,sans-serif;background:#f4f4f4;padding:20px;">
              <div style="max-width:500px;margin:auto;background:#fff;border-radius:10px;overflow:hidden;box-shadow:0 2px 10px rgba(0,0,0,0.1);">
                <div style="background:#dc2626;color:#fff;padding:20px;text-align:center;">
                  <h1 style="margin:0;">Booking Cancelled ✗</h1>
                </div>
                <div style="padding:20px;">
                  <p>Your booking <strong>#%d</strong> has been cancelled.</p>
                  <p style="color:#666;">If you did not request this cancellation, please contact support immediately.</p>
                  <p style="color:#666;font-size:13px;">Any payment made will be refunded within 5-7 business days.</p>
                </div>
              </div>
            </body>
            </html>
            """.formatted(bookingId));
    }

    @Async
    public void sendPaymentSuccess(String toEmail, Long bookingId, String transactionId, BigDecimal amount) {
        sendHtmlEmail(toEmail, "Payment Successful - #" + transactionId, """
            <html>
            <body style="font-family:Arial,sans-serif;background:#f4f4f4;padding:20px;">
              <div style="max-width:500px;margin:auto;background:#fff;border-radius:10px;overflow:hidden;box-shadow:0 2px 10px rgba(0,0,0,0.1);">
                <div style="background:#16a34a;color:#fff;padding:20px;text-align:center;">
                  <h1 style="margin:0;">Payment Successful ✓</h1>
                </div>
                <div style="padding:20px;">
                  <table style="width:100%%;border-collapse:collapse;margin:15px 0;">
                    <tr><td style="padding:8px;border-bottom:1px solid #eee;color:#666;">Booking ID</td><td style="padding:8px;border-bottom:1px solid #eee;font-weight:bold;">#%d</td></tr>
                    <tr><td style="padding:8px;border-bottom:1px solid #eee;color:#666;">Transaction ID</td><td style="padding:8px;border-bottom:1px solid #eee;font-weight:bold;">%s</td></tr>
                    <tr><td style="padding:8px;color:#666;">Amount Paid</td><td style="padding:8px;font-weight:bold;color:#16a34a;">₹%s</td></tr>
                  </table>
                  <p style="color:#666;font-size:13px;">Your e-ticket has been generated. Have a great journey!</p>
                </div>
              </div>
            </body>
            </html>
            """.formatted(bookingId, transactionId, amount.toString()));
    }

    @Async
    public void sendPaymentFailed(String toEmail, Long bookingId, String transactionId) {
        sendHtmlEmail(toEmail, "Payment Failed - Booking #" + bookingId, """
            <html>
            <body style="font-family:Arial,sans-serif;background:#f4f4f4;padding:20px;">
              <div style="max-width:500px;margin:auto;background:#fff;border-radius:10px;overflow:hidden;box-shadow:0 2px 10px rgba(0,0,0,0.1);">
                <div style="background:#dc2626;color:#fff;padding:20px;text-align:center;">
                  <h1 style="margin:0;">Payment Failed ✗</h1>
                </div>
                <div style="padding:20px;">
                  <p>Payment for booking <strong>#%d</strong> has failed.</p>
                  <p>Transaction ID: <strong>%s</strong></p>
                  <p style="color:#666;">Your seats have been released. Please try booking again.</p>
                  <p style="color:#666;font-size:13px;">If money was deducted, it will be refunded within 5-7 business days.</p>
                </div>
              </div>
            </body>
            </html>
            """.formatted(bookingId, transactionId));
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email to " + to, e);
        }
    }
}
