package com.patient;
import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.Statement;




class Approve_Doctor{
	Statement smt;

	Approve_Doctor(Statement smt){

		this.smt = smt;

	}

	  public void run() throws IOException, SQLException {

		   ResultSet rs = ((java.sql.Statement) smt).executeQuery("select * from Doctor where status = 'pending'");

		 
		   

		   System.out.println("Do You want to Approve Any Doctor  Enter yes or no");

		   
          
		   
		   
		   String check ;

		   

		   BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		   

		   check = br.readLine();
		   
		   if(!(check.equalsIgnoreCase("yes") || check.equalsIgnoreCase("no"))) {
			   System.out.println("please enterd correct choice ");
			   run();
			   return ;
		   }
		   
		   System.out.println("Please Find the all the pending list of doctors ");

		     while(rs.next()){
			    System.out.println(rs.getString(1)+  " " + rs.getString(3) + " " + rs.getInt(4) );
			   }
 
		   
		   while(check.equalsIgnoreCase("yes")) {

			   System.out.println("Enter Email id");

			   String email = br.readLine();		

			   String query1 = "update Doctor set status = 'approved' where email_id = '"+email+"'";

			   int count = smt.executeUpdate(query1);
			   
			   if(count > 0) {
				   System.out.println("Doctor approved ");
				   
			           String query2 = "insert into slots (d_email_id , slot_id , count) values " + "('"+email+"', '09:00 AM to 10:00 AM', 0)";
					   
			           String query3 = "insert into slots (d_email_id , slot_id , count) values " + "('"+email+"', '10:00 AM to 11:00 AM', 0)";				

					   String query4 = "insert into slots (d_email_id , slot_id , count) values " + "('"+email+"', '11:00 AM to 12:00 PM', 0)";

					   String query5 = "insert into slots (d_email_id , slot_id , count) values " + "('"+email+"', '12:00 PM to 01:00 PM', 0)";

					   String query6 = "insert into slots (d_email_id , slot_id , count) values " + "('"+email+"', '12:00 PM to 01:00 PM', 0)";

					   String query7 = "insert into slots (d_email_id , slot_id , count) values " + "('"+email+"', '02:00 PM to 03:00 PM', 0)";

					   String query8 = "insert into slots (d_email_id , slot_id , count) values " + "('"+email+"', '03:00 PM to 04:00 PM', 0)";

					   try {

						   smt.executeUpdate(query2);

						   smt.executeUpdate(query3);

						   smt.executeUpdate(query4);

						   smt.executeUpdate(query5);

						   smt.executeUpdate(query6);

						   smt.executeUpdate(query7);

						   smt.executeUpdate(query8);

					   } catch (Exception ex) {

						   ex.printStackTrace();

					   }
				   
			   }
			   else {
				   System.out.println("please enter correct email id ");
			   }

			   System.out.println("Do You want to Approve more Doctor  Enter yes or no");

			   

			   

			   check = br.readLine();
			   
			   if(!(check.equalsIgnoreCase("yes") || check.equalsIgnoreCase("no"))) {
				   System.out.println("please enterd correct choice ");
				   run();
				   return ;
			   }

		   }

     }	   

	

}

public class admin {

	 BufferedReader br;
	 Statement  smt;
	 Connection con;
	 admin( Statement  smt, Connection con){
		 this.smt = smt;
		 this.con = con;
		 this.br = new BufferedReader(new InputStreamReader(System.in));
	 }
	 
	  private void Approve() {
		  
		 
		  try

		  {

		   Approve_Doctor ad = new Approve_Doctor(smt);

		   ad.run();

//		   con.close();

		  }catch(Exception e) {

		   System.out.println(e);

		  }  
	  }
	  public void run() throws NumberFormatException, IOException, SQLException {
		  int cnt = 0;
		  while(true) {
			  if (cnt++ > 3) {
				  System.out.println("Too many incorrect attempts! Try again.");
				  return;
			  }
			  System.out.println("Admin Panel\n--------\n\n");
			  System.out.println("1. Approve Doctor");
			  System.out.println("2. Delete Doctor");
			  System.out.println("3. Add another admin");
			  System.out.println("4. Exit");
			   
			  int choice =  Integer.parseInt(br.readLine());
			  switch(choice) {
			   		case 1: Approve();
			   				break;
			   		case 2: deleteDoctor();
			   				break;
			   		case 3: addAdmin();
			   				break;
			   		case 4: System.out.println("Admin logged out!");
			   				return;
			   		default: System.out.println("Invalid choice! Try Again");
			  }
		  }
	  }

	private void addAdmin() throws IOException {
		System.out.print("Enter name: ");
		String name = br.readLine();

		System.out.print("Enter email: ");
		String email = br.readLine();

		String password = "", password2 = "";
		
		int cnt = 0;
		do {
			if (cnt > 3) {
				System.out.println("Too many incorrect attempts! Try again later.");
				return;
			}
			if (!password.equals(password2))
				System.out.println("\nPasswords do not match! Please try again.\n");

			System.out.print("Enter password: ");
			password = br.readLine();

			System.out.print("Confirm password: ");
			password2 = br.readLine();
		} while	(!password.equals(password2));	

		try {
			PreparedStatement psmt = con.prepareStatement("insert into admin values(?,?,?)");
			psmt.setString(1, email);
			psmt.setString(2, password);
			psmt.setString(3, name);

			psmt.executeUpdate();

			System.out.println("Admin Registered!\n\n");

		} catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	private void deleteDoctor() throws SQLException, IOException {
		 ResultSet rs = ((java.sql.Statement) smt).executeQuery("select email_id, name, specialization from Doctor");
		 System.out.println("Please find the all the list of Doctor");
		 while(rs.next()){
			    System.out.println(rs.getString(1)+ " " +rs.getString(2) + " " + rs.getString(3));
	     }
		 System.out.println("Enter Email id of doctor you want to delete");
		 String email = br.readLine();
		 String query = "delete from doctor where email_id = '"+email+"'";
		 int count = smt.executeUpdate(query);
		 
		 if(count > 0) {
			 System.out.println("Doctor Deleted successfully ");
		 }
		 else {
			 System.out.println("Doctor could not be deleted . Try again later ");
		 }
		 
	}

}

