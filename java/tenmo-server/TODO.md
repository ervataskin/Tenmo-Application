# TODO
 Each TODO item is listed under the method where the comment is located. Once the comment has been moved, we should move the TODO item.
 We can add TODOs anywhere we need to, but we should probably make sure they're added to this list so we can keep track.

Use this document to reference what still needs to be done, and to add notes on things you notice need doing. There's another file in the project now for brainstorming, and a third for tracking our moving pieces. You can ignore these if you want to, but it's helpful glue work for me. 
## Server Side
### DAO
#### Transfer
This section includes TODOs that started in TransferDao and need to be moved elsewhere.
I will remove this notice once they're moved.

**TODO COMMENTS**
* method: getMyTransfers()
    * Update toString method to return transfers as a formatted string on the client side
* method: sendTransfer()
    * Set up "reject transfer" method
* method: approveTransfer
    * Consider using the userId for all the methods that call a user account directly?
    * Otherwise we have to build the method to convert username to user ID into our transfer methods. Which we can also do, I guess.

**GENERAL TASKS**
* Complete the createTransfer method
* Create getMyPendingTransfers method
    * How can we reduce the duplicate code so that getMyTransfers also allows us to filter our transfers by type? Overloaded method? (still duplicate code tho) 
* Set methods that are going to report directly to controller to return transfer objects, not individual values of transfer objects.

#### Account
This section includes TODOs that started in the AccountDao and need to be moved. 
I'll remove this notice once they're moved.


**TODO COMMENTS**
* method: getAccountBalance
    * Create an overloaded method of getBalance that allows us to use the ID OR the username
    * **OR** consider using username instead of userId in other methods.
    * That's what all the other comments in this DAO say too, so I'm not sure why I repeated them.

**GENERAL TASKS**
* Set methods that are going to report directly to the controller to return account objects, not the individual values of account objects.

#### User
(UserDao should not have any TODO items, probably, maybe.)


### Models

### Controllers

## Client Side

### Services

### Models

### Application