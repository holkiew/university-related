CREATE SEQUENCE seq_Parking;
CREATE SEQUENCE seq_Car;
CREATE SEQUENCE seq_Car_log;
CREATE SEQUENCE seq_Parking_place;
CREATE SEQUENCE seq_Parking_level;
CREATE SEQUENCE seq_Transaction;
CREATE SEQUENCE seq_Parking_type;
CREATE TABLE Parking (
  id           number(10) NOT NULL, 
  name         varchar2(255) NOT NULL, 
  no_of_places number(10) NOT NULL, 
  free_places  number(10) NOT NULL, 
  hour_cost    number(4, 2) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE Car (
  id                    number(10) NOT NULL, 
  Car_logid             number(10) NOT NULL, 
  identyfication_number varchar2(255) NOT NULL, 
  colour                varchar2(255) NOT NULL, 
  manufacturer          varchar2(255) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE Car_log (
  id         number(10) NOT NULL, 
  parking_id number(10) NOT NULL, 
  entry_date timestamp(0) NOT NULL, 
  exit_date  timestamp(0) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE Parking_place (
  id               number(10) NOT NULL, 
  parking_level_id number(10) NOT NULL, 
  Parking_typeid   number(10) NOT NULL, 
  is_occupied      number(1) NOT NULL, 
  code_name        varchar2(255) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE Parking_level (
  id                number(10) NOT NULL, 
  parking_id        number(10) NOT NULL, 
  total_places      number(10) NOT NULL, 
  total_free_places number(10) NOT NULL, 
  covered           number(1) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE "Transaction" (
  id         number(10) NOT NULL, 
  car_log_id number(10) NOT NULL, 
  cost       number(10, 2) NOT NULL, 
  duration   number(10) NOT NULL, 
  code       varchar2(255) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE Parking_type (
  id          number(10) NOT NULL, 
  description number(10) NOT NULL, 
  PRIMARY KEY (id));
ALTER TABLE Car_log ADD CONSTRAINT FKCar_log821897 FOREIGN KEY (parking_id) REFERENCES Parking (id);
ALTER TABLE Parking_level ADD CONSTRAINT FKParking_le37543 FOREIGN KEY (parking_id) REFERENCES Parking (id);
ALTER TABLE Parking_place ADD CONSTRAINT FKParking_pl720313 FOREIGN KEY (parking_level_id) REFERENCES Parking_level (id);
ALTER TABLE "Transaction" ADD CONSTRAINT FKTransactio749740 FOREIGN KEY (car_log_id) REFERENCES Car_log (id);
ALTER TABLE Car ADD CONSTRAINT FKCar809380 FOREIGN KEY (Car_logid) REFERENCES Car_log (id);
ALTER TABLE Parking_place ADD CONSTRAINT FKParking_pl909272 FOREIGN KEY (Parking_typeid) REFERENCES Parking_type (id);
