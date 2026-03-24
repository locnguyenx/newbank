# Cash Management Services - BDD Specifications (Gherkin)

**Date:** 2026-03-24  
**Version:** 1.0  
**Status:** Draft  

## Feature: Payroll Processing

### Scenario: Successful payroll file upload and validation
Given a corporate customer is logged into the banking portal
And the customer has navigated to the Cash Management > Payroll section
When the customer uploads a valid payroll file in CSV format containing 50 employee records
And the file includes valid bank account information for all employees
Then the system should accept the file for processing
And display a confirmation message indicating successful upload
And show the number of records processed (50)
And move the file to the "Processing" queue

### Scenario: Payroll file upload with invalid data
Given a corporate customer is logged into the banking portal
And the customer has navigated to the Cash Management > Payroll section
When the customer uploads a payroll file containing records with invalid bank account numbers
Then the system should reject the file
And display error messages highlighting the specific validation failures
And not process any records from the file
And allow the customer to correct and re-upload the file

### Scenario: Payroll processing with insufficient funds check
Given a payroll file has been uploaded and validated successfully
When the system processes the payroll during the scheduled run
And the system checks available funding in the source account
Then if sufficient funds are available, the system should proceed with payment file generation
And if insufficient funds are available, the system should hold the payroll for approval
And notify the appropriate approvers of the funding shortage
And generate an exception report for the funding issue

### Scenario: Payroll payment file generation
Given validated payroll data with sufficient funds
When the system generates the payment file for bank transmission
Then the system should create a file in the specified format (ACH/SEPA)
And include all required payment details for each employee
And apply appropriate processing dates
And generate a transmission report
And update the payroll status to "Submitted"

## Feature: Receivables Management

### Scenario: Invoice presentment to customer
Given a corporate customer has outstanding invoices in the system
When the customer accesses the Receivables Management portal
Then the system should display all outstanding invoices
And show invoice details including amount, due date, and customer information
And allow the customer to view invoice PDFs
And enable the customer to make payments against invoices

### Scenario: Automatic payment matching
Given a customer makes a payment against an outstanding invoice
When the payment is received and processed by the system
Then the system should automatically match the payment to the correct invoice
Based on invoice number, customer reference, or amount
And update the invoice status to "Partially Paid" or "Paid" as appropriate
And generate a payment confirmation for both parties
And update the accounts receivable balance

### Scenario: Partial payment handling
Given an outstanding invoice for $10,000
When a customer makes a payment of $6,000 against that invoice
Then the system should apply the payment to the invoice
And update the invoice status to "Partially Paid"
And show a remaining balance of $4,000
And keep the invoice active in the receivables workflow
And generate appropriate accounting entries

### Scenario: Dunning management for overdue invoices
Given an invoice that is 30 days past due
When the dunning process runs according to schedule
Then the system should generate a first reminder notice
And send it to the customer via configured channel (email/portal)
And log the dunning activity in the audit trail
And escalate to higher levels of collection if configured

## Feature: Liquidity Optimization

### Scenario: Real-time cash position viewing
Given a corporate customer with multiple bank accounts
When the customer accesses the Liquidity Management dashboard
Then the system should display consolidated cash positions
Showing balances by account, currency, and entity
With real-time updates reflecting the latest posted transactions
And allow drill-down to view individual account details
And provide currency conversion using current FX rates

### Scenario: Cash pooling between accounts
Given two accounts belonging to the same legal entity
With Account A having a surplus balance of $100,000
And Account B having a deficit balance of -$50,000
When the cash pooling process is executed
Then the system should transfer $50,000 from Account A to Account B
Resulting in Account A balance of $50,000 and Account B balance of $0
And generate appropriate accounting entries for both accounts
And apply any applicable charges via the Charges Management module
And create a cash pooling transaction record in the audit trail

### Scenario: Liquidity forecasting
Given historical transaction data for the past 90 days
When the customer requests a liquidity forecast for the next 30 days
Then the system should analyze historical patterns
And generate a forecast showing expected cash inflows and outflows
By day, week, and month
With confidence intervals based on historical variance
And allow the customer to adjust assumptions for scenario planning
And export the forecast in multiple formats

## Feature: Batch Payment Processing

### Scenario: Batch payment file upload and validation
Given a corporate customer needs to make multiple vendor payments
When the customer uploads a batch payment file in ISO 20022 format
Containing 500 payment instructions to various beneficiaries
Then the system should validate the file structure
And validate each payment instruction for required fields
And check beneficiary account details against known formats
And accept the file if all validations pass
Or reject the file with specific error details if validations fail

### Scenario: Batch payment processing with limit checking
Given a validated batch payment file has been uploaded
When the system processes the batch during the scheduled window
Then for each payment instruction, the system should:
Check if the amount exceeds the customer's payment limit
If within limits, proceed with payment processing
If exceeding limits, hold the payment for approval
And generate an exception report for payments requiring approval
And continue processing other payments in the batch

### Scenario: Batch payment file generation and transmission
Given a batch of validated payment instructions within limits
When the system generates the payment file for bank transmission
Then the system should create a properly formatted payment file
Include all required payment details for each transaction
Apply the requested payment date
Generate a transmission control record
And update the status of all payments to "Submitted for Processing"
And create transmission records in the audit trail

### Scenario: Batch payment cancellation before cut-off time
Given a batch payment file has been submitted for processing
But is still within the cancellation window
When a customer requests cancellation of the batch
Then the system should cancel all payments in the batch
Return any submitted payment files to the bank if possible
Update all payment statuses to "Cancelled"
Generate a cancellation confirmation
And create appropriate audit trail entries

## Feature: Auto-Collection

### Scenario: Automatic collection setup
Given a corporate customer with recurring receivables
When the customer configures an auto-collection rule
Specifying collection of full invoice amount on due date
From a specific funding account
With 2-day pre-notification to customers
Then the system should save the auto-collection rule
And activate it for applicable invoices
And confirm the rule configuration to the customer
And log the rule creation in the audit trail

### Scenario: Automatic collection execution
Given an active auto-collection rule for a customer
And an invoice reaches its due date
When the auto-collection process runs
Then the system should:
Verify sufficient funds exist in the funding account
Attempt to collect the full invoice amount
If successful, update the invoice status to "Paid"
Generate a collection confirmation for both parties
Apply any collection charges via Charges Management
Create accounting entries for the collection
If unsuccessful due to insufficient funds,
Hold the collection attempt for retry
Notify the customer of the failed attempt
And log all collection activities in the audit trail

### Scenario: Auto-collection with partial collection rule
Given an auto-collection rule configured to collect 50% of invoice amount
And an invoice for $1,000 reaches its due date
When the auto-collection process runs
Then the system should attempt to collect $500 (50% of $1,000)
From the designated funding account
If successful, update invoice to show $500 collected, $500 remaining
Generate collection confirmation for the partial amount
Apply charges based on the collected amount
If unsuccessful, follow the same failure handling as full collection

## Feature: Reporting and Analytics

### Scenario: Real-time cash position dashboard
Given a customer with multiple currency accounts
When the customer accesses the Cash Management dashboard
Then the system should display:
Total cash position by currency
Individual account balances
Recent transaction activity
Liquidity metrics (current ratio, cash conversion cycle)
With data refreshed no more than 5 minutes old
And allow the customer to filter by date range, account group, or entity
And export the dashboard data in multiple formats

### Scenario: Treasury report generation
Given historical transaction data is available
When a customer requests a standard cash forecast report
For the next 30 days with weekly periods
Then the system should generate a report showing:
Opening balance for each period
Expected cash inflows
Expected cash outflows
Net cash flow
Closing balance for each period
Based on historical patterns and scheduled transactions
And allow the customer to download the report in PDF or Excel format
And schedule the report for automatic generation and delivery

### Scenario: Audit trail reporting for compliance
Given the requirement to produce an audit trail report
For all cash management transactions in a date range
When a compliance officer requests the audit trail report
Specifying transaction types and date range
Then the system should generate a report showing:
All cash management transactions in the period
With timestamps, user IDs, transaction details
Before and after states where applicable
Reference to related documents or payment files
Sorted chronologically
In a format suitable for regulatory submission
And allow the report to be exported for external auditor review

## Feature: Security and Compliance

### Scenario: Role-based access control for cash management functions
Given a user with the "Treasury Viewer" role
When the user attempts to access payment initiation functions
Then the system should deny access to payment creation and submission
But allow access to cash position viewing and reporting
And log the access attempt in the security audit trail
And display an appropriate authorization error message

### Scenario: Dual control for high-value payments
Given a payment instruction exceeding the dual control threshold
When a user creates and submits the payment for approval
Then the system should:
Require approval from a second authorized user
Not process the payment until dual approval is received
Notify the second approver of the pending payment
Hold the payment in an approval queue
And release the payment for processing only after both approvals are given

### Scenario: Audit trail integrity for financial transactions
Given any financial transaction occurs in the Cash Management module
When the transaction is completed
Then the system must create an immutable audit record
Containing:
Unique transaction identifier
Timestamp of the transaction
User or system entity initiating the transaction
Transaction type and details
Account(s) affected
Amounts and currencies involved
Pre-transaction state (where applicable)
Post-transaction state
IP address and user agent (for user-initiated transactions)
And ensure this audit record cannot be altered or deleted
And make it available for compliance and internal audit review

## Feature: Integration with Foundation Modules

### Scenario: Customer validation during payment processing
Given a payment instruction is being processed
When the system validates the beneficiary information
Then the system should check if the beneficiary exists as a customer
In the Customer Management module
If the beneficiary is not a customer, proceed with standard validation
If the beneficiary is an existing customer,
Use existing customer data for validation where applicable
And log the customer check in the audit trail

### Scenario: Account balance verification for funding
Given a payment instruction requires funding from a specific account
When the system processes the payment
Then the system should query the Account Management module
To verify the current balance in the funding account
And ensure sufficient funds are available (considering pending transactions)
If sufficient funds exist, proceed with payment processing
If insufficient funds exist, handle according to insufficient funds procedure
And log the balance check in the audit trail

### Scenario: Limit checking for payment authorization
Given a payment instruction is ready for processing
When the system checks if the payment requires authorization
Then the system should query the Limits Management module
To determine if the payment amount exceeds the user's or account's limits
If within limits, proceed with standard processing
If exceeding limits, initiate the appropriate approval workflow
And apply the limit check result to the payment processing decision
And log the limit check in the audit trail

### Scenario: Charge application for cash management services
Given a cash management transaction has been processed
When finalizing the transaction
Then the system should query the Charges Management module
To determine if any charges apply to this transaction
Based on transaction type, amount, currency, and customer pricing
If charges apply, calculate the appropriate amount
And create a separate charge transaction
Link the charge to the original transaction
Apply the charge to the customer's account
And log the charge application in the audit trail