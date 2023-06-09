INSERT INTO users (id, username, password, status, created_at)
VALUES (1,'test@domain.com','123','ACTIVE','2023-06-06 12:00:00');

INSERT INTO operation (id, type,cost, created_at)
VALUES (0,'INIT','0','2023-06-06 12:00:00');
INSERT INTO operation (id, type,cost, created_at)
VALUES (1,'ADDITION','1.0','2023-06-06 12:00:00');

INSERT INTO record (id, operation_id, user_id, amount, user_balance, operation_response, operation_date, created_at)
VALUES ('e0e465c4-320c-4df1-82e3-71248bea8f81',0,1,0,10.0,'PERFORMED','2023-06-06 12:00:00','2023-06-06 12:00:00');