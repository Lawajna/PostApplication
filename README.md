# PostApplication – Aplikacja do postów

## Spis treści
- [Opis projektu](#opis-projektu)
- [Funkcje aplikacji](#funkcje-aplikacji)
- [Zastosowane technologie](#zastosowane-technologie)

---

## Opis projektu

PostApplication to aplikacja stworzona w języku Kotlin z użyciem nowoczesnego podejścia Jetpack Compose. Służy do przeglądania postów oraz uzyskiwania informacji o ich autorach i przypisanych zadaniach. Wykorzystuje dane z otwartego API:  
🌐 `https://jsonplaceholder.typicode.com`

---

## Funkcje aplikacji

### Strona główna – Lista postów i autorów
- Automatyczne pobieranie postów i użytkowników z sieci.
- Prezentacja danych w formie przewijanej listy (`LazyColumn`).
- Dotknięcie tytułu przenosi użytkownika do szczegółowego widoku posta.
- Kliknięcie nazwiska autora otwiera ekran z jego danymi.

Działanie:  

  ![Nagrywanie-ekranu-2025-06-04-085523](https://github.com/user-attachments/assets/073985da-1111-4a26-b306-f1bd46b5ff73)

---

### Szczegóły wpisu
- Dane konkretnego wpisu pobierane na podstawie identyfikatora (`postId`).
- Wyświetlane są: tytuł posta, jego treść oraz ID autora.
- Wbudowana nawigacja powrotna do listy postów.

Działanie:  

![Nagrywanie-ekranu-2025-06-04-085314](https://github.com/user-attachments/assets/f4ab41eb-022d-4c82-9ace-360eaa9123ff)

---

### Informacje o autorze posta
- Aplikacja pobiera profil użytkownika oraz jego listę zadań.
- Pokazywane informacje obejmują:
  - Imię i nazwisko
  - Nazwę użytkownika (login)
  - Adres e-mail, 
  - Numer telefonu, 
  - Stronę www
  - Firmę i adres zamieszkania
- Zadania prezentowane są jako lista z możliwością odznaczania wykonania.
- Dostępna jest opcja powrotu do ekranu startowego.

Działanie:  

![Nagrywanie-ekranu-2025-06-04-085036](https://github.com/user-attachments/assets/ef3c762e-9d06-4e79-8cce-c27815721af9)

---

### Profil użytkownika
- Zmiana i zapisanie imienia i nazwiska
- Wybór i zapisanie zdjęcia użytkownika
- Powrót do strony głównej

Działanie: 

![Nagrywanie-ekranu-2025-06-04-085036](https://github.com/user-attachments/assets/2a212dcb-e22d-4cdd-b2b1-6d2941609dc3)


---

## Problemy do rozwiązania



---

## Zastosowane technologie

- `Kotlin`
- `Jetpack Compose`
- `Retrofit` do komunikacji z API
- `Gson` oraz `kotlinx.serialization` do przetwarzania danych JSON
- Architektura oparta na wzorcu `MVVM`
- `Navigation Compose` do zarządzania nawigacją między ekranami
