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

DROP INDEX public.mdl_user_mneuse_uix;
DROP INDEX public.mdl_user_las_ix;
DROP INDEX public.mdl_user_las2_ix;
DROP INDEX public.mdl_user_idn_ix;
DROP INDEX public.mdl_user_fir_ix;
DROP INDEX public.mdl_user_ema_ix;
DROP INDEX public.mdl_user_del_ix;
DROP INDEX public.mdl_user_cou_ix;
DROP INDEX public.mdl_user_con_ix;
DROP INDEX public.mdl_user_cit_ix;
DROP INDEX public.mdl_user_aut_ix;
ALTER TABLE ONLY public.mdl_user DROP CONSTRAINT mdl_user_id_pk;
ALTER TABLE public.mdl_user ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE public.mdl_user_id_seq;
DROP TABLE public.mdl_user;
SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: mdl_user; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mdl_user (
    id bigint NOT NULL,
    auth character varying(20) DEFAULT 'manual'::character varying NOT NULL,
    confirmed smallint DEFAULT 0 NOT NULL,
    policyagreed smallint DEFAULT 0 NOT NULL,
    deleted smallint DEFAULT 0 NOT NULL,
    suspended smallint DEFAULT 0 NOT NULL,
    mnethostid bigint DEFAULT 0 NOT NULL,
    username character varying(100) DEFAULT ''::character varying NOT NULL,
    password character varying(32) DEFAULT ''::character varying NOT NULL,
    idnumber character varying(255) DEFAULT ''::character varying NOT NULL,
    firstname character varying(100) DEFAULT ''::character varying NOT NULL,
    lastname character varying(100) DEFAULT ''::character varying NOT NULL,
    email character varying(100) DEFAULT ''::character varying NOT NULL,
    emailstop smallint DEFAULT 0 NOT NULL,
    icq character varying(15) DEFAULT ''::character varying NOT NULL,
    skype character varying(50) DEFAULT ''::character varying NOT NULL,
    yahoo character varying(50) DEFAULT ''::character varying NOT NULL,
    aim character varying(50) DEFAULT ''::character varying NOT NULL,
    msn character varying(50) DEFAULT ''::character varying NOT NULL,
    phone1 character varying(20) DEFAULT ''::character varying NOT NULL,
    phone2 character varying(20) DEFAULT ''::character varying NOT NULL,
    institution character varying(40) DEFAULT ''::character varying NOT NULL,
    department character varying(30) DEFAULT ''::character varying NOT NULL,
    address character varying(70) DEFAULT ''::character varying NOT NULL,
    city character varying(120) DEFAULT ''::character varying NOT NULL,
    country character varying(2) DEFAULT ''::character varying NOT NULL,
    lang character varying(30) DEFAULT 'en'::character varying NOT NULL,
    theme character varying(50) DEFAULT ''::character varying NOT NULL,
    timezone character varying(100) DEFAULT '99'::character varying NOT NULL,
    firstaccess bigint DEFAULT 0 NOT NULL,
    lastaccess bigint DEFAULT 0 NOT NULL,
    lastlogin bigint DEFAULT 0 NOT NULL,
    currentlogin bigint DEFAULT 0 NOT NULL,
    lastip character varying(45) DEFAULT ''::character varying NOT NULL,
    secret character varying(15) DEFAULT ''::character varying NOT NULL,
    picture bigint DEFAULT 0 NOT NULL,
    url character varying(255) DEFAULT ''::character varying NOT NULL,
    description text,
    descriptionformat smallint DEFAULT 0 NOT NULL,
    mailformat smallint DEFAULT 1 NOT NULL,
    maildigest smallint DEFAULT 0 NOT NULL,
    maildisplay smallint DEFAULT 2 NOT NULL,
    htmleditor smallint DEFAULT 1 NOT NULL,
    autosubscribe smallint DEFAULT 1 NOT NULL,
    trackforums smallint DEFAULT 0 NOT NULL,
    timecreated bigint DEFAULT 0 NOT NULL,
    timemodified bigint DEFAULT 0 NOT NULL,
    trustbitmask bigint DEFAULT 0 NOT NULL,
    imagealt character varying(255)
);


ALTER TABLE public.mdl_user OWNER TO postgres;

--
-- Name: TABLE mdl_user; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE mdl_user IS 'One record for each person';


--
-- Name: mdl_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE mdl_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.mdl_user_id_seq OWNER TO postgres;

--
-- Name: mdl_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE mdl_user_id_seq OWNED BY mdl_user.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mdl_user ALTER COLUMN id SET DEFAULT nextval('mdl_user_id_seq'::regclass);


--
-- Data for Name: mdl_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mdl_user (id, auth, confirmed, policyagreed, deleted, suspended, mnethostid, username, password, idnumber, firstname, lastname, email, emailstop, icq, skype, yahoo, aim, msn, phone1, phone2, institution, department, address, city, country, lang, theme, timezone, firstaccess, lastaccess, lastlogin, currentlogin, lastip, secret, picture, url, description, descriptionformat, mailformat, maildigest, maildisplay, htmleditor, autosubscribe, trackforums, timecreated, timemodified, trustbitmask, imagealt) FROM stdin;
1	manual	1	0	0	0	1	guest	f9353abcef00bc04b5f7104d83b0992d		Gast	 	root@localhost	0													de		99	0	0	0	0			0		Der Nutzer 'Gast' hat ausschließlich Lesezugriff für Kurse, die den Gastzugriff ausdrücklich erlauben.	0	1	0	2	1	1	0	0	1386688995	0	\N
3	none	1	0	1	0	1	bla@blababaselbva.de.1386692831	e02d631a74f402a308d869b410dfeb25		Christian	Kaus	670456741df3843d1cdb47acd69293dd	0											Berlin	DE	de		99	0	0	0	0			0			1	1	0	2	1	1	0	1386691695	1386692831	0	
2	manual	1	0	0	0	1	admin	9b67464eaf416e699290c6965a99d2e2		Mr	Moodle	sascha.feldmann@gmx.de	0											Berlin	DE	de		99	1386689101	1386693424	0	1386689101	127.0.0.1		0			0	1	0	1	1	1	0	0	1386689460	0	\N
4	none	1	0	0	0	1	ckaus	9b67464eaf416e699290c6965a99d2e2		Christian	Kaus	test@groupelite.de	0											Berlin	DE	de		99	1386692970	1386693564	0	1386692970	127.0.0.1		0			1	1	0	2	1	1	0	1386692930	1386692930	0	
\.


--
-- Name: mdl_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('mdl_user_id_seq', 4, true);


--
-- Name: mdl_user_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mdl_user
    ADD CONSTRAINT mdl_user_id_pk PRIMARY KEY (id);


--
-- Name: mdl_user_aut_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_user_aut_ix ON mdl_user USING btree (auth);


--
-- Name: mdl_user_cit_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_user_cit_ix ON mdl_user USING btree (city);


--
-- Name: mdl_user_con_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_user_con_ix ON mdl_user USING btree (confirmed);


--
-- Name: mdl_user_cou_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_user_cou_ix ON mdl_user USING btree (country);


--
-- Name: mdl_user_del_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_user_del_ix ON mdl_user USING btree (deleted);


--
-- Name: mdl_user_ema_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_user_ema_ix ON mdl_user USING btree (email);


--
-- Name: mdl_user_fir_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_user_fir_ix ON mdl_user USING btree (firstname);


--
-- Name: mdl_user_idn_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_user_idn_ix ON mdl_user USING btree (idnumber);


--
-- Name: mdl_user_las2_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_user_las2_ix ON mdl_user USING btree (lastaccess);


--
-- Name: mdl_user_las_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_user_las_ix ON mdl_user USING btree (lastname);


--
-- Name: mdl_user_mneuse_uix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX mdl_user_mneuse_uix ON mdl_user USING btree (mnethostid, username);


--
-- PostgreSQL database dump complete
--

