CREATE TABLE users (
  id BIGINT PRIMARY KEY,
  username VARCHAR(30) NOT NULL,
  password VARCHAR(200) NOT NULL,
  status VARCHAR(30) NOT NULL,
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
  created_at TIMESTAMP NOT NULL,
  FOREIGN KEY (operation_id) REFERENCES operation(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE spring_session (
   session_id VARCHAR(255) PRIMARY KEY,
      session_data BYTEA,
      expiry_time TIMESTAMP
);

