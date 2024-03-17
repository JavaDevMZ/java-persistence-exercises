CREATE TABLE account (
    id bigint,
    email VARCHAR(255) not null UNIQUE,
    first_name VARCHAR(255) not null,
    last_name VARCHAR(255) not null,
    gender VARCHAR(255) not null,
    birthday DATE not null,
    balance DECIMAL(19,4),
    creation_time TIMESTAMP not null DEFAULT now(),
CONSTRAINT account_pk PRIMARY KEY(id),
CONSTRAINT account_email_uq UNIQUE(email)
                     )