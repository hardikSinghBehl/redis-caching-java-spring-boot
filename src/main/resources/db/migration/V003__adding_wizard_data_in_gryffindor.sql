SET @gryffindor_house_id = (SELECT id FROM master_houses WHERE name = 'Gryffindor');

INSERT INTO wizards (first_name, last_name, gender, house_id)
VALUES
  ('Harry', 'Potter', 'Male', @gryffindor_house_id),
  ('Hermione', 'Granger', 'Female', @gryffindor_house_id),
  ('Ron', 'Weasley', 'Male', @gryffindor_house_id);