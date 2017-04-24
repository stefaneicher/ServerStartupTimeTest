package ch.stefan.jetty;

import com.google.common.base.Stopwatch;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class JettyTest {

    private static final int PORT = 8080;

    private static int MAX = 100;

    public static void main(String[] args) throws Exception {
        Arrays.<Supplier<Server>>asList(
//                JettyTest::simplestServer,
                JettyTest::fileServer
//                JettyTest::webapp
        )
                .forEach(serverSupplier -> {

                    LongStream.range(0, MAX)
                            .map(new Function() {
                                @Override
                                public Object apply(Object o) {
                                    return null;
                                }
                            }     )
//                                    (value) -> {
//                                Long elapsed = Long.MIN_VALUE;
//                                try {
//                                    Stopwatch stopwatch = Stopwatch.createStarted();
//                                    startAndStop(serverSupplier);
//                                    elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
//                                    System.out.println("time: " + elapsed);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                return elapsed;
//                            }
// )
//                            .map(value->value+5)
                            .collect(Collectors.toList());
                });
    }

    private static long sdf(long l) {
        return 0;
    }

    private static void startAndStop(Supplier<Server> supplier) throws Exception {
        Server server = supplier.get();
        server.start();
        server.stop();
    }


    // see https://www.eclipse.org/jetty/documentation/9.4.x/embedding-jetty.html for servers
    private static Server simplestServer() {
        Server server = new Server(PORT);
        return server;
    }

    private static Server fileServer() {
        Server server = new Server(PORT);

        ResourceHandler resourceHandler = new ResourceHandler();

        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
//        String file = JettyTest.class.getResource("index.html").getFile();
//        resourceHandler.setResourceBase("src/main/webapp/.");
        resourceHandler.setResourceBase(".");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, new DefaultHandler()});
        server.setHandler(handlers);
        return server;
    }

    private static Server webapp() {
        Server server = new Server(PORT);

        WebAppContext webapp = new WebAppContext();
        File warFile = new File("./target/jetty-1.0-SNAPSHOT.war");
        webapp.setWar(warFile.getAbsolutePath());

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{webapp});
        server.setHandler(handlers);

        return server;
    }
}
