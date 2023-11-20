-- Create master_houses table
CREATE TABLE master_houses (
  id BINARY(16) PRIMARY KEY DEFAULT (UUID_TO_BIN(UUID())),
  name VARCHAR(50) NOT NULL UNIQUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create wizards table
CREATE TABLE wizards (
  id BINARY(16) PRIMARY KEY DEFAULT (UUID_TO_BIN(UUID())),
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50),
  gender VARCHAR(20) NOT NULL,
  house_id BINARY(16) NOT NULL,
  wand_type VARCHAR(20),
  quidditch_position VARCHAR(10),
  blood_status VARCHAR(10),
  patronus VARCHAR(20),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT wizard_fkey_house FOREIGN KEY (house_id)
  REFERENCES master_houses (id)
);