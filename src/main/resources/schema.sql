DROP TABLE IF EXISTS users, films, mpa, genres, film_genre, film_like, user_friend CASCADE;

CREATE TABLE genres (
    id_genre int GENERATED by DEFAULT as IDENTITY PRIMARY KEY,
    name varchar(50) NOT NULL , --UNIQUE
    CONSTRAINT genre_name_not_blank CHECK name <> ''
);

CREATE TABLE mpa (
    id_mpa int GENERATED by DEFAULT as IDENTITY PRIMARY KEY,
    name varchar(50) NOT NULL ,--UNIQUE
    CONSTRAINT mpa_name_not_blank CHECK name <> ''
);

CREATE TABLE users (
    id_user int GENERATED by DEFAULT as IDENTITY PRIMARY KEY,
    login varchar(100) NOT NULL ,--UNIQUE
    name varchar(100) NOT NULL,
    email varchar(150) NOT NULL , --UNIQUE
    birthday date,
    CONSTRAINT user_login_not_blank CHECK login <> '',
    CONSTRAINT user_name_not_blank CHECK name <> '',
    CONSTRAINT user_email_not_blank CHECK email <> '',
    CONSTRAINT user_birthday_before_now CHECK birthday <= CURRENT_DATE
);

CREATE TABLE films (
    id_film int GENERATED by DEFAULT as IDENTITY PRIMARY KEY,
    name varchar(150) NOT NULL,
    description varchar(200),
    release_date date,
    duration int,
    id_mpa int NOT NULL REFERENCES mpa(id_mpa) ON DELETE CASCADE,
    CONSTRAINT release_date_after_birthday CHECK release_date > '1895-12-28',
    CONSTRAINT duration_more_0 CHECK duration > 0,
    CONSTRAINT film_name_not_blank CHECK name <> '',
    CONSTRAINT mpa_id_more_0 CHECK id_mpa > 0
);

CREATE TABLE film_genre (
    id_film int NOT NULL REFERENCES films(id_film) ON DELETE CASCADE,
    id_genre int NOT NULL REFERENCES genres(id_genre) ON DELETE CASCADE,
    PRIMARY KEY (id_film, id_genre)
    -- нужно ли проверять на > 0 ???
);

CREATE TABLE film_like (
    id_film int NOT NULL REFERENCES films(id_film) ON DELETE CASCADE,
    id_user int NOT NULL REFERENCES users(id_user) ON DELETE CASCADE,
    PRIMARY KEY (id_film, id_user)
    -- нужно ли проверять на > 0 ???
);

CREATE TABLE user_friend (
    id_user int NOT NULL REFERENCES users(id_user) ON DELETE CASCADE,
    id_friend int NOT NULL REFERENCES users(id_user) ON DELETE CASCADE,
    --friendship_status boolean,
    PRIMARY KEY (id_user, id_friend)
);