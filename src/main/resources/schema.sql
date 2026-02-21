DROP TABLE IF EXISTS cities;

CREATE TABLE cities (
    id IDENTITY PRIMARY KEY,
    city VARCHAR(100) NOT NULL,
    city_ascii VARCHAR(100) NOT NULL,
    state_id CHAR(2) NOT NULL,
    state_name VARCHAR(50) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

-- Optimization: Composite index for the Bounding Box search
CREATE INDEX idx_spatial_lookup ON cities(latitude, longitude);
