package org.example.analyser;

import org.example.model.Stock;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class StockAnalyzer {

    private List<Stock> stocks;

    public StockAnalyzer(List<Stock> stocks) {
        this.stocks = stocks;
    }

    // 1. Find the Top 5 stocks with the highest price
    public List<Stock> top5ExpensiveStocks() {
        return stocks.stream()
                .sorted(Comparator.comparingDouble(Stock::getPrice).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    // 2. Find the Top 5 stocks with the lowest price
    public List<Stock> top5CheapestStocks() {
        return stocks.stream()
                .sorted(Comparator.comparingDouble(Stock::getPrice))
                .limit(5)
                .collect(Collectors.toList());
    }

    // 3. Calculate the average change percentage
    public double averageChangePercent() {
        return stocks.stream()
                .mapToDouble(Stock::getChangePercent)
                .average()
                .orElse(0.0);
    }

    // 4. Find the company with the highest price
    public Stock companyWithHighestPrice() {
        return stocks.stream()
                .max(Comparator.comparingDouble(Stock::getPrice))
                .orElse(null);
    }

    // 5. Find the company with the lowest price
    public Stock companyWithLowestPrice() {
        return stocks.stream()
                .min(Comparator.comparingDouble(Stock::getPrice))
                .orElse(null);
    }

    // 6. Find stable companies with minimal price changes
    public List<Stock> lowVolatilityCompanies(double threshold) {
        return stocks.stream()
                .filter(stock -> Math.abs(stock.getChangePercent()) <= threshold)
                .collect(Collectors.toList());
    }

    // 7. Find volatile companies with large price changes
    public List<Stock> highVolatilityCompanies(double threshold) {
        return stocks.stream()
                .filter(stock -> Math.abs(stock.getChangePercent()) > threshold)
                .collect(Collectors.toList());
    }

    // 8. Count the number of companies with positive and negative changes
    public Map<String, Long> countPositiveAndNegativeChanges() {
        long positiveCount = stocks.stream()
                .filter(stock -> stock.getChangePercent() > 0)
                .count();

        long negativeCount = stocks.stream()
                .filter(stock -> stock.getChangePercent() < 0)
                .count();

        Map<String, Long> result = new HashMap<>();
        result.put("Positive", positiveCount);
        result.put("Negative", negativeCount);

        return result;
    }

    // 9. Calculate total cumulative change percentage (Market Growth)
    public double cumulativeChangePercent() {
        return stocks.stream()
                .mapToDouble(Stock::getChangePercent)
                .sum();
    }

    // 10. Classify companies by price range
    public Map<String, Long> classifyByPriceRange(double lowRange, double highRange) {
        long lowPriceCount = stocks.stream()
                .filter(stock -> stock.getPrice() <= lowRange)
                .count();

        long midPriceCount = stocks.stream()
                .filter(stock -> stock.getPrice() > lowRange && stock.getPrice() <= highRange)
                .count();

        long highPriceCount = stocks.stream()
                .filter(stock -> stock.getPrice() > highRange)
                .count();

        Map<String, Long> result = new HashMap<>();
        result.put("Low Price", lowPriceCount);
        result.put("Mid Price", midPriceCount);
        result.put("High Price", highPriceCount);

        return result;
    }
}