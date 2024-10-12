package org.example.analyser;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.example.model.Stock;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockAnalysisApp {

    public static List<Stock> readStockData(String filePath) throws IOException, CsvValidationException {
        List<Stock> stocks = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            reader.readNext();  // Skip the header row

            while ((line = reader.readNext()) != null) {
                String companyName = line[0].trim();
                double price = Double.parseDouble(line[1].trim());
                double changePercent = Double.parseDouble(line[2].replace("%", "").trim());
                stocks.add(new Stock(companyName, price, changePercent));
            }
        }
        return stocks;
    }

    public static void main(String[] args) {
        try {
            // Read the stock data from the CSV file
            List<Stock> stockData = readStockData("stock_data.csv");

            // Initialize the StockAnalyzer with the stock data
            StockAnalyzer analyzer = new StockAnalyzer(stockData);

            // Print the insights
            System.out.println("Top 5 Most Expensive Stocks:");
            analyzer.top5ExpensiveStocks().forEach(stock ->
                    System.out.println(stock.getCompanyName() + " - $" + stock.getPrice())
            );

            System.out.println("\nTop 5 Cheapest Stocks:");
            analyzer.top5CheapestStocks().forEach(stock ->
                    System.out.println(stock.getCompanyName() + " - $" + stock.getPrice())
            );

            System.out.println("\nAverage Change Percent: " + analyzer.averageChangePercent() + "%");

            Stock highestPrice = analyzer.companyWithHighestPrice();
            System.out.println("\nCompany with the Highest Price: " +
                    highestPrice.getCompanyName() + " - $" + highestPrice.getPrice());

            Stock lowestPrice = analyzer.companyWithLowestPrice();
            System.out.println("Company with the Lowest Price: " +
                    lowestPrice.getCompanyName() + " - $" + lowestPrice.getPrice());

            System.out.println("\nLow Volatility Companies (<= 1% change):");
            analyzer.lowVolatilityCompanies(1).forEach(stock ->
                    System.out.println(stock.getCompanyName() + " - " + stock.getChangePercent() + "%")
            );

            System.out.println("\nHigh Volatility Companies (> 5% change):");
            analyzer.highVolatilityCompanies(5).forEach(stock ->
                    System.out.println(stock.getCompanyName() + " - " + stock.getChangePercent() + "%")
            );

            Map<String, Long> changeCounts = analyzer.countPositiveAndNegativeChanges();
            System.out.println("\nPositive Changes: " + changeCounts.get("Positive"));
            System.out.println("Negative Changes: " + changeCounts.get("Negative"));

            System.out.println("\nTotal Market Growth (Cumulative Change %): " +
                    analyzer.cumulativeChangePercent() + "%");

            System.out.println("\nPrice Range Distribution:");
            Map<String, Long> priceRange = analyzer.classifyByPriceRange(10, 100);
            System.out.println("Low Price Companies: " + priceRange.get("Low Price"));
            System.out.println("Mid Price Companies: " + priceRange.get("Mid Price"));
            System.out.println("High Price Companies: " + priceRange.get("High Price"));

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
}