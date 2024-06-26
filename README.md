# DBMS-Java

This repository contains the implementation of a light-weight DBMS in Java.


## Overview: A light-weight DBMS

I have built a prototype light-weight DBMS using Java, using the inbuilt modules. The file format for persistent storage is unique and data fault tolerant. This DBMS performs user login, and signup with 2 factor authentication and security with hashing algorithms. It also provides user to create multiple databases and create multiple tables inside of selected database. Table level operations are also supported such as: creating database, creating table (DDL), DML- select, insert, delete - data from table. Where clause is supported for select and delete operations.
Moreover, the DBMS provides extensive error handling capabilities with meaningful error messages displayed to user just like in MySQL DBMS. This project employs the S.O.L.I.D. and design principle for better code readability, maintainability, and extensibility. 

*Java IDE used*: IntelliJ IDEA 2023.1.2 
*JDK*: JDK 7.0.7 Stable latest long-term support Release [2]
*Java Documentation*: Using Java Docs
*Application Type*: Console based.
Chosen Algorithm for this light-weight DBMS: *MD5*

### Persistent Storage structure design:

This light-weight DBMS stores data into ‘.table’ and ‘.metadata’ files. For instance, the ‘.table’ file contains header information which are the column names. Moreover, it also contains the record data in rows, which contain the actual data of the table. The delimiters are different for both the headers and the data record. 
Row Delimiter:   *\n*
Column Delimiter: *;*
The same is with the metadata file, in which the file structure is such that the first line contains the table name information, followed by each column name and data type information.


### Functionalities of the DBMS: 

#### Authentication Operations:

_Execution flow for authentication functionality_: 

Upon each execution of the DBMS program, it first checks if the Authentication Metadata, authcontext to see if context user already logged in, if not system will prompt to register or login. There are persistent files to support this features.

*Package Name*: authentication
*Classes*:

- _Authentication_: This is the service layer authentication class. It handles the login, signup functionality with MD5 hasing of password stored in persistent file storage.

*Package Name*: dataprocessor

*Package Name*: transactionprocessor

*Package Name*: patternmatchers

*Package Name*: utils

*Package Name*: exception

#### Query Operations supported:

The DBMS offers multiple operations. Here is the description of the operations offered and the extent/ scope of which the functionality is provided:

1. Show databases: 
2. Use database <databasename>:
3. Create database:
4. Create table:
5. Select operation:
6. Insert operation:
7. Delete operation:



` Author : Jay Patel `

-----
-----
-----
