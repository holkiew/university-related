
CREATE TABLE Parking (
  id               char(100) NOT NULL, 
  name             varchar2(255) NOT NULL, 
  no_of_places     number(10) NOT NULL, 
  free_places      number(10) NOT NULL, 
  hour_cost        number(4, 2) NOT NULL);
CREATE TABLE Car (
  id                    char(100) NOT NULL, 
  car_log               number(10) NOT NULL, 
  identyfication_number number(10) NOT NULL, 
  colour                varchar2(255) NOT NULL, 
  manufacturer          varchar2(255) NOT NULL);
CREATE TABLE Car_log (
  id         char(100) NOT NULL, 
  parking_id number(10) NOT NULL, 
  entry_date timestamp NOT NULL, 
  exit_date  timestamp NOT NULL);
CREATE TABLE Parking_place (
  id               char(100) NOT NULL, 
  parking_level_id number(10) NOT NULL, 
  is_occupied      number(1) NOT NULL, 
  code_name        varchar2(255) NOT NULL);
CREATE TABLE Parking_type (
  id               char(100) NOT NULL, 
  description      varchar2(255) NOT NULL);
CREATE TABLE Parking_level (
  id                char(100) NOT NULL, 
  parking_id        number(10) NOT NULL, 
  total_places      number(10) NOT NULL, 
  total_free_places number(10) NOT NULL, 
  covered           number(1) NOT NULL);
CREATE TABLE "Transaction" (
  id         char(100) NOT NULL, 
  car_log_id number(10) NOT NULL, 
  cost       number(10, 2) NOT NULL, 
  duration   number(10) NOT NULL, 
  code       varchar2(255) NOT NULL);
