package com.zxj.httpserver;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	App a = new App();
    	System.out.println();
    	try {
			Class b = Class.forName(a.getClass().getName());
			b.newInstance();
			System.out.println(b);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       
    }
}
