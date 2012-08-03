package me.atshughson.softeng325.a01.httpserver;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;

/***
 * Represents HTTP responses.
 * 
 * @author ahug048
 * 
 */
public class Response {
    private static final String SITE_DIR = "site";
    private Request _request;

    public Response(Request r) {
        _request = r;
    }

    /**
     * Get the file extension of a file given a URI.
     * 
     * @param uri
     *            The URI from which to extract the file extension.
     * @return A string containing the file extension or "html" if the URI was
     *         simply "/".
     */
    private String getFileExtension(String uri) {
        String[] parts = uri.split("\\.");
        String fileExtension = "";

        // If a file was specified, we hope it has a file extension. Otherwise,
        // we assume that the request was for / and return index.html.
        if (parts.length > 1)
            fileExtension = parts[parts.length - 1];
        else
            fileExtension = "html";

        return fileExtension;
    }

    /**
     * Get the mimetype corresponding to a given file extension.
     * 
     * @param fileExtension
     *            The file extension from which to derive the mimetype.
     * @return A string containing the mimetype or null if none can be
     *         derived/the file type is not supported.
     */
    private String getMimeType(String fileExtension) {
        String mimetype = null; // default mimetype.
        fileExtension = fileExtension.toLowerCase();

        if (fileExtension.equals("html") || fileExtension.equals("htm")) {
            mimetype = "text/html";
        } else if (fileExtension.equals("css")) {
            mimetype = "text/css";
        } else if (fileExtension.equals("jpeg") || fileExtension.equals("jpg")) {
            mimetype = "image/jpeg";
        } else if (fileExtension.equals("gif")) {
            mimetype = "image/gif";
        }

        return mimetype;
    }

    /**
     * Get an input stream to read the requested resource from disk.
     * 
     * @param uri
     *            The uri of the requested resource.
     * @return BufferedInputStream for reading the resource or null if the
     *         resource does not exist.
     * @throws FileNotFoundException
     */
    private BufferedInputStream getResource(String uri)
            throws FileNotFoundException {

        if (uri.equals("/"))
            uri = "index.html";
        else
            uri = uri.substring(1); // remove the leading "/".

        File f = new File(SITE_DIR + File.separator + uri);

        if (f.exists()) {
            return new BufferedInputStream(new FileInputStream(f));
        } else {
            return null;
        }
    }

    /**
     * Get the header information for the HTTP response. Must be called after
     * getResource(...) in order to be correct.
     * 
     * @param statusCode
     * 
     * @return A string containing the headers of the HTTP response, including
     *         the blank line at the end which delimits headers/body.
     */
    private String getHeaders(HTTPStatusCode statusCode) {
        String headers = "HTTP/1.0 " + statusCode.toString() + "\r\n"
                + "Server: ahug048 Server\r\n" + "Connection: close\r\n";

        if (statusCode != HTTPStatusCode.NOT_FOUND
                && statusCode != HTTPStatusCode.UNSUPPORTED_MEDIA_TYPE) {
            headers += "Content-Type: "
                    + getMimeType(getFileExtension(_request.getUri()))
                    + "\r\n\r\n";
        }

        return headers;
    }

    /**
     * Send the headers and the resource to the host which requested it.
     * 
     * @param out
     *            A socket output stream to write the HTTP response to the
     *            network.
     * @throws IOException
     */
    public void send(DataOutputStream out) throws IOException {
        HTTPStatusCode statusCode = HTTPStatusCode.OK;
        BufferedInputStream resource = null;

        if (!_request.isValid()) {
            // If the request is invalid we say it is a bad request.
            statusCode = HTTPStatusCode.BAD_REQUEST;
        } else if (!_request.getMethod().equalsIgnoreCase("GET")) {
            // This server only supports the GET HTTP method.
            statusCode = HTTPStatusCode.NOT_IMPLEMENTED;
        } else if ((resource = getResource(_request.getUri())) == null) {
            // If the file does not exist, we return a 404.
            statusCode = HTTPStatusCode.NOT_FOUND;
        } else if (getMimeType(getFileExtension(_request.getUri())) == null) {
            // If the file is of a format that we do not support, return a 415.
            statusCode = HTTPStatusCode.UNSUPPORTED_MEDIA_TYPE;
        }

        try {
            // First write the headers to the output stream.
            out.writeBytes(getHeaders(statusCode));

            // Send the resource.
            if (resource != null) {
                int b;
                while ((b = resource.read()) != -1) {
                    out.write(b);
                }

                resource.close();
            }
        } catch (SocketException e) {
            System.out
                    .println("Could not service request, socket closed.");
        }
    }
}
