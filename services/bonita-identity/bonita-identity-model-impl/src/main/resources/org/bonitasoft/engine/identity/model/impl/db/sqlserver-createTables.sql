CREATE TABLE group_ (
  tenantid NUMERIC(19, 0) NOT NULL,
  id NUMERIC(19, 0) NOT NULL,
  name VARCHAR(50) NOT NULL,
  parentPath VARCHAR(50),
  displayName VARCHAR(75),
  description VARCHAR(MAX),
  iconName VARCHAR(50),
  iconPath VARCHAR(50),
  createdBy NUMERIC(19, 0),
  creationDate NUMERIC(19, 0),
  lastUpdate NUMERIC(19, 0),
  UNIQUE (tenantid, parentPath, name),
  PRIMARY KEY (tenantid, id)
)
GO

CREATE TABLE role (
  tenantid NUMERIC(19, 0) NOT NULL,
  id NUMERIC(19, 0) NOT NULL,
  name VARCHAR(50) NOT NULL,
  displayName VARCHAR(75),
  description VARCHAR(MAX),
  iconName VARCHAR(50),
  iconPath VARCHAR(50),
  createdBy NUMERIC(19, 0),
  creationDate NUMERIC(19, 0),
  lastUpdate NUMERIC(19, 0),
  UNIQUE (tenantid, name),
  PRIMARY KEY (tenantid, id)
)
GO

CREATE INDEX idx_role_name ON role (tenantid, name, id)
GO

CREATE TABLE user_ (
  tenantid NUMERIC(19, 0) NOT NULL,
  id NUMERIC(19, 0) NOT NULL,
  enabled BIT NOT NULL,
  userName VARCHAR(50) NOT NULL,
  password VARCHAR(60),
  firstName VARCHAR(50),
  lastName VARCHAR(50),
  title VARCHAR(50),
  jobTitle VARCHAR(50),
  managerUserId NUMERIC(19, 0),
  delegeeUserName VARCHAR(50),
  iconName VARCHAR(50),
  iconPath VARCHAR(50),
  createdBy NUMERIC(19, 0),
  creationDate NUMERIC(19, 0),
  lastUpdate NUMERIC(19, 0),
  lastConnection NUMERIC(19, 0),
  UNIQUE (tenantid, userName),
  PRIMARY KEY (tenantid, id)
)
GO

CREATE INDEX idx_user_name ON user_ (tenantid, userName, id)
GO

CREATE TABLE user_contactinfo (
  tenantid NUMERIC(19, 0) NOT NULL,
  id NUMERIC(19, 0) NOT NULL,
  userId NUMERIC(19, 0) NOT NULL,
  email VARCHAR(50),
  phone VARCHAR(50),
  mobile VARCHAR(50),
  fax VARCHAR(50),
  building VARCHAR(50),
  room VARCHAR(50),
  address VARCHAR(50),
  zipCode VARCHAR(50),
  city VARCHAR(50),
  state VARCHAR(50),
  country VARCHAR(50),
  website VARCHAR(50),
  personal BIT NOT NULL,
  UNIQUE (tenantid, userId, personal),
  PRIMARY KEY (tenantid, id)
)
GO
ALTER TABLE user_contactinfo ADD CONSTRAINT fk_contact_user FOREIGN KEY (tenantid, userId) REFERENCES user_ (tenantid, id) ON DELETE CASCADE
GO
CREATE INDEX idx_user_contactinfo ON user_contactinfo (userId, tenantid, personal, id)
GO


CREATE TABLE p_metadata_def (
  tenantid NUMERIC(19, 0) NOT NULL,
  id NUMERIC(19, 0) NOT NULL,
  name VARCHAR(50) NOT NULL,
  displayName VARCHAR(75),
  description VARCHAR(MAX),
  UNIQUE (tenantid, name),
  PRIMARY KEY (tenantid, id)
)
GO

CREATE INDEX idx_p_metadata_def_name ON p_metadata_def (name, id)
GO

CREATE TABLE p_metadata_val (
  tenantid NUMERIC(19, 0) NOT NULL,
  metadataName VARCHAR(50) NOT NULL,
  userName VARCHAR(50) NOT NULL,
  value VARCHAR(50),
  PRIMARY KEY (tenantid, metadataName, userName)
)
GO

CREATE TABLE user_membership (
  tenantid NUMERIC(19, 0) NOT NULL,
  id NUMERIC(19, 0) NOT NULL,
  userId NUMERIC(19, 0) NOT NULL,
  roleId NUMERIC(19, 0) NOT NULL,
  groupId NUMERIC(19, 0) NOT NULL,
  assignedBy NUMERIC(19, 0),
  assignedDate NUMERIC(19, 0),
  UNIQUE (tenantid, userId, roleId, groupId),
  PRIMARY KEY (tenantid, id)
)
GO
