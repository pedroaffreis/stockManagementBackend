package com.example.SecurityTransactions.repo;

import com.example.SecurityTransactions.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShareRepository extends JpaRepository<Share, Long> {

    Optional<Share> findBySymbol(String symbol);

    Boolean existsBySymbol (String symbol);
}
