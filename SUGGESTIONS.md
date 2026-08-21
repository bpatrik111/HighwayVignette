# Javaslati lista

## API / interfész

- **A `/v1/highway/info` végpont túl sokat visel egyszerre.** Egyetlen hívás
  adja vissza az összes matricatípust, vármegyét és jármű-kategóriát. A
  fejlesztés során ez azt jelentette, hogy a kezdőoldal betöltéséhez és a
  vármegyeválasztó betöltéséhez is ugyanazt a nagy payloadot kellett újra
  lekérnem. Érdemes lenne szétbontani (`/vignette-types`, `/counties`), hogy
  célzottabban lehessen cache-elni.
- **Nincs autentikáció** a `/v1/highway/vehicle` és `/v1/highway/order`
  végpontokon.
- A rendelés válasza nem ad vissza egyedi tranzakcióazonosítót, pedig egy
  rendelésszám jól jönne a sikeres vásárlás képernyőn.
- Szerver oldali validáció sincs a beküldött `highwayOrders` tételekre (pl.
  hogy a `type` egy létező matrica-kód-e, vagy hogy a `cost` egyezik-e a
  hivatalos árral), jelenleg bármilyen adatot elfogad, ha a JSON-struktúra
  megfelelő.

## UI / UX

- A szomszédsági figyelmeztetés jelenleg csak tájékoztat, nem akadályozza
  meg a nem összefüggő terület kiválasztását. Egy megerősítő dialógus
  talán jobb UX lenne, mint egy sima szöveges figyelmeztetés.
- Skeleton loading state jól jönne a jelenlegi egyszerű töltésjelző helyett,
  hogy a kezdőoldal és a vármegyeválasztó betöltés közben is mutassa a
  végleges elrendezést.
- A hibaállapotok jelenleg egy általános üzenetet mutatnak hálózati hiba
  esetén, külön lehetne kezelni az időtúllépést, a szerver oldali hibát és
  a hiányzó kapcsolatot, célzottabb üzenetekkel és retry-mechanizmussal.
- Akadálymentesítés: hiányoznak a `contentDescription`-ök és a
  átgondolt fókusz-sorrend, főleg a térképes vármegyeválasztón.
- Dark theme támogatás jelenleg nincs.

## Kód / architektúra

- Unit tesztek hiányoznak
- Cache réteg sincs: a vármegyeválasztóra visszalépve újra lekérdeződik a
  `getCounties()`/`getCountyVignettePrice()`, holott ez fejlesztés közben
  ismételten ugyanaz volt, egy egyszerű in-memory cache javítana a
  válaszidőn és csökkentené a szerverterhelést.