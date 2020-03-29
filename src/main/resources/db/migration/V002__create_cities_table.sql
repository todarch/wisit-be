CREATE TABLE cities (
  id SERIAL NOT NULL,
  name VARCHAR(255) NOT NULL UNIQUE,
  country_id smallint NOT NULL
);

INSERT INTO cities (name, country_id) VALUES ('Ankara', (select id from wisit.countries where name = 'Turkey'));
INSERT INTO cities (name, country_id) VALUES ('Istanbul', (select id from wisit.countries where name = 'Turkey'));

INSERT INTO cities (name, country_id) VALUES ('Tirana', (SELECT id FROM wisit.countries WHERE name = 'Albania'));
INSERT INTO cities (name, country_id) VALUES ('Vienna', (SELECT id FROM wisit.countries WHERE name = 'Austria'));
INSERT INTO cities (name, country_id) VALUES ('Minsk', (SELECT id FROM wisit.countries WHERE name = 'Belarus'));
INSERT INTO cities (name, country_id) VALUES ('Brussels', (SELECT id FROM wisit.countries WHERE name = 'Belgium'));
INSERT INTO cities (name, country_id) VALUES ('Sarajevo', (SELECT id FROM wisit.countries WHERE name = 'Bosnia and Herzegovina'));
INSERT INTO cities (name, country_id) VALUES ('Sofia', (SELECT id FROM wisit.countries WHERE name = 'Bulgaria'));
INSERT INTO cities (name, country_id) VALUES ('Zagreb', (SELECT id FROM wisit.countries WHERE name = 'Croatia'));
INSERT INTO cities (name, country_id) VALUES ('Prague', (SELECT id FROM wisit.countries WHERE name = 'Czech Republic'));
INSERT INTO cities (name, country_id) VALUES ('Copenhagen', (SELECT id FROM wisit.countries WHERE name = 'Denmark'));
INSERT INTO cities (name, country_id) VALUES ('Tallinn', (SELECT id FROM wisit.countries WHERE name = 'Estonia'));
INSERT INTO cities (name, country_id) VALUES ('Helsinki', (SELECT id FROM wisit.countries WHERE name = 'Finland'));
INSERT INTO cities (name, country_id) VALUES ('Paris', (SELECT id FROM wisit.countries WHERE name = 'France'));
INSERT INTO cities (name, country_id) VALUES ('Berlin', (SELECT id FROM wisit.countries WHERE name = 'Germany'));
INSERT INTO cities (name, country_id) VALUES ('Athens', (SELECT id FROM wisit.countries WHERE name = 'Greece'));
INSERT INTO cities (name, country_id) VALUES ('Budapest', (SELECT id FROM wisit.countries WHERE name = 'Hungary'));
INSERT INTO cities (name, country_id) VALUES ('Reykjavik', (SELECT id FROM wisit.countries WHERE name = 'Iceland'));
INSERT INTO cities (name, country_id) VALUES ('Dublin', (SELECT id FROM wisit.countries WHERE name = 'Ireland'));
INSERT INTO cities (name, country_id) VALUES ('Rome', (SELECT id FROM wisit.countries WHERE name = 'Italy'));
INSERT INTO cities (name, country_id) VALUES ('Pristina', (SELECT id FROM wisit.countries WHERE name = 'Kosovo'));
INSERT INTO cities (name, country_id) VALUES ('Riga', (SELECT id FROM wisit.countries WHERE name = 'Latvia'));
INSERT INTO cities (name, country_id) VALUES ('Vilnius', (SELECT id FROM wisit.countries WHERE name = 'Lithuania'));
INSERT INTO cities (name, country_id) VALUES ('Valletta', (SELECT id FROM wisit.countries WHERE name = 'Malta'));
INSERT INTO cities (name, country_id) VALUES ('Podgorica', (SELECT id FROM wisit.countries WHERE name = 'Montenegro'));
INSERT INTO cities (name, country_id) VALUES ('Amsterdam', (SELECT id FROM wisit.countries WHERE name = 'Netherlands'));
INSERT INTO cities (name, country_id) VALUES ('Skopje', (SELECT id FROM wisit.countries WHERE name = 'North Macedonia'));
INSERT INTO cities (name, country_id) VALUES ('Oslo', (SELECT id FROM wisit.countries WHERE name = 'Norway'));
INSERT INTO cities (name, country_id) VALUES ('Warsaw', (SELECT id FROM wisit.countries WHERE name = 'Poland'));
INSERT INTO cities (name, country_id) VALUES ('Lisbon', (SELECT id FROM wisit.countries WHERE name = 'Portugal'));
INSERT INTO cities (name, country_id) VALUES ('Bucharest', (SELECT id FROM wisit.countries WHERE name = 'Romania'));
INSERT INTO cities (name, country_id) VALUES ('Moscow', (SELECT id FROM wisit.countries WHERE name = 'Russia'));
INSERT INTO cities (name, country_id) VALUES ('Belgrade', (SELECT id FROM wisit.countries WHERE name = 'Serbia'));
INSERT INTO cities (name, country_id) VALUES ('Bratislava', (SELECT id FROM wisit.countries WHERE name = 'Slovakia'));
INSERT INTO cities (name, country_id) VALUES ('Ljubljana', (SELECT id FROM wisit.countries WHERE name = 'Slovenia'));
INSERT INTO cities (name, country_id) VALUES ('Madrid', (SELECT id FROM wisit.countries WHERE name = 'Spain'));
INSERT INTO cities (name, country_id) VALUES ('Stockholm', (SELECT id FROM wisit.countries WHERE name = 'Sweden'));
INSERT INTO cities (name, country_id) VALUES ('Bern', (SELECT id FROM wisit.countries WHERE name = 'Switzerland'));
INSERT INTO cities (name, country_id) VALUES ('Kyiv', (SELECT id FROM wisit.countries WHERE name = 'Ukraine'));
INSERT INTO cities (name, country_id) VALUES ('London', (SELECT id FROM wisit.countries WHERE name = 'United Kingdom'));
INSERT INTO cities (name, country_id) VALUES ('Vatican City', (SELECT id FROM wisit.countries WHERE name = 'Vatican City'));
