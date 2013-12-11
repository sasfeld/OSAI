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

DROP INDEX public.mdl_forudisc_use_ix;
DROP INDEX public.mdl_forudisc_for_ix;
ALTER TABLE ONLY public.mdl_forum_discussions DROP CONSTRAINT mdl_forudisc_id_pk;
ALTER TABLE public.mdl_forum_discussions ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE public.mdl_forum_discussions_id_seq;
DROP TABLE public.mdl_forum_discussions;
SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: mdl_forum_discussions; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mdl_forum_discussions (
    id bigint NOT NULL,
    course bigint DEFAULT 0 NOT NULL,
    forum bigint DEFAULT 0 NOT NULL,
    name character varying(255) DEFAULT ''::character varying NOT NULL,
    firstpost bigint DEFAULT 0 NOT NULL,
    userid bigint DEFAULT 0 NOT NULL,
    groupid bigint DEFAULT (-1) NOT NULL,
    assessed smallint DEFAULT 1 NOT NULL,
    timemodified bigint DEFAULT 0 NOT NULL,
    usermodified bigint DEFAULT 0 NOT NULL,
    timestart bigint DEFAULT 0 NOT NULL,
    timeend bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.mdl_forum_discussions OWNER TO postgres;

--
-- Name: TABLE mdl_forum_discussions; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE mdl_forum_discussions IS 'Forums are composed of discussions';


--
-- Name: mdl_forum_discussions_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE mdl_forum_discussions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.mdl_forum_discussions_id_seq OWNER TO postgres;

--
-- Name: mdl_forum_discussions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE mdl_forum_discussions_id_seq OWNED BY mdl_forum_discussions.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mdl_forum_discussions ALTER COLUMN id SET DEFAULT nextval('mdl_forum_discussions_id_seq'::regclass);


--
-- Data for Name: mdl_forum_discussions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mdl_forum_discussions (id, course, forum, name, firstpost, userid, groupid, assessed, timemodified, usermodified, timestart, timeend) FROM stdin;
1	2	2	Optionale Aufgaben	1	2	-1	1	1386692724	2	0	0
2	2	2	Operatoren für numerische Typen	2	2	-1	1	1386692862	2	0	0
3	2	2	Vector und Iterator	3	4	-1	1	1386693564	4	0	0
\.


--
-- Name: mdl_forum_discussions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('mdl_forum_discussions_id_seq', 3, true);


--
-- Name: mdl_forudisc_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mdl_forum_discussions
    ADD CONSTRAINT mdl_forudisc_id_pk PRIMARY KEY (id);


--
-- Name: mdl_forudisc_for_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_forudisc_for_ix ON mdl_forum_discussions USING btree (forum);


--
-- Name: mdl_forudisc_use_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_forudisc_use_ix ON mdl_forum_discussions USING btree (userid);


--
-- PostgreSQL database dump complete
--

