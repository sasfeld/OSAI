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

DROP INDEX public.mdl_forupost_use_ix;
DROP INDEX public.mdl_forupost_par_ix;
DROP INDEX public.mdl_forupost_mai_ix;
DROP INDEX public.mdl_forupost_dis_ix;
DROP INDEX public.mdl_forupost_cre_ix;
ALTER TABLE ONLY public.mdl_forum_posts DROP CONSTRAINT mdl_forupost_id_pk;
ALTER TABLE public.mdl_forum_posts ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE public.mdl_forum_posts_id_seq;
DROP TABLE public.mdl_forum_posts;
SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: mdl_forum_posts; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mdl_forum_posts (
    id bigint NOT NULL,
    discussion bigint DEFAULT 0 NOT NULL,
    parent bigint DEFAULT 0 NOT NULL,
    userid bigint DEFAULT 0 NOT NULL,
    created bigint DEFAULT 0 NOT NULL,
    modified bigint DEFAULT 0 NOT NULL,
    mailed smallint DEFAULT 0 NOT NULL,
    subject character varying(255) DEFAULT ''::character varying NOT NULL,
    message text NOT NULL,
    messageformat smallint DEFAULT 0 NOT NULL,
    messagetrust smallint DEFAULT 0 NOT NULL,
    attachment character varying(100) DEFAULT ''::character varying NOT NULL,
    totalscore smallint DEFAULT 0 NOT NULL,
    mailnow bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.mdl_forum_posts OWNER TO postgres;

--
-- Name: TABLE mdl_forum_posts; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE mdl_forum_posts IS 'All posts are stored in this table';


--
-- Name: mdl_forum_posts_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE mdl_forum_posts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.mdl_forum_posts_id_seq OWNER TO postgres;

--
-- Name: mdl_forum_posts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE mdl_forum_posts_id_seq OWNED BY mdl_forum_posts.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mdl_forum_posts ALTER COLUMN id SET DEFAULT nextval('mdl_forum_posts_id_seq'::regclass);


--
-- Data for Name: mdl_forum_posts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mdl_forum_posts (id, discussion, parent, userid, created, modified, mailed, subject, message, messageformat, messagetrust, attachment, totalscore, mailnow) FROM stdin;
1	1	0	2	1386692724	1386692724	0	Optionale Aufgaben	<p>Hallo allerseits,</p>\r\n<p><span>ich hatte auf den Aufgabenblättern zu Aufgabe 2  nicht angegeben, ob es optionale Aufgaben für das Erreichen einer sehr guten Note gibt. Das lag daran, dass bei der ersten Aufgabe im Kurs alle den Eindruck erweckt haben, eine sehr gute Note anzustreben ;-)</span></p>\r\n<p><span>Für den Fall, dass die Zeit knapp wird und Sie etwas weglassen müssen oder wollen, überlasse ich Ihnen was Sie weglassen. Gute Kandidaten dafür sind das "Zerstören und Kopieren" und die Iteratoren, darauf baut sonst nichts auf. Falls Sie große Probleme mit den Templates haben, ist es auch denkbar, die Templates wegzulassen und die Iteratoren für die Nicht-Template-Version der Map zu implementieren.<br /></span></p>\r\n<p><span>Es gibt schon einige wenige, die alles bis zum Ende implementiert haben. Aber falls sich herausstellen sollte, dass die Aufgabe doch etwas zu heftig war und viele einenTeil auslassen müssen, werde ich das bei der Bewertung entsprechend berücksichtigen.</span></p>\r\n<p><span>Also ich drücke die Daumen für den Endspurt. Ich weiss, dass gerade die Zeiger-Verbiegerei, const und die Templates hakelig sind. Wenn Sie irgendwo "hängen" melden Sie sich, man kann auch ein Problem mal zusammen bei mir im Büro oder via TeamViewer anschauen. </span></p>\r\n<p>Noch ein Hinweis: diese Aufgabe ist der absolute "Berg" des Kurses, es ist ja auch die längste und umfangreichste Aufgabe. Wenn Sie durch sind, wird es easy, danach kommt nur noch die Kür-Aufgabe, bei der Sie mit Qt eine kleine Applikation nach eigenem Gusto erstellen.</p>\r\n<p><span>Beste Grüße und DURCHHALTEN</span></p>\r\n<p> </p>\r\n<p><span>Hartmut Schirmacher</span></p>	1	0		0	0
2	2	0	2	1386692862	1386692862	0	Operatoren für numerische Typen	<p>Aufgrund des Codes, den ich bisher zur Aufgabe 2.1 gesehen habe, noch ein kleiner Tipp: bauen Sie möglichst wenige redundante Operatoren in Ihre Typen ein. Die Idee bei Days, Months und Years ist, dass es sich jeweils nur um einen einfachen Wrapper für einen Integer handelt. Durch den Konstruktor und den Cast-Operator können die Objekte in Integer verwandelt werden (und umgekehrt), und in C++ wird sehr viel IMPLIZIT umgewandelt.  D.h. Sie müssen z.B. keinen Vergleichsoperator oder +-Operator zwischen zwei Months-Objekten implementieren, wenn die Objekte sich automatisch in unsigned int umwandeln können. Beispiel:</p>\r\n<p>Days a(5), b(6);<br />assert(a!=b);</p>\r\n<p>Was passiert hier? Zunächst würde der Compiler schauen, ob es Days::operator!=(Days) gibt. Wenn nicht, schaut der Compiler, in was er b umwandeln kann, und schaut für alle möglichen Typen Tb, ob Days::operator!=(Tb) existiert. Wenn auch das nicht geht, überprüft der Compiler auch noch sämtliche Umwandlungen von a in einen Typ Ta, und schaut welcher operator!=(Ta,Tb) existiert.</p>\r\n<p>Wenn Sie also einen Days::operator unsigned int() implementiert haben, dann wird der Compiler fündig: der eingebaute operator!=(unsigned int, unsigned int) kann verwendet werden, weil sowohl a als auch b in unsigned int umwandelbar sind.</p>\r\n<p>Alles klar? Also, Sie müssen eigentlich nur die in der Aufgabenstellung gelisteten Operatoren umsetzen, dann sollte es gehen.</p>\r\n<p>Viel Erfolg und noch einen schönen Sonntag</p>\r\n<p> </p>\r\n<p id="yui_3_7_3_3_1386692797241_174">Hartmut Schirmacher</p>	1	0		0	0
3	3	0	4	1386693263	1386693263	0	Vector und Iterator	<p style="margin: 0px 0px 1em; padding: 0px; font-family: Arial, Helvetica, sans-serif; color: #333333; font-size: 13px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: 20px; orphans: auto; text-align: left; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: #f5f5f5;">Irgendwie versteh ich den iterator nicht.</p>\r\n<p style="margin: 0px 0px 1em; padding: 0px; font-family: Arial, Helvetica, sans-serif; color: #333333; font-size: 13px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: 20px; orphans: auto; text-align: left; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: #f5f5f5;">siehe kommentar.</p>\r\n<p style="margin: 0px 0px 1em; padding: 0px; font-family: Arial, Helvetica, sans-serif; color: #333333; font-size: 13px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: 20px; orphans: auto; text-align: left; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: #f5f5f5;">std::vector &lt;MyThread*&gt;::iterator iter;<br />        for(iter=listOfThreads.begin(); iter != listOfThreads.end(); iter++){<br />            //wieso kann ich ueber den iterator<br />            //keine thread funktionen wie z.b deleteLater()aufrufen?<br />            *iter.<br />        }<br />Christian</p>	1	0		0	0
4	3	3	2	1386693400	1386693400	0	Re: Vector und Iterator	<p>Der Typ in dem vector ist ja MyThread*. Also ist *iter auch vom Typ MyThread*. Also muss man um eine Methode an einem MyThread aufzurufen schreiben: (*iter)-&gt;deleteLater(). Klappt das?</p>\r\n<p>Gruß HS</p>	1	0		0	0
5	3	4	4	1386693564	1386693564	0	Re: Vector und Iterator	<p style="margin: 0px 0px 1em; padding: 0px; font-family: Arial, Helvetica, sans-serif; color: #333333; font-size: 13px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: 20px; orphans: auto; text-align: left; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: #f5f5f5;">Danke,</p>\r\n<p style="margin: 0px 0px 1em; padding: 0px; font-family: Arial, Helvetica, sans-serif; color: #333333; font-size: 13px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: 20px; orphans: auto; text-align: left; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: #f5f5f5;">Klappt alles super. Die IDE hat nicht per Autokorrektur alle Funktionen angezeigt.</p>\r\n<p style="margin: 0px 0px 1em; padding: 0px; font-family: Arial, Helvetica, sans-serif; color: #333333; font-size: 13px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: 20px; orphans: auto; text-align: left; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: #f5f5f5;">(keine MyThread Funktionen).<span class="Apple-converted-space"> </span><span class="st"><em style="font-style: italic; font-weight: normal;">It's</em><span class="Apple-converted-space"> </span>not a<span class="Apple-converted-space"> </span><em style="font-style: italic; font-weight: normal;">bug</em>,<span class="Apple-converted-space"> </span><em style="font-style: italic; font-weight: normal;">it's</em><span class="Apple-converted-space"> </span>a<span class="Apple-converted-space"> </span><em style="font-style: italic; font-weight: normal;">feature</em></span><span class="Apple-converted-space"> </span>:D</p>\r\n<p style="margin: 0px 0px 1em; padding: 0px; font-family: Arial, Helvetica, sans-serif; color: #333333; font-size: 13px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: 20px; orphans: auto; text-align: left; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: #f5f5f5;"> Christian</p>	1	0		0	0
\.


--
-- Name: mdl_forum_posts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('mdl_forum_posts_id_seq', 5, true);


--
-- Name: mdl_forupost_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mdl_forum_posts
    ADD CONSTRAINT mdl_forupost_id_pk PRIMARY KEY (id);


--
-- Name: mdl_forupost_cre_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_forupost_cre_ix ON mdl_forum_posts USING btree (created);


--
-- Name: mdl_forupost_dis_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_forupost_dis_ix ON mdl_forum_posts USING btree (discussion);


--
-- Name: mdl_forupost_mai_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_forupost_mai_ix ON mdl_forum_posts USING btree (mailed);


--
-- Name: mdl_forupost_par_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_forupost_par_ix ON mdl_forum_posts USING btree (parent);


--
-- Name: mdl_forupost_use_ix; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mdl_forupost_use_ix ON mdl_forum_posts USING btree (userid);


--
-- PostgreSQL database dump complete
--

