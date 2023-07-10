![Chess-java](https://github.com/nilspolek/chess-java/blob/43695b245298d5f50e2f18497baeeddd8c85c9e4/Chess-java.jpeg)
[![Build Status](https://drone.webnils.de/api/badges/nilspolek/chess-java/status.svg)](https://drone.webnils.de/nilspolek/chess-java) [![](https://jitpack.io/v/nilspolek/chess-java.svg)](https://jitpack.io/#nilspolek/chess-java) [![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Dieses GitHub-Repository enthält eine Java-Bibliothek namens `com.github.nilspolek.chess-java`, die die Implementierung eines Schachbretts und von Schachzügen bietet.

# Installation
## Maven

```xml
<repositories>
	<repository>
		<id>jitpack.io</id>
 		<url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
	<groupId>com.github.nilspolek</groupId>
	<artifactId>chess-java</artifactId>
	<version>LATEST</version>
</dependency>
```

## Verwendung

### Schachbrett erstellen

```java
Board board = new Board();
```

### Schachstellung einrichten

Das Schachbrett kann durch die Verwendung der FEN-Notation (Forsyth-Edwards-Notation) eingerichtet werden.

```java
board.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
```

### Zugberechnung

```java
Move move = board.findBestMove();
```

Dieser Aufruf berechnet den besten Zug für das aktuelle Schachbrett. Der berechnete Zug wird als `Move`-Objekt zurückgegeben.

### Zug ausführen

```java
boolean success = board.move(move);
```

Mit diesem Aufruf wird der berechnete Zug auf dem Schachbrett ausgeführt. Der Rückgabewert `success` gibt an, ob der Zug erfolgreich war.

### Spiel speichern und laden

```java
board.saveGame("game.txt");
board.loadGame("game.txt");
```

Mit diesen Methoden können Sie den aktuellen Spielstand speichern und aus einer Datei laden.

### Weitere Methoden

Das `Board`-Objekt bietet auch andere nützliche Methoden, z. B. zum Abrufen der aktuellen Schachbrettstellung (`getBoard()`), zur Bewertung des Schachbretts (`evaluate()`) und zur Überprüfung, ob das Spiel beendet ist (`isCheckMate()`).

## Beispiel

```java
import com.github.nilspolek.Board;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Board board = new Board();
        board.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");

        board.findBestMove();
        while (board.getLastBestMove() == null)Thread.sleep(1000);
        boolean success = board.move(board.getLastBestMove());

        if (success) {
            System.out.println("Bester Zug: " + board.getLastBestMove());
            System.out.println("Neue Stellung:");
            System.out.println(board);
        } else {
            System.out.println("Kein gültiger Zug gefunden.");
        }
    }
}
```

Dieses Beispiel zeigt, wie Sie das `Board`-Objekt verwenden können, um den besten Zug für das gegebene Schachbrett zu finden und auszuführen.

## Lizenz

Dieses Projekt ist unter der Apache License lizenziert. Weitere Informationen finden Sie in der [Lizenzdatei](LICENSE).
