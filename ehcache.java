/**
Ehcache -  это библиотека для кеширования.
SOR - system-of-record - авторитетный источник данных (обычно БД).
CacheManager - это класс для управления кешами, жизнь кэша привязана к его CacheManager.
Кэш - это набор Element. 
Element содержит пару Key-Value.
**/
//создать или вернуть синглтон с настройками по умолчанию
CacheManager manager = CacheManage.newInstance();
//создать потоково-безопасный кеш
Ehcache cache = new Cache("name",
		maxElementsInMemory,
		сохранять на диск?,
		эл-ты устаревают?,
		ttl,inactivity time);
//ttl - время жизни элемента - интервал м\у созданием элемента и его удалением из кеша, 0 по умолчанию.
//inactivity time - интервал, отсчитываемый с момента последнего доступа к
//элементу, по его прошествии элемент удаляется из кеша, 0 по умолчанию.
//перед использованием кеша его нужно добавить к менеджеру
manager.addcache(cache);
//CRUD операции с кешем
Element element = new Element("key1", "value1");
cache.put(element);
//This updates the entry for "key1"
cache.put(new Element("key1", "value2"));
//get element, get value from it
Element element = cache.get("key1");
Serializable value = element.getValue();
Object value = element.getObjectValue();
//Remove an element from a cache with a key of “key1”.
cache.remove("key1");
//shutdown CacheManager and all caches
manager.shutdown();
//===========================================================================================
//создаем кеш, работающий как SOR т.о. он пишет значения в SOR и периодически читает их оттуда
 
Cache cache = new Cache(...);
//добавим CacheWriter, служащий для записи значений в sor
cache.registerCacheWriter(new MyCacheWriter()); 
//обернем кеш в SelfPopulatingCache, это позволит получать из sor необходимые данные
cache = new SelfPopulatingCache(cache, new MyCacheEntryFactory()); 
/** 
* Implement the CacheEntryFactory that allows the cache to provide 
* the read-through strategy 
*/ 
private class MyCacheEntryFactory implements CacheEntryFactory 
{ 
   public Object createEntry(Object key) throws Exception 
   { 
       return readDataFromDataStore(key); 
   } 
} 
/** 
* Implement the CacheWriter interface which allows the cache to provide 
* the write-through or write-behind strategy. 
*/ 
private class MyCacheWriter implements CacheWriter 
   public CacheWriter clone(Ehcache cache) throws CloneNotSupportedException; 
   { 
       throw new CloneNotSupportedException(); 
   } 
   
   public void init() { } 
   
   void dispose() throws CacheException { } 
   
   void write(Element element) throws CacheException; 
   { 
       writeDataToDataStore(element.getKey(), element.getValue()); 
   } 
   
   void writeAll(Collection<Element> elements) throws CacheException 
   { 
       for (Element element : elements) { 
           write(element); 
       } 
   } 
   
   void delete(CacheEntry entry) throws CacheException 
   { 
       deleteDataFromDataStore(entry.getKey()); 
   } 
   
   void deleteAll(Collection<CacheEntry> entries) throws CacheException 
   { 
       for (CacheEntry entry : entries) { 
           delete(entry); 
       } 
   } 
} 
}
