package ru.crystals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PriceMerger {

    public static void main(String[] args) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        Price currentPrice = new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("31.01.2013 23:59:59"), 6000);
        Price newPrice = new Price(1, "122856", 1, 1, dateFormat.parse("20.01.2013 00:00:00"), dateFormat.parse("21.01.2013 23:59:59"), 5000);

        List<Price> result = PriceMerger.mergePrices(Collections.singletonList(currentPrice), Collections.singletonList(newPrice));

        Consumer<Price> pricePrinter = p -> {
            System.out.format("%10s %8s %8s %30s %30s %10s",
                p.getProductCode(), p.getNumber(), p.getDepartment(), p.getBegin(), p.getEnd(), p.getValue()
            );
            System.out.println();
        };

        System.out.println("-----------------------------------------------------------------------------------------------------");
        System.out.printf("%10s %8s %8s %30s %30s %10s", "Product", "Number", "Depart", "Begin", "End", "Value");
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------------------------------");
        pricePrinter.accept(currentPrice);
        pricePrinter.accept(newPrice);
        System.out.println("Result:");
        result.forEach(price -> pricePrinter.accept(price));
    }

    /**
     * Merge current prices with new prices from external system.
     *
     * @param currentPrices
     * @param newPrices
     * @return
     */
    public static List<Price> mergePrices(List<Price> currentPrices, List<Price> newPrices) {
        List<Price> result = new ArrayList<>();

        // Make deep copy of lists to avoid source data changing and group prices by key to simplify the search and iteration
        Map<PriceKey, List<Price>> currentP = currentPrices.stream().map(Price::new).collect(Collectors.groupingBy(PriceKey::new));
        Map<PriceKey, List<Price>> newP = newPrices.stream().map(Price::new).collect(Collectors.groupingBy(PriceKey::new));

        // Check and merge all current prices with each new price with the same product id, price number and department id
        newP.forEach((k, v) -> {
            List<Price> cur = currentP.get(k);

            for (Price newPrice : v) {
                cur = merge(cur, newPrice);
            }

            result.addAll(cur);
            currentP.remove(k);
        });

        // Add current prices which have no updates
        result.addAll(currentP.values().stream().flatMap(List::stream).collect(Collectors.toList()));

        return result;
    }

    /**
     * Check all current prices for overlap with new price and merge if necessary
     *
     * @param currentPrices
     * @param newPrice
     * @return
     */
    protected static List<Price> merge(List<Price> currentPrices, Price newPrice) {
        List<Price> result = new ArrayList<>();

        // Add new price if product has no any price before
        if (currentPrices == null || currentPrices.size() == 0) {
            result.add(newPrice);
            return result;
        }

        for (Price currentPrice : currentPrices) {
            if (isInside(newPrice, currentPrice)) {
                if (currentPrice.getValue() == newPrice.getValue()) {
                    // Current price engulfs new
                    newPrice = currentPrice;
                } else {
                    // Split current price and insert new price if price values are different
                    Price cur = new Price(currentPrice);
                    currentPrice.setEnd(newPrice.getBegin());
                    result.add(currentPrice);
                    cur.setBegin(newPrice.getEnd());
                    result.add(cur);
                }
            } else if (isInside(currentPrice, newPrice)) {
                // NOPE: New price engulfs current
            } else if (isLeftOverlap(currentPrice, newPrice)) {
                if (currentPrice.getValue() == newPrice.getValue()) {
                    newPrice.setBegin(currentPrice.getBegin());
                } else {
                    currentPrice.setEnd(newPrice.getBegin());
                    result.add(currentPrice);
                }
            } else if (isRightOverlap(currentPrice, newPrice)) {
                if (currentPrice.getValue() == newPrice.getValue()) {
                    newPrice.setEnd(currentPrice.getEnd());
                } else {
                    currentPrice.setBegin(newPrice.getEnd());
                    result.add(currentPrice);
                }
            } else {
                // if no overlap
                result.add(currentPrice);
            }
        }

        result.add(newPrice);
        result.sort(Comparator.comparing(Price::getBegin));

        return result;
    }

    /**
     * Checks that the new price starts within the time of the current price.
     *
     * @param currentPrice
     * @param newPrice
     * @return
     */
    protected static boolean isLeftOverlap(Price currentPrice, Price newPrice) {
        return newPrice.getBegin().compareTo(currentPrice.getBegin()) > 0 && newPrice.getBegin().compareTo(currentPrice.getEnd()) < 0;
    }

    /**
     * Checks that the new price ends within the time of the current price.
     *
     * @param currentPrice
     * @param newPrice
     * @return
     */
    protected static boolean isRightOverlap(Price currentPrice, Price newPrice) {
        return newPrice.getEnd().compareTo(currentPrice.getBegin()) > 0 && newPrice.getEnd().compareTo(currentPrice.getEnd()) < 0;
    }

    /**
     * Checks that the one price time interval inside another.
     *
     * @param internal
     * @param external
     * @return
     */
    protected static boolean isInside(Price internal, Price external) {
        return external.getBegin().compareTo(internal.getBegin()) <= 0 && external.getEnd().compareTo(internal.getEnd()) >= 0;
    }
}
