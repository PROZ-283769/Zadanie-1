package pl.edu.pw.elka.proz.projekt1;

import java.util.HashMap;
import java.util.Set;
/**
 * Environment class represents the database storing usernames and passwords
 * for some environment.
 * 
 * @author Michał Sokólski
 */
public class Environment {
	private String name;
	private HashMap<String, String> users;
	
	/**
	 * Default constructor
	 * 
	 * @param name
	 * 		  name  of the environment
	 */
	 Environment(String name_){
		name = name_;
		users = new HashMap<String, String>();
	}
	/**
	 * To toString() implementation for environment
	 *
	 * @return name of the environment
	 */
	@Override
	public String toString() {
		return name;
	}
	/**
	 * Adds user to the environment
	 * 
	 * @param username
	 * @param password
	 */
	public void addUser(String username, String password) {
		users.put(username, password);
	}

	/**
	 * Checks if login data matches data in database
	 * 
	 * @param username
	 * @param password
	 * @return true if there if the password is correct and false if not
	 *
	 */
	public boolean verifyLogin(String username, String password) {
			return (users.get(username).equals(password));
	}
	
	/**
	 *  Gets all usernames in the environment
	 * @return users.keySet();
	 */
	
	public Set<String> getUsernames() {
		return users.keySet();
	}
	
	/**
	 * 
	 * @param username
	 * @return true if username exists in the environment and false if not
	 */
	public boolean userExists(String username) {
		return users.containsKey(username);
	}
}
