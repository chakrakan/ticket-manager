CREATE TABLE tickets (
  ticket_id INT NOT NULL,
  destination varchar(100) NOT NULL,
  dep_time varchar(30) NOT NULL,
  arr_time varchar(30) NOT NULL,
  ticket_num varchar(100) NOT NULL,
  seat_num INT NOT NULL,
  PRIMARY KEY (ticket_id)
);
COMMIT;
