package com.example.rememberme.BarcodeScannerHelper;

public class ScannedResult {
    private static ScannedResult instance;
    private static String code;

    public ScannedResult getInstance() {
        if (instance == null){
            instance = new ScannedResult();
        }
        return instance;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}
