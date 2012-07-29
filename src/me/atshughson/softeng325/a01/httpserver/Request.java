package me.atshughson.softeng325.a01.httpserver;

import java.io.BufferedReader;
import java.io.IOException;

/***
 * Represents HTTP requests.
 * 
 * @author ahug048
 * 
 */
public class Request {
    private String _method;
    private String _uri;
    private String _httpVersion;
    private boolean _valid = true;

    /**
     * Takes an socket input stream and parses the incoming HTTP request into
     * it's component parts.
     * 
     * @param in
     *            Input stream of the socket where the HTTP request is being
     *            made.
     */
    public Request(BufferedReader in) {
        String inputLine;

        try {
            if ((inputLine = in.readLine()) != null) {
                String[] parts = inputLine.split(" ");

                // We assume all requests will be of the form [method] [uri]
                // [httpversion]. If not, then this will fail.
                _method = parts[0];
                _uri = parts[1];
                _httpVersion = parts[2];
            } else {
                _valid = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            _valid = false;
        }
    }

    public String getMethod() {
        return _method;
    }

    public String getUri() {
        return _uri;
    }

    /**
     * Check if the request is valid.
     * 
     * @return Returns false if the request was not understood by the server
     *         either because it was empty or badly formatted.
     */
    public boolean isValid() {
        return _valid;
    }

    @Override
    public String toString() {
        return _method + " " + _uri + " " + _httpVersion;
    }
}
