package vn.fptu.reasbe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.fptu.reasbe.model.dto.exchange.EvidenceExchangeRequest;
import vn.fptu.reasbe.model.dto.exchange.ExchangeRequestRequest;
import vn.fptu.reasbe.model.dto.exchange.ExchangeRequestResponse;
import vn.fptu.reasbe.model.dto.exchange.ExchangeResponse;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeHistory;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeRequest;
import vn.fptu.reasbe.service.ExchangeService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/exchange")
@RequiredArgsConstructor
public class ExchangeController {
    private final ExchangeService exchangeService;


    @GetMapping("/current-user")
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<List<ExchangeResponse>> getAllExchangesByStatusOfCurrentUser(@RequestParam StatusExchangeRequest statusExchangeRequest,
                                                                                       @RequestParam(required = false) StatusExchangeHistory statusExchangeHistory) {
        return ResponseEntity.ok(exchangeService.getAllExchangeByStatusOfCurrentUser(statusExchangeRequest, statusExchangeHistory));
    }

    @GetMapping("/{exchangeId}")
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<ExchangeResponse> getExchangeById(@PathVariable Integer exchangeId) {
        return ResponseEntity.ok(exchangeService.getExchangeById(exchangeId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<ExchangeRequestResponse> makeAnExchange(@RequestBody @Valid ExchangeRequestRequest request) {
        return ResponseEntity.ok(exchangeService.createExchangeRequest(request));
    }

    @PutMapping("/negotiated-price")
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<ExchangeRequestResponse> updateExchangeRequestPrice(@RequestParam Integer exchangeId, @RequestParam BigDecimal negotiatedPrice) {
        return ResponseEntity.ok(exchangeService.updateExchangeRequestPrice(exchangeId, negotiatedPrice));
    }

    @PutMapping("/review")
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<ExchangeResponse> reviewExchangeRequest(@RequestParam Integer exchangeId, @RequestParam StatusExchangeRequest statusExchangeRequest) {
        return ResponseEntity.ok(exchangeService.reviewExchangeRequest(exchangeId, statusExchangeRequest));
    }

    @PostMapping("/evidence")
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<ExchangeResponse> uploadExchangeEvidence(@RequestBody @Valid EvidenceExchangeRequest request) {
        return ResponseEntity.ok(exchangeService.uploadEvidence(request));
    }

    @PutMapping("/cancel")
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<ExchangeResponse> cancelApprovedExchange(@RequestParam Integer exchangeId) {
        return ResponseEntity.ok(exchangeService.cancelApprovedExchange(exchangeId));
    }
}
