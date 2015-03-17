package com.example.test.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Blob;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
    private static final String SERVER_IP = "10.0.2.2";
    private static final String SERVER_PORT = "8080";
    private static final String SERVER_ROOT = "Server";
    private static final String NETWORK_ERROR = "网络异常！";

    public static String getBASE_URL() {
        StringBuilder stringBuilder = new StringBuilder("http://");
        stringBuilder.append(SERVER_IP);
        stringBuilder.append(":");
        stringBuilder.append(SERVER_PORT);
        stringBuilder.append("/");
        stringBuilder.append(SERVER_ROOT);
        stringBuilder.append("/");
        return stringBuilder.toString();
    }

    public static HttpGet getHttpGet(String url) {
        HttpGet request = new HttpGet(url);
        return request;
    }

    public static HttpPost getHttpPost(String url) {
        HttpPost request = new HttpPost(url);
        return request;
    }

    public static HttpResponse getHttpResponse(HttpGet request) throws ClientProtocolException, IOException {
        HttpResponse response = new DefaultHttpClient().execute(request);
        return response;
    }

    public static HttpResponse getHttpResponse(HttpPost request) throws ClientProtocolException, IOException {
        HttpResponse response = new DefaultHttpClient().execute(request);
        return response;
    }


    public static String queryStringForPost(String url) {
        HttpPost request = HttpUtil.getHttpPost(url);
        String result = null;
        try {
            HttpResponse response = HttpUtil.getHttpResponse(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(),"utf-8");
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result = NETWORK_ERROR;
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result = NETWORK_ERROR;
            return result;
        }
        return null;
    }

    public static String queryStringForPost(HttpPost request) {
        String result = null;
        try {
            HttpResponse response = HttpUtil.getHttpResponse(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result = NETWORK_ERROR;
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result = NETWORK_ERROR;
            return result;
        }
        return null;
    }

    public static String queryStringForGet(String url) {
        HttpGet request = HttpUtil.getHttpGet(url);
        String result = null;
        try {
            HttpResponse response = HttpUtil.getHttpResponse(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result = NETWORK_ERROR;
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result = NETWORK_ERROR;
            return result;
        }
        return null;
    }

    public static Bitmap queryInputStreamForPost(String url)
    {
        HttpGet request = HttpUtil.getHttpGet(url);
        InputStream result = null;
        try {
            HttpResponse response = HttpUtil.getHttpResponse(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(entity);
                result = bufferedHttpEntity.getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(result);
                return bitmap;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitMapByUrl(String url){
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static byte[] blobToByte(Blob blob) throws Exception {

        byte[] b = null;
        try {
            if (blob != null) {
                long in = 0;
                b = blob.getBytes(in, (int) (blob.length()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("fault");
        }
        return b;
    }

    public static byte[] BitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
