package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;

public class ShareItGatewayTest {
    @Test
    public void mainShouldCallSpringApplicationRun() {
        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(ShareItGateway.class, new String[]{})).thenReturn(null);

            assertDoesNotThrow(() -> ShareItGateway.main(new String[]{}));

            mocked.verify(() -> SpringApplication.run(ShareItGateway.class, new String[]{}));
        }
    }
}

