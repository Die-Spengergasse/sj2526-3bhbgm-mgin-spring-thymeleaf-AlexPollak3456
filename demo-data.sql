-- sql
-- Datei: demo-data.sql
-- Fügt Demo-Daten für MSSQL hinzu. Passen Sie Tabellennamen/Spalten bei Bedarf an.

SET NOCOUNT ON;

-- Patienten
IF NOT EXISTS (SELECT 1 FROM sys.tables WHERE name = 'patient')
    PRINT 'Tabelle patient nicht gefunden — bitte Tabellennamen anpassen.';

IF EXISTS (SELECT 1 FROM sys.tables WHERE name = 'patient')
BEGIN
    IF NOT EXISTS (SELECT 1 FROM patient WHERE id = 1)
BEGIN
        SET IDENTITY_INSERT dbo.patient ON;
INSERT INTO patient (id, first_name, last_name, birth_date, email)
VALUES
    (1, 'Anna', 'Müller', '1985-04-12', 'anna.mueller@example.com'),
    (2, 'Max', 'Schmidt', '1990-09-01', 'max.schmidt@example.com'),
    (3, 'Lea', 'Fischer', '1978-11-22', 'lea.fischer@example.com');
SET IDENTITY_INSERT dbo.patient OFF;
END
END

-- Geräte / Devices
IF EXISTS (SELECT 1 FROM sys.tables WHERE name = 'device')
BEGIN
    IF NOT EXISTS (SELECT 1 FROM device WHERE id = 1)
BEGIN
        SET IDENTITY_INSERT dbo.device ON;
INSERT INTO device (id, name, serial_number, description)
VALUES
    (1, 'MRI Scanner A', 'SN-MRI-001', '3T MRI Gerät im Erdgeschoss'),
    (2, 'CT Scanner B', 'SN-CT-002', '64-Slice CT'),
    (3, 'Ultraschall C', 'SN-US-003', 'Doppler Ultraschall');
SET IDENTITY_INSERT dbo.device OFF;
END
END

-- Reservierungen (reservations)
-- Spalten: id, start_date_time, end_date_time, patient_id, device_id, body_region
IF EXISTS (SELECT 1 FROM sys.tables WHERE name = 'reservation')
BEGIN
    IF NOT EXISTS (SELECT 1 FROM reservation WHERE id = 1)
BEGIN
        SET IDENTITY_INSERT dbo.reservation ON;
INSERT INTO reservation (id, start_date_time, end_date_time, patient_id, device_id, body_region)
VALUES
    (1, '2026-05-04T09:00:00', '2026-05-04T09:30:00', 1, 1, 'HEAD'),
    (2, '2026-05-04T10:00:00', '2026-05-04T10:45:00', 2, 2, 'CHEST'),
    (3, '2026-05-04T11:00:00', '2026-05-04T11:30:00', 3, 3, 'ABDOMEN'),
    (4, '2026-05-05T09:00:00', '2026-05-05T09:30:00', 1, 2, 'LEG');
SET IDENTITY_INSERT dbo.reservation OFF;
END
END

PRINT 'Demo-Daten eingefügt (sofern Tabellen vorhanden und Einträge noch nicht vorhanden).';
