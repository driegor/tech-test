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
  username varchar(45) NOT NULL,
  role varchar(45) NOT NULL,
  PRIMARY KEY (role,username),
  UNIQUE KEY uni_username_role (role,username),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username),
  CONSTRAINT fk_role FOREIGN KEY (role) REFERENCES roles (role)
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


INSERT INTO USER_ROLES(role,username) VALUES ('PAGE_1','user1');

INSERT INTO USER_ROLES(role,username) VALUES ('PAGE_1','user2');
INSERT INTO USER_ROLES(role,username) VALUES ('PAGE_2','user2');

INSERT INTO USER_ROLES(role,username) VALUES ('PAGE_1','user3');
INSERT INTO USER_ROLES(role,username) VALUES ('PAGE_2','user3');
INSERT INTO USER_ROLES(role,username) VALUES ('PAGE_3','user3');



