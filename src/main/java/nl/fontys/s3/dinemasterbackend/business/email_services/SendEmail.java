package nl.fontys.s3.dinemasterbackend.business.email_services;

public interface SendEmail {
    void sendEmail(String to, String subject, String body);
}
