package com.feelody.feelody_backend;

import com.feelody.feelody_backend.global.common.GcsFileManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ActiveProfiles("test")
@SpringBootTest
class FeelodyApplicationTests {

    @MockitoBean
    private GcsFileManager gcsFileManager;

    @Test
    void contextLoads() {
    }

}
