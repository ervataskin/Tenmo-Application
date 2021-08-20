## DAOs
1. UserDao reads in and manipulates information about an individual user. This has been completed for us.
2. AccountDao reads in and manipulates information about an individual account using the user information.
    1. *getAccountBalance* 
       * gets the balance for the account. (Does what it says on the tin.)
    2. *checkBalance*
    3. *verifyAccount*
    4. *withdrawFunds*
    5. *depositFunds*
    6. *mapRowToAccount*
       * (not written yet) maps the returned query to an account object to return.
3. TransferDao reads in information and manipulates information about an individual account using the user and account information.
    1. *getTransferById* 
       * allows you to get an individual transfer by its transfer_id.
    2. *getMyTransfers* 
       * allows you to get all transfers associated with the current user.
    3. *createTransfer* 
       * creates a transfer object in the database. Could theoretically be private?
    4. *requestTransfer* 
       * allows you to request money from another user.
    5. *sendTransfer* 
       * allows you to send money to another user.
    6. *approveTransfer* 
       * actually executes the math for any transfer. Should be used inside the sendTransfer method. ONLY APPROVES TRANSFERS IN PENDING STATUS; cannot re-approve approved transfers and cannot approve rejected transfers.
    7. *rejectTransfer* 
       * sets status of transfer to rejected.
    8. *mapRowToTransfer* 
       * uses the sql rowset to map transfers to a 

## Controllers

## Models