--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: mdl_user_enrolments; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mdl_user_enrolments (
    id bigint NOT NULL,
    status bigint DEFAULT 0 NOT NULL,
    enrolid bigint NOT NULL,
    userid bigint NOT NULL,
    timestart bigint DEFAULT 0 NOT NULL,
    timeend bigint DEFAULT 2147483647 NOT NULL,
    modifierid bigint DEFAULT 0 NOT NULL,
    timecreated bigint DEFAULT 0 NOT NULL,
    timemodified bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.mdl_user_enrolments OWNER TO postgres;

--
-- Name: TABLE mdl_user_enrolments; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE mdl_user_enrolments IS 'Users participating in courses (aka enrolled users) - everybody who is participating/visible in course, that means both teachers and students';


--
-- Name: mdl_user_enrolments_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE mdl_user_enrolments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.mdl_user_enrolments_id_seq OWNER TO postgres;

--
-- Name: mdl_user_enrolments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE mdl_user_enrolments_id_seq OWNED BY mdl_user_enrolments.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mdl_user_enrolments ALTER COLUMN id SET DEFAULT nextval('mdl_user_enrolments_id_seq'::regclass);


--
-- Data for Name: mdl_user_enrolments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mdl_user_enrolments (id, status, enrolid, userid, timestart, timeend, modifierid, timecreated, timemodified) FROM stdin;
1	0	1	4	1386630000	0	2	1386693128	1386693128
2	0	1	5	1386802800	0	2	1386841981	1386841981
\.


--
-- Name: mdl_user_enrolments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('mdl_user_enrolments_id_seq', 2, true);


--
-- Name: mdl_userenro_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mdl_user_enrolments
    ADD CONSTRAINT mdl_userenro_id_pk PRIMARY KEY (id);


--
-- Name: mdl_userenro_enr_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_userenro_enr_ix ON mdl_user_enrolments USING btree (enrolid);


--
-- Name: mdl_userenro_enruse_uix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX mdl_userenro_enruse_uix ON mdl_user_enrolments USING btree (enrolid, userid);


--
-- Name: mdl_userenro_mod_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_userenro_mod_ix ON mdl_user_enrolments USING btree (modifierid);


--
-- Name: mdl_userenro_use_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_userenro_use_ix ON mdl_user_enrolments USING btree (userid);


--
-- PostgreSQL database dump complete
--

