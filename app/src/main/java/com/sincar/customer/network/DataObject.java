package com.sincar.customer.network;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 2019.03.29 박정기
 * 통신 데이터 용 DAO
 */
public class DataObject implements Serializable{

	private String mKey;
	private String mValue;
	private ArrayList<ParseValue> mDataList;
	private boolean isExistChild;

	private int mIndex;
	private int mTotalCount;
	private boolean isRequested = false;

	public DataObject(){

	}

	public DataObject(String key, String value){
		this.mKey = key;
		this.mValue = value;
		isExistChild = false;
	}

	public DataObject(String key, ArrayList<ParseValue> list){
		this.mKey = key;
		this.mDataList = list;
		isExistChild = true;
	}

	public void setValue(String param, String value){
		ParseValue parseValue = new ParseValue(param, value);
		if(mDataList == null){
			mDataList = new ArrayList<ParseValue>();
		}

		int count = containParam(param);
		if(count == -1){
			mDataList.add(parseValue);
		}else{
			mDataList.get(count).setValue(value);
		}
	}

	public int containParam(String param){
		if(mDataList==null) return -1;
		for(int i=0;i<mDataList.size();i++){
			ParseValue pv = mDataList.get(i);
			if(pv.getParam().equals(param)){
				return i;
			}
		}
		return -1;
	}

	public String getValue(String param){
		//exception
		if(null == param) {
			return "";
		}

		if(!isExistChild){
			if(mDataList != null){
				if(mDataList.size() == 0){
					return "";
				}
			}
			return "";
		}

		//Logic
		for(int i = 0; i < mDataList.size(); i++){
			ParseValue p = mDataList.get(i);
			if(param.equals(p.getParam())){
				return p.getValue();
			}
		}

		return "";
	}

	/**
	 * @return the mKey
	 */
	public String getKey() {
		return mKey;
	}

	/**
	 * @param key the mKey to set
	 */
	public void setKey(String key) {
		this.mKey = key;
	}

	/**
	 * @return the mValue
	 */
	public String getValue() {
		return mValue;
	}

	/**
	 * @param value the mValue to set
	 */
	public void setValue(String value) {
		this.mValue = value;
	}

	/**
	 * @return the mDataList
	 */
	public ArrayList<ParseValue> getDataList() {
		return mDataList;
	}

	/**
	 * @param dataList the mDataList to set
	 */
	public void setDataList(ArrayList<ParseValue> dataList) {
		this.mDataList = dataList;
	}
}
