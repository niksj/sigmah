package org.sigmah.offline.indexeddb;

/*
 * #%L
 * Sigmah
 * %%
 * Copyright (C) 2010 - 2016 URD
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Native IndexedDB object store (equivalent to an SQL table).
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 * @param <V> Type of the stored objects.
 * @param <K> Type of the key.
 */
final class IDBObjectStore<K, V> extends JavaScriptObject {
	
	/**
	 * Empty protected constructor. Required for subclasses of JavaScriptObject.
	 */
	protected IDBObjectStore() {
		// Empty.
	}
	
	/**
	 * Adds a new object to the object store. If an object with the same key
	 * already exists, an exception will be thrown.
	 * <br>
	 * Result is the key of the added object.
	 * 
	 * @param object
	 *			Object to add.
	 * @return A request to add the given object.
	 */
	public native IDBRequest<K> add(V object) /*-{
		return this.add(object);
	}-*/;
	
	public native IDBRequest<K> add(V object, K key) /*-{
		return this.add(object, key);
	}-*/;
	
	public native IDBRequest<K> add(V object, int key) /*-{
		return this.add(object, key);
	}-*/;
	
	public native IDBRequest<K> add(V object, double key) /*-{
		return this.add(object, key);
	}-*/;
	
	public native IDBRequest<K> add(V object, float key) /*-{
		return this.add(object, key);
	}-*/;
	
	public native IDBRequest<K> add(V object, char key) /*-{
		return this.add(object, key);
	}-*/;
	
	public native IDBRequest<K> add(V object, boolean key) /*-{
		return this.add(object, key);
	}-*/;
	
	/**
	 * Adds or update an object to the object store.
	 * Result is the key of the added object.
	 * 
	 * @param object
	 *			Object to update.
	 * @return A request to update the given object.
	 */
	public native IDBRequest<K> put(V object) /*-{
		return this.put(object);
	}-*/;
	
	public native IDBRequest<K> put(V object, K key) /*-{
		return this.put(object, key);
	}-*/;
	
	public native IDBRequest<K> put(V object, int key) /*-{
		return this.put(object, key);
	}-*/;
	
	public native IDBRequest<K> put(V object, double key) /*-{
		return this.put(object, key);
	}-*/;
	
	public native IDBRequest<K> put(V object, float key) /*-{
		return this.put(object, key);
	}-*/;
	
	public native IDBRequest<K> put(V object, char key) /*-{
		return this.put(object, key);
	}-*/;
	
	public native IDBRequest<K> put(V object, boolean key) /*-{
		return this.put(object, key);
	}-*/;
	
	public native IDBRequest clear() /*-{
		return this.clear();
	}-*/;
	
	public native IDBRequest<Integer> count() /*-{
		return this.count();
	}-*/;
	
	public native IDBRequest<Integer> count(Object value) /*-{
		return this.count();
	}-*/;
	
	public native IDBRequest<Integer> count(int value) /*-{
		return this.count();
	}-*/;
	
	public native IDBRequest<Integer> count(float value) /*-{
		return this.count();
	}-*/;
	
	public native IDBRequest<Integer> count(double value) /*-{
		return this.count();
	}-*/;
	
	public native IDBRequest<Integer> count(char value) /*-{
		return this.count();
	}-*/;
	
	public native IDBRequest<Integer> count(boolean value) /*-{
		return this.count();
	}-*/;
	
	public native IDBRequest<Integer> count(IDBKeyRange keyRange) /*-{
		return this.count(keyRange);
	}-*/;
	
	/**
	 * Removes the object associated with the given key.
	 * 
	 * @param key
	 *			Key of the object to delete.
	 * @return A request to delete the object with the given key.
	 */
	public native IDBRequest<Object> delete(K key) /*-{
		return this['delete'](key);
	}-*/;
	
	public native IDBRequest<Object> delete(int key) /*-{
		return this['delete'](key);
	}-*/;
	
	public native IDBRequest<Object> delete(double key) /*-{
		return this['delete'](key);
	}-*/;
	
	public native IDBRequest<Object> delete(float key) /*-{
		return this['delete'](key);
	}-*/;
	
	public native IDBRequest<Object> delete(char key) /*-{
		return this['delete'](key);
	}-*/;
	
	public native IDBRequest<Object> delete(boolean key) /*-{
		return this['delete'](key);
	}-*/;
	
	/**
	 * Retrieves the object associated with the given key.
	 * 
	 * @param key
	 *			Key of the object to search.
	 * @return A request to retrieve the object with the given key.
	 */
	public native IDBRequest<V> get(K key) /*-{
		return this.get(key);
	}-*/;
	
	public native IDBRequest<V> get(int key) /*-{
		return this.get(key);
	}-*/;
	
	public native IDBRequest<V> get(double key) /*-{
		return this.get(key);
	}-*/;
	
	public native IDBRequest<V> get(float key) /*-{
		return this.get(key);
	}-*/;
	
	public native IDBRequest<V> get(char key) /*-{
		return this.get(key);
	}-*/;
	
	public native IDBRequest<V> get(boolean key) /*-{
		return this.get(key);
	}-*/;
	
	public native IDBIndex<K, V> index(String name) /*-{
		return this.index(name);
	}-*/;
	
	public native IDBRequest<IDBCursor<V>> openCursor() /*-{
		return this.openCursor();
	}-*/;
	
	public native IDBRequest<IDBCursor<V>> openCursor(IDBKeyRange keyRange) /*-{
		return this.openCursor(keyRange);
	}-*/;
	
	public native IDBRequest<IDBCursor<V>> openCursor(IDBKeyRange keyRange, String order) /*-{
		return this.openCursor(keyRange, order);
	}-*/;
	
	public native void createIndex(String name, String keyPath) /*-{
		this.createIndex(name, keyPath);
	}-*/;
	
	public native void createIndex(String name, String keyPath, boolean unique, boolean multiEntry) /*-{
		this.createIndex(name, keyPath, {
			"unique": unique,
			"multiEntry": multiEntry
		});
	}-*/;
	
	public native final String getName() /*-{
		return this.objectStore;
	}-*/;
	
	public native final IDBTransaction getTransaction() /*-{
		return this.transaction;
	}-*/;
	
}
