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
        when(testclock.instant()).
                thenReturn(submitDate.plusSeconds(25*60*60));
        try {
            order.submit();
            order.confirm();
        } catch (OrderExpiredException o){
        }
    }
}
