CREATE TABLE USERS (
  username VARCHAR(45) NOT NULL ,
  password VARCHAR(45) NOT NULL ,
  PRIMARY KEY (username)
);

CREATE TABLE ROLES (
  role_id int(11) NOT NULL AUTO_INCREMENT,
  role VARCHAR(45) NOT NULL ,
  PRIMARY KEY (role_id)
);

CREATE TABLE USER_ROLES (
  user_role_id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(45) NOT NULL,
  role_id int(11) NOT NULL,
  PRIMARY KEY (user_role_id),
  UNIQUE KEY uni_username_role (role_id,username),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username),
  CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES roles (role_id)
);

INSERT INTO users(username,password)
VALUES ('user1','123456');

INSERT INTO users(username,password)
VALUES ('user2','123456');

INSERT INTO users(username,password)
VALUES ('user3','123456');

INSERT INTO users(username,password)
VALUES ('admin','123456');


INSERT INTO roles(role)
VALUES ('PAGE_1');

INSERT INTO roles(role)
VALUES ('PAGE_2');

INSERT INTO roles(role)
VALUES ('PAGE_3');

INSERT INTO roles(role)
VALUES ('ADMIN');

