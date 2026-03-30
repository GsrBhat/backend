-- Sample Doctors (Password: password)
DELETE FROM users;
INSERT INTO users (name, email, password, role, specialization, rating) VALUES 
('Dr. Smith', 'smith@hospital.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'DOCTOR', 'Cardiology', 4.8),
('Dr. Johnson', 'johnson@hospital.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'DOCTOR', 'Dermatology', 4.5),
('Dr. Williams', 'williams@hospital.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'DOCTOR', 'Neurology', 4.9),
('Dr. Brown', 'brown@hospital.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'DOCTOR', 'Pediatrics', 4.7),
('Dr. Jones', 'jones@hospital.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'DOCTOR', 'Orthopedics', 4.6),
('Dr. Garcia', 'garcia@hospital.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'DOCTOR', 'Cardiology', 4.4),
('Dr. Miller', 'miller@hospital.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'DOCTOR', 'General Medicine', 4.3),
('Dr. Davis', 'davis@hospital.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'DOCTOR', 'Dermatology', 4.2),
('Dr. Rodriguez', 'rodriguez@hospital.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'DOCTOR', 'Neurology', 4.7),
('Dr. Martinez', 'martinez@hospital.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'DOCTOR', 'Pediatrics', 4.5),
('Dr. Hernandez', 'hernandez@hospital.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'DOCTOR', 'Orthopedics', 4.8),
('Dr. Lopez', 'lopez@hospital.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'DOCTOR', 'General Medicine', 4.1),
('Dr. Gonzalez', 'gonzalez@hospital.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'DOCTOR', 'Cardiology', 4.6),
('Dr. Wilson', 'wilson@hospital.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'DOCTOR', 'Dermatology', 4.4),
('Dr. Anderson', 'anderson@hospital.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'DOCTOR', 'Neurology', 4.9);

-- Admin User (Password: admin123)
-- bcrypt hash for 'admin123': $2a$10$vY08Z7PZ8fGzR.7yQ5p5.O/3.J3.J3.J3.J3.J3.J3.J3.J3.J3.J3. (just using 'password' for all for simplicity in demo)
INSERT INTO users (name, email, password, role) VALUES 
('Admin User', 'admin@hospital.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'ADMIN');

-- Patient User (Password: password)
INSERT INTO users (name, email, password, role) VALUES 
('John Doe', 'john@gmail.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'PATIENT');
