# Kallen Kalenteri
Ohjelma on kalenteri, missä käyttäjät pystyvät luomaan yksityisiä ja julkisia tapahtumia sekä katsomaan, mitä tapahtumia on tulossa.

#### Dokumentointi
[vaatimusmäärittely](https://github.com/014589012/ot-harjoitustyo/blob/master/dokumentointi/vaatimusmaarittely.md)

[työaikakirjaanpito](https://github.com/014589012/ot-harjoitustyo/blob/master/dokumentointi/tyoaikakirjaanpito.md)

[arkkitehtuuri](https://github.com/014589012/ot-harjoitustyo/blob/master/dokumentointi/arkkitehtuuri.md)

[käyttöohje](https://github.com/014589012/ot-harjoitustyo/edit/master/dokumentointi/kayttoohje.md)

#### Github Release
[First pre-release](https://github.com/014589012/ot-harjoitustyo/releases/tag/viikko5)

[Second pre-release](https://github.com/014589012/ot-harjoitustyo/releases/tag/viikko6)

#### Komennot

##### Jar

Jar tiedoston voi generoida komennolla
`mvn package`, jolloin

hakemistossa target voi käynnistää sovelluksen komennolla

`java -jar otkalenteri-1.0-SNAPSHOT.jar`.

##### Testaus

Testit voi pyörittää komennolla

`mvn test`.

Testikattavuus raportin voi luoda komennolla

`mvn jacoco:report`, jolloin

raportin *target/site/jacoco/index.html* voi avata selaimella.

##### Checkstyle

Checkstyle-tarkistukset voi suorittaa komennolla

`mvn jxr:jxr checkstyle:checkstyle`.
