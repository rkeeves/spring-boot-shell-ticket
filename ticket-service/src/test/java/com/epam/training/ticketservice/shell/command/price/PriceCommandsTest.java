package com.epam.training.ticketservice.shell.command.price;

import com.epam.training.ticketservice.core.price.service.PriceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceCommandsTest {

    @Mock
    private PriceService priceService;

    @InjectMocks
    private PriceCommands priceCommands;

    @Test
    void givenAuthorizedToUseCommand_whenUpdateBasePrice_thenCallService() {
        // given
        // when
        priceCommands.updateBasePrice(5000);
        // then
        verify(priceService, times(1))
                .updateBasePrice(5000);
        verifyNoMoreInteractions(priceService);
    }
}