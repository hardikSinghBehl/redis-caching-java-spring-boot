-- Declare a variable to store Slytherin house_id
SET @slytherin_house_id = (SELECT id FROM master_houses WHERE name = 'Slytherin');

-- Insert wizard data in Slytherin
INSERT INTO wizards (first_name, last_name, gender, house_id)
VALUES
  ('Severus', 'Snape', 'Male', @slytherin_house_id),
  ('Tom', 'Riddle', 'Male', @slytherin_house_id),
  ('Bellatrix', 'Lestrange', 'Female', @slytherin_house_id);