package com.canhmai.ailatrieuphu.view.act;

public interface OnMainCallBack {
    void callBack(Object data, String key) ;
    void showFragment(String frgName, Object data, boolean isBacked);
    void showFragmentByTag(String name, Object data, String tag, boolean isBacked);
}
