Installation procedures:

Create database commandline in mysql client:

**CMD**: create database payment;

**Insert querys:**

INSERT INTO payment_app.roles(name) VALUES('ROLE_USER');

INSERT INTO payment_app.roles(name) VALUES('ROLE_ADMIN');

INSERT INTO payment_app.roles(name) VALUES('ROLE_MERCHANT');

----------------------------------------------------------
Flow of the application

1)Register User and Register admin user
2)Login as Admin and create Merchants
3)Create Authorized Transaction
4)Aprove transaction
5)Delete Transaction only Admin can do this
6)Refun transaction
7)Reverse transaction
8)Unsubscribe Merchant
9) Cron job is set up for 3600s so this makes one hour you can change it