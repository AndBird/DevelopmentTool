package com.android_development.tool;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;


/**
 * ����:���Ϲ�����
 * */
public class CollectionsUtil {
	/**
	 * ʹ�� Map��key��������
	 * @param map
	 * @param asc: true ����
	 * @return
	 */
	public static Map<String, String> sortMapByKey(Map<String, String> map, boolean asc) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, String> sortMap = null;
		if(asc){
			sortMap = new TreeMap<String, String>();//TreeMapĬ���������
		}else{
			sortMap = new TreeMap<String, String>(new MapKeyComparator());
		}
		sortMap.putAll(map);
		return sortMap;
	}
	
	static class MapKeyComparator implements Comparator<String>{
		@Override
		public int compare(String str1, String str2) {
			//return str1.compareTo(str2);//����
			return str2.compareTo(str1);//����
		}
	}
	
	
	/**��������
	 * Collections.sort(list, new ObjectKeyComparator());
	 * 
	 * */
	static class ObjectKeyComparator implements Comparator<Object>{
		private Collator collator = Collator.getInstance();//����鿴����api��� 
		
		@Override
		public int compare(Object obj1, Object obj2) {
			//return str1.compareTo(str2);//����
			//return str2.compareTo(str1);//����
			
			//return collator.compare(obj1, obj2);
			return 0;
		}
	}
}
