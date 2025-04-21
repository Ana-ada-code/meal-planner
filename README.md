# 🥗 Planer Posiłków
**Autor: Anna Adamik**
### Opis
Aplikacja webowa umożliwiająca planowanie codziennych posiłków, zarządzanie przepisami kulinarnymi oraz tworzenie spersonalizowanych list dań i ulubionych. Projekt został stworzony w celu ułatwienia organizacji codziennego gotowania.

### Link do strony
https://meal-planner.pl/

### Funkcjonalności
- Tworzenie i zarządzanie przepisami kulinarnymi
- Planowanie posiłków
- Tworzenie listy ulubionych posiłków
- Dodawanie ocen do poszczególnych dań
- Filtrowanie przepisów według kategorii
- Responsywny interfejs użytkownika dostosowany do urządzeń mobilnych

### Założenia ogólne
Dla wszystkich:
- możliwość zobaczenia pełnej listy dań 
- możliwość zobaczenia 10 najlepiej ocenianych dań
- możliwość zobaczenia listy kategorii
- możliwość zobaczenia dań z wybranej kategorii
- możliwość wyszukiwania dań po nazwie
- możliwość podglądu konkretnego dania (składników, przepisu, oceny)
- możliwość rejestracji i logowania
Dla zalogowanych:
- możliwość dodania dania do swojego plannera
- możliwość zobaczenia pełnej listy wybranych dań oraz jej edycji (usuwania poszczególnych pozycji, konkretnego dnia, wszystkich)
- możliwość oceniania dań
- możliwość dodania dania do ulubionych
- możliwość zobaczenia listy ulubionych dań
Dla administratorów:
- możliwość dodawania, edytowania oraz usuwania dań
- możliwość dodawania, edytowania nazwy oraz usuwania kategorii wraz ze wszystkim przypisanymi do niej daniami

### Założenia techniczne
- prosta aplikacja webowa
- listy użytkowników, ról użytkowników, dań, kategorii, ocen, wybranych dań oraz ulubionych zapisywane w bazie danych
- widoki: lista dań, podgląd konkretnego dania, lista kategorii, lista top 10, lista ulubionych, lista zaplanowyanych dań, panel administracyjny, dodawanie nowego dania, edycja dania, logowanie, rejestracja

### Stos technologiczny
- Java 17
- Spring Boot 3.4.4 + Spring Security
- Hibernate (JPA)
- PostgreSQL, H2database
- Liquibase
- Thymeleaf
- Bootstrap 5
- JUnit, Mockito, AssertJ
- Project Lombok
- Cloudinary
- Docker
- Maven
- VPS, Nginx Proxy Manager

### Instrukcja obsługi

### 🍽 Korzystanie z Planera -  dla wszystkich
### Pierwsze uruchomienie lokalnie - profil developerski
1. Uruchomić aplikację - profil dev.
2. W przeglądarce internetowej wejść na stronę: http://localhost:8080/

### Pierwsze uruchomienie lokalnie - profil produkcyjny
1. W db.properties ustawić dane do swojej bazy danych.
2. Dodać początkowe dane do tabeli user_role np:
   insert into user_role (name, description)
   values ('ADMIN', 'pełne uprawnienia'),
   ('USER', 'podstawowe uprawnienia, możliwość oddawania głosów, dodawania posiłków do kalendarza'),
   ('EDITOR', 'podstawowe uprawnienia + możliwość zarządzania treściami'); W cloud.properties dodać dane cloud.
3. Uruchomić aplikację - profil prod.
4. W przeglądarce internetowej wejść na stronę: http://localhost:8080/

### Pierwsze uruchomienie online
1. W przeglądarce internetowej wejść na stronę: https://meal-planner.pl/

### Wyświetlanie listy dań
1. W przeglądarce internetowej wejść na stronę: http://localhost:8080/ (https://meal-planner.pl/) lub skorzystać z przycisku "Meal Planner" w pasku na górze strony.

### Wyświetlanie listy kategorii
1. W przeglądarce internetowej wejść na stronę: http://localhost:8080/kategorie-dan (https://meal-planner.pl/kategorie-dan) lub skorzystać z przycisku "Kategorie" w pasku na górze strony.

### Wyświetlanie listy dań z danej kategorii
1. W przeglądarce internetowej wejść na stronę: http://localhost:8080/kategorie-dan (https://meal-planner.pl/kategorie-dan) lub skorzystać z przycisku "Kategorie" w pasku na górze strony.
2. Kliknąć na nazwę danej kategorii.

### Wyświetlanie listy 10 najwyżej ocenianych dań
1. W przeglądarce internetowej wejść na stronę: http://localhost:8080/top (https://meal-planner.pl/top) lub skorzystać z przycisku "Top10" w pasku na górze strony.

### Wyświetlanie wyszukiwanych dań
1. Wpisać nazwę wyszukiwanego dania w polu wyszukiwania w pasku na górze strony.
2. Kliknąć Enter lub ikonę lupki.

### 🔑 Rejestracja i logowanie
### Logowanie
1. W przeglądarce internetowej wejść na stronę: http://localhost:8080/login (https://meal-planner.pl/login).
2. Uzupełnić dane logowania.
3. Kliknąć Enter lub przycisk "Zaloguj się".

### Rejestracja
1. W przeglądarce internetowej wejść na stronę: http://localhost:8080/rejestracja (https://meal-planner.pl/rejestracja) lub na stronie logowania kliknąć przycisk "Zarejestruj się".
2. Uzupełnić dane rejestracji. Uwaga: Nie podawaj prawdziwych danych!
3. Kliknąć Enter lub przycisk "Zarejestruj się".

### Wylogowanie
1. Kliknąć przycisk "Wyloguj" w pasku na górze strony.

### 🍽 Korzystanie z Planera - dla zalogowanych
### Wyświetlanie listy ulubionych dań
1. Zalogować się na konto użytkownika.
2. W przeglądarce internetowej wejść na stronę: http://localhost:8080/ulubione (https://meal-planner.pl/ulubione) lub skorzystać z przycisku "Ulubione" w pasku na górze strony.

### Wyświetlanie listy wybranych dań
1. Zalogować się na konto użytkownika.
2. W przeglądarce internetowej wejść na stronę: http://localhost:8080/planer (https://meal-planner.pl/planer) lub skorzystać z przycisku "Planer" w pasku na górze strony.

### Zmiana daty zaplanowanego dania
1. Zalogować się na konto użytkownika.
2. W przeglądarce internetowej wejść na stronę: http://localhost:8080/planer (https://meal-planner.pl/planer) lub skorzystać z przycisku "Planer" w pasku na górze strony.
3. Kliknąć ikonkę kalendarza przy nazwie dania którego datę chce się zmienić.
4. Wybrać nową datę i kliknąć "Zmień datę".

### Usunięcie z planera danego dania
1. Zalogować się na konto użytkownika.
2. W przeglądarce internetowej wejść na stronę: http://localhost:8080/planer (https://meal-planner.pl/planer) lub skorzystać z przycisku "Planer" w pasku na górze strony.
3. Kliknąć ikonkę "-" przy nazwie dania.

### Usunięcie z planera danego dnia
1. Zalogować się na konto użytkownika.
2. W przeglądarce internetowej wejść na stronę: http://localhost:8080/planer (https://meal-planner.pl/planer) lub skorzystać z przycisku "Planer" w pasku na górze strony.
3. Kliknąć ikonkę "-" przy konkretnej dacie.

### Usunięcie z planera wszystkich dań
1. Zalogować się na konto użytkownika.
2. W przeglądarce internetowej wejść na stronę: http://localhost:8080/planer (https://meal-planner.pl/planer) lub skorzystać z przycisku "Planer" w pasku na górze strony.
3. Kliknąć przycisk "Usuń wszystkie dania".

### Podgląd szczegółowy danego dania
1. Z widoku "LISTA DAŃ" kliknąć na nazwę danego dania.
2. Z tego miejsca można:
- przejść do widoku logowania
Po zalogowaniu:
- dodać danie do ulubionych - klikając ikonę serduszka
- ocenić danie - klikając na odpowiednią ikonę gwiazdki
- dodać danie do planera - klikając przycisk dodaj do planera i wybierając datę

### 🛠 Panel administratora
### Dodawanie dań
1. Zalogować się na konto administratora. Profil developerski email:admin@planer.pl hasło:adminpass
2. W przeglądarce internetowej wejść na stronę: http://localhost:8080/admin lub skorzystać z przycisku "Administracja" w pasku na górze strony.
3. Kliknąć przycisk "Dodaj danie".
4. Uzupełnić: nazwę dania, składniki, przepis, kategorię oraz dodać grafikę. 
5. Kliknąć "Dodaj danie".

### Usuwanie dań
1. Zalogować się na konto administratora. Profil developerski email:admin@planer.pl hasło:adminpass
2. W przeglądarce internetowej wejść na stronę: http://localhost:8080/admin lub skorzystać z przycisku "Administracja" w pasku na górze strony.
3. Kliknąć przycisk "Usuń danie".
4. Wybrać kategorię z listy.
5. Wybrać danie z listy.
6. Kliknąć przycisk "Usuń danie".

### Edytowanie dań
1. Zalogować się na konto administratora. Profil developerski email:admin@planer.pl hasło:adminpass
2. W przeglądarce internetowej wejść na stronę: http://localhost:8080/admin lub skorzystać z przycisku "Administracja" w pasku na górze strony.
3. Kliknąć przycisk "Edytuj danie".
4. Wybrać kategorię z listy.
5. Wybrać danie z listy.
6. Kliknąć przycisk "Edytuj danie". 
7. Zaktualizować: nazwę dania, składniki, przepis, kategorię oraz dodać grafikę. 
8. Kliknąć "Zapisz zmiany".

### Dodawanie kategorii
1. Zalogować się na konto administratora. Profil developerski email:admin@planer.pl hasło:adminpass
2. W przeglądarce internetowej wejść na stronę: http://localhost:8080/admin lub skorzystać z przycisku "Administracja" w pasku na górze strony.
3. Kliknąć przycisk "Dodaj kategorię".
4. Uzupełnić: nazwę kategorii.
5. Kliknąć przycisk "Dodaj kategorię".

### Usuwanie kategorii
1. Zalogować się na konto administratora. Profil developerski email:admin@planer.pl hasło:adminpass
2. W przeglądarce internetowej wejść na stronę: http://localhost:8080/admin lub skorzystać z przycisku "Administracja" w pasku na górze strony.
3. Kliknąć przycisk "Usuń kategorię".
4. Wybrać kategorię z listy.
5. Kliknąć przycisk "Usuń kategorię".

### Zmiana nazwy kategorii
1. Zalogować się na konto administratora. Profil developerski email:admin@planer.pl hasło:adminpass
2. W przeglądarce internetowej wejść na stronę: http://localhost:8080/admin lub skorzystać z przycisku "Administracja" w pasku na górze strony.
3. Kliknąć przycisk "Zmień nazwę kategorii".
4. Wybrać kategorię z listy.
5. Uzupełnić: nową nazwę kategorii.
6. Kliknąć przycisk "Zmień nazwę".
