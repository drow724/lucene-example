DROP TABLE if exists MEMBER;

CREATE TABLE MEMBER (
	id BIGINT NOT NULL AUTO_INCREMENT primary key,
	name VARCHAR(50),
	birth DATE,
	city VARCHAR(50),
	gender VARCHAR(50),
	join_dt DATE,
	mail VARCHAR(50),
	login_ip VARCHAR(20)
)