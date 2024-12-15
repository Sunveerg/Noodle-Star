package com.noodlestar.noodlestar.GmailSubdomain;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Properties;
import java.util.Base64;

import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;

public class GmailService {

    private static final String APPLICATION_NAME = "Spring Boot Gmail API";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String USER = "me";
    private static final String TEST_EMAIL = "sunveerghumman@gmail.com";
    private static final java.util.List<String> SCOPES = Collections.singletonList(GMAIL_SEND);
    private final Gmail service;

    public GmailService() throws Exception {
        service = getGmailService();
    }

    // Retrieve Gmail service instance
    public static Gmail getGmailService() throws Exception {
        Credential credential = getCredentials();
        return new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    // Get credentials from the client secrets file
    private static Credential getCredentials() throws IOException, GeneralSecurityException {
        InputStream in = GmailService.class.getResourceAsStream("/credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get(TOKENS_DIRECTORY_PATH).toFile()))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        Credential credential = flow.loadCredential("user");
        if (credential != null && credential.getRefreshToken() != null) {
            return credential;  // Use the stored credentials if they exist
        }

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    // Send email
    public void sendMail(String to, String subject, String messageBody) throws Exception {
        MimeMessage email = createEmail(to, TEST_EMAIL, subject, messageBody);
        Message message = createMessageWithEmail(email);
        service.users().messages().send(USER, message).execute();
        System.out.println("Email sent successfully.");
    }

    // Create MIME email
    public MimeMessage createEmail(String to, String from, String subject, String bodyText) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setContent(bodyText, "text/html; charset=UTF-8");
        return email;
    }

    // Create Message from MIME email
    private Message createMessageWithEmail(MimeMessage email) throws IOException, MessagingException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        String encodedEmail = Base64.getUrlEncoder().encodeToString(buffer.toByteArray());
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
}