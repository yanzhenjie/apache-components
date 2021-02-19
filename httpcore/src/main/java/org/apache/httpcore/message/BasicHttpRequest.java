/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.httpcore.message;

import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpVersion;
import org.apache.httpcore.ProtocolVersion;
import org.apache.httpcore.RequestLine;
import org.apache.httpcore.util.Args;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Basic implementation of {@link HttpRequest}.
 *
 * @since 4.0
 */
public class BasicHttpRequest extends AbstractHttpMessage implements HttpRequest {

    private final Socket socket;
    private final String method;
    private final String uri;

    private RequestLine requestline;

    /**
     * Creates an instance of this class using the given request method
     * and URI.
     *
     * @param method request method.
     * @param uri request URI.
     */
    public BasicHttpRequest(final Socket socket, final String method, final String uri) {
        super();
        this.socket = socket;
        this.method = Args.notNull(method, "Method name");
        this.uri = Args.notNull(uri, "Request URI");
        this.requestline = null;
    }

    /**
     * Creates an instance of this class using the given request method, URI
     * and the HTTP protocol version.
     *
     * @param method request method.
     * @param uri request URI.
     * @param ver HTTP protocol version.
     */
    public BasicHttpRequest(final Socket socket, final String method,
                            final String uri, final ProtocolVersion ver) {
        this(socket, new BasicRequestLine(method, uri, ver));
    }

    /**
     * Creates an instance of this class using the given request line.
     *
     * @param requestline request line.
     */
    public BasicHttpRequest(final Socket socket, final RequestLine requestline) {
        super();
        this.socket = socket;
        this.requestline = Args.notNull(requestline, "Request line");
        this.method = requestline.getMethod();
        this.uri = requestline.getUri();
    }

    /**
     * Returns the HTTP protocol version to be used for this request.
     *
     * @see #BasicHttpRequest(Socket, String, String)
     */
    @Override
    public ProtocolVersion getProtocolVersion() {
        return getRequestLine().getProtocolVersion();
    }

    /**
     * Returns the request line of this request.
     *
     * @see #BasicHttpRequest(Socket, String, String)
     */
    @Override
    public RequestLine getRequestLine() {
        if (this.requestline == null) {
            this.requestline = new BasicRequestLine(this.method, this.uri, HttpVersion.HTTP_1_1);
        }
        return this.requestline;
    }

    private String localName;

    @Override
    public String getLocalName() {
        if (localName != null) {
            return localName;
        }

        SocketAddress socketAddress = socket.getLocalSocketAddress();
        if (socketAddress instanceof InetSocketAddress) {
            localName = ((InetSocketAddress) socketAddress).getHostName();
        }
        return localName;
    }

    private String localAddr;

    @Override
    public String getLocalAddr() {
        if (localAddr != null) {
            return localAddr;
        }

        SocketAddress socketAddress = socket.getLocalSocketAddress();
        if (socketAddress instanceof InetSocketAddress) {
            localAddr = ((InetSocketAddress) socketAddress).getAddress().getHostAddress();
        }
        return localAddr;
    }

    private int localPort = -1;

    @Override
    public int getLocalPort() {
        if (localPort != -1) {
            return localPort;
        }

        SocketAddress socketAddress = socket.getLocalSocketAddress();
        if (socketAddress instanceof InetSocketAddress) {
            localPort = ((InetSocketAddress) socketAddress).getPort();
        }
        return localPort;
    }

    private String remoteAddr;

    @Override
    public String getRemoteAddr() {
        if (remoteAddr != null) {
            return remoteAddr;
        }

        SocketAddress socketAddress = socket.getRemoteSocketAddress();
        if (socketAddress instanceof InetSocketAddress) {
            remoteAddr = ((InetSocketAddress) socketAddress).getAddress().getHostAddress();
        }
        return remoteAddr;
    }

    private String remoteHost;

    @Override
    public String getRemoteHost() {
        if (remoteHost != null) {
            return remoteHost;
        }

        SocketAddress socketAddress = socket.getRemoteSocketAddress();
        if (socketAddress instanceof InetSocketAddress) {
            remoteHost = ((InetSocketAddress) socketAddress).getAddress().getHostName();
            if (remoteAddr == null) {
                remoteAddr = ((InetSocketAddress) socketAddress).getAddress().getHostAddress();
            }
        }
        return remoteHost;
    }

    private int remotePort = -1;

    @Override
    public int getRemotePort() {
        if (remotePort != -1) {
            return remotePort;
        }

        SocketAddress socketAddress = socket.getRemoteSocketAddress();
        if (socketAddress instanceof InetSocketAddress) {
            remotePort = ((InetSocketAddress) socketAddress).getPort();
        }
        return remotePort;
    }

    @Override
    public String toString() {
        return this.method + ' ' + this.uri + ' ' + this.headergroup;
    }

}
