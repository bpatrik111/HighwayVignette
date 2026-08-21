# Highway Vignette (E-matrica) – telepítési dokumentáció

## Előfeltételek

- Android Studio (AGP 9+ built-in Kotlin támogatással), JDK 17
- Docker + Docker Compose
- Android emulátor vagy fizikai eszköz (minSdk 30+)

## 1. A mock API elindítása

```bash
cd api
docker compose up --build
```

A szerver ezután a `http://localhost:8080` címen érhető el.

## 2. Az Android alkalmazás futtatása

1. Nyisd meg a projekt gyökerét Android Studio-ban, várd meg a Gradle sync-et.
2. Indítsd el az `app` konfigurációt egy emulátoron vagy eszközön.

### `BASE_URL` beállítása

Az app a mock API címét a `BuildConfig.BASE_URL`-en keresztül kapja meg
(`app/build.gradle.kts`), ami a futtatási környezettől függ:

| Környezet | BASE_URL |
|---|---|
| Android emulátor | `http://10.0.2.2:8080/` |
| Fizikai eszköz, `adb reverse tcp:8080 tcp:8080` után | `http://localhost:8080/` |
| Fizikai eszköz, azonos Wi-Fi hálózaton | a gép hálózati IP-je, pl. `http://192.168.0.130:8080/` |
