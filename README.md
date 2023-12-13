# RUBank
Your team will develop a simple software to process the banking transactions for RU Bank. The transactions will be
entered through the command lines on the terminal. The software is an interactive system to produce the output
whenever a transaction is entered. The software shall be able to process transactions of opening an account, closing
an existing account, depositing money to an existing account, withdrawing money from an existing account, and print
the details of all accounts. RU Bank provides four types of banking accounts listed in the table below. Note that, same
person can hold different types of accounts, however, cannot hold a College Checking and Checking at the same time.
For all account types, must be age of 16 or older to open, for College Checking, must be under the age of 24 to open.
The interest rates and monthly fees are different based on the account types and account options.

O command, to open an account with the desired account type. There are 4 account types: C – checking, CC –
college checking, S – savings, MM – money market savings: O C John Doe 2/19/2000 599.99

C command, to close an existing account. When an account is closed, it will be removed from the account
database: C MM Jane Doe 10/1/1995

D command, to deposit money to an existing account. You should reject the transaction if an invalid amount is
entered: D C John Doe 2/19/1990 100

W command, to withdraw money from an existing account. Command line formats are the same with the D
command, and the same rules apply. However, you must check if there is enough balance for the withdrawal. For
Money Market accounts, the loyal customer status will be unchecked if balance dropped below $2000.

P command to display all the accounts in the account database, sorted by the account types. For the same account
type, sort by the account holder’s profile (last name, first name and dob.)

PI command, to display all the accounts in the account database, the same order with the P command. In addition,
display the calculated fees and monthly interests based on current account balances. Fees and interests should be
displays with 2 decimal places, see the sample output.

UB command, to update the account balance for all accounts by applying the fees and interests earned. This
should reset the number of withdrawals of the Money Market accounts to 0.

Q command, to stop the program execution.
