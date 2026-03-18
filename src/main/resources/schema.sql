-- database for thesis project


CREATE TABLE roles(
                      role_id SERIAL PRIMARY KEY ,
                      role_name VARCHAR(50) NOT NULL UNIQUE
);


CREATE TABLE app_users(
                          app_user_id SERIAL PRIMARY KEY ,
                          name VARCHAR(50) NOT NULL ,
                          email VARCHAR(50) NOT NULL UNIQUE ,
                          phone VARCHAR(10) NOT NULL UNIQUE ,
                          description TEXT ,
                          role_id INT,
                          FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);


-- define enum type
CREATE TYPE ENUM_STATUS AS ENUM ('pending', 'processing', 'completed', 'cancelled');

CREATE TABLE clients(
                        client_id SERIAL PRIMARY KEY ,
                        name VARCHAR(50) NOT NULL ,
                        email VARCHAR(50) NOT NULL UNIQUE ,
                        phone VARCHAR(10) NOT NULL UNIQUE ,
                        address VARCHAR(100),
                        status ENUM_STATUS DEFAULT  'pending',
                        create_at TIMESTAMP,
                        updated_at TIMESTAMP
);

CREATE TABLE cases(
                      case_id SERIAL  PRIMARY KEY ,
                      title VARCHAR(50),
                      description TEXT,
                      status ENUM_STATUS DEFAULT 'pending',
                      start_date TIMESTAMP,
                      end_date TIMESTAMP,
                      lawyer_id INT,
                      client_id INT,
                      FOREIGN KEY (lawyer_id) REFERENCES app_users(app_user_id) ON DELETE CASCADE ,
                      FOREIGN KEY (client_id) REFERENCES clients(client_id) ON DELETE CASCADE,
                      created_at TIMESTAMP,
                      updated_at TIMESTAMP
);

CREATE TABLE tasks(
                      task_id SERIAL PRIMARY KEY ,
                      case_id INT,
                      FOREIGN KEY (case_id) REFERENCES cases(case_id) ON DELETE CASCADE,
                      lawyer_id INT,
                      FOREIGN KEY (lawyer_id) REFERENCES roles(role_id) ON DELETE CASCADE,
                      title VARCHAR(50),
                      description TEXT,
                      status ENUM_STATUS DEFAULT  'pending',
                      due_date TIMESTAMP,
                      create_at TIMESTAMP,
                      update_at TIMESTAMP
);

CREATE TABLE appointments
(
    appointment_id   SERIAL PRIMARY KEY ,
    client_id        INT,
    FOREIGN KEY (client_id) REFERENCES clients (client_id) ON DELETE CASCADE,
    case_Id          INT,
    FOREIGN KEY (case_Id) REFERENCES cases (case_id) ON DELETE CASCADE,
    appointment_date TIMESTAMP,
    location         VARCHAR(100),
    purpose          VARCHAR(255),
    status           ENUM_STATUS DEFAULT 'pending',
    create_at TIMESTAMP,
    updated_at TIMESTAMP
);

DROP TABLE app_users;



-- ==========================
-- Insert mock data
-- ==========================

-- Roles
INSERT INTO roles (role_name)
VALUES
    ('Admin'),
    ('Lawyer'),
    ('Paralegal'),
    ('Receptionist');

-- App Users
INSERT INTO app_users (name, email, phone, role_id)
VALUES
    ('Alice Johnson', 'alice@example.com', '0123456789', 1), -- Admin
    ('Brian Smith', 'brian@example.com', '0123456790', 2),  -- Lawyer
    ('Clara Lopez', 'clara@example.com', '0123456791', 2),  -- Lawyer
    ('Daniel Green', 'daniel@example.com', '0123456792', 3),-- Paralegal
    ('Eva Brown', 'eva@example.com', '0123456793', 4);      -- Receptionist

-- Clients
INSERT INTO clients (name, email, phone, address, status, create_at, updated_at)
VALUES
    ('John Doe', 'johndoe@example.com', '0987654321', '123 Main St, Cityville', 'pending', NOW(), NOW()),
    ('Jane Roe', 'janeroe@example.com', '0987654322', '456 Oak St, Townsville', 'processing', NOW(), NOW()),
    ('Michael Chan', 'michaelc@example.com', '0987654323', '789 Pine St, Metrocity', 'completed', NOW(), NOW()),
    ('Sara Kim', 'sarakim@example.com', '0987654324', '321 Maple St, Villagetown', 'pending', NOW(), NOW());

-- Cases
INSERT INTO cases (title, description, status, start_date, end_date, lawyer_id, client_id, created_at, updated_at)
VALUES
    ('Contract Dispute', 'Dispute over service contract terms.', 'processing', NOW() - INTERVAL '10 days', NULL, 2, 1, NOW(), NOW()),
    ('Divorce CaseResponse', 'Divorce filing and custody matters.', 'pending', NOW() - INTERVAL '5 days', NULL, 3, 2, NOW(), NOW()),
    ('Property Claim', 'Claim regarding disputed land ownership.', 'completed', NOW() - INTERVAL '20 days', NOW() - INTERVAL '2 days', 2, 3, NOW(), NOW()),
    ('Personal Injury', 'Car accident injury claim.', 'pending', NOW() - INTERVAL '15 days', NULL, 3, 4, NOW(), NOW());

-- Tasks
INSERT INTO tasks (case_id, lawyer_id, title, description, status, due_date, create_at, update_at)
VALUES
    (1, 2, 'Draft Contract Review', 'Prepare review of disputed contract terms.', 'processing', NOW() + INTERVAL '5 days', NOW(), NOW()),
    (1, 2, 'Client Meeting', 'Meet with client to discuss updates.', 'pending', NOW() + INTERVAL '2 days', NOW(), NOW()),
    (2, 3, 'Prepare Court Filing', 'Draft divorce papers for submission.', 'pending', NOW() + INTERVAL '7 days', NOW(), NOW()),
    (3, 2, 'Land Registry Check', 'Verify ownership with land registry.', 'completed', NOW() - INTERVAL '1 day', NOW(), NOW()),
    (4, 3, 'Collect Medical Records', 'Gather medical evidence from hospital.', 'pending', NOW() + INTERVAL '10 days', NOW(), NOW());

-- Appointments
INSERT INTO appointments (client_id, case_id, appointment_date, location, purpose, status, create_at, updated_at)
VALUES
    (1, 1, NOW() + INTERVAL '3 days', 'Law Office, Room 101', 'Discuss contract dispute updates', 'pending', NOW(), NOW()),
    (2, 2, NOW() + INTERVAL '1 week', 'Court Hall 2', 'Court appearance for divorce filing', 'processing', NOW(), NOW()),
    (3, 3, NOW() - INTERVAL '3 days', 'Law Office, Room 203', 'Finalize property claim case', 'completed', NOW(), NOW()),
    (4, 4, NOW() + INTERVAL '2 days', 'Hospital Conference Room', 'Review injury reports with lawyer', 'pending', NOW(), NOW());


ALTER TABLE app_users
ADD COLUMN description TEXT;


DROP TABLE app_users,roles,cases,clients,tasks,appointments

Select * From app_users WHERE  email = 'yy'
