package com.sincar.customer.preference;

public interface PreferenceModule {
    /**
     * Preference 저장처리
     * @return
     */
    public abstract boolean saveKey();
    
    /**
     * boolean 값 저장 save 메소드 자동 실행
     * @param key
     * @param value
     */
    public abstract void putBoolean(String key, boolean value);
    /**
     * int 값 저장 save 메소드 자동 실행
     * @param key
     * @param value
     */
    public abstract void putInteger(String key, int value);
    
    /**
     * String 값 저장 save 메소드 자동 실행
     * @param key
     * @param value
     */
    public abstract void putString(String key, String value);
    /**
     * String 값 반환
     * @param key
     * @param value
     * @return
     */
    public abstract String getString(String key, String value);
    /**
     * Boolean 값 반환
     * @param key
     * @param value
     * @return
     */
    public abstract boolean getBoolean(String key, boolean value);
    /**
     * Int 값 반환
     * @param key
     * @param value
     * @return
     */
    public abstract int getInteger(String key, int value);
}
