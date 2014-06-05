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
-- Name: mdl_enrol; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mdl_enrol (
    id bigint NOT NULL,
    enrol character varying(20) DEFAULT ''::character varying NOT NULL,
    status bigint DEFAULT 0 NOT NULL,
    courseid bigint NOT NULL,
    sortorder bigint DEFAULT 0 NOT NULL,
    name character varying(255),
    enrolperiod bigint DEFAULT 0,
    enrolstartdate bigint DEFAULT 0,
    enrolenddate bigint DEFAULT 0,
    expirynotify smallint DEFAULT 0,
    expirythreshold bigint DEFAULT 0,
    notifyall smallint DEFAULT 0,
    password character varying(50),
    cost character varying(20),
    currency character varying(3),
    roleid bigint DEFAULT 0,
    customint1 bigint,
    customint2 bigint,
    customint3 bigint,
    customint4 bigint,
    customint5 bigint,
    customint6 bigint,
    customint7 bigint,
    customint8 bigint,
    customchar1 character varying(255),
    customchar2 character varying(255),
    customchar3 character varying(1333),
    customdec1 numeric(12,7),
    customdec2 numeric(12,7),
    customtext1 text,
    customtext2 text,
    customtext3 text,
    customtext4 text,
    timecreated bigint DEFAULT 0 NOT NULL,
    timemodified bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.mdl_enrol OWNER TO postgres;

--
-- Name: TABLE mdl_enrol; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE mdl_enrol IS 'Instances of enrolment plugins used in courses, fields marked as custom have a plugin defined meaning, core does not touch them. Create a new linked table if you need even more custom fields.';


--
-- Name: mdl_enrol_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE mdl_enrol_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.mdl_enrol_id_seq OWNER TO postgres;

--
-- Name: mdl_enrol_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE mdl_enrol_id_seq OWNED BY mdl_enrol.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mdl_enrol ALTER COLUMN id SET DEFAULT nextval('mdl_enrol_id_seq'::regclass);


--
-- Data for Name: mdl_enrol; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mdl_enrol (id, enrol, status, courseid, sortorder, name, enrolperiod, enrolstartdate, enrolenddate, expirynotify, expirythreshold, notifyall, password, cost, currency, roleid, customint1, customint2, customint3, customint4, customint5, customint6, customint7, customint8, customchar1, customchar2, customchar3, customdec1, customdec2, customtext1, customtext2, customtext3, customtext4, timecreated, timemodified) FROM stdin;
1	manual	0	2	0	\N	0	0	0	0	86400	0	\N	\N	\N	5	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	1386689816	1386689816
2	guest	1	2	1	\N	0	0	0	0	0	0		\N	\N	0	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	1386689816	1386689816
3	self	1	2	2	\N	0	0	0	0	86400	0	\N	\N	\N	5	0	0	0	1	0	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	1386689816	1386689816
\.


--
-- Name: mdl_enrol_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('mdl_enrol_id_seq', 3, true);


--
-- Name: mdl_enro_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mdl_enrol
    ADD CONSTRAINT mdl_enro_id_pk PRIMARY KEY (id);


--
-- Name: mdl_enro_cou_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_enro_cou_ix ON mdl_enrol USING btree (courseid);


--
-- Name: mdl_enro_enr_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_enro_enr_ix ON mdl_enrol USING btree (enrol);


--
-- PostgreSQL database dump complete
--

