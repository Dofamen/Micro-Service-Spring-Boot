package org.sid.secservice.sec.filters;

public class JWTUtil {
    public static final String SECRET="MySecretKey123";
    public static final String AUTH_HEADER="Authorization";
    public static final int EXPIRE_ACCESS_TOKEN= 2*60*100;
    public static final int EXPIRE_REFRESH_TOKEN= 15*60*100;
    public static final String PREFIX= "Bearer ";
}
