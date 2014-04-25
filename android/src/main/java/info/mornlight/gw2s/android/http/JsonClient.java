package info.mornlight.gw2s.android.http;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import info.mornlight.gw2s.android.model.Json;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.*;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonClient {
    static ThreadSafeClientConnManager connManager;
    static {
        HttpParams params = new BasicHttpParams();
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        connManager = new ThreadSafeClientConnManager(params, schemeRegistry);
    }
    private static Logger log = Logger.getLogger(JsonClient.class.getCanonicalName());

    private boolean methodOverride = true;

    DefaultHttpClient http;
    protected static final String CHARSET = "UTF-8";
    protected static final String CONTENT_TYPE = "application/json";
    protected ObjectMapper mapper;
    protected TypeFactory typeFactory;

    public JsonClient() {
        http = new DefaultHttpClient(connManager, null);

        mapper = Json.getMapper();
        typeFactory = Json.getTypeFactory();
    }

    protected void initRequest(HttpRequestBase request) {
        request.addHeader("Accept", CONTENT_TYPE);
        request.addHeader("Content-Type", CONTENT_TYPE);
        request.addHeader("Content-Encoding", CHARSET);
    }
    
    protected Object invoke(HttpRequestBase request) throws IOException {
        return invoke(request, (JavaType)null);
    }

    protected Object invoke(HttpRequestBase request, Class<?> respCls) throws IOException {
        JavaType type = respCls == null ? null : typeFactory.constructType(respCls);
        return invoke(request, type);
    }

    protected Object invoke(HttpRequestBase request, JavaType type) throws IOException {
        HttpResponse response = http.execute(request);
        return processResponse(response, type);
    }

    protected HttpGet newGet(String uri) throws IOException {
        HttpGet request = new HttpGet(uri);
        initRequest(request);

        return request;
    }

    protected HttpRequestBase newDelete(String uri) throws IOException {
        HttpRequestBase request;
        if(methodOverride) {
            request = new HttpPost(uri);
            request.setHeader("X-HTTP-Method-Override", "DELETE");
            //request.getParams().setParameter("_method", "DELETE");
        } else {
            request = new HttpDelete(uri);
        }

        initRequest(request);

        return request;
    }

    protected HttpEntityEnclosingRequestBase newPut(String uri, Object data) throws IOException {
        HttpEntityEnclosingRequestBase request;
        if(methodOverride) {
            request = new HttpPost(uri);
            request.setHeader("X-HTTP-Method-Override", "PUT");
            //request.getParams().setParameter("_method", "PUT");
        } else {
            request = new HttpPut(uri);
        }

        initRequest(request);

        if (data != null) {
            String dataStr = mapper.writeValueAsString(data);
            request.setEntity(new StringEntity(dataStr, CHARSET));
        }

        return request;
    }

    protected HttpPost newPost(String uri, Object data) throws IOException {
        HttpPost request = new HttpPost(uri);
        initRequest(request);

        if (data != null) {
            String dataStr = mapper.writeValueAsString(data);
            request.setEntity(new StringEntity(dataStr, CHARSET));
        }

        return request;
    }

    private Object processResponse(HttpResponse response, JavaType type) throws IOException {
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            InputStream is = response.getEntity().getContent();
            String str = IOUtils.toString(is, CHARSET);

            log.warning("Error: " + response.getStatusLine().getStatusCode() + " " + str);

            /*Map<String, String> error = mapper.readValue(str, Map.class);
            String code = error.get("code");
            String message = error.get("message");
            throw ErrorUtils.convertException(code, message);*/
            throw new IOException("Error http code: " + response.getStatusLine().getStatusCode());
        }
        
        HttpEntity entity = response.getEntity();
        if(entity == null)
            return null;

        String str = null;
        try {
            //do not expect any return value
            if (type == null)
                return null;

            InputStream is = entity.getContent();
            str = IOUtils.toString(is, CHARSET);

            //log.info("Response: " + str);

            return mapper.readValue(str, type);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Parse json error: " + str, e);
            throw e;
        } finally {
            entity.consumeContent();
            //EntityUtils.consume(entity);
        }
        
    }
}
