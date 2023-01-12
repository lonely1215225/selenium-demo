package org.taichiman;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TransactionEntity {
    private String txnHash;
    private String method;
    private String from;
    private String to;
    private String quantity;
}
