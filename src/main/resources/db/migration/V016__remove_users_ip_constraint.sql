
-- switching to keycloak, same ip can be used by different users
ALTER TABLE users DROP CONSTRAINT users_ip_key;
