package com.sayweee.app2;

import com.sayweee.wrapper.http.cookie.CookieStore;


import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class DefaultCookieStore implements CookieStore {
    private ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();
    @Override
    public void saveCookie(HttpUrl url, List<Cookie> cookie) {
        cookieStore.put(url.host(), cookie);
    }

    @Override
    public void saveCookie(HttpUrl url, Cookie cookie) {

    }

    @Override
    public List<Cookie> loadCookie(HttpUrl url) {
        return cookieStore.get(url);
    }

    @Override
    public List<Cookie> getAllCookie() {
        return null;
    }

    @Override
    public List<Cookie> getCookie(HttpUrl url) {
        return cookieStore.get(url);
    }

    @Override
    public boolean removeCookie(HttpUrl url, Cookie cookie) {
        return false;
    }

    @Override
    public boolean removeCookie(HttpUrl url) {
        return false;
    }

    @Override
    public boolean removeAllCookie() {
        return false;
    }
}
