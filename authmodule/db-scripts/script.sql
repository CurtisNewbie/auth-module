CREATE TABLE IF NOT EXISTS user (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT "primary key",
    username VARCHAR(255) NOT NULL UNIQUE COMMENT "username (must be unique)",
    password VARCHAR(255) NOT NULL COMMENT "password in hash",
    salt VARCHAR(10) NOT NULL COMMENT "salt",
    role VARCHAR(20) NOT NULL COMMENT "role",
    create_time DATETIME NOT NULL DEFAULT NOW() COMMENT 'when the user is created',
    create_by VARCHAR(255) NOT NULL COMMENT 'who created this user',
    is_disabled INT NOT NULL DEFAULT 0 COMMENT 'whether the user is disabled, 0-normal, 1-disabled',
    update_time DATETIME COMMENT 'when the user is updated',
    update_by VARCHAR(255) COMMENT 'who updated this user'
) ENGINE=InnoDB COMMENT 'user';

CREATE TABLE IF NOT EXISTS access_log (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'primary key',
    access_time TIMESTAMP NOT NULL DEFAULT NOW() COMMENT 'when the user signed in',
    ip_address VARCHAR(255) NOT NULL COMMENT 'ip address',
    username VARCHAR(255) NOT NULL COMMENT 'username',
    user_id INT UNSIGNED NOT NULL COMMENT 'primary key of user'
) ENGINE=InnoDB COMMENT 'access log';

-- for upgrading
-- ALTER TABLE user ADD COLUMN role VARCHAR(20) NOT NULL COMMENT "role"

-- for upgrading 19/06/2021
ALTER TABLE user MODIFY COLUMN username VARCHAR(255) NOT NULL UNIQUE COMMENT "username (must be unique)";
ALTER TABLE user ADD COLUMN create_time DATETIME NOT NULL DEFAULT NOW() COMMENT 'when the user is created';
ALTER TABLE user ADD COLUMN create_by VARCHAR(255) NOT NULL COMMENT 'who created this user';
ALTER TABLE user ADD COLUMN is_disabled INT NOT NULL DEFAULT 0 COMMENT 'whether the user is disabled, 0-normal, 1-disabled';
ALTER TABLE user ADD COLUMN disable_time DATETIME COMMENT 'the date when the user is disabled';
ALTER TABLE user ADD COLUMN disable_by VARCHAR(255) COMMENT 'who disable this user';

-- for upgrading 24/06/2021
ALTER TABLE user RENAME COLUMN disable_time TO update_time;
ALTER TABLE user RENAME COLUMN disable_by TO update_by;
ALTER TABLE user MODIFY COLUMN update_time DATETIME COMMENT 'when the user is updated';
ALTER TABLE user MODIFY COLUMN update_by VARCHAR(255) COMMENT 'who updated this user';

