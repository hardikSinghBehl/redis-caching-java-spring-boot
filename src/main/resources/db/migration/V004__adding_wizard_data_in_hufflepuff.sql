-- Declare a variable to store Hufflepuff house_id
SET @hufflepuff_house_id = (SELECT id FROM master_houses WHERE name = 'Hufflepuff');

-- Insert wizard data in Hufflepuff
INSERT INTO wizards (first_name, last_name, gender, house_id)
VALUES
  ('Cedric', 'Diggory', 'Male', @hufflepuff_house_id),
  ('Newt', 'Scamander', 'Male', @hufflepuff_house_id),
  ('Nymphadora', 'Tonks', 'Female', @hufflepuff_house_id);