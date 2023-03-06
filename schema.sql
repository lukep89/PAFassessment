
create database acme_bank;

use acme_bank;

create table accounts (
account_id char(10) not null primary key,
name varchar(128) not null ,
balance decimal(10,2) not null default 0.00
);


insert into accounts (account_id, name, balance)
values ("V9L3Jd1BBI", "fred", 100.00),
("fhRq46Y6vB", "barney", 300.00),
("uFSFRqUpJy","wilma",1000.00),
("ckTV56axff","betty",1000.00),
("Qgcnwbshbh","pebbles",50.00),
("if9l185l18","bambam",50.00);

use acme_bank;

select * from accounts;

select * from accounts
where account_id = "if9l185l18";

update accounts
set balance = 470
where account_id = "ckTV56axff";


