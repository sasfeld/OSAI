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

DROP INDEX public.mdl_cour_sor_ix;
DROP INDEX public.mdl_cour_sho_ix;
DROP INDEX public.mdl_cour_idn_ix;
DROP INDEX public.mdl_cour_cat_ix;
ALTER TABLE ONLY public.mdl_course DROP CONSTRAINT mdl_cour_id_pk;
ALTER TABLE public.mdl_course ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE public.mdl_course_id_seq;
DROP TABLE public.mdl_course;
SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: mdl_course; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mdl_course (
    id bigint NOT NULL,
    category bigint DEFAULT 0 NOT NULL,
    sortorder bigint DEFAULT 0 NOT NULL,
    fullname character varying(254) DEFAULT ''::character varying NOT NULL,
    shortname character varying(255) DEFAULT ''::character varying NOT NULL,
    idnumber character varying(100) DEFAULT ''::character varying NOT NULL,
    summary text,
    summaryformat smallint DEFAULT 0 NOT NULL,
    format character varying(21) DEFAULT 'topics'::character varying NOT NULL,
    showgrades smallint DEFAULT 1 NOT NULL,
    sectioncache text,
    modinfo text,
    newsitems integer DEFAULT 1 NOT NULL,
    startdate bigint DEFAULT 0 NOT NULL,
    marker bigint DEFAULT 0 NOT NULL,
    maxbytes bigint DEFAULT 0 NOT NULL,
    legacyfiles smallint DEFAULT 0 NOT NULL,
    showreports smallint DEFAULT 0 NOT NULL,
    visible smallint DEFAULT 1 NOT NULL,
    visibleold smallint DEFAULT 1 NOT NULL,
    groupmode smallint DEFAULT 0 NOT NULL,
    groupmodeforce smallint DEFAULT 0 NOT NULL,
    defaultgroupingid bigint DEFAULT 0 NOT NULL,
    lang character varying(30) DEFAULT ''::character varying NOT NULL,
    theme character varying(50) DEFAULT ''::character varying NOT NULL,
    timecreated bigint DEFAULT 0 NOT NULL,
    timemodified bigint DEFAULT 0 NOT NULL,
    requested smallint DEFAULT 0 NOT NULL,
    enablecompletion smallint DEFAULT 0 NOT NULL,
    completionstartonenrol smallint DEFAULT 0 NOT NULL,
    completionnotify smallint DEFAULT 0 NOT NULL
);


ALTER TABLE public.mdl_course OWNER TO postgres;

--
-- Name: TABLE mdl_course; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE mdl_course IS 'Central course table';


--
-- Name: mdl_course_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE mdl_course_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.mdl_course_id_seq OWNER TO postgres;

--
-- Name: mdl_course_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE mdl_course_id_seq OWNED BY mdl_course.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mdl_course ALTER COLUMN id SET DEFAULT nextval('mdl_course_id_seq'::regclass);


--
-- Data for Name: mdl_course; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mdl_course (id, category, sortorder, fullname, shortname, idnumber, summary, summaryformat, format, showgrades, sectioncache, modinfo, newsitems, startdate, marker, maxbytes, legacyfiles, showreports, visible, visibleold, groupmode, groupmodeforce, defaultgroupingid, lang, theme, timecreated, timemodified, requested, enablecompletion, completionstartonenrol, completionnotify) FROM stdin;
1	0	1	Beuth Hochschule Test Moodle	Beuth HS Test		<p><strong>Blabla keks</strong></p>\r\n<p><strong></strong></p>\r\n<p><strong>Das ist ein Keks-Moodle :) Have fun!</strong></p>	0	site	1	a:0:{}	a:0:{}	3	0	0	0	0	0	1	1	0	0	0			1386688994	1386689634	0	0	0	0
2	1	10001	C/C++ für Java-Programmierer (SS 2013)	c4j-2013ss	4737	<p>Fake-Course "C4J" :D</p>	1	weeks	1	a:11:{i:0;O:8:"stdClass":2:{s:2:"id";s:1:"1";s:4:"name";N;}i:1;O:8:"stdClass":2:{s:2:"id";s:1:"2";s:4:"name";N;}i:2;O:8:"stdClass":2:{s:2:"id";s:1:"3";s:4:"name";N;}i:3;O:8:"stdClass":2:{s:2:"id";s:1:"4";s:4:"name";N;}i:4;O:8:"stdClass":2:{s:2:"id";s:1:"5";s:4:"name";N;}i:5;O:8:"stdClass":2:{s:2:"id";s:1:"6";s:4:"name";N;}i:6;O:8:"stdClass":2:{s:2:"id";s:1:"7";s:4:"name";N;}i:7;O:8:"stdClass":2:{s:2:"id";s:1:"8";s:4:"name";N;}i:8;O:8:"stdClass":2:{s:2:"id";s:1:"9";s:4:"name";N;}i:9;O:8:"stdClass":2:{s:2:"id";s:2:"10";s:4:"name";N;}i:10;O:8:"stdClass":2:{s:2:"id";s:2:"11";s:4:"name";N;}}	a:2:{i:1;O:8:"stdClass":10:{s:2:"id";s:1:"1";s:2:"cm";s:1:"1";s:3:"mod";s:5:"forum";s:7:"section";s:1:"0";s:9:"sectionid";s:1:"1";s:6:"module";s:1:"9";s:5:"added";s:10:"1386690345";s:7:"visible";s:1:"1";s:10:"visibleold";s:1:"1";s:4:"name";s:16:"Nachrichtenforum";}i:2;O:8:"stdClass":10:{s:2:"id";s:1:"2";s:2:"cm";s:1:"2";s:3:"mod";s:5:"forum";s:7:"section";s:1:"0";s:9:"sectionid";s:1:"1";s:6:"module";s:1:"9";s:5:"added";s:10:"1386691523";s:7:"visible";s:1:"1";s:10:"visibleold";s:1:"1";s:4:"name";s:40:"Forum für Fragen, Antworten, Tipps etc.";}}	5	1386716400	0	2097152	0	0	1	1	0	0	0			1386689816	1386689816	0	0	0	0
\.


--
-- Name: mdl_course_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('mdl_course_id_seq', 2, true);


--
-- Name: mdl_cour_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mdl_course
    ADD CONSTRAINT mdl_cour_id_pk PRIMARY KEY (id);


--
-- Name: mdl_cour_cat_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_cour_cat_ix ON mdl_course USING btree (category);


--
-- Name: mdl_cour_idn_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_cour_idn_ix ON mdl_course USING btree (idnumber);


--
-- Name: mdl_cour_sho_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_cour_sho_ix ON mdl_course USING btree (shortname);


--
-- Name: mdl_cour_sor_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_cour_sor_ix ON mdl_course USING btree (sortorder);


--
-- PostgreSQL database dump complete
--

