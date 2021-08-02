# Vizsgaremek - Drinking-App

## Leírás

A Drinking-appel feljegyezhetjük egy este elfogyasztott italait, megbecsülhetjük az adott illuminált személy aktuális vér alkohol szintjét, a kijózanodáshoz szükséges időt, az aktuális és az összesen bevitt alkohol mennyiségét. Ehhez a becsléshez Widmark formulát használom:

Wildmark formula : https://en.wikipedia.org/wiki/Blood_alcohol_content

---

## Használata

### Person

A `Person` entitás a következőket tarja karban:

* név (nem lehet üres, max. 255 karakter)
* nem (MALE,FEMALE)
* tömeg (pozitív)

A következő végpontokon érjük el az entitást

| Http metódus | Vég pont            | Leírás                           |
| ------------ | ------------------- | -------------------------------- |
| GET          | `"api/people"`      | lekérdezi az összes személyt     |
| GET          | `"api/people/{id}"` | lekérdez egy személyt id alapján |
| POST         | `"api/people"`      | létrehoz egy személyt            |
| PUT          | `"api/people/{id}"` | módosít egy személyt id alapján  |
| DELETE       | `"api/people/{id}"` | töröl egy személyt id alapján    |

---

### Drink

A `Drink` entitás a következőket tarja karban:

* név (nem lehet üres, max. 255 karakter)
* mennyiség `Bigdecimal` (pozitív)
* alkohol százalék `Bigdecimal` (pozitív)
* alkohol mennyisége grammban `Bigdecimal`: az alkohol mennyiségéből és az alkohol százalékából számolandó
* idő: `LocalDateTime` az alkohol elfogyasztásának az ideje
* eltelt idő: `Long` nem kerül be az adatbázisba, az idő és az aktuális idő közt eltelt percek



A `Person` és a `Drink` entitás között Kétirányú `@OneToMany`-`@ManyToOne` kapcsolat van.

A következő végpontokon érjük el az entitást

| Http metódus | Vég pont                             | Leírás                                            |
| ------------ | ------------------------------------ | ------------------------------------------------- |
| GET          | `"api/people/{id}/drinks"`           | lekérdezi az adott személyt összes italát         |
| GET          | `"api/people/{id}/drinks/{drinkId}"` | lekérdezi az adott személyt egy italát id alapján |
| POST         | `"api/people/{id}/drinks"`           | hozzáadd egy új italt a személyhez                |
| PUT          | `"api/people/{id}/drinks/{drinkId}"` | Módosítja az adott személyt egy italát id alapján |
| DELETE       | `"api/people/{id}/drinks/{drinkId}"` | törli az adott személyt egy italát id alapján     |

---

### Statistics

A `Statistics` osztály egy adott személyről add információkat:

* Összes alkohol mennyisége grammban `BigDecimal`
* Az első italtól eltelt időt percben `BigDecimal`
* Az aktuális véralkohol szintet `BigDecimal`
* Az aktuális alkohol mennyiségét a vérben grammban `BigDecimal`
* A kijózanodáshoz szükséges idő `BigDecimal`

A következő végponton érjük el ez a információt:

| Http metódus | Vég pont                       | Leírás                                         |
| ------------ | ------------------------------ | ---------------------------------------------- |
| GET          | `"api/people/{id}/statistics"` | lekérdezi az adott személynek a statisztikáját |

---
