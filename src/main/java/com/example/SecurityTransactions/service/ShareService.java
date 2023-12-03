package com.example.SecurityTransactions.service;

import com.example.SecurityTransactions.entity.Share;
import com.example.SecurityTransactions.entity.ShareBalance;
import com.example.SecurityTransactions.entity.Transaction;
import com.example.SecurityTransactions.entity.TransactionType;
import com.example.SecurityTransactions.exception.DuplicateEntryException;
import com.example.SecurityTransactions.repo.ShareRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ShareService {
    private final ShareRepository shareRepository;

    @Autowired
    public ShareService(ShareRepository shareRepository) {
        this.shareRepository = shareRepository;
    }

    public List<Share> findAllShare() {
        return shareRepository.findAll();
    }

    public Share addShare(Share share) {
        if (shareRepository.existsBySymbol(share.getSymbol().toUpperCase())) {
            throw new DuplicateEntryException("Share with symbol already registered");
        }
        return shareRepository.save(share);
    }

    public Share updateShare(Share share) {
        String currentSymbol = shareRepository.findById(share.getId()).get().getSymbol().toLowerCase();
        if (!currentSymbol.equals(share.getSymbol().toLowerCase())){
            throw new DuplicateEntryException("This symbol already exists");
        }
        return shareRepository.save(share);
    }

    public Share findByTransactionId(Long transactionId) {
        List<Share> shares = shareRepository.findAll();
        Optional<Share> optionalShare = shares.stream()
                .filter(share -> share.getStockTransactions().stream()
                        .anyMatch(transaction -> transaction.getId().equals(transactionId)))
                .findFirst();
        return optionalShare.orElse(null);
    }

    public float getBookPrice(Share share) {
        float totalValue = 0;
        int totalVolume = 0;
        List<Transaction> allTransactions = share.getStockTransactions();
        for (Transaction transaction : allTransactions) {
            if (transaction.getType() == TransactionType.PURCHASE) {
                totalValue += transaction.getPrice() * transaction.getVolume();
                totalVolume += transaction.getVolume();
            }
        }
        float averageBookPrice = totalValue / totalVolume;
        return averageBookPrice;
    }

    public List<ShareBalance> getCurrentBalance() {
        List<ShareBalance> balance = new ArrayList<>();
        List<Share> allShares = shareRepository.findAll();
        for (int i = 0; i < allShares.size(); i++) {
            long initialBalance = 0;
            List<Transaction> transactions = allShares.get(i).getStockTransactions();
            for (int j = 0; j < transactions.size(); j++) {
                if (transactions.get(j).getType() == TransactionType.PURCHASE) {
                    initialBalance += transactions.get(j).getVolume();
                } else {
                    initialBalance -= transactions.get(j).getVolume();
                }
            }
            balance.add(new ShareBalance(allShares.get(i).getSymbol(), allShares.get(i).getShareName(), initialBalance, allShares.get(i).getCurrency(), getBookPrice(allShares.get(i))));
        }

        return balance;
    }

}
