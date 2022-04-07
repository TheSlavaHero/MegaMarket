package com.gmail.theslavahero.ai.googleSheets;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class GoogleSheetController {
    private static Sheets sheetsService;
    private static String APPLICATION_NAME = "My Project 37006";
    private static String SPREADSHEET_ID = "1N0XO9XH-vqvOt9LkCQcN-ySqxrTeoca5K_cgdQ8EYHE";

    private static Credential authorize() throws IOException, GeneralSecurityException {
        InputStream in = GoogleSheetController.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JacksonFactory.getDefaultInstance(), new InputStreamReader(in)
        );

        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver())
                .authorize("user");
        return credential;
    }

    public static Sheets getSheetsService() throws GeneralSecurityException, IOException {
        Credential credential = authorize();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public List<String> getAllProductNames() throws GeneralSecurityException, IOException {

        List<String> productNames = new java.util.ArrayList<>(Collections.emptyList());
        List<List<Object>> values = readValues("C2:C1000");

        for (List row : values) {
            productNames.add(row.get(0).toString());
        }
        return productNames;
    }

    public void writeAllPrices(List<String> prices) throws IOException, GeneralSecurityException {
        log.info("Writing all prices, {} in total", prices.size());
        List<List<Object>> values = new java.util.ArrayList<>(Collections.emptyList());

        List<Object> singletonDateList = Collections.singletonList(getCurrentDate());
        values.add(singletonDateList);

        for (String price : prices) {
            List<Object> singletonPriceList = Collections.singletonList(price);
            values.add(singletonPriceList);
        }
        int amountOfOccupiedColumns = getAmountOfOccupiedColumns();
        log.info("Amount of detected occupied columns in google sheets: {}", amountOfOccupiedColumns);
        String letter = numberOfLetter(amountOfOccupiedColumns + 1);
        log.info("Writing all data into column with letter: \"{}\"", letter);
        String range = letter + "1:" + letter + "200";
        ValueRange body = new ValueRange()
                .setValues(values);
        UpdateValuesResponse result =
                sheetsService.spreadsheets().values().update(SPREADSHEET_ID, range, body)
                        .setValueInputOption("USER_ENTERED")
                        .execute();
        log.info("Overall info for the save: updated columns: {}, updated rows: {}, updated cells: {}, updated range: {}",
                result.getUpdatedColumns(), result.getUpdatedRows(), result.getUpdatedCells(), result.getUpdatedRange());
    }

    public int getAmountOfOccupiedColumns() throws GeneralSecurityException, IOException {
        List<List<Object>> values = readValues("A1:Z1");
        return values.get(0).size();
    }

    private List<List<Object>> readValues(String range) throws GeneralSecurityException, IOException {
        sheetsService = getSheetsService();

        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute();

        List<List<Object>> values = response.getValues();

        if (values == null || values.isEmpty()) {
            log.warn("no data found");
            return Collections.emptyList();
        } else {
            return values;
        }
    }

    private String numberOfLetter(int number) {
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return Character.toString(abc.charAt(number - 1));
    }

    private String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
