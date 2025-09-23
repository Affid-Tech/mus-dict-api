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
    city_id          UUID NOT NULL REFERENCES city (id) ON DELETE RESTRICT,
    coordinates      GEOGRAPHY(Point, 4326),
    readable_address TEXT NOT NULL
);
-- Spatial index for coordinates
CREATE INDEX IF NOT EXISTS idx_address_coordinates_gist ON address USING GIST (coordinates);

-- Location
CREATE TABLE location
(
    id          UUID PRIMARY KEY,
    name        TEXT NOT NULL,
    cover       TEXT,
    address_id  UUID NOT NULL REFERENCES address (id) ON DELETE RESTRICT,
    description TEXT,
    contacts    TEXT
);

-- Opening Hours
CREATE TABLE opening_hours
(
    id          UUID PRIMARY KEY,
    location_id UUID    NOT NULL REFERENCES location (id) ON DELETE CASCADE,
    day_of_week INTEGER NOT NULL CHECK (day_of_week BETWEEN 0 AND 6), -- 0 = Monday
    open_time   TIME    NOT NULL,
    close_time  TIME    NOT NULL,
    CHECK (open_time < close_time)
);
-- (Optional) prevent duplicates of identical intervals
CREATE UNIQUE INDEX IF NOT EXISTS ux_opening_hours_loc_day_open_close
    ON opening_hours (location_id, day_of_week, open_time, close_time);

-- Profiles (attachable capabilities)
CREATE TABLE rental_profile
(
    location_id UUID PRIMARY KEY REFERENCES location (id) ON DELETE CASCADE
);

CREATE TABLE studio_profile
(
    location_id UUID PRIMARY KEY REFERENCES location (id) ON DELETE CASCADE
);

CREATE TABLE rehearsal_base_profile
(
    location_id UUID PRIMARY KEY REFERENCES location (id) ON DELETE CASCADE
);

CREATE TABLE concert_venue_profile
(
    location_id UUID PRIMARY KEY REFERENCES location (id) ON DELETE CASCADE,
    capacity    INTEGER,
    terms       TEXT
);

-- Equipment catalog
CREATE TABLE equipment
(
    id          UUID PRIMARY KEY,
    name        TEXT NOT NULL,
    cover       TEXT,
    description TEXT
);

-- Equipment links (Concert Venue)
CREATE TABLE concert_venue_equipment
(
    venue_id     UUID    NOT NULL REFERENCES concert_venue_profile (location_id) ON DELETE CASCADE,
    equipment_id UUID    NOT NULL REFERENCES equipment (id) ON DELETE RESTRICT,
    quantity     INTEGER NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (venue_id, equipment_id)
);

-- Equipment links (Rental)
CREATE TABLE rental_equipment
(
    rental_id    UUID    NOT NULL REFERENCES rental_profile (location_id) ON DELETE CASCADE,
    equipment_id UUID    NOT NULL REFERENCES equipment (id) ON DELETE RESTRICT,
    quantity     INTEGER NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (rental_id, equipment_id)
);
