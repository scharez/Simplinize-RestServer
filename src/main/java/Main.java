import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import utils.ConsoleColor;

import java.io.IOException;
import java.net.URI;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final String BASE_URI = "http://0.0.0.0:8080/rest";

    private static org.glassfish.grizzly.http.server.HttpServer startServer() {

        final ResourceConfig rc = new ResourceConfig().packages("service", "filter");

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {

        Logger l = Logger.getLogger("org.glassfish.grizzly.http.server.HttpHandler");
        l.setLevel(Level.FINE);
        l.setUseParentHandlers(false);
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.FINE);
        l.addHandler(ch);

        final org.glassfish.grizzly.http.server.HttpServer server = startServer();
        server.getServerConfiguration().addHttpHandler(new StaticHttpHandler("public"), "/");

        System.out.println(ConsoleColor.green() + "Simplinize-Server startet!" + ConsoleColor.reset());
        System.in.read();
        server.shutdown();
    }
}

