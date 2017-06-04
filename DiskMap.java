
/**
******************************************************************************
*@file			DiskMap.java
*@brief			This file contains implementation of Employee and CacheMap along with their functionalities
*@author		Shrisamarth Kamurti
*@date created	04-June-2017																					     
******************************************************************************
*/

/**
 *  Below are the packages imported that are being used for implementation of respective functionalities
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 *  Class Employee with its members
 */
class Employee
{
	// Employee Id
	int empId;
	// Employee Name
	String empName;
	// Employee Salary
	int empSalary;

	/**
	 * Parameterized constructors
	 * @param empId
	 * @param empName
	 * @param empSalary
	 */
	public Employee(int empId, String empName, int empSalary)
	{
		this.empId = empId;
		this.empName = empName;
		this.empSalary = empSalary;
	}
}

/**
 *  Class CacheMap with its required members
 */
class CacheMap<K, V> implements Serializable
{
	// Map data, which contains records of class Employee
    private Map<K, V> data;
    // LinkedList listOfKeys, which contains list of 'keys' those are present in Map
    private LinkedList<K> listOfKeys;
    // maxCapacity is a member which holds maximum Map size
    private final int maxCapacity;
    // filename is name of a file used to store records which spills on to disk
    String filename;
    
    /**
     *  Parameterized constructor
     * @param maxCapacity
     * @param filename
     * @throws IOException
     */
    public CacheMap(int maxCapacity, String filename) throws IOException
    {
        if (maxCapacity < 1)
        {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.maxCapacity = maxCapacity;
        data  = new HashMap<K, V>();
        listOfKeys = new LinkedList<K>();
        this.filename = filename;
    }

    /**
     *  Method to clear the Map
     */
    public void clear()
    {
        data.clear();
    }
    
    /**
     * Method that checks whether provided key is present in Map or not
     * @param key
     * @return
     */
    public boolean containsKey(Object key)
    {
        return data.containsKey(key);
    }

    /**
     * Method that checks whether provided value is present in Map or not
     * @param value
     * @return
     */
    public boolean containsValue(Object value)
    {
        return data.containsValue(value);
    }

    /**
     * Method that returns employee record of matching key 
     * @param key
     * @return
     * @throws IOException
     */
    public synchronized V get(Object key) throws IOException
    {
    	V record = data.get(key);
    	if(null == record)
    	{
    		int indexOfKeyList = 1;
    		for(K curKey : listOfKeys)
    		{
    			if((Integer)key == (Integer)curKey)
    			{
    				String line = null;
    				int indexOfKeyLine = 1;
    				BufferedReader buffReader = new BufferedReader(new FileReader(filename));
    	    		while((line = buffReader.readLine()) != null)
    	    		{
    	    			if(indexOfKeyLine == indexOfKeyList)
    	    			{
    	    				String values[] = line.split(",");
    	    				Employee objEmp = new Employee(Integer.parseInt(values[0]), values[1], Integer.parseInt(values[2]));
    	    				record = (V) objEmp;
    	    				buffReader.close();
    	    				return record;
    	    			}
    	    			indexOfKeyLine++;
    	    		}
    			}
    			indexOfKeyList++;
    		}
    	}
        return record;
    }

    /**
     * Method to check whether Map is empty 
     * @return
     */
    public boolean isEmpty()
    {
        return data.isEmpty();
    }

    /**
     * Method returns set view of all the entries in Map
     * @return
     */
    public Set<java.util.Map.Entry<K, V>> entrySet()
    {
        return data.entrySet();
    }
    
    /**
     * Method to store a record in Map/Disk respective to space availability in Map 
     * @param key
     * @param value
     * @throws IOException
     */
    public synchronized void put(K key, V value) throws IOException
    {
    	if(data.size() == maxCapacity)
    	{
    		BufferedWriter buffWriter = new BufferedWriter(new FileWriter(filename, true));
    		// If Map is full, move 50% record into files
    		int limitToRemoveRecords = (maxCapacity * 50) / 100;
    		int counter = 0;
    		for(Entry<K, V> record:data.entrySet())
			{
    			Employee objEmp = (Employee) record.getValue();
    			listOfKeys.add(record.getKey());
    			buffWriter.append(objEmp.empId + "," + objEmp.empName + "," + objEmp.empSalary + "\n");
    			counter ++;
    			if(counter == limitToRemoveRecords)
    				break;
			}
    		for(int index = 0; index < listOfKeys.size(); index++)
    			data.remove(listOfKeys.get(index));
    		buffWriter.flush();
    		buffWriter.close();
    	}
        data.put(key, value);
    }

    /**
     * Method to remove provided Key from Map
     * @param key
     * @return
     */
    public V remove(Object key)
    {
        return data.remove(key);
    }

    /**
     * Method that return size of Map
     * @return
     */
    public int size()
    {
        return data.size();
    }
}
