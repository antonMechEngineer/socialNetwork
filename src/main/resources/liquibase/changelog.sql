--liquibase formatted sql
--change set ANN: v1
-- Table: public.block_history

-- DROP TABLE IF EXISTS public.block_history;

CREATE TABLE IF NOT EXISTS public.block_history
(
    id bigint NOT NULL DEFAULT nextval('block_history_id_seq'::regclass),
    action character varying(255) COLLATE pg_catalog."default",
    comment_id bigint,
    post_id bigint,
    "time" timestamp without time zone,
    person_id bigint,
    CONSTRAINT block_history_pkey PRIMARY KEY (id),
    CONSTRAINT fkh19qwit0k2uus76nwmhnw7yu6 FOREIGN KEY (person_id)
    REFERENCES public.person (id) MATCH SIMPLE
                     ON UPDATE NO ACTION
                     ON DELETE NO ACTION
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.block_history
    OWNER to postgres;

-- Table: public.captcha

-- DROP TABLE IF EXISTS public.captcha;

CREATE TABLE IF NOT EXISTS public.captcha
(
    id bigint NOT NULL DEFAULT nextval('captcha_id_seq'::regclass),
    code character varying(255) COLLATE pg_catalog."default",
    secret_code character varying(255) COLLATE pg_catalog."default",
    "time" timestamp without time zone,
    CONSTRAINT captcha_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.captcha
    OWNER to postgres;

-- Table: public.city

-- DROP TABLE IF EXISTS public.city;

CREATE TABLE IF NOT EXISTS public.city
(
    id bigint NOT NULL DEFAULT nextval('city_id_seq'::regclass),
    clouds character varying(255) COLLATE pg_catalog."default",
    country_id bigint,
    temp character varying(255) COLLATE pg_catalog."default",
    title character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT city_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.city
    OWNER to postgres;

-- Table: public.country

-- DROP TABLE IF EXISTS public.country;

CREATE TABLE IF NOT EXISTS public.country
(
    id bigint NOT NULL DEFAULT nextval('country_id_seq'::regclass),
    title character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT country_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.country
    OWNER to postgres;

-- Table: public.currency

-- DROP TABLE IF EXISTS public.currency;

CREATE TABLE IF NOT EXISTS public.currency
(
    id bigint NOT NULL DEFAULT nextval('currency_id_seq'::regclass),
    name character varying(255) COLLATE pg_catalog."default",
    price character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT currency_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.currency
    OWNER to postgres;

-- Table: public.dialog

-- DROP TABLE IF EXISTS public.dialog;

CREATE TABLE IF NOT EXISTS public.dialog
(
    id bigint NOT NULL DEFAULT nextval('dialog_id_seq'::regclass),
    last_time_active timestamp without time zone,
    first_person_id bigint,
    last_message_id bigint,
    second_person_id bigint,
    CONSTRAINT dialog_pkey PRIMARY KEY (id),
    CONSTRAINT fk2m0h0riawl715ida31n0bumvl FOREIGN KEY (first_person_id)
        REFERENCES public.person (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkdwu7e3c4y8ifrc6myy3mikxyg FOREIGN KEY (second_person_id)
        REFERENCES public.person (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkhuqu5i4t2ns0jqfj951v0yl5n FOREIGN KEY (last_message_id)
        REFERENCES public.message (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.dialog
    OWNER to postgres;

-- Table: public.friendship

-- DROP TABLE IF EXISTS public.friendship;

CREATE TABLE IF NOT EXISTS public.friendship
(
    id bigint NOT NULL DEFAULT nextval('friendship_id_seq'::regclass),
    sent_time timestamp without time zone,
    dst_person_id bigint,
    status_id bigint,
    src_person_id bigint,
    CONSTRAINT friendship_pkey PRIMARY KEY (id),
    CONSTRAINT fk3hgcbqvacdpytapoifp4vp94l FOREIGN KEY (src_person_id)
        REFERENCES public.person (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fka2qymhjgi1g6sqt7drxep9oyl FOREIGN KEY (dst_person_id)
        REFERENCES public.person (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fksj4u1nwtnv5hhiugv2jru445e FOREIGN KEY (status_id)
        REFERENCES public.friendship_status (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.friendship
    OWNER to postgres;

-- Table: public.friendship_status

-- DROP TABLE IF EXISTS public.friendship_status;

CREATE TABLE IF NOT EXISTS public.friendship_status
(
    id bigint NOT NULL DEFAULT nextval('friendship_status_id_seq'::regclass),
    code character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    "time" timestamp without time zone,
    CONSTRAINT friendship_status_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.friendship_status
    OWNER to postgres;

-- Table: public.message

-- DROP TABLE IF EXISTS public.message;

CREATE TABLE IF NOT EXISTS public.message
(
    id bigint NOT NULL DEFAULT nextval('message_id_seq'::regclass),
    is_deleted boolean,
    message_text character varying(10000) COLLATE pg_catalog."default",
    read_status character varying(255) COLLATE pg_catalog."default",
    "time" timestamp without time zone,
    author_id bigint,
    dialog_id bigint,
    recipient_id bigint,
    CONSTRAINT message_pkey PRIMARY KEY (id),
    CONSTRAINT fk4w1d00yruji96uisih4ujagd1 FOREIGN KEY (dialog_id)
        REFERENCES public.dialog (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk95ioxv5jboo88gsflm6m5o945 FOREIGN KEY (author_id)
        REFERENCES public.person (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkkxjacbuilanixymiq1kae6od2 FOREIGN KEY (recipient_id)
        REFERENCES public.person (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.message
    OWNER to postgres;

-- Table: public.notification

-- DROP TABLE IF EXISTS public.notification;

CREATE TABLE IF NOT EXISTS public.notification
(
    id bigint NOT NULL DEFAULT nextval('notification_id_seq'::regclass),
    contact character varying(255) COLLATE pg_catalog."default",
    entity_id bigint,
    is_read boolean,
    notification_type character varying(255) COLLATE pg_catalog."default",
    sent_time timestamp without time zone,
    person_id bigint,
    CONSTRAINT notification_pkey PRIMARY KEY (id),
    CONSTRAINT fkp5l9v0ndx4i4a7lx14o2fck FOREIGN KEY (person_id)
        REFERENCES public.person (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.notification
    OWNER to postgres;

-- Table: public.person

-- DROP TABLE IF EXISTS public.person;

CREATE TABLE IF NOT EXISTS public.person
(
    id bigint NOT NULL DEFAULT nextval('person_id_seq'::regclass),
    about character varying(255) COLLATE pg_catalog."default",
    birth_date timestamp without time zone,
    change_password_token character varying(255) COLLATE pg_catalog."default",
    city character varying(255) COLLATE pg_catalog."default",
    confirmation_code bigint,
    country character varying(255) COLLATE pg_catalog."default",
    deleted_time timestamp without time zone,
    email character varying(255) COLLATE pg_catalog."default",
    first_name character varying(255) COLLATE pg_catalog."default",
    is_approved boolean,
    is_blocked boolean,
    is_deleted boolean,
    last_name character varying(255) COLLATE pg_catalog."default",
    last_online_time timestamp without time zone,
    message_permission character varying(255) COLLATE pg_catalog."default",
    notifications_session_id character varying(255) COLLATE pg_catalog."default",
    online_status character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    phone character varying(255) COLLATE pg_catalog."default",
    photo character varying(255) COLLATE pg_catalog."default",
    reg_date timestamp without time zone,
    CONSTRAINT person_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.person
    OWNER to postgres;

-- Table: public.person_settings

-- DROP TABLE IF EXISTS public.person_settings;

CREATE TABLE IF NOT EXISTS public.person_settings
(
    id bigint NOT NULL DEFAULT nextval('person_settings_id_seq'::regclass),
    comment_comment_notification boolean,
    friend_birthday_notification boolean,
    friend_request_notification boolean,
    like_notification boolean,
    message_notification boolean,
    post_comment_notification boolean,
    post_notification boolean,
    person_id bigint,
    CONSTRAINT person_settings_pkey PRIMARY KEY (id),
    CONSTRAINT fkrp8dfgw6jb6sgl11ok8w4t2xn FOREIGN KEY (person_id)
        REFERENCES public.person (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.person_settings
    OWNER to postgres;

-- Table: public.post

-- DROP TABLE IF EXISTS public.post;

CREATE TABLE IF NOT EXISTS public.post
(
    id bigint NOT NULL DEFAULT nextval('post_id_seq'::regclass),
    is_blocked boolean,
    is_deleted boolean,
    post_text character varying(10000) COLLATE pg_catalog."default",
    "time" timestamp without time zone,
    time_delete timestamp without time zone,
    title character varying(255) COLLATE pg_catalog."default",
    author_id bigint,
    CONSTRAINT post_pkey PRIMARY KEY (id),
    CONSTRAINT fkbafujapncubfpx85cmv9wrtwg FOREIGN KEY (author_id)
        REFERENCES public.person (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.post
    OWNER to postgres;

-- Table: public.post2tag

-- DROP TABLE IF EXISTS public.post2tag;

CREATE TABLE IF NOT EXISTS public.post2tag
(
    id bigint NOT NULL DEFAULT nextval('post2tag_id_seq'::regclass),
    post_id bigint,
    tag_id bigint,
    CONSTRAINT post2tag_pkey PRIMARY KEY (id),
    CONSTRAINT fk6j2st4ahgm1pd0xqwsyj62r59 FOREIGN KEY (tag_id)
        REFERENCES public.tag (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.post2tag
    OWNER to postgres;

-- Table: public.post_comment

-- DROP TABLE IF EXISTS public.post_comment;

CREATE TABLE IF NOT EXISTS public.post_comment
(
    id bigint NOT NULL DEFAULT nextval('post_comment_id_seq'::regclass),
    password character varying(255) COLLATE pg_catalog."default",
    "time" timestamp without time zone,
    type character varying(255) COLLATE pg_catalog."default",
    author_id bigint,
    post_id bigint,
    parent_id bigint,
    CONSTRAINT post_comment_pkey PRIMARY KEY (id),
    CONSTRAINT fkkgjq8oc8mi5nv8lthnwtu3n8n FOREIGN KEY (author_id)
        REFERENCES public.person (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkmqxhu8q0j94rcly3yxlv0u498 FOREIGN KEY (parent_id)
        REFERENCES public.post_comment (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkna4y825fdc5hw8aow65ijexm0 FOREIGN KEY (post_id)
        REFERENCES public.post (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.post_comment
    OWNER to postgres;

-- Table: public.post_file

-- DROP TABLE IF EXISTS public.post_file;

CREATE TABLE IF NOT EXISTS public.post_file
(
    id bigint NOT NULL DEFAULT nextval('post_file_id_seq'::regclass),
    name bigint NOT NULL,
    path bigint NOT NULL,
    post_id bigint,
    CONSTRAINT post_file_pkey PRIMARY KEY (id),
    CONSTRAINT fkn75omflablcagq3jsuoognqwy FOREIGN KEY (post_id)
        REFERENCES public.post (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.post_file
    OWNER to postgres;

-- Table: public.post_like

-- DROP TABLE IF EXISTS public.post_like;

CREATE TABLE IF NOT EXISTS public.post_like
(
    id bigint NOT NULL DEFAULT nextval('post_like_id_seq'::regclass),
    post_id bigint,
    "time" timestamp without time zone,
    type character varying(255) COLLATE pg_catalog."default",
    person_id bigint,
    CONSTRAINT post_like_pkey PRIMARY KEY (id),
    CONSTRAINT fk3sw1w4okji2yfol7u009an0by FOREIGN KEY (person_id)
        REFERENCES public.person (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.post_like
    OWNER to postgres;

-- Table: public.tag

-- DROP TABLE IF EXISTS public.tag;

CREATE TABLE IF NOT EXISTS public.tag
(
    id bigint NOT NULL DEFAULT nextval('tag_id_seq'::regclass),
    tag character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT tag_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.tag
    OWNER to postgres;
