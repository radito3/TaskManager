DROP DATABASE IF EXISTS Think_a_bitDB;
CREATE DATABASE Think_a_bitDB CHARACTER SET utf8 COLLATE utf8_general_ci;
USE Think_a_bitDB;

CREATE TABLE Eventt (
  Id INTEGER NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
  Title VARCHAR(64) NOT NULL,
  TypeOf ENUM('TASK', 'REMINDER', 'GOAL') NOT NULL DEFAULT 'TASK',
  Location TEXT DEFAULT NULL,
  TimeOf TIMESTAMP NULL,
  Description TEXT DEFAULT NULL,
  AllDay BOOLEAN NOT NULL DEFAULT FALSE,
  Duration INTEGER NULL,
  Reminder INTEGER NOT NULL DEFAULT 0,
  ToRepeat ENUM('NONE', 'DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY') NOT NULL DEFAULT 'NONE'
);

CREATE TABLE User (
  Id INTEGER NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
  UserName VARCHAR(128) NOT NULL,
  Email VARCHAR(64) DEFAULT NULL,
  Password TEXT NOT NULL
);

CREATE TABLE CalendarEvents (
  Id INTEGER NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
  EventId INTEGER NOT NULL,
  Date DATE NOT NULL,
  FOREIGN KEY (EventId) REFERENCES Eventt(Id) ON UPDATE CASCADE ON DELETE CASCADE
);
-- will add UserId to this table later
