CREATE TABLE short_urls (
    id uuid NOT NULL PRIMARY KEY,
    original_url varchar(2048) NOT NULL,
    short_key varchar(20) NOT NULL UNIQUE,
    created_at timestamptz NOT NULL DEFAULT now(),
    expires_at timestamptz
)