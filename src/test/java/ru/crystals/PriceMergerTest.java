package ru.crystals;

import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PriceMergerTest {

    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Test
    void testMergeWithExampleValues() throws ParseException {
        List<Price> currentPrices = new ArrayList<>();
        currentPrices.add(new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("31.01.2013 23:59:59"), 11000));
        currentPrices.add(new Price(1, "122856", 2, 1, dateFormat.parse("10.01.2013 00:00:00"), dateFormat.parse("20.01.2013 23:59:59"), 99000));
        currentPrices.add(new Price(1, "6654", 1, 2, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("31.01.2013 00:00:00"), 5000));

        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price(1, "122856", 1, 1, dateFormat.parse("20.01.2013 00:00:00"), dateFormat.parse("20.02.2013 23:59:59"), 11000));
        newPrices.add(new Price(1, "122856", 2, 1, dateFormat.parse("15.01.2013 00:00:00"), dateFormat.parse("25.01.2013 23:59:59"), 92000));
        newPrices.add(new Price(1, "6654", 1, 2, dateFormat.parse("12.01.2013 00:00:00"), dateFormat.parse("13.01.2013 00:00:00"), 4000));

        List<Price> expected = new ArrayList<>();
        expected.add(new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("20.02.2013 23:59:59"), 11000));
        expected.add(new Price(1, "122856", 2, 1, dateFormat.parse("10.01.2013 00:00:00"), dateFormat.parse("15.01.2013 00:00:00"), 99000));
        expected.add(new Price(1, "122856", 2, 1, dateFormat.parse("15.01.2013 00:00:00"), dateFormat.parse("25.01.2013 23:59:59"), 92000));
        expected.add(new Price(1, "6654", 1, 2, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("12.01.2013 00:00:00"), 5000));
        expected.add(new Price(1, "6654", 1, 2, dateFormat.parse("12.01.2013 00:00:00"), dateFormat.parse("13.01.2013 00:00:00"), 4000));
        expected.add(new Price(1, "6654", 1, 2, dateFormat.parse("13.01.2013 00:00:00"), dateFormat.parse("31.01.2013 00:00:00"), 5000));

        List<Price> actual = PriceMerger.mergePrices(currentPrices, newPrices);

        assertTrue(actual.containsAll(expected), "The merged prices should be as in the example.");
    }

    @Test
    void testMergeWithMixedValues() throws ParseException {
        List<Price> currentPrices = new ArrayList<>();
        currentPrices.add(new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("20.02.2013 23:59:59"), 11000));
        currentPrices.add(new Price(1, "122856", 2, 1, dateFormat.parse("10.01.2013 00:00:00"), dateFormat.parse("15.01.2013 00:00:00"), 99000));
        currentPrices.add(new Price(1, "122856", 2, 1, dateFormat.parse("15.01.2013 00:00:00"), dateFormat.parse("25.01.2013 23:59:59"), 92000));
        currentPrices.add(new Price(1, "122444", 1, 1, dateFormat.parse("17.01.2013 00:00:00"), dateFormat.parse("25.01.2013 23:59:59"), 145000));
        currentPrices.add(new Price(1, "6654", 1, 2, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("12.01.2013 00:00:00"), 5000));
        currentPrices.add(new Price(1, "6654", 1, 2, dateFormat.parse("12.01.2013 00:00:00"), dateFormat.parse("13.01.2013 00:00:00"), 4000));
        currentPrices.add(new Price(1, "6654", 1, 2, dateFormat.parse("13.01.2013 00:00:00"), dateFormat.parse("31.01.2013 00:00:00"), 5000));

        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price(1, "6654", 1, 2, dateFormat.parse("11.01.2013 00:00:00"), dateFormat.parse("29.01.2013 00:00:00"), 3000));

        List<Price> expected = new ArrayList<>();
        expected.add(new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("20.02.2013 23:59:59"), 11000));
        expected.add(new Price(1, "122856", 2, 1, dateFormat.parse("10.01.2013 00:00:00"), dateFormat.parse("15.01.2013 00:00:00"), 99000));
        expected.add(new Price(1, "122856", 2, 1, dateFormat.parse("15.01.2013 00:00:00"), dateFormat.parse("25.01.2013 23:59:59"), 92000));
        expected.add(new Price(1, "122444", 1, 1, dateFormat.parse("17.01.2013 00:00:00"), dateFormat.parse("25.01.2013 23:59:59"), 145000));
        expected.add(new Price(1, "6654", 1, 2, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("11.01.2013 00:00:00"), 5000));
        expected.add(new Price(1, "6654", 1, 2, dateFormat.parse("11.01.2013 00:00:00"), dateFormat.parse("29.01.2013 00:00:00"), 3000));
        expected.add(new Price(1, "6654", 1, 2, dateFormat.parse("29.01.2013 00:00:00"), dateFormat.parse("31.01.2013 00:00:00"), 5000));

        List<Price> actual = PriceMerger.mergePrices(currentPrices, newPrices);

        assertTrue(actual.containsAll(expected), "The merged prices should be as expected.");
    }

    /**
     * Example 1
     *
     * @throws ParseException
     */
    @Test
    void testMergeWhenCurrentEndLaterNewEnd() throws ParseException {
        Price currentPrice = new Price(1, "6654", 1, 2, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("31.01.2013 00:00:00"), 5000);
        Price newPrice = new Price(1, "6654", 1, 2, dateFormat.parse("12.01.2013 00:00:00"), dateFormat.parse("13.01.2013 00:00:00"), 6000);

        List<Price> expected = new ArrayList<>();
        expected.add(new Price(1, "6654", 1, 2, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("12.01.2013 00:00:00"), 5000));
        expected.add(new Price(1, "6654", 1, 2, dateFormat.parse("12.01.2013 00:00:00"), dateFormat.parse("13.01.2013 00:00:00"), 6000));
        expected.add(new Price(1, "6654", 1, 2, dateFormat.parse("13.01.2013 00:00:00"), dateFormat.parse("31.01.2013 00:00:00"), 5000));

        List<Price> actual = PriceMerger.merge(Collections.singletonList(currentPrice), newPrice);
        assertEquals(expected, actual, "Should return 3 prices when dates intersect and current price end later then new price end.");
    }

    /**
     * Example 2
     *
     * @throws ParseException
     */
    @Test
    void testMergeWithDoubleOverlap() throws ParseException {
        ArrayList<Price> currentPrice = new ArrayList<>();
        currentPrice.add(new Price(1, "6654", 1, 2, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("20.01.2013 00:00:00"), 10000));
        currentPrice.add(new Price(1, "6654", 1, 2, dateFormat.parse("20.01.2013 00:00:00"), dateFormat.parse("31.01.2013 00:00:00"), 12000));

        Price newPrice = new Price(1, "6654", 1, 2, dateFormat.parse("15.01.2013 00:00:00"), dateFormat.parse("25.01.2013 00:00:00"), 11000);

        List<Price> expected = new ArrayList<>();
        expected.add(new Price(1, "6654", 1, 2, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("15.01.2013 00:00:00"), 10000));
        expected.add(new Price(1, "6654", 1, 2, dateFormat.parse("15.01.2013 00:00:00"), dateFormat.parse("25.01.2013 00:00:00"), 11000));
        expected.add(new Price(1, "6654", 1, 2, dateFormat.parse("25.01.2013 00:00:00"), dateFormat.parse("31.01.2013 00:00:00"), 12000));

        List<Price> actual = PriceMerger.merge(currentPrice, newPrice);
        assertEquals(expected, actual, "Should insert new price between two current and update dates.");
    }

    /**
     * Example 3
     *
     * @throws ParseException
     */
    @Test
    void testMergeWithTwoNewPrices() throws ParseException {
        ArrayList<Price> currentPrices = new ArrayList<>();
        currentPrices.add(new Price(1, "6654", 1, 2, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("10.01.2013 00:00:00"), 8000));
        currentPrices.add(new Price(1, "6654", 1, 2, dateFormat.parse("10.01.2013 00:00:00"), dateFormat.parse("20.01.2013 14:54:00"), 8700));
        currentPrices.add(new Price(1, "6654", 1, 2, dateFormat.parse("20.01.2013 14:54:00"), dateFormat.parse("31.01.2013 00:00:00"), 9000));

        ArrayList<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price(1, "6654", 1, 2, dateFormat.parse("05.01.2013 00:00:00"), dateFormat.parse("15.01.2013 00:00:00"), 8000));
        newPrices.add(new Price(1, "6654", 1, 2, dateFormat.parse("15.01.2013 00:00:00"), dateFormat.parse("27.01.2013 00:00:00"), 8500));

        List<Price> expected = new ArrayList<>();
        expected.add(new Price(1, "6654", 1, 2, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("15.01.2013 00:00:00"), 8000));
        expected.add(new Price(1, "6654", 1, 2, dateFormat.parse("15.01.2013 00:00:00"), dateFormat.parse("27.01.2013 00:00:00"), 8500));
        expected.add(new Price(1, "6654", 1, 2, dateFormat.parse("27.01.2013 00:00:00"), dateFormat.parse("31.01.2013 00:00:00"), 9000));

        List<Price> actual = PriceMerger.merge(currentPrices, newPrices.get(0));
        actual = PriceMerger.merge(actual, newPrices.get(1));
        assertEquals(expected, actual, "Should insert two new prices and remove overlapped current price.");
    }

    /**
     *
     * Protected methods tests
     *
     */

    /*
     * [----60----] +
     *      [---60---] =
     * [-----60------]
     */
    @Test
    void testMergeEqualPriceAndNewLonger() throws ParseException {
        Price currentPrice = new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("31.01.2013 23:59:59"), 6000);
        Price newPrice = new Price(1, "122856", 1, 1, dateFormat.parse("20.01.2013 00:00:00"), dateFormat.parse("20.02.2013 23:59:59"), 6000);

        List<Price> expected = new ArrayList<>();
        expected.add(new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("20.02.2013 23:59:59"), 6000));

        List<Price> actual = PriceMerger.merge(Collections.singletonList(currentPrice), newPrice);
        assertEquals(expected, actual, "Should expand product end date.");
    }

    /*
     * [-----60-----] +
     *       [---50---] =
     * [-60-][---50---]
     */
    @Test
    void testMergeDifferentPriceAndNewLonger() throws ParseException {
        Price currentPrice = new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("31.01.2013 23:59:59"), 6000);
        Price newPrice = new Price(1, "122856", 1, 1, dateFormat.parse("20.01.2013 00:00:00"), dateFormat.parse("20.02.2013 23:59:59"), 5000);

        List<Price> expected = new ArrayList<>();
        expected.add(new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("20.01.2013 00:00:00"), 6000));
        expected.add(new Price(1, "122856", 1, 1, dateFormat.parse("20.01.2013 00:00:00"), dateFormat.parse("20.02.2013 23:59:59"), 5000));

        List<Price> actual = PriceMerger.merge(Collections.singletonList(currentPrice), newPrice);
        assertEquals(expected, actual, "Should reduce current price and add new price.");
    }

    /*
     *    [----60----] +
     * [---60---] =
     * [-----60------]
     */
    @Test
    void testMergeEqualPriceAndCurrentLonger() throws ParseException {
        Price currentPrice = new Price(1, "122856", 1, 1, dateFormat.parse("10.01.2013 00:00:00"), dateFormat.parse("31.01.2013 23:59:59"), 6000);
        Price newPrice = new Price(1, "122856", 1, 1, dateFormat.parse("05.01.2013 00:00:00"), dateFormat.parse("20.01.2013 23:59:59"), 6000);

        List<Price> expected = new ArrayList<>();
        expected.add(new Price(1, "122856", 1, 1, dateFormat.parse("05.01.2013 00:00:00"), dateFormat.parse("31.01.2013 23:59:59"), 6000));

        List<Price> actual = PriceMerger.merge(Collections.singletonList(currentPrice), newPrice);
        assertEquals(expected, actual, "Should expand product start date.");
    }

    /*
     *    [----60----] +
     * [---50---] =
     * [---50---][60-]
     */
    @Test
    void testMergeDifferentPriceAndCurrentLonger() throws ParseException {
        Price currentPrice = new Price(1, "122856", 1, 1, dateFormat.parse("10.01.2013 00:00:00"), dateFormat.parse("31.01.2013 23:59:59"), 6000);
        Price newPrice = new Price(1, "122856", 1, 1, dateFormat.parse("05.01.2013 00:00:00"), dateFormat.parse("20.01.2013 23:59:59"), 5000);

        List<Price> expected = new ArrayList<>();
        expected.add(new Price(1, "122856", 1, 1, dateFormat.parse("05.01.2013 00:00:00"), dateFormat.parse("20.01.2013 23:59:59"), 5000));
        expected.add(new Price(1, "122856", 1, 1, dateFormat.parse("20.01.2013 23:59:59"), dateFormat.parse("31.01.2013 23:59:59"), 6000));

        List<Price> actual = PriceMerger.merge(Collections.singletonList(currentPrice), newPrice);
        assertEquals(expected, actual, "Should shift current price and insert new price.");
    }

    /*
     * [-----60-----] +
     *    [---60---] =
     * [-----60-----]
     */
    @Test
    void testMergeEqualPriceAndNewPriceInsideCurrent() throws ParseException {
        Price currentPrice = new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("31.01.2013 23:59:59"), 6000);
        Price newPrice = new Price(1, "122856", 1, 1, dateFormat.parse("20.01.2013 00:00:00"), dateFormat.parse("30.01.2013 23:59:59"), 6000);

        List<Price> expected = new ArrayList<>();
        expected.add(new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("31.01.2013 23:59:59"), 6000));

        List<Price> actual = PriceMerger.merge(Collections.singletonList(currentPrice), newPrice);
        assertEquals(expected, actual, "Should ignore new price if it inside current price and have the same value.");
    }

    /*
     * [-------60--------] +
     *     [---50---] =
     * [60][---50---][60-]
     */
    @Test
    void testMergeDifferentPriceAndNewPriceInsideCurrent() throws ParseException {
        Price currentPrice = new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("31.01.2013 23:59:59"), 6000);
        Price newPrice = new Price(1, "122856", 1, 1, dateFormat.parse("20.01.2013 00:00:00"), dateFormat.parse("30.01.2013 23:59:59"), 5000);

        List<Price> expected = new ArrayList<>();
        expected.add(new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("20.01.2013 00:00:00"), 6000));
        expected.add(new Price(1, "122856", 1, 1, dateFormat.parse("20.01.2013 00:00:00"), dateFormat.parse("30.01.2013 23:59:59"), 5000));
        expected.add(new Price(1, "122856", 1, 1, dateFormat.parse("30.01.2013 23:59:59"), dateFormat.parse("31.01.2013 23:59:59"), 6000));

        List<Price> actual = PriceMerger.merge(Collections.singletonList(currentPrice), newPrice);
        assertEquals(expected, actual, "Should split current price and insert new price if value is different.");
    }

    /*
     * [--60---] +
     * [----60----] =
     * [----60----]
     */
    @Test
    void testMergeEqualPriceAndCurrentPriceInsideNew() throws ParseException {
        Price currentPrice = new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("10.01.2013 23:59:59"), 6000);
        Price newPrice = new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("20.01.2013 23:59:59"), 6000);

        List<Price> expected = new ArrayList<>();
        expected.add(new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("20.01.2013 23:59:59"), 6000));

        List<Price> actual = PriceMerger.merge(Collections.singletonList(currentPrice), newPrice);
        assertEquals(expected, actual, "Should ignore current price if it inside new price and have the same value.");
    }

    /*
     * [--60---] +
     * [----50----] =
     * [----50----]
     */
    @Test
    void testMergeDifferentPriceAndCurrentPriceInsideNew() throws ParseException {
        Price currentPrice = new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("10.01.2013 23:59:59"), 6000);
        Price newPrice = new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("20.01.2013 23:59:59"), 5000);

        List<Price> expected = new ArrayList<>();
        expected.add(new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("20.01.2013 23:59:59"), 5000));

        List<Price> actual = PriceMerger.merge(Collections.singletonList(currentPrice), newPrice);
        assertEquals(expected, actual, "Should ignore current price if it inside new price and value is different.");
    }

    /*
     * [--60---]         [---70---] +
     *           [--50-] =
     * [--60---] [--50-] [---70---]
     */
    @Test
    void testMergeWithNoOverlap() throws ParseException {
        ArrayList<Price> currentPrice = new ArrayList<>();
        currentPrice.add(new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("20.01.2013 23:59:59"), 6000));
        currentPrice.add(new Price(1, "122856", 1, 1, dateFormat.parse("11.02.2013 00:00:00"), dateFormat.parse("21.02.2013 23:59:59"), 7000));
        Price newPrice = new Price(1, "122856", 1, 1, dateFormat.parse("22.01.2013 00:00:00"), dateFormat.parse("10.02.2013 23:59:59"), 5000);

        List<Price> expected = new ArrayList<>();
        expected.add(new Price(1, "122856", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("20.01.2013 23:59:59"), 6000));
        expected.add(new Price(1, "122856", 1, 1, dateFormat.parse("22.01.2013 00:00:00"), dateFormat.parse("10.02.2013 23:59:59"), 5000));
        expected.add(new Price(1, "122856", 1, 1, dateFormat.parse("11.02.2013 00:00:00"), dateFormat.parse("21.02.2013 23:59:59"), 7000));

        List<Price> actual = PriceMerger.merge(currentPrice, newPrice);
        assertEquals(expected, actual, "Should add new price between two current prices if no overlap.");
    }

    @Test
    void testIsLeftOverlap() throws ParseException {
        Price currentPrice = new Price(1, "1", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("31.01.2013 00:00:00"), 0);
        Price newPrice = new Price(1, "1", 1, 1, dateFormat.parse("01.02.2013 00:00:00"), dateFormat.parse("13.02.2013 00:00:00"), 0);

        assertFalse(PriceMerger.isLeftOverlap(currentPrice, newPrice));

        currentPrice = new Price(1, "1", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("31.01.2013 00:00:00"), 0);
        newPrice = new Price(1, "1", 1, 1, dateFormat.parse("12.01.2013 00:00:00"), dateFormat.parse("13.01.2013 00:00:00"), 0);

        assertTrue(PriceMerger.isLeftOverlap(currentPrice, newPrice));
    }

    @Test
    void testIsRightOverlap() throws ParseException {
        Price currentPrice = new Price(1, "1", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("31.01.2013 00:00:00"), 0);
        Price newPrice = new Price(1, "1", 1, 1, dateFormat.parse("01.02.2013 00:00:00"), dateFormat.parse("13.02.2013 00:00:00"), 0);

        assertFalse(PriceMerger.isRightOverlap(currentPrice, newPrice));

        currentPrice = new Price(1, "1", 1, 1, dateFormat.parse("10.01.2013 00:00:00"), dateFormat.parse("31.01.2013 00:00:00"), 0);
        newPrice = new Price(1, "1", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("13.01.2013 00:00:00"), 0);

        assertTrue(PriceMerger.isRightOverlap(currentPrice, newPrice));
    }

    @Test
    void testIsInsight() throws ParseException {
        Price inside = new Price(1, "1", 1, 1, dateFormat.parse("10.02.2013 00:00:00"), dateFormat.parse("13.02.2013 00:00:00"), 0);
        Price outside = new Price(1, "1", 1, 1, dateFormat.parse("11.01.2013 00:00:00"), dateFormat.parse("31.01.2013 00:00:00"), 0);

        assertFalse(PriceMerger.isInside(inside, outside));

        inside = new Price(1, "1", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("13.01.2013 00:00:00"), 0);
        outside = new Price(1, "1", 1, 1, dateFormat.parse("01.01.2013 00:00:00"), dateFormat.parse("31.01.2013 00:00:00"), 0);

        assertTrue(PriceMerger.isInside(inside, outside));
    }
}