/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.currencyconverter;

 import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.net.HttpURLConnection;
 import java.net.URL;
 import java.util.HashMap;
 import java.util.Scanner;
 import org.json.JSONObject;
 
 public class CurrencyConverter {
     private static final String API_URL = "https://v6.exchangerate-api.com/v6/8bdcc86c637cf9d30f0cc818/latest/USD";
     private static HashMap<String, Double> exchangeRates = new HashMap<>();
 
     public static void main(String[] args) {
         // Fetch exchange rates
         fetchExchangeRates();
 
         // Get user input
         Scanner scanner = new Scanner(System.in);
         System.out.print("Enter the amount to convert: ");
         double amount = scanner.nextDouble();
 
         System.out.print("Enter the currency to convert from (e.g., USD, EUR, GBP, JPY): ");
         String fromCurrency = scanner.next().toUpperCase();
 
         System.out.print("Enter the currency to convert to (e.g., USD, EUR, GBP, JPY): ");
         String toCurrency = scanner.next().toUpperCase();
 
         // Convert the currency
         double convertedAmount = convertCurrency(amount, fromCurrency, toCurrency);
         System.out.println("Converted amount: " + convertedAmount + " " + toCurrency);
     }
 
     private static void fetchExchangeRates() {
         try {
             URL url = new URL(API_URL);
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             connection.setRequestMethod("GET");
 
             int responseCode = connection.getResponseCode();
             if (responseCode == HttpURLConnection.HTTP_OK) {
                 BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                 StringBuilder response = new StringBuilder();
                 String line;
 
                 while ((line = reader.readLine()) != null) {
                     response.append(line);
                 }
                 reader.close();
 
                 JSONObject responseData = new JSONObject(response.toString());
                 JSONObject ratesData = responseData.getJSONObject("conversion_rates");
 
                 // Iterate over ratesData to populate exchangeRates map
                 for (String currency : ratesData.keySet()) {
                     double exchangeRate = ratesData.getDouble(currency);
                     exchangeRates.put(currency, exchangeRate);
                 }
             } else {
                 System.out.println("Failed to fetch exchange rates. Response code: " + responseCode);
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
 
     private static double convertCurrency(double amount, String fromCurrency, String toCurrency) {
         // Get exchange rates
         double fromRate = exchangeRates.get(fromCurrency);
         double toRate = exchangeRates.get(toCurrency);
 
         // Convert currency
         return (amount / fromRate) * toRate;
     }
 }
 