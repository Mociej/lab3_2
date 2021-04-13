package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@ExtendWith(MockitoExtension.class)
class OrderTest {

    @Mock
    private Clock testclock;
    private Order order;
    private Instant submitDate;

    @BeforeEach
    void setUp() throws Exception {
        order=new Order(testclock);
        submitDate = Instant.parse("2018-12-25T00:00:00Z");
    }

    @Test
    public void TestOrderExpiredCatch() {
        when(testclock.getZone()).thenReturn(ZoneId.systemDefault());
        when(testclock.instant()).thenReturn(submitDate).thenReturn(submitDate.plusSeconds(25*60*60));

        try {
            order.submit();
            order.confirm();

            fail("fail no exception");

        } catch (OrderExpiredException ignored) {}
    }
    @Test
    public void TestOrderExpiredNoCatch() {
        when(testclock.getZone()).thenReturn(ZoneId.systemDefault());
        when(testclock.instant()).thenReturn(submitDate).thenReturn(submitDate.plusSeconds(24*60*60));

        try {
            order.submit();
            order.confirm();

        } catch (OrderExpiredException ignored) {
            fail("fail exception");
        }
    }
    @Test
    public void TestOrderExpiredCatchState() {
        when(testclock.getZone()).thenReturn(ZoneId.systemDefault());
        when(testclock.instant()).thenReturn(submitDate).thenReturn(submitDate.plusSeconds(25*60*60));

        try {
            order.submit();
            order.confirm();

            //fail("fail no exception");

        } catch (OrderExpiredException ignored) {}

        Order.State orderState= order.getOrderState();
        assertEquals(orderState, Order.State.CANCELLED);

    }
    @Test
    public void TestOrderExpiredNoCatchState() {
        when(testclock.getZone()).thenReturn(ZoneId.systemDefault());
        when(testclock.instant()).thenReturn(submitDate).thenReturn(submitDate.plusSeconds(24*60*60));

        try {
            order.submit();
            order.confirm();

        } catch (OrderExpiredException ignored) {
            fail("fail exception");
        }
        Order.State orderState= order.getOrderState();
        assertEquals(orderState, Order.State.CONFIRMED);
    }
}
