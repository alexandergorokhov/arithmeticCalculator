CREATE TABLE users (
  id BIGINT PRIMARY KEY,
  username VARCHAR(30) NOT NULL,
  password VARCHAR(200) NOT NULL,
  status VARCHAR(30) NOT NULL,
  balance DECIMAL(10, 2) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP
);

CREATE TABLE operation (
  id BIGINT PRIMARY KEY,
  type VARCHAR(50) NOT NULL,
  cost DECIMAL(10, 2) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP
);

CREATE TABLE record (
  id UUID PRIMARY KEY,
  operation_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  amount DECIMAL(10, 2) NOT NULL,
  user_balance DECIMAL(10, 2) NOT NULL,
  operation_response VARCHAR(50) NOT NULL,
  operation_date TIMESTAMP NOT NULL,
  deleted BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL,
  deleted_at TIMESTAMP,
  FOREIGN KEY (operation_id) REFERENCES operation(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE SPRING_SESSION (
   SESSION_ID VARCHAR(255) PRIMARY KEY,
      SESSION_DATA BYTEA,
      EXPIRY_TIME TIMESTAMP
);

