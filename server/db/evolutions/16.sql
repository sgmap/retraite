# --- !Ups
create table FakeData (
	id serial not null,
	nir varchar(255),
	nom varchar(255),
	regimes varchar(255),
	primary key (id)
);
  

INSERT INTO FakeData (nir, nom, regimes) VALUES 
	('1380626111111','AAAA','CCMSA,CANCAVA'),
	('2000406111111','BBBB','AGIRC ARRCO,CNAV'),
	('1200438111111','CCCC','CARPIMKO'),
	('1250875111111','DDDD','IRCANTEC,CNAV,CARMF'),
	('1290799111111','EEEE','CANCAVA'),
	('1310558111111','FFFF','CANCAVA'),
	('1320134111111','GGGG','CNAV,ORGANIC'),
	('1320199111111','HHHH','CANCAVA,CNAV'),
	('1320567111111','IIII','CARPV,IRCANTEC,CNAV'),
	('2340169111111','JJJJ','CANCAVA,CNAV'),
	('1341025111111','KKKK','IRCANTEC,CIPAV'),
	('2341171111111','LLLL','CNAV,ORGANIC'),
	('1350533111111','MMMM','IRCANTEC,CIPAV'),
	('1361154111111','NNNN','IRCANTEC,CNAV,CAVOM'),
	('1380534111111','OOOO','CNAV,ORGANIC'),
	('2381167111111','PPPP','CNAV,CIPAV'),
	('1381294111111','QQQQ','CNAV,ORGANIC'),
	('1390375111111','RRRR','CARPIMKO,IRCANTEC'),
	('1390652111111','SSSS','CNAV'),
	('1391099111111','TTTT','CNAV'),
	('1400399111111','UUUU','CNAV'),
	('1400687111111','VVVV','IRCANTEC,CNAV,CIPAV'),
	('2400799111111','WWWW','CNAV,ORGANIC'),
	('2401261111111','XXXX','CCMSA,CNAV'),
	('1410259111111','YYYY','IRCANTEC,CNAV,CAVOM'),
	('1410307111111','ZZZZ','CCMSA,CNAV'),
	('2410436111111','aaaa','IRCANTEC,CNAV,RAFP'),
	('1410657111111','bbbb','CCMSA,CNAV'),
	('1410707111111','cccc','CCMSA,CNAV'),
	('1410899111111','dddd','CARCD,IRCANTEC,CNAV'),
	('1411013111111','eeee','CNAV,CIPAV'),
	('1411092111111','ffff','CANCAVA,CNAV'),
	('1411203111111','gggg','CNAV'),
	('1420133111111','hhhh','AGIRC ARRCO,CANCAVA,CNAV,ORGANIC'),
	('2420336111111','iiii','AGIRC ARRCO,IRCANTEC,CNAV'),
	('1420369111111','jjjj','AGIRC ARRCO,IRCANTEC,CCMSA,CNAV,CANCAVA'),
	('2420375111111','kkkk','AGIRC ARRCO,CNAV,ORGANIC'),
	('1420389111111','llll','AGIRC ARRCO,CAVEC,CNAV'),
	('2420399111111','mmmm','AGIRC ARRCO,IRCANTEC,CNAV,CIPAV'),
	('1420476111111','nnnn','AGIRC ARRCO,CNAV,CIPAV'),
	('1420573111111','oooo','AGIRC ARRCO,CCMSA,CNAV'),
	('1420621111111','pppp','AGIRC ARRCO,CCMSA,CNAV'),
	('1420745111111','qqqq','AGIRC ARRCO,CCMSA,CNAV'),
	('1421113111111','rrrr','CARPIMKO'),
	('2421134111111','ssss','CCMSA,CNAV'),
	('2421146111111','tttt','AGIRC ARRCO,IRCANTEC,CNAV'),
	('1421213111111','uuuu','AGIRC ARRCO,ENIM,CNAV,ORGANIC'),
	('1421299111111','vvvv',''),
	('2430101111111','wwww','AGIRC ARRCO,CNAV,CAVIMAC'),
	('1430106111111','xxxx','AGIRC ARRCO,CNAV,CAVIMAC');

# --- !Downs

DROP TABLE FakeData;
