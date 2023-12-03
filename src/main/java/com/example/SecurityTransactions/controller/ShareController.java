package com.example.SecurityTransactions.controller;

import com.example.SecurityTransactions.entity.Share;
import com.example.SecurityTransactions.entity.ShareBalance;
import com.example.SecurityTransactions.exception.DuplicateEntryException;
import com.example.SecurityTransactions.service.ShareService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/share")
public class ShareController {
    private final ShareService shareService;

    public ShareController(ShareService shareService) {
        this.shareService = shareService;
    }

    @GetMapping("/balance")
    public List<ShareBalance> getBalances() {
        return shareService.getCurrentBalance();
    }

    @GetMapping("/all")
    public List<Share> getAllShares() {
        return shareService.findAllShare();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addShare (@RequestBody Share share){
        try {
            Share newShare = shareService.addShare(share);
            return new ResponseEntity<>(newShare, HttpStatus.CREATED);
        } catch (
                DuplicateEntryException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateShare(@RequestBody Share theShare) {
        try {
            Share updatedShare = shareService.updateShare(theShare);
            return new ResponseEntity<>(updatedShare, HttpStatus.OK);
        } catch (DuplicateEntryException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<Share> getShareByTransactionId(@PathVariable("id") Long id) {
        Share foundShare = shareService.findByTransactionId(id);
        return new ResponseEntity<>(foundShare, HttpStatus.OK);
    }

}