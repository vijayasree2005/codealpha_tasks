import java.io.*;
import java.util.*;

// Represents a Stock
class Stock {
    private String symbol;
    private double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() { return symbol; }
    public double getPrice() { return price; }

    // Simulate price change
    public void updatePrice() {
        double change = (Math.random() - 0.5) * 2; // -1 to +1
        price = Math.max(1, price + change); // Minimum price = 1
    }

    @Override
    public String toString() {
        return symbol + " : $" + String.format("%.2f", price);
    }
}

// Represents a user's portfolio
class Portfolio {
    private Map<String, Integer> holdings = new HashMap<>();
    private double cash;

    public Portfolio(double initialCash) {
        this.cash = initialCash;
    }

    public void buyStock(Stock stock, int quantity) {
        double cost = stock.getPrice() * quantity;
        if (cost > cash) {
            System.out.println("Not enough cash to buy " + quantity + " shares of " + stock.getSymbol());
            return;
        }
        cash -= cost;
        holdings.put(stock.getSymbol(), holdings.getOrDefault(stock.getSymbol(), 0) + quantity);
        System.out.println("Bought " + quantity + " shares of " + stock.getSymbol());
    }

    public void sellStock(Stock stock, int quantity) {
        int owned = holdings.getOrDefault(stock.getSymbol(), 0);
        if (quantity > owned) {
            System.out.println("Not enough shares to sell " + quantity + " of " + stock.getSymbol());
            return;
        }
        cash += stock.getPrice() * quantity;
        holdings.put(stock.getSymbol(), owned - quantity);
        System.out.println("Sold " + quantity + " shares of " + stock.getSymbol());
    }

    public void showPortfolio(List<Stock> marketStocks) {
        System.out.println("\n--- Portfolio ---");
        System.out.println("Cash: $" + String.format("%.2f", cash));
        double totalValue = cash;
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            String symbol = entry.getKey();
            int qty = entry.getValue();
            double price = marketStocks.stream()
                                       .filter(s -> s.getSymbol().equals(symbol))
                                       .findFirst().get().getPrice();
            System.out.println(symbol + " : " + qty + " shares @ $" + String.format("%.2f", price));
            totalValue += qty * price;
        }
        System.out.println("Total Portfolio Value: $" + String.format("%.2f", totalValue));
    }

    // Save portfolio to file
    public void savePortfolio(String filename) {
        try (PrintWriter out = new PrintWriter(filename)) {
            out.println(cash);
            for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
                out.println(entry.getKey() + "," + entry.getValue());
            }
            System.out.println("Portfolio saved!");
        } catch (IOException e) {
            System.out.println("Error saving portfolio: " + e.getMessage());
        }
    }

    // Load portfolio from file
    public void loadPortfolio(String filename) {
        try (Scanner sc = new Scanner(new File(filename))) {
            if (sc.hasNextLine()) cash = Double.parseDouble(sc.nextLine());
            holdings.clear();
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                holdings.put(parts[0], Integer.parseInt(parts[1]));
            }
            System.out.println("Portfolio loaded!");
        } catch (IOException e) {
            System.out.println("Error loading portfolio: " + e.getMessage());
        }
    }
}

public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        List<Stock> marketStocks = Arrays.asList(
            new Stock("AAPL", 150),
            new Stock("GOOG", 2800),
            new Stock("AMZN", 3400),
            new Stock("TSLA", 800)
        );

        Portfolio portfolio = new Portfolio(10000);

        while (true) {
            System.out.println("\n--- Market ---");
            for (Stock s : marketStocks) {
                s.updatePrice();
                System.out.println(s);
            }

            System.out.println("\nOptions: 1-Buy 2-Sell 3-Show Portfolio 4-Save 5-Load 6-Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Stock symbol to buy: ");
                    String buySymbol = sc.nextLine();
                    System.out.print("Quantity: ");
                    int buyQty = sc.nextInt();
                    sc.nextLine();
                    marketStocks.stream()
                                .filter(s -> s.getSymbol().equalsIgnoreCase(buySymbol))
                                .findFirst()
                                .ifPresent(s -> portfolio.buyStock(s, buyQty));
                    break;
                case 2:
                    System.out.print("Stock symbol to sell: ");
                    String sellSymbol = sc.nextLine();
                    System.out.print("Quantity: ");
                    int sellQty = sc.nextInt();
                    sc.nextLine();
                    marketStocks.stream()
                                .filter(s -> s.getSymbol().equalsIgnoreCase(sellSymbol))
                                .findFirst()
                                .ifPresent(s -> portfolio.sellStock(s, sellQty));
                    break;
                case 3:
                    portfolio.showPortfolio(marketStocks);
                    break;
                case 4:
                    portfolio.savePortfolio("portfolio.txt");
                    break;
                case 5:
                    portfolio.loadPortfolio("portfolio.txt");
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }
}
