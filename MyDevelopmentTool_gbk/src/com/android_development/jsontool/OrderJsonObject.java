package com.android_development.jsontool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 * 
 * 问题描述:在android 4.4.2(android-19)上，JsonObject用的是HashMap(无序)，在android 4.4w(android-20)上，JsonObject用的是LinkedHashMap(有序)
 * 
 * 作用：用jsonobject传参数时，生成的字符串与json put的顺序不一致，导致生成的字符串顺序乱了，
 * 		用OrderJsonObject来传参时固定顺序(生成的字符串与put顺序一致)， 不可用于解析json
 * 
 * 
 * */
public class OrderJsonObject extends JSONObject{
	 private static final Double NEGATIVE_ZERO = -0d;

	    /**
	     * A sentinel value used to explicitly define a name with no value. Unlike
	     * {@code null}, names with this value:
	     * <ul>
	     *   <li>show up in the {@link #names} array
	     *   <li>show up in the {@link #keys} iterator
	     *   <li>return {@code true} for {@link #has(String)}
	     *   <li>do not throw on {@link #get(String)}
	     *   <li>are included in the encoded JSON string.
	     * </ul>
	     *
	     * <p>This value violates the general contract of {@link Object#equals} by
	     * returning true when compared to {@code null}. Its {@link #toString}
	     * method returns "null".
	     */
	    public static final Object NULL = new Object() {
	        @Override public boolean equals(Object o) {
	            return o == this || o == null; // API specifies this broken equals implementation
	        }
	        @Override public String toString() {
	            return "null";
	        }
	    };

	    private final LinkedHashMap<String, Object> nameValuePairs;

	    /**
	     * Creates a {@code JSONObject} with no name/value mappings.
	     */
	    public OrderJsonObject() {
	        nameValuePairs = new LinkedHashMap<String, Object>();
	    }

	    /**
	     * Creates a new {@code JSONObject} by copying all name/value mappings from
	     * the given map.
	     *
	     * @param copyFrom a map whose keys are of type {@link String} and whose
	     *     values are of supported types.
	     * @throws NullPointerException if any of the map's keys are null.
	     */
	    /* (accept a raw type for API compatibility) */
	    public OrderJsonObject(Map copyFrom) {
	        this();
	        Map<?, ?> contentsTyped = (Map<?, ?>) copyFrom;
	        for (Map.Entry<?, ?> entry : contentsTyped.entrySet()) {
	            /*
	             * Deviate from the original by checking that keys are non-null and
	             * of the proper type. (We still defer validating the values).
	             */
	            String key = (String) entry.getKey();
	            if (key == null) {
	                throw new NullPointerException("key == null");
	            }
	            nameValuePairs.put(key, wrap(entry.getValue()));
	        }
	    }


	    /**
	     * Creates a new {@code JSONObject} by copying mappings for the listed names
	     * from the given object. Names that aren't present in {@code copyFrom} will
	     * be skipped.
	     */
	    public OrderJsonObject(JSONObject copyFrom, String[] names) throws JSONException {
	        this();
	        for (String name : names) {
	            Object value = copyFrom.opt(name);
	            if (value != null) {
	                nameValuePairs.put(name, value);
	            }
	        }
	    }

	    /**
	     * Returns the number of name/value mappings in this object.
	     */
	    public int length() {
	        return nameValuePairs.size();
	    }

	    /**
	     * Maps {@code name} to {@code value}, clobbering any existing name/value
	     * mapping with the same name.
	     *
	     * @return this object.
	     */
	    public OrderJsonObject put(String name, boolean value) throws JSONException {
	        nameValuePairs.put(checkName(name), value);
	        return this;
	    }

	    /**
	     * Maps {@code name} to {@code value}, clobbering any existing name/value
	     * mapping with the same name.
	     *
	     * @param value a finite value. May not be {@link Double#isNaN() NaNs} or
	     *     {@link Double#isInfinite() infinities}.
	     * @return this object.
	     */
	    public OrderJsonObject put(String name, double value) throws JSONException {
	    	nameValuePairs.put(checkName(name), value);
	        return this;
	    }

	    /**
	     * Maps {@code name} to {@code value}, clobbering any existing name/value
	     * mapping with the same name.
	     *
	     * @return this object.
	     */
	    public OrderJsonObject put(String name, int value) throws JSONException {
	        nameValuePairs.put(checkName(name), value);
	        return this;
	    }

	    /**
	     * Maps {@code name} to {@code value}, clobbering any existing name/value
	     * mapping with the same name.
	     *
	     * @return this object.
	     */
	    public OrderJsonObject put(String name, long value) throws JSONException {
	        nameValuePairs.put(checkName(name), value);
	        return this;
	    }

	    /**
	     * Maps {@code name} to {@code value}, clobbering any existing name/value
	     * mapping with the same name. If the value is {@code null}, any existing
	     * mapping for {@code name} is removed.
	     *
	     * @param value a {@link JSONObject}, {@link JSONArray}, String, Boolean,
	     *     Integer, Long, Double, {@link #NULL}, or {@code null}. May not be
	     *     {@link Double#isNaN() NaNs} or {@link Double#isInfinite()
	     *     infinities}.
	     * @return this object.
	     */
	    public OrderJsonObject put(String name, Object value) throws JSONException {
	        if (value == null) {
	            nameValuePairs.remove(name);
	            return this;
	        }
	        /*if (value instanceof Number) {
	            // deviate from the original by checking all Numbers, not just floats & doubles
	            JSON.checkDouble(((Number) value).doubleValue());
	        }*/
	        nameValuePairs.put(checkName(name), value);
	        return this;
	    }

	    /**
	     * Equivalent to {@code put(name, value)} when both parameters are non-null;
	     * does nothing otherwise.
	     */
	    public OrderJsonObject putOpt(String name, Object value) throws JSONException {
	        if (name == null || value == null) {
	            return this;
	        }
	        return put(name, value);
	    }

	    String checkName(String name) throws JSONException {
	        if (name == null) {
	            throw new JSONException("Names must be non-null");
	        }
	        return name;
	    }

	    /**
	     * Removes the named mapping if it exists; does nothing otherwise.
	     *
	     * @return the value previously mapped by {@code name}, or null if there was
	     *     no such mapping.
	     */
	    public Object remove(String name) {
	        return nameValuePairs.remove(name);
	    }

	    /**
	     * Returns true if this object has no mapping for {@code name} or if it has
	     * a mapping whose value is {@link #NULL}.
	     */
	    public boolean isNull(String name) {
	        Object value = nameValuePairs.get(name);
	        return value == null || value == NULL;
	    }

	    /**
	     * Returns true if this object has a mapping for {@code name}. The mapping
	     * may be {@link #NULL}.
	     */
	    public boolean has(String name) {
	        return nameValuePairs.containsKey(name);
	    }

	    /**
	     * Returns the value mapped by {@code name}, or throws if no such mapping exists.
	     *
	     * @throws JSONException if no such mapping exists.
	     */
	    public Object get(String name) throws JSONException {
	        Object result = nameValuePairs.get(name);
	        if (result == null) {
	            throw new JSONException("No value for " + name);
	        }
	        return result;
	    }

	    /**
	     * Returns an iterator of the {@code String} names in this object. The
	     * returned iterator supports {@link Iterator#remove() remove}, which will
	     * remove the corresponding mapping from this object. If this object is
	     * modified after the iterator is returned, the iterator's behavior is
	     * undefined. The order of the keys is undefined.
	     */
	    public Iterator<String> keys() {
	        return nameValuePairs.keySet().iterator();
	    }

	    /**
	     * Returns the set of {@code String} names in this object. The returned set
	     * is a view of the keys in this object. {@link Set#remove(Object)} will remove
	     * the corresponding mapping from this object and set iterator behaviour
	     * is undefined if this object is modified after it is returned.
	     *
	     * See {@link #keys()}.
	     *
	     * @hide.
	     */
	    public Set<String> keySet() {
	        return nameValuePairs.keySet();
	    }

	    /**
	     * Returns an array containing the string names in this object. This method
	     * returns null if this object contains no mappings.
	     */
	    public JSONArray names() {
	        return nameValuePairs.isEmpty()
	                ? null
	                : new JSONArray(new ArrayList<String>(nameValuePairs.keySet()));
	    }

	    /**
	     * Encodes this object as a compact JSON string, such as:
	     * <pre>{"query":"Pizza","locations":[94043,90210]}</pre>
	     */
	    @Override 
	    public String toString() {
	        try {
	            JSONStringer stringer = new JSONStringer();
	            writeTo(stringer);
	            return stringer.toString();
	        } catch (JSONException e) {
	            return null;
	        }
	    }

	    void writeTo(JSONStringer stringer) throws JSONException {
	        stringer.object();
	        for (Map.Entry<String, Object> entry : nameValuePairs.entrySet()) {
	            stringer.key(entry.getKey()).value(entry.getValue());
	        }
	        stringer.endObject();
	    }

	    /**
	     * Wraps the given object if necessary.
	     *
	     * <p>If the object is null or , returns {@link #NULL}.
	     * If the object is a {@code JSONArray} or {@code JSONObject}, no wrapping is necessary.
	     * If the object is {@code NULL}, no wrapping is necessary.
	     * If the object is an array or {@code Collection}, returns an equivalent {@code JSONArray}.
	     * If the object is a {@code Map}, returns an equivalent {@code JSONObject}.
	     * If the object is a primitive wrapper type or {@code String}, returns the object.
	     * Otherwise if the object is from a {@code java} package, returns the result of {@code toString}.
	     * If wrapping fails, returns null.
	     */
	    public static Object wrap(Object o) {
	        if (o == null) {
	            return NULL;
	        }
	        if (o instanceof JSONArray || o instanceof JSONObject) {
	            return o;
	        }
	        if (o.equals(NULL)) {
	            return o;
	        }
	        try {
	            if (o instanceof Collection) {
	                return new JSONArray((Collection) o);
	            } else if (o.getClass().isArray()) {
	                return new JSONArray(o);
	            }
	            if (o instanceof Map) {
	                return new JSONObject((Map) o);
	            }
	            if (o instanceof Boolean ||
	                o instanceof Byte ||
	                o instanceof Character ||
	                o instanceof Double ||
	                o instanceof Float ||
	                o instanceof Integer ||
	                o instanceof Long ||
	                o instanceof Short ||
	                o instanceof String) {
	                return o;
	            }
	            if (o.getClass().getPackage().getName().startsWith("java.")) {
	                return o.toString();
	            }
	        } catch (Exception ignored) {
	        }
	        return null;
	    }
}
