-- Declare a variable to store Ravenclaw house_id
SET @ravenclaw_house_id = (SELECT id FROM master_houses WHERE name = 'Ravenclaw');

-- Insert wizard data in Ravenclaw
INSERT INTO wizards (first_name, last_name, gender, house_id)
VALUES
  ('Filius', 'Flitwick', 'Male', @ravenclaw_house_id),
  ('Gilderoy', 'Lockhart', 'Male', @ravenclaw_house_id),
  ('Sybill', 'Trelawney', 'Female', @ravenclaw_house_id);