package example;
/**
 The MIT License (MIT)

 Copyright (c) 2013 Gioele Meoni

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.npn.NextProtoNego;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.spdy.client.SPDYClient;

import org.eclipse.jetty.spdy.server.SPDYServerConnectionFactory;
import org.eclipse.jetty.spdy.server.SPDYServerConnector;
import org.eclipse.jetty.spdy.server.http.HTTPSPDYServerConnectionFactory;
import org.eclipse.jetty.spdy.server.http.HTTPSPDYServerConnector;
import org.eclipse.jetty.spdy.server.http.PushStrategy;
import org.eclipse.jetty.spdy.server.proxy.HTTPProxyEngine;
import org.eclipse.jetty.spdy.server.proxy.HTTPSPDYProxyServerConnector;
import org.eclipse.jetty.spdy.server.proxy.ProxyEngineSelector;
import org.eclipse.jetty.spdy.server.proxy.SPDYProxyEngine;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author giok57
 * @email gioelemeoni@gmail.com
 * @modifiedBy giok57
 * <p/>
 * Date: 24/04/13
 * Time: 22:55
 */
public class Main {
    public static void main(String[]args) throws Exception {
        String webapp = "/home/ubuntu/disk/parkogre";
        //String keystorePath = "./web/certificates/lzokeystore";



        Server server = new Server();

        /**
         <!-- =========================================================== -->
         <!-- Setup the SSL Context factory used to establish all TLS     -->
         <!-- Connections and session.                                    -->
         <!--                                                             -->
         <!-- Consult the javadoc of o.e.j.util.ssl.SslContextFactory     -->
         <!-- o.e.j.server.HttpConnectionFactory for all configuration    -->
         <!-- that may be set here.                                       -->
         <!-- =========================================================== -->
         */

        //SslContextFactory sslContextFactory = new SslContextFactory();
        //sslContextFactory.setKeyStorePath(keystorePath);
        //sslContextFactory.setKeyStorePassword("lazoootest");
        //sslContextFactory.setProtocol("TLSv1");



        //HTTPSPDYServerConnector spdy = new HTTPSPDYServerConnector(server, sslContextFactory);
        //spdy.setPort(8443);

        //HTTPSPDYServerConnector spdyh = new HTTPSPDYServerConnector(server);
        ServerConnector s = new ServerConnector(server);
        s.setPort(80);
        //spdyh.setPort(8080);

        //server.addConnector(spdyh);
        server.addConnector(s);


        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(false);
        //resource_handler.setWelcomeFiles(new String[]{"index.html"});
        //resource_handler.setResourceBase(webapp);

        //NextProtoNego.debug = true;

        WebAppContext context = new WebAppContext();

        context.setDefaultsDescriptor(webapp + "/WEB-INF/web.xml");

        context.setResourceBase(webapp);
        context.setContextPath("/");
        //context.setParentLoaderPriority(true);

        HandlerList hlist = new HandlerList();
        hlist.setHandlers(new Handler[]{ resource_handler, context });

        server.setHandler(hlist);


        server.start();
        server.join();
    }
}
