package com.banking.limits.event;

import com.banking.account.event.AccountOpenedEvent;
import com.banking.limits.service.LimitAssignmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Listener for account opened events to automatically assign product limits.
 * <p>
 * This listener runs asynchronously after an account is created. It retrieves
 * the product limits configured for the account's product and assigns them to
 * the new account.
 * </p>
 * <p>
 * By using an event listener, the Limits module decouples from the Account module.
 * The Account module simply publishes an event and doesn't know or care that
 * limits are being assigned. This follows the architectural principle of
 * loose coupling between modules.
 * </p>
 * <p>
 * Error handling: If limit assignment fails for a specific limit, the error
 * is logged but does not prevent other limits from being assigned. This ensures
 * that account creation is not blocked by limit assignment issues.
 * </p>
 */
@Component
public class AccountOpenedEventListener {

    private static final Logger log = LoggerFactory.getLogger(AccountOpenedEventListener.class);

    private final LimitAssignmentService limitAssignmentService;

    public AccountOpenedEventListener(LimitAssignmentService limitAssignmentService) {
        this.limitAssignmentService = limitAssignmentService;
    }

    /**
     * Handles account opened events by assigning product limits to the new account.
     *
     * @param event The AccountOpenedEvent containing account and product details
     */
    @EventListener
    public void handleAccountOpened(AccountOpenedEvent event) {
        log.info("Processing AccountOpenedEvent for account: {}, product: {}", 
                 event.getAccountNumber(), event.getProductCode());

        try {
            // Get all product limits configured for this product
            List<com.banking.limits.dto.response.ProductLimitResponse> productLimits = 
                limitAssignmentService.getProductLimits(event.getProductCode());
            
            log.debug("Found {} product limits for product {}", productLimits.size(), event.getProductCode());

            // Assign each limit to the new account
            for (com.banking.limits.dto.response.ProductLimitResponse productLimit : productLimits) {
                try {
                    limitAssignmentService.assignToAccount(
                        productLimit.getLimitDefinitionId(),
                        event.getAccountNumber(),
                        productLimit.getOverrideAmount()
                    );
                    log.info("Assigned limit {} to account {} with override amount {}", 
                             productLimit.getLimitDefinitionId(), 
                             event.getAccountNumber(),
                             productLimit.getOverrideAmount());
                } catch (Exception e) {
                    // Log error but continue with other limits
                    log.warn("Failed to assign limit {} to account {}: {}", 
                             productLimit.getLimitDefinitionId(), 
                             event.getAccountNumber(), 
                             e.getMessage());
                }
            }
            
            log.info("Completed limit assignment for account: {}", event.getAccountNumber());
        } catch (Exception e) {
            // Global error handler - log but don't rethrow (event-driven, fire-and-forget)
            log.error("Failed to process AccountOpenedEvent for account {}: {}", 
                      event.getAccountNumber(), e.getMessage(), e);
        }
    }
}