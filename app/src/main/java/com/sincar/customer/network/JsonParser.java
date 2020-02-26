package com.sincar.customer.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * sincar Json 데이터 Parser
 */
public class JsonParser {

    public static ArrayList<DataObject> getStoreData(String jsonString) {
        jsonString = jsonString.replace("\n", "");
        if (jsonString.indexOf("<body>") != -1) {
            jsonString = jsonString.substring(jsonString.indexOf("<body>") + 6, jsonString.indexOf("</body>"));
        }

        ArrayList<DataObject> item = new ArrayList<DataObject>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                DataObject items = null;
                String key = iterator.next();
                Object object = jsonObject.get(key);
                if (object instanceof JSONArray) {
//                    Log.d("haste", key +" is array");
                    JSONArray array = (JSONArray) object;
                    item.addAll(getFromParamtoArray(array, key));
                } else if (object instanceof JSONObject) {
//                    Log.d("haste", key +" is object");
                    items = getFromParamtoItem((JSONObject) object, key);
                } else if (object instanceof String) {
                    items = new DataObject(key, object.toString());
                }
                item.add(items);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return item;
    }



    /**
     * JsonObject 데이터 DataObject 파싱
     * @param jsonString
     * @return
     */
    public static ArrayList<DataObject> getData(String jsonString) {
        jsonString = jsonString.replace("\n", "");
        if (jsonString.indexOf("<body>") != -1) {
            jsonString = jsonString.substring(jsonString.indexOf("<body>") + 6, jsonString.indexOf("</body>"));
        }

        ArrayList<DataObject> item = new ArrayList<DataObject>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                DataObject items = null;
                String key = iterator.next();
                Object object = jsonObject.get(key);
                if (object instanceof JSONArray) {
//                    Log.d("haste", key +" is array");
                    JSONArray array = (JSONArray) object;
                    item.addAll(getFromParamtoArray(array, key));
                } else if (object instanceof JSONObject) {
//                    Log.d("haste", key +" is object");
                    items = getFromParamtoItem((JSONObject) object, key);
                } else if (object instanceof String) {
                    items = new DataObject(key, object.toString());
                }
                item.add(items);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return item;
    }

    /**
     * JsonObject 중 해당 key 값에 해당하는 데이터 리턴
     * @param jsonString
     * @param param
     * @return
     */
    public static DataObject getFromParamtoItem(String jsonString, String param) {
        if (null == param) return null;
        DataObject item = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject child = jsonObject.getJSONObject(param);
            Iterator<String> iterator = child.keys();
            ArrayList<ParseValue> list = new ArrayList<ParseValue>();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = child.getString(key);
                if (value.equals("")) {
                    continue;
                }
                ParseValue parseValue = new ParseValue(key, value);
                list.add(parseValue);
            }
            item = new DataObject(param, list);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return item;
    }

    /**
     * JsonObject 중
     * @param jsonObject
     * @param param
     * @return
     */
    public static DataObject getFromParamtoItem(JSONObject jsonObject, String param) {
        if (null == param) return null;
        DataObject item = null;
        try {
            Iterator<String> iterator = jsonObject.keys();
            ArrayList<ParseValue> list = new ArrayList<ParseValue>();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = jsonObject.getString(key);
                if (value.equals("")) {
                    continue;
                }
                ParseValue parseValue = new ParseValue(key, value);
                list.add(parseValue);
            }
            item = new DataObject(param, list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }

    public static ArrayList<DataObject> getFromParamtoArray(JSONArray array, String param) {
        if (null == param) return null;

        ArrayList<DataObject> item = new ArrayList<DataObject>();
        try {

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Iterator<String> iterator = object.keys();
                DataObject items = null;
                ArrayList<ParseValue> list = new ArrayList<ParseValue>();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    String value = object.getString(key);
                    if (value.equals("")) {
                        continue;
                    }
                    ParseValue parseValue = new ParseValue(key, value);
                    list.add(parseValue);
                }
                items = new DataObject(param, list);
                item.add(items);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }
}
