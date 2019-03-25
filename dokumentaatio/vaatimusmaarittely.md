#Vaatimusmäärittely
###Sovelluksen tarkoitus
Sovellus on kalenteri. Käyttäjät pystyvät lisäämän tapahtumia yksityiseen listaan, jota vain he pystyvät katsoa. 
Tapahtuma koostuu tapahtuman nimestä ja ajankohdasta.
Tapahtumat katoavat listasta, kun ne ovat menneet, mutta niitä voi myös poistaa manuaalisesti.
Käyttäjät pystyvät myös luomaan julkisia tapahtumia; julkiset tapahtumat näkyvät kaikille.
###Käyttäjät
Sovelluksella on normaali käyttäjiä ja admin käyttäjä. Admin pystyy poistamaan julkisia tapahtumia. Normaali käyttäjä voi
poistaa vain tapahtumia, joita hän loi itse.
###Toiminnallisuus
Aloitusnäkymä on kirjautumisnäkymä, missä käyttäjä kirjautuu sisään vanhoilla tunnuksilla tai klikkaa "create user".
Uuden käyttäjän luomisella on oma näkymä.

Kirjautumisen jälkeen on kaksi näkymää, joiden välillä voi vaihdella. Näistä ensimmäinen on _yksityiset tapahtumat_, missä on listattu
käyttäjän yksityiset tapahtumat ja samassa näkymässä pystyy luomaan uuden yksityisen tapahtuman. Toinen näkymä on 
_julkiset tapahtumat_, jossa näkyy kaikki julkiset tapahtumat ja niiden luojat ja pystyy luomaan uuden julkisen tapahtuman.
Molempi näkymä sisältää yläkulmassa sign-out napin, josta pääsee aloitusnäkymään.
###Jatkokehitysideat
  *Pystyy ilmoittamaan, että osallistuu julkiseen tapahtumaan, ja pystyy katsomaan muut osallistujat.
  *Visuaalinen kalenteri, josta näkee, minä päivinä on tapahtumia.
