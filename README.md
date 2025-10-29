# Amőba (Gomoku) – Parancssoros Java játék

## 🧩 Leírás
Ez a projekt egy **parancssoros Amőba (Gomoku)** játék, amelyet Java 21 és Maven segítségével készítettem.  
A játék egy **NxM-es táblán** zajlik (alapértelmezés: 10x10).  
A játékos `'x'`, a gép `'o'` jelet használja.  
A cél: **négy egymást követő jel kirakása** vízszintesen, függőlegesen vagy átlósan.

A játék képes:
- fájlból beolvasni egy mentett állást,
- új játékot indítani, ha nincs megadott fájl,
- játékállást fájlba menteni,
- és véletlenszerű gépi lépéseket generálni.

---

## ⚙️ Követelmények
- Java 21
- Maven 3.9+
- Git (opcionális, ha klónozni akarod)

---

## 🏗️ Build

A projektet a gyökérkönyvtárban az alábbi paranccsal tudod buildelni:

```bash
mvn clean package
