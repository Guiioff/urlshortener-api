CREATE TABLE url_access_analytics (
    id uuid NOT NULL PRIMARY KEY,
    short_url_id UUID NOT NULL,
    user_agent TEXT NOT NULL,
    referrer VARCHAR(512),
    accessed_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_url_access_analytics_short_urls
        FOREIGN KEY (short_url_id) REFERENCES short_urls(id)
)