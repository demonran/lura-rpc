package org.lura.rpc.server;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * TomcatHttpServer
 *
 * @author Liu Ran
 */
public class TomcatHttpServer implements HttpServer {


    @Override
    public void doStart(int port) {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(createTempDir("tomcat." + port + ".").getAbsolutePath());
        tomcat.setPort(port);
        tomcat.getConnector();
        tomcat.setHostname("localhost");

        Context context = tomcat.addContext("/", null);

        try {

            tomcat.addServlet(context, "hello", new RpcServlet());

            context.addServletMappingDecoded("", "hello");
            tomcat.start();
            System.out.println("Server is now listening on port " + port);
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }

    private File createTempDir(String prefix) {
        try {
            File tempDir = Files.createTempDirectory(prefix).toFile();
            tempDir.deleteOnExit();
            return tempDir;
        } catch (IOException ex) {
            throw new RuntimeException(
                    "Unable to create tempDir. java.io.tmpdir is set to " + System.getProperty("java.io.tmpdir"), ex);
        }
    }
}
