CREATE TABLE account (
  account_id BIGINT(20) 		    NOT NULL UNIQUE,
  family_id BIGINT(20) 			    DEFAULT NULL,
  role varchar(20) 			        DEFAULT "USER",
  first_name varchar(30)		    NOT NULL,
  last_name varchar(30) 		    NOT NULL,
  email varchar(75) 			    NOT NULL UNIQUE,
  phone_number varchar(12),
  password text 			        NOT NULL,
  date_of_registration timestamp	NOT NULL,
  enabled tinyint 			        NOT NULL DEFAULT 0,

  PRIMARY KEY (account_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE family (
  family_id BIGINT(20) 			    NOT NULL UNIQUE,
  owner_id BIGINT(20)			    NOT NULL UNIQUE,
  budget_id BIGINT(20)			    UNIQUE,
  title varchar(50) 			    NOT NULL,
  max_members INT 			        DEFAULT 5,
  email_notif_enabled TINYINT(1) 	DEFAULT 0,
  phone_notif_enabled TINYINT(1) 	DEFAULT 0,

  PRIMARY KEY (family_id),
  FOREIGN KEY (owner_id) REFERENCES account(account_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE account ADD CONSTRAINT FK_family_id FOREIGN KEY (family_id) REFERENCES family(family_id) ON DELETE SET NULL;

CREATE TABLE budget (
  budget_id BIGINT(20) 			NOT NULL UNIQUE,
  family_id BIGINT(20) 			NOT NULL UNIQUE,
  max_jars INT 				    DEFAULT 5,

  PRIMARY KEY (budget_id),
  FOREIGN KEY (family_id) REFERENCES family(family_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE jar (
  jar_id BIGINT(20) 			NOT NULL UNIQUE,
  budget_id BIGINT(20) 			NOT NULL,
  jar_name varchar(50) 			NOT NULL,
  current_amount INT		    NOT NULL DEFAULT 0,
  capacity INT				    NOT NULL,
  status varchar(30)            NOT NULL,

  PRIMARY KEY (jar_id),
  FOREIGN KEY (budget_id) REFERENCES budget(budget_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE new_password (
  account_id BIGINT(20) 		NOT NULL UNIQUE,
  new_password text			    NOT NULL,
  apply_time timestamp			NOT NULL,
  reset_code text			    NOT NULL,

  FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE activation_code (
  activation_code_id BIGINT(20)		NOT NULL UNIQUE,
  account_id  BIGINT(20)		    NOT NULL,
  activation_code text			    NOT NULL,
  creation_time timestamp		    NOT NULL,

  PRIMARY KEY (activation_code_id),
  FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE family_invitation (
  invitation_id BIGINT(20) 		NOT NULL UNIQUE,
  family_id BIGINT(20) 			NOT NULL,
  email varchar(75) 			NOT NULL,
  apply_time timestamp			NOT NULL,
  new_user tinyint			    NOT NULL DEFAULT 0,
  invitation_code text,

  PRIMARY KEY (invitation_id),
  FOREIGN KEY (family_id) REFERENCES family(family_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE VIEW family_members AS
	SELECT family.family_id, family.max_members, COUNT(account.family_id) as members_amount from family
	JOIN account ON family.family_id = account.family_id
        GROUP BY family.family_id;
/*
	SELECT family.family_id,
			family.max_members,
			COUNT(account.family_id) as members_amount,
			(members_amount-max_members) as free_slots
			from family
	JOIN account ON family.family_id = account.family_id
	GROUP BY account.family_id;

Select (max_members-members_amount) as free_slots from family_members
 where family_id =1;*/
