# Arkkitehtuurikuvaus

### Rakenne
![alt text](https://github.com/014589012/ot-harjoitustyo/blob/master/dokumentointi/kuvat/otpackages.PNG)

Ohjelma koostuu kolmesta paketista: dao, domain ja ui. Domain sisältää luokat käyttäjälle ja tapahtumalle. Dao sisältää luokkia ja rajapintoja, että pystymme käsittelemään käyttäjiä ja tapahtumia listana. Domain sisältää myös EventService-luokan, jossa on metodi jokaiselle toiminnolle, jota käyttöliittymä vaatii.
Ui puolestaan sisältää main-loukan eli sovelluksen graafisen ja interaktiivisen osuuden, joka on toteutettu JavaFX:llä.

### Luokkakaavio
![alt text](https://github.com/014589012/ot-harjoitustyo/blob/master/dokumentointi/kuvat/otkaavio.png)

### Käyttöliittymä

Sovelluksessa on 4 näkymää: kirjautuminen, käyttäjän luominen, julkiset tapahtumat ja yksityiset tapahtumat. Nämä ovat siis **Scene**-olioita, joista aina yksi näytetään asettamalla se *start*-metodin **Stage**-parametriksi ja kutsumalla *show()*.

Ensimmäinen näkymä sovelluksen käynnistyttyä on kirjautuminen. Tästä voi tarvittaessa siirtyä toiseen näkymään luomaan uusi käyttäjä. Kun käyttäjä on luotu, palaa näkymä kirjautumiseen. Kirjautumisen jälkeen siirtyy julkisten tapahtumien näkymään. Sitten pystyy vapaasti siirtymään julkisten ja yksityisten tapahtumien näkymien välillä tai kirjautumaan ulos. Ulos kirjautuminen vie ensimmäiseen näkymään.

EventService-loukan metodit hoitavat kaikki toiminnot, jotka koskevat daon tapahtuma- tai käyttäjälistaa. Näihin kuuluu esim. käyttäjän luominen ja tulevien tapahtumien hakeminen. Käyttöliittymä pystyy kuitenkin luomaan visuaalisen **HBox**-olion olemassa olevasta tapahtumasta. Käyttöliittymän metodi *redrawEventlist()*  päivittää **VBox**:in, jossa on kaikkien tulevien julkisten tapahtumien **HBox**. Metodi *redrawEventlistPrivate()* tekee saman kirjautuneen käyttäjän yksityisille tapahtumille.

### Sovelluslogiikka
Käyttäjällä on id, nimi ja salasana.

Tapahtumalla on id, nimi, päivämäärä, käyttäjä ja totuusarvo siitä, onko tapahtuma yksityinen.

Dao-paketin luokat ja rajapinnat mahdollistavat käyttäjälistan ja tapahtumalistan käsittelyn.

Loput backend-toiminnot suorittaa EventService. Oleellisia metodeja ovat mm.
* createUser(String username, String password),
* createEvent(String text, String dd, boolean prive),
* getUpcomingPublic(),
* getUpcomingPrivate(User user).
> Huom. Koska haemme tulevia tapahtumia, ei tapahtuma ilmesty listaan käyttöliittymässä, jos päivämäärä on jo mennyt.


### Sekvenssikaaviot
![alt text](https://github.com/014589012/ot-harjoitustyo/blob/master/dokumentointi/kuvat/eventsuccess.PNG)


