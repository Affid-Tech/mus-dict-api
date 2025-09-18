CREATE EXTENSION IF NOT EXISTS postgis;

-- City
CREATE TABLE city
(
    id   UUID PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

-- Address
CREATE TABLE address
(
    id               UUID PRIMARY KEY,
    city_id          UUID REFERENCES city (id),
    coordinates      GEOGRAPHY(Point, 4326),
    readable_address TEXT
);

-- Location
CREATE TABLE location
(
    id          UUID PRIMARY KEY,
    name        TEXT                         NOT NULL,
    cover       TEXT,
    address_id  UUID REFERENCES address (id) NOT NULL,
    description TEXT,
    contacts    TEXT
);

-- Opening Hours
CREATE TABLE opening_hours
(
    id          UUID PRIMARY KEY,
    location_id UUID REFERENCES location (id),
    day_of_week INTEGER NOT NULL CHECK (day_of_week BETWEEN 0 AND 6), -- 0 = Monday
    open_time   TIME    NOT NULL,
    close_time  TIME    NOT NULL
);


CREATE TABLE rental
(
    location_id UUID PRIMARY KEY REFERENCES location (id)
);

-- Studio
CREATE TABLE studio
(
    location_id UUID PRIMARY KEY REFERENCES location (id)
);

-- Rehearsal Base
CREATE TABLE rehearsal_base
(
    location_id UUID PRIMARY KEY REFERENCES location (id)
);

-- Concert Venue
CREATE TABLE concert_venue
(
    location_id UUID PRIMARY KEY REFERENCES location (id),
    capacity    INTEGER,
    terms       TEXT
);

-- Equipment
CREATE TABLE equipment
(
    id          UUID PRIMARY KEY,
    name        TEXT NOT NULL,
    cover       TEXT,
    description TEXT
);

CREATE TABLE concert_venue_equipment
(
    venue_id     UUID REFERENCES concert_venue (location_id),
    equipment_id UUID REFERENCES equipment (id),
    quantity     INTEGER
);

CREATE TABLE rental_equipment
(
    rental_id    UUID REFERENCES rental (location_id),
    equipment_id UUID REFERENCES equipment (id),
    quantity     INTEGER
)