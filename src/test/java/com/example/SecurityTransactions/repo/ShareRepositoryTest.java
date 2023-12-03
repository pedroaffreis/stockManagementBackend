package com.example.SecurityTransactions.repo;

import com.example.SecurityTransactions.entity.Share;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ShareRepositoryTest {
    @Autowired
    private ShareRepository shareRepository;

    @AfterEach
    void tearDown() {
        shareRepository.deleteAll();
    }

    @Test
    public void canFindBySymbol() {
        //Given
        Share testShare = new Share(1L, "CompanyName",
                "ShareName", "AAPL", "US",
                "Technology", "USD", new ArrayList<>());
        shareRepository.save(testShare);


        //When
        String shareSymbol = testShare.getSymbol();
        Optional<Share> foundShare = shareRepository.findBySymbol(shareSymbol);

        //Then
        assertTrue(foundShare.isPresent());
        assertEquals(testShare.getSymbol(), foundShare.get().getSymbol());
    }

}