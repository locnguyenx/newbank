package com.banking.cashmanagement.exception;

import com.banking.common.exception.BaseException;
import com.banking.common.message.MessageCatalog;

public class AutoCollectionRuleNotFoundException extends BaseException {
    
    public AutoCollectionRuleNotFoundException(Long id) {
        super(MessageCatalog.CAS_AUTO_COLLECTION_RULE_NOT_FOUND, "Auto-collection rule not found with id: " + id);
    }
    
    public static AutoCollectionRuleNotFoundException forId(Long id) {
        return new AutoCollectionRuleNotFoundException(id);
    }
}
