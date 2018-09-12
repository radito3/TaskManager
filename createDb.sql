DROP DATABASE IF EXISTS Think_a_bitDB;
CREATE DATABASE Think_a_bitDB CHARACTER SET utf8 COLLATE utf8_general_ci;
USE Think_a_bitDB;

CREATE TABLE Task (
  Id INTEGER NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
  Title VARCHAR(64) NOT NULL,
  Body TEXT NOT NULL,
  AllDay BOOLEAN NOT NULL DEFAULT FALSE,
  Duration TIME NOT NULL DEFAULT 0
);

CREATE TABLE Reminder (
  Id INTEGER NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
  Title VARCHAR(128) NOT NULL
);

CREATE TABLE Goal (
  Id INTEGER NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
  Title VARCHAR(64) NOT NULL,
  Duration TIME NOT NULL DEFAULT 0
);
