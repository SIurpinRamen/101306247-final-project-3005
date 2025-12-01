INSERT INTO Member (first_name, last_name, email, date_of_birth, gender, phone_number)
VALUES 
    ('Luke', 'Skywalker', 'luke.skywalker@email.com', '1995-08-15', 'Male', '613-457-9012'),
    ('Han', 'Solo', 'han.solo@email.com', '1987-03-25', 'Male', '613-525-0314'),
	('Leia', 'Organa', 'leia.organa@email.com', '2000-05-08', 'Female', '613-862-3569');

INSERT INTO Trainer (first_name, last_name, phone_number)
VALUES 
    ('Obiwan', 'Kenobi', '613-339-3734'),
	('Mace', 'Windu', '613-937-3273'),
    ('Lando', 'Calrissian', '613-296-5603');

INSERT INTO Admin (password)
VALUES
	('admin1'),
	('admin2'),
	('admin3');

INSERT INTO FitnessGoal (member_id, goal_info, start_date, end_date)
VALUES 
	(1, 'Lose 10kg', '2025-01-01', '2026-06-01'),
    (2, 'Gain 5kg of muscle', '2025-02-01', '2026-08-01'),
	(3, 'Lose 15kg', '2025-01-01', '2026-02-05');

INSERT INTO HealthHistory (member_id, weight, heart_rate, timestamp)
VALUES 
    (1, 70.5, 72, '2025-03-01 08:00:00'),
    (1, 69.0, 75, '2025-04-01 08:00:00'),
    (2, 85.0, 78, '2025-03-15 09:00:00'),
    (2, 87.0, 80, '2025-04-15 09:00:00'),
    (3, 68.0, 82, '2025-03-20 10:00:00'),
    (3, 80.0, 79, '2025-07-20 10:00:00');

INSERT INTO Room (capacity)
VALUES 
    (20),
	(12),
    (10);

INSERT INTO Equipment (room_id, equipment_name, status, updater_id)
VALUES 
    (1, 'Treadmill', 'Working', 1),
	(2, 'Bench Press', 'Working', 2),
    (3, 'Dumbbells', 'Under Repair', 3);

INSERT INTO Session (session_type, session_date, session_time, room_id, trainer_id, capacity)
VALUES 
    ('Personal Training', '2026-05-01', '10:00:00', 3, 1, 1),
	('Class', '2026-05-02', '11:00:00', 1, 2, 15),
    ('Class', '2026-05-03', '15:00:00', 2, 3, 8);

INSERT INTO Booking (session_id, member_id)
VALUES 
    (1, 1),
	(2, 2),
    (2, 3);

INSERT INTO Availability (trainer_id, start_time, end_time)
VALUES 
    (1, '09:00:00', '11:00:00'),
	(2, '08:30:00', '10:30:00'),
    (3, '14:00:00', '16:00:00');
