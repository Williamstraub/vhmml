Github README file for vHMML, ver. 2.1.9 - 9 December 2016 

vHMML (virtual Hill Museum & Manuscript Library), an open source Java-based application, is an online environment for manuscript studies. VHMML consists of six closely linked, interoperable and mutually-reinforcing components: 
1.	Reading Room – search and find records and surrogate images of manuscripts and printed materials

2.	Cataloging – backend online forms for cataloging manuscripts, printed books, and archival objects. Allows importing from Microsoft Access MDB files

3.	School – instructional material composed in HTML for teaching paleography and codicology for languages/cultures represented in HMML’s collections

4.	Lexicon – a crowdsourced glossary for manuscript studies inclusive of Western and non-Western manuscripts

5.	Reference – a database of digital and print resources useful for manuscript studies such as classic works on paleography, manuscript catalogs and influential journal articles, with links to PDF surrogates when available

6.	Folio – richly described sample manuscript folios from HMML’s collections that illustrate the chronological and regional development of writing styles, both Western and Eastern

vHMML began as a concept developed in 2011 by Columba Stewart, OSB, the Executive Director of HMML at Saint John’s University in Collegeville Minnesota. Reading Room was added to the original project scope in 2013. The first phase of the project (School, Lexicon, Reference, Folio) was live in October 2015; Reading Room was launched along with a redesign of the other components in August 2016. More information about the project is available at https://www.vhmml.org. 

vHMML was written in Java™, HTML, JavaScript, CSS, and Handlebars and is designed to run on an Apache Tomcat Server utilizing MySQL for storage. Elastic Search is used for indexed searching of catalog records and terms in Reference and the IIIF viewer compliant Mirador for viewing objects. Images are stored on a Digilib image server. Bootstrap was utilized for responsive design and image viewing for vHMML School utilizes OpenSeaDragon. The w3id provides permanent links capability.

Because HMML has a very small staff, we are not able to help you with implementation or setup of this code on your own system.

Some tips for building the code:

At HMML we use Eclipse EE  for Web Developers for coding and testing. VHMML is a dynamic web application module, not an enterprise Java app. Make sure that you are using the Java JDK not the JRE, i.e you need the Java Enterprise Edition. The max heap space for Eclipse will probably need to be increased to 4096 in the eclipse.ini file:
-Xmx4096m

Version. information for the servers
running Java(TM) SE Runtime Environment (build 1.8.0_111-b14);
Tomcat 7.0.54;
MySQL Community Server version 5.5.50;
ElasticSearch version 1.7.5 (anything above version 2.x may break the code without testing);
DigiLib version 2.3.8a

============================================================
Funding for vHMML was provided by the Institute of Museum and Library Services (IMLS), the Henry Luce Foundation, the Andrew W Mellon Foundation, Arcadia, and generous individuals. 
