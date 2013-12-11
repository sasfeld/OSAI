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

DROP INDEX public.mdl_foru_cou_ix;
ALTER TABLE ONLY public.mdl_forum DROP CONSTRAINT mdl_foru_id_pk;
ALTER TABLE public.mdl_forum ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE public.mdl_forum_id_seq;
DROP TABLE public.mdl_forum;
SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: mdl_forum; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mdl_forum (
    id bigint NOT NULL,
    course bigint DEFAULT 0 NOT NULL,
    type character varying(20) DEFAULT 'general'::character varying NOT NULL,
    name character varying(255) DEFAULT ''::character varying NOT NULL,
    intro text NOT NULL,
    introformat smallint DEFAULT 0 NOT NULL,
    assessed bigint DEFAULT 0 NOT NULL,
    assesstimestart bigint DEFAULT 0 NOT NULL,
    assesstimefinish bigint DEFAULT 0 NOT NULL,
    scale bigint DEFAULT 0 NOT NULL,
    maxbytes bigint DEFAULT 0 NOT NULL,
    maxattachments bigint DEFAULT 1 NOT NULL,
    forcesubscribe smallint DEFAULT 0 NOT NULL,
    trackingtype smallint DEFAULT 1 NOT NULL,
    rsstype smallint DEFAULT 0 NOT NULL,
    rssarticles smallint DEFAULT 0 NOT NULL,
    timemodified bigint DEFAULT 0 NOT NULL,
    warnafter bigint DEFAULT 0 NOT NULL,
    blockafter bigint DEFAULT 0 NOT NULL,
    blockperiod bigint DEFAULT 0 NOT NULL,
    completiondiscussions integer DEFAULT 0 NOT NULL,
    completionreplies integer DEFAULT 0 NOT NULL,
    completionposts integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.mdl_forum OWNER TO postgres;

--
-- Name: TABLE mdl_forum; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE mdl_forum IS 'Forums contain and structure discussion';


--
-- Name: mdl_forum_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE mdl_forum_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.mdl_forum_id_seq OWNER TO postgres;

--
-- Name: mdl_forum_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE mdl_forum_id_seq OWNED BY mdl_forum.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mdl_forum ALTER COLUMN id SET DEFAULT nextval('mdl_forum_id_seq'::regclass);


--
-- Data for Name: mdl_forum; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mdl_forum (id, course, type, name, intro, introformat, assessed, assesstimestart, assesstimefinish, scale, maxbytes, maxattachments, forcesubscribe, trackingtype, rsstype, rssarticles, timemodified, warnafter, blockafter, blockperiod, completiondiscussions, completionreplies, completionposts) FROM stdin;
1	2	news	Nachrichtenforum	Nachrichten und Ankündigungen	1	0	0	0	1	0	1	1	1	0	0	1386690345	0	0	0	0	0	0
2	2	general	Forum für Fragen, Antworten, Tipps etc.	<p><span>Dieses Forum soll dem Austausch von Informationen und der Hilfe bei Problemen inhaltlicher oder organisatorischer Art dienen. Alle können Beiträge posten und beantworten und das Forum ggf. abbestellen.</span></p>	1	0	0	0	0	512000	9	0	1	0	0	1386691523	0	0	0	0	0	0
\.


--
-- Name: mdl_forum_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('mdl_forum_id_seq', 2, true);


--
-- Name: mdl_foru_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mdl_forum
    ADD CONSTRAINT mdl_foru_id_pk PRIMARY KEY (id);


--
-- Name: mdl_foru_cou_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_foru_cou_ix ON mdl_forum USING btree (course);


--
-- PostgreSQL database dump complete
--

