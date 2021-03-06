CREATE TABLE master_houses(
  id UUID PRIMARY KEY,
  name CHARACTER VARYING(50) NOT NULL UNIQUE,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE wizards(
  id UUID PRIMARY KEY,
  first_name CHARACTER VARYING (50) NOT NULL, 
  middle_name CHARACTER VARYING (50), 
  last_name CHARACTER VARYING (50), 
  gender CHARACTER VARYING (20) NOT NULL,
  house_id UUID NOT NULL,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT wizard_fkey_house FOREIGN KEY (house_id)
        REFERENCES master_houses (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
