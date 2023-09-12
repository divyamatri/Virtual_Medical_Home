package com.patient;

import java.util.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class WelcomeMenu {
	private Connection con;
    public BufferedReader br;
	WelcomeMenu(Connection con) {
		this.con = con;
		br = new BufferedReader(new InputStreamReader(System.in));
	}
	Scanner sc = new Scanner(System.in);

	public void printMenu() throws SQLException, IOException {
		try {
			Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("\nHospital Management System\n--------------------------\n");
			System.out.print("\nSelect Role:\n");
			System.out.println("1. Doctor");
			System.out.println("2. Patient");
			System.out.println("3. Admin");
			System.out.println("4. Exit");
			
			
			int role=sc.nextInt();
			int ch=0;
			switch(role) {
			case 1: System.out.println("Enter your choice");
				System.out.println("1. Doctor Login");
				System.out.println("2. Doctor Registration");
				ch=sc.nextInt();
				switch(ch) {
				case 1: doctorLogin();
				break;
				case 2: doctorRegistration();
				break;
				default: System.out.println("Invalid choice! Please try again.");
				}
				break;
			case 2: System.out.println("Enter your choice");
				System.out.println("1. Patient Login");
				System.out.println("2. Patient Registration");
				ch=sc.nextInt();
				switch(ch) {
				case 1: patientLogin();
				break;
				case 2: patientRegistration();
				break;
				default: System.out.println("Invalid choice! Please try again.");
				}
				break; 
			case 3: adminLogin();
				break;
			case 4: System.out.println("Thank you for using our services!");
				con.close();
				System.exit(0);
			default: System.out.println("Invalid choice! Please try again.");
			}
		}
		}
		catch (Exception e) {
			System.out.println("Enter valid input format"+ e);
			printMenu();
		}

	}
	private void adminLogin() throws SQLException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Enter your user email ");
	    String email = br.readLine();
	    System.out.println("Enter your password ");
	    String pas = br.readLine();
//	    System.out.println(name + " " + pas);
	    Statement smt = (Statement) con.createStatement();
	    
	    ResultSet rs = ((java.sql.Statement) smt).executeQuery("select email_id, password from admin where email_id = '"+email+"' and  password = '"+pas+"' ");
	    
	    if(rs.next()) {
	    	System.out.println("Admin logged in!");
//			Statement smt = con.createStatement();
			admin ad = new admin(smt, con);
			ad.run();
	    }
	    else {
	    	System.out.println("please enter correct admin details");
	    	adminLogin();
	    }
		

	}
	private void patientRegistration() throws IOException {
		Scanner sc = new Scanner(System.in);
		try {
		System.out.print("Enter name: ");
		String nameRegex= "^[a-zA-Z. ]+$";
		String name = br.readLine();
		Pattern patname = Pattern.compile(nameRegex);
		boolean checkname;
        if (name == null)
            checkname=false;
        else
        	checkname=patname.matcher(name).matches();
		
		while(!checkname) {
			System.out.print("Enter valid name (only . & ' ' allowed, no special characters):");
			name = sc.next();
			if (name == null)
	            checkname=false;
	        else
	        	checkname=patname.matcher(name).matches();
		}
		
		
		System.out.print("Enter age: ");
		int age;
		
			 age = sc.nextInt();
			 if(age<0 || age>150) {
		        	System.out.print("Age not valid for registration");
		        	return;
		        }
       
		

		System.out.print("Enter email: ");
		String email = sc.next();
		String emailregex="^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
		Pattern patemail = Pattern.compile(emailregex);
		boolean checkemail;
        if (email == null)
            checkemail=false;
        else
        	checkemail=patemail.matcher(email).matches();
		
		while(!checkemail) {
			System.out.print("Enter valid email: ");
			email = sc.next();
			if (email == null)
	            checkemail=false;
	        else
	        	checkemail=patemail.matcher(email).matches();
		}


		String password = "", password2 = "";

		do {
			if (!password.equals(password2))
				System.out.println("\nPasswords do not match! Please try again.\n");

			System.out.print("Enter password: ");
			password = sc.next();

			System.out.print("Confirm password: ");
			password2 = sc.next();
		} while	(!password.equals(password2));	


		System.out.println("Enter contact number: ");
		Pattern p = Pattern.compile("^\\d{10}$");
		String contact = sc.next();
		while(!p.matcher(contact).matches()) {
			System.out.println("Please enter a valid 10-digit contact number: ");
			contact = sc.next();//br.readLine();
		}

		
		
		
		System.out.println("Enter city: ");
//		String address = sc.next();//.readLine();
		
		Pattern p_add = Pattern.compile("^[a-zA-Z]*$");
		String address = sc.next();
		while(!p_add.matcher(address).matches()) {
			System.out.println("Please enter a valid city name: ");
			address = sc.next();//br.readLine();
		}

		sc.nextLine();

		System.out.println("Enter blood group: ");
		String bg = sc.next().toUpperCase();
		List<String> bg_list=new ArrayList<>(Arrays.asList("A+","B+","A-","B-","AB+","AB-","O+","O-"));
		while(!bg_list.contains(bg)) {
			System.out.println("Please enter a valid blood group: ");
			bg = sc.next().toUpperCase();
		}

		try {
			PreparedStatement psmt = con.prepareStatement("insert into patient values(?,?,?,?,?,?,?,?)");
			psmt.setString(1, email);
			psmt.setString(2, password);
			psmt.setString(3, name);
			psmt.setInt(4, age);
			psmt.setString(5, contact);
			psmt.setString(6, address);
			psmt.setString(7, bg);
			psmt.setString(8, "empty");
			psmt.executeUpdate();

			System.out.println("Patient Registered!\n\n");

		} catch(Exception e)
		{
			e.printStackTrace();
		}
		}
		catch(Exception e) {
			System.out.println("Please enter valid details. Try again. "+e.getMessage());
			patientRegistration();
		}
	}
	private void doctorRegistration() throws IOException, SQLException {
		Scanner sc = new Scanner(System.in);
        try {
		System.out.print("Enter name: ");
		String nameRegex= "^[a-zA-Z. ]+$";
		String name = br.readLine();
		Pattern patname = Pattern.compile(nameRegex);
		boolean checkname;
        if (name == null)
            checkname=false;
        else
        	checkname=patname.matcher(name).matches();
		
		while(!checkname) {
			System.out.print("Enter valid name (only . & ' ' allowed, no special characters):");
			name = sc.next();
			if (name == null)
	            checkname=false;
	        else
	        	checkname=patname.matcher(name).matches();
		}
		
		System.out.print("Enter age: ");
		int age;
		
			 age = sc.nextInt();
			 if(age<20 || age>70) {
		        	System.out.print("Age not valid for registration");
		        	return;
		        }
       
		System.out.print("Enter email: ");
		String email = sc.next();
		String emailregex="^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
		Pattern patemail = Pattern.compile(emailregex);
		boolean checkemail;
        if (email == null)
            checkemail=false;
        else
        	checkemail=patemail.matcher(email).matches();
		
		while(!checkemail) {
			System.out.print("Enter valid email: ");
			email = sc.next();
			if (email == null)
	            checkemail=false;
	        else
	        	checkemail=patemail.matcher(email).matches();
		}

		String password = "", password2 = "";

		do {
			if (!password.equals(password2))
				System.out.println("\nPasswords do not match! Please try again.\n");

			System.out.print("Enter password: ");
			password = sc.next();

			System.out.print("Confirm password: ");
			password2 = sc.next();
		} while	(!password.equals(password2));	
		//sc.nextLine();
		System.out.println("Please select your Specialisation from the list below:");
		System.out.println("----LIST OF SPECIALISATION----");
		System.out.println("\n1.Allergy and immunology\n2.Heart\n3.Dermatology\n4.Kidney\n5.Emergency medicine\n6.Neurology\n7.Gynecology\n8.Ophthalmology\n9.Pathology\n10.Pediatrics\n");
		//System.out.print("\nEnter your choice of specialisation: ");
		int choice=0;//= sc.nextInt();

		String specilization=" ";
		while(true) {
		
		System.out.print("\nEnter your choice of specialisation: ");
		choice = sc.nextInt();
		switch(choice) {
		case 1: specilization="Allergy and immunology";
		break;
		case 2: specilization="Heart";
		break;
		case 3: specilization="Dermatology";
		break;
		case 4: specilization="Diagnostic radiology";
		break;
		case 5: specilization="Emergency medicine";
		break;
		case 6: specilization="Neurology";
		break;
		case 7: specilization="Gynecology";
		break;
		case 8: specilization="Ophthalmology";
		break;
		case 9: specilization="Pathology";
		break;
		case 10: specilization="Pediatrics";
		break;
//		case 11: System.out.println("Thank you for using our services!");
//				con.close();
//				System.exit(0);
		default: System.out.println("Invalid choice! Please try again.");

		}
		if(!specilization.equals(" ")) {
			break;
		}
		}
		//specilization = sc.next();//Line();
		//sc.nextLine();
		
		System.out.println("Enter Fees :");
		int fees = sc.nextInt();
		//sc.nextLine();
		
//		System.out.println("Enter start time (HHMM) in 24 hr format:");
//		int st=sc.nextInt();
//		int hh=st/100, mm=st-(st/100)*100;
//		while((hh<0||hh>23)||(mm<0||mm>59)) {
//			System.out.print("Enter valid start time (HHMM) in 24 hr format: ");
//			st=sc.nextInt();
//			hh=st/100; mm=st-(st/100)*100;
//		}
//		String start_time = String.valueOf(hh)+":"+String.valueOf(mm);//sc.next();//.readLine();
//		
//		
//		System.out.println("Enter end time (HHMM) in 24 hr format:");
//		int et=sc.nextInt();
//		int h=et/100, m=et-(et/100)*100;
//		while((h<0||h>23)||(m<0||m>59)||h-hh<=0) {
//			if((h<0||h>23)||(m<0||m>59))
//				System.out.print("Enter valid end time (HHMM) in 24 hr format: ");
//			else
//				System.out.print("End time should be greater than start time, please enter End time again: ");
//			et=sc.nextInt();
//			h=et/100; m=et-(et/100)*100;
//		}
//		String end_time = String.valueOf(h)+":"+String.valueOf(m);//.readLine();
		
		System.out.println("Enter contact number: ");
		Pattern p = Pattern.compile("^\\d{10}$");
		String contact = sc.next();
		while(!p.matcher(contact).matches()) {
			System.out.println("Please enter a valid 10-digit contact number: ");
			contact = sc.next();//br.readLine();
		}

		System.out.println("Enter city: ");
//		String address = sc.next();//.readLine();
		Pattern p_add = Pattern.compile("^[a-zA-Z]*$");
		String address = sc.next();
		while(!p_add.matcher(address).matches()) {
			System.out.println("Please enter a valid city name: ");
			address = sc.next();//br.readLine();
		}
		sc.nextLine();
//		while(sc.hasNext())
//		sc.next();
		
		System.out.println("Enter blood group: ");
		String bg = sc.next().toUpperCase();
		List<String> bg_list=new ArrayList<>(Arrays.asList("A+","B+","A-","B-","AB+","AB-","O+","O-"));
		while(!bg_list.contains(bg)) {
			System.out.println("Please enter a valid blood group: ");
			bg = sc.next().toUpperCase();
		}
		
		String status = "pending";

		try {
//			System.out.println("Here");
			PreparedStatement psmt = con.prepareStatement("insert into doctor values(?,?,?,?,?,?,?,?,?,?)");
			psmt.setString(1, email);
			psmt.setString(2, password);
			psmt.setString(3, name);
			psmt.setInt(4, age);
			psmt.setString(5,specilization );
			psmt.setInt(6,fees);
//			psmt.setString(7, start_time);
//			psmt.setString(8, end_time);
			psmt.setString(7, contact);
			psmt.setString(8, address);
			psmt.setString(9, bg);
			psmt.setString(10, status);

			psmt.executeUpdate();
			System.out.println("Doctor registered!");

		} 
		catch(Exception e)
		{
			System.out.println("Duplicate entries not allowed ");
			e.printStackTrace();
			printMenu();
			
		}
        }
        catch (Exception e) {
        	System.out.println("Please enter valid details. Try again. "+e.getMessage());
			printMenu();
		}
		
	}
	private void patientLogin() {
		while(true)
		{
			System.out.println("\nPatient Login\n--------------------\n");	
			System.out.print("Enter email: ");
			String email = sc.next();
//			System.out.println(email);

			System.out.print("Enter password: ");
			String password = sc.next();
//			System.out.println(password);
			
			try {
				Statement smt = con.createStatement();
				ResultSet rs = smt.executeQuery("select * from patient where email_id ='"+email+"' and password='"+password+"'");
				

//				rs.next();
//				System.out.println(rs.getString(1));
				boolean f =false;
//				while(rs.next())
//				{
//				
//					 f =true;
//				}
//				
				if(rs.next())
				{
					System.out.println("Patient logged in!\n\n");
					String e = null;
					String pass = null;
					String name = null;
					int age = 0;
					String contact = "";
					String address = null;
					String bloodGroup = null;
				
						e = rs.getString(1);
						pass = rs.getString(2);
						name = rs.getString(3);
						age = rs.getInt(4);
						contact = rs.getString(5);
						address = rs.getString(6);
						bloodGroup = rs.getString(7);
					patient p = new patient(con,e,pass,name,age,contact,address,bloodGroup);
					p.viewPatientMenu();
					
				}
				else {
					System.out.println("Invalid Credentials! Please try again.\n");
				}
//				con.close();
			} catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	private void doctorLogin() {

		while(true)
		{
			System.out.println("\nDoctor Login\n--------------------\n");	
			System.out.print("Enter email: ");
			String email = sc.next();
//			System.out.println(email);

			System.out.print("Enter password: ");
			String password = sc.next();
//			System.out.println(password);

			try {
				Statement smt = con.createStatement();
				ResultSet rs = smt.executeQuery("select * from doctor where email_id ='"+email+"' and password='"+password+"' and  status = 'approved'");

//				rs.next();
//				System.out.println(rs.getString(1));
				boolean f =false;
//				rs.next();
//				System.out.println(rs.getString(1));
				if(rs.next())
				{
					System.out.println("Doctor logged in!\n\n");
					String e = null;
					String pass = null;
					String name = null;
					int age = 0;
					String specialization;
					int fees;
//					String start_time;
//					String end_time;
					String contact = "";
					String address = null;
					String status;
					String bloodGroup = null;
				
						e = rs.getString(1);
						pass = rs.getString(2);
						name = rs.getString(3);
						age = rs.getInt(4);
						specialization = rs.getString(5);
						fees = rs.getInt(6);
						contact = rs.getString(7);
						address = rs.getString(8);
						bloodGroup = rs.getString(9);
						status = rs.getString(10);
					doctor d = new doctor(con,e,pass,name,age,specialization,fees,contact,address,bloodGroup,status);
					d.viewDoctorMenu();
					
				}
				else {
					System.out.println("Invalid Credentials or Account Approval Pending! Please try again later.\n");
					return;
				}
////				con.close();
			} catch(Exception e)
			{
				e.printStackTrace();
			}
		}

	}
}
