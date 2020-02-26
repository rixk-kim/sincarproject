package com.sincar.customer.util;

import com.sincar.customer.network.DataObject;

import java.util.ArrayList;

/**
 * 
 * @author haste
 *	
 * @ClassName : DataParser.java
 *	
 * @Description :
 *	
 * @Modification Information :
 *	
 *       수정일           수정자                수정내용
 *    -----------    -----------     -------------------
 *	  2020. 2. 17	    			   신규작성
 *	
 *	
 *	
 *	@since 2020. 2. 17.
 */
public class DataParser {
	
	/**
	 * 파라미터에 해당하는 DataObject ArrayList 반환
	 * @param list DataObject arrayList
	 * @param param 파라미터
	 * @return DataObject arrayList
	 */
	public static ArrayList<DataObject> getFromParamtoArray(ArrayList<DataObject> list, String param){
		if(null == param) return null;
		ArrayList<DataObject> item = new ArrayList<DataObject>();
		param = param.trim();
		for(DataObject v : list){
		    if(v == null)
		        continue;
			if(v.getKey().equals(param)){
				item.add(v);
			}
		}

		return item;
	}

	public static ArrayList<DataObject> getFromParamtoArray1(ArrayList<DataObject> aaa, ArrayList<DataObject> list, String param){
		if(null == param) return null;
		//ArrayList<DataObject> item = new ArrayList<DataObject>();
		param = param.trim();
		for(DataObject v : list){
			if(v == null)
				continue;
			if(v.getKey().equals(param)){
				aaa.add(v);
			}
		}

		return aaa;
	}
	
	/**
	 * 파라미터에 해당하는 DataObject 반환
	 * @param list DataObject arrayList
	 * @param param 파라미터
	 * @return DataObject
	 */
	public static DataObject getFromParamtoItem(ArrayList<DataObject> list, String param){
		if(null == param) return null;
		DataObject item = null;
		for(DataObject v : list){
		    if(v == null)
		        continue;
			if(v.getKey().equals(param)){
				item = v;
				break;
			}
		}

		return item;
	}
	
}
