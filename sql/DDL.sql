CREATE TABLE Member (
    member_id       SERIAL          PRIMARY KEY,
    first_name      VARCHAR(50)     NOT NULL,
    last_name       VARCHAR(50)     NOT NULL,
    email           VARCHAR(100)    UNIQUE NOT NULL,
    date_of_birth   DATE            NOT NULL,
    gender          VARCHAR(10),
    phone_number    VARCHAR(15)
);

CREATE TABLE Trainer (
    trainer_id      SERIAL          PRIMARY KEY,
    first_name      VARCHAR(50)     NOT NULL,
    last_name       VARCHAR(50)     NOT NULL,
    phone_number    VARCHAR(15)
);

CREATE TABLE Admin (
    admin_id        SERIAL          PRIMARY KEY,
    password        VARCHAR(50)     NOT NULL
);

CREATE TABLE Room (
    room_id         SERIAL          PRIMARY KEY,
    capacity        INT
);

CREATE TABLE Equipment (
    equipment_id    SERIAL          PRIMARY KEY,
    room_id         INT             NOT NULL,
    equipment_name  VARCHAR(100),
    status          VARCHAR(20),
    updater_id      INT,                         -- admin_id who logged equipment status
    CONSTRAINT fk_room_equipment
        FOREIGN KEY (room_id)
        REFERENCES Room (room_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_updater
        FOREIGN KEY (updater_id)
        REFERENCES Admin (admin_id)
        ON DELETE SET NULL
);

CREATE TABLE Session (
    session_id      SERIAL          PRIMARY KEY,
    session_type    VARCHAR(50)     NOT NULL,
    session_date    DATE            NOT NULL,
    session_time    TIME            NOT NULL,
    room_id         INT             NOT NULL,
    trainer_id      INT             NOT NULL,
    capacity        INT             DEFAULT 1,
    CONSTRAINT fk_room
        FOREIGN KEY (room_id)
        REFERENCES Room (room_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_trainer
        FOREIGN KEY (trainer_id)
        REFERENCES Trainer (trainer_id)
        ON DELETE CASCADE
);

CREATE TABLE Booking (
    booking_id      SERIAL          PRIMARY KEY,
    session_id      INT             NOT NULL,
    member_id       INT             NOT NULL,
    CONSTRAINT fk_session_booking
        FOREIGN KEY (session_id)
        REFERENCES Session (session_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_member_booking
        FOREIGN KEY (member_id)
        REFERENCES Member (member_id)
        ON DELETE CASCADE,
    CONSTRAINT unique_booking
        UNIQUE (session_id, member_id)
);

CREATE TABLE HealthHistory (
    health_history_id   SERIAL      PRIMARY KEY,
    member_id           INT         NOT NULL,
    weight              DECIMAL(5,2),
    heart_rate          INT,
    timestamp           TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_member_healthhistory
        FOREIGN KEY (member_id)
        REFERENCES Member (member_id)
        ON DELETE CASCADE
);

CREATE TABLE FitnessGoal (
    goal_id         SERIAL          PRIMARY KEY,
    member_id       INT             NOT NULL,
    goal_info       VARCHAR(50),
    start_date      DATE            NOT NULL,
    end_date        DATE            NOT NULL,
    CONSTRAINT fk_member_fitnessgoal
        FOREIGN KEY (member_id)
        REFERENCES Member (member_id)
        ON DELETE CASCADE
);

CREATE TABLE Availability (
    availability_id SERIAL          PRIMARY KEY,
    trainer_id      INT             NOT NULL,
    start_time      TIME            NOT NULL,
    end_time        TIME            NOT NULL,
    CONSTRAINT fk_trainer_availability
        FOREIGN KEY (trainer_id)
        REFERENCES Trainer (trainer_id)
        ON DELETE CASCADE
);

-- Index
CREATE INDEX ind_member_email           ON  Member (email);

-- View for all bookings with member names and session details
CREATE VIEW view_booking_details AS
SELECT 
    b.booking_id,
    m.first_name            AS member_first_name,
    m.last_name             AS member_last_name,
    s.session_type,
    s.session_date,
    s.session_time,
    r.capacity              AS room_capacity,
    t.first_name            AS trainer_first_name,
    t.last_name             AS trainer_last_name
FROM 
    Booking b
JOIN 
    Member m        ON b.member_id = m.member_id
JOIN 
    Session s       ON b.session_id = s.session_id
JOIN 
    Room r          ON s.room_id = r.room_id
JOIN 
    Trainer t       ON s.trainer_id = t.trainer_id;

-- Availability function based on a new session
CREATE OR REPLACE FUNCTION insert_trainer_availability()
RETURNS TRIGGER AS $$
DECLARE
    trainer_start_time TIME;
    trainer_end_time   TIME;
BEGIN
    trainer_start_time := NEW.session_time;
    trainer_end_time := (NEW.session_time + interval '2.5 hour')::time;

    INSERT INTO Availability (trainer_id, start_time, end_time)
    VALUES (NEW.trainer_id, trainer_start_time, trainer_end_time);

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for the above function
CREATE TRIGGER trg_insert_availability
    AFTER INSERT ON Session
    FOR EACH ROW
EXECUTE FUNCTION insert_trainer_availability();
