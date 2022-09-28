create table if not exists shortened_urls (
    id text primary key,
    title text not null,
    original_url text not null,
    created_at timestamp not null default now()
);