INSERT INTO users (id, username, password, status, balance, created_at)
VALUES (1,'test@domain.com','$2a$10$RMvB0po4bpJGwpcyX20c1u1NmIgl0uUd0t9UWPXwerd2W/nPuruxe','ACTIVE',20.0,'2023-06-06 12:00:00');

INSERT INTO operation (id, type,cost, created_at)
VALUES (0,'INIT','0','2023-06-06 12:00:00');
INSERT INTO operation (id, type,cost, created_at)
VALUES (1,'ADDITION','1.0','2023-06-06 12:00:00');
INSERT INTO operation (id, type,cost, created_at)
VALUES (2,'SUBTRACTION','1.0','2023-06-06 12:00:00');
INSERT INTO operation (id, type,cost, created_at)
VALUES (3,'MULTIPLICATION','1.0','2023-06-06 12:00:00');
INSERT INTO operation (id, type,cost, created_at)
VALUES (4,'DIVISION','1.0','2023-06-06 12:00:00');
INSERT INTO operation (id, type,cost, created_at)
VALUES (5,'SQUARE_ROOT','1.0','2023-06-06 12:00:00');
INSERT INTO operation (id, type,cost, created_at)
VALUES (6,'RANDOM_STRING','5.0','2023-06-06 12:00:00');
