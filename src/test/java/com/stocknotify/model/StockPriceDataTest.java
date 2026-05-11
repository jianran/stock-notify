package com.stocknotify.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StockPriceDataTest {

    @Test
    void shouldNotifyWhenPriceDropsMoreThan3Percent() {
        StockPriceData data = new StockPriceData("AAPL", 100, 103);
        assertTrue(data.isShouldNotify());
        assertEquals(2.91, data.getPercentChange(), 0.01);
    }

    @Test
    void shouldNotNotifyWhenPriceDropsLessThan3Percent() {
        StockPriceData data = new StockPriceData("AAPL", 100, 102);
        assertFalse(data.isShouldNotify());
    }

    @Test
    void shouldNotifyExactlyAt3PercentDrop() {
        StockPriceData data = new StockPriceData("AAPL", 97, 100);
        assertTrue(data.isShouldNotify());
        assertEquals(3.0, data.getPercentChange(), 0.01);
    }
}
