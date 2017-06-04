
/**
******************************************************************************
*@file			Main.java
*@brief			This file contains flow of the algorithm 
*@author		Shrisamarth Kamurti
*@date created	04-June-2017																					     
******************************************************************************
*/

/**
 *  Below are the packages imported that are being used for implementation of respective functionalities
 */

import java.util.Scanner;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Class Main
 */
public class Main
{
	public static void main(String args[])
	{
		int choice, keyIndex, capacity;
		int id, salary;
		String name;
		try
		{
			Scanner sc = new Scanner(System.in);
			FileOutputStream fout = new FileOutputStream("stateOfObject.txt");
			ObjectOutputStream outStream = new ObjectOutputStream(fout);
			System.out.println("Please enter the maximum capacity of Map");
			capacity = sc.nextInt();
			CacheMap<Integer, Employee> objRecordsMap = new CacheMap<Integer, Employee>(capacity, "records.txt");
			Employee objEmployee = null;
			while(true)
			{
				System.out.println("Please Enter\n\t 1 -> To add new record");
				System.out.println("\t 2 -> To read record");
				System.out.println("\t 3 -> Exit");
				choice = sc.nextInt();
				switch(choice)
				{
					case 1: System.out.println("Please Enter below details:");
						System.out.println("Key ");
						keyIndex = sc.nextInt();
						System.out.println("Employee Id ");
						id = sc.nextInt();
						System.out.println("Employee Name ");
						name = sc.next();
						System.out.println("Employee Salary ");
						salary = sc.nextInt();
						objEmployee = new Employee(id, name, salary);
						objRecordsMap.put(keyIndex, objEmployee);						
						break;
					case 2: System.out.println("Please enter key :");
						keyIndex = sc.nextInt();
						objEmployee = objRecordsMap.get(keyIndex);
						if(null != objEmployee)
						{
							System.out.println("Employee Id : " + objEmployee.empId);
							System.out.println("Employee Name : " + objEmployee.empName);
							System.out.println("Employee Salary : " + objEmployee.empSalary);
						}
						else
						{
							System.out.println("Entered key not found!");
						}
						break;
					case 3:
						outStream.writeObject(objRecordsMap);
						fout.flush();
						fout.close();
						outStream.flush();
						outStream.close();
						System.exit(0);
					default : System.out.println(" Please Enter a valid input ");
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}