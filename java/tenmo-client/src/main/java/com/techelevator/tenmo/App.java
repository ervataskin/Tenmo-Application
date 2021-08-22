package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.ApplicationService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.view.ConsoleService;
import io.cucumber.java.bs.A;

import java.math.BigDecimal;
import java.sql.SQLOutput;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080/";

	private static final String MENU_OPTION_EXIT = "Exit";
	private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };

	private static final String transferListHeader = "-------------------------------------------\n" +
			"Transfers\n" +
			"ID\t\t\tFrom/To  \t\t\tAmount\n" +
			"-------------------------------------------\n";

	private AuthenticatedUser currentUser;
	private ConsoleService console;
	private AuthenticationService authenticationService;
	private ApplicationService applicationService;

	public static void main(String[] args) {
		App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new ApplicationService(API_BASE_URL));
		app.run();
	}

	public App(ConsoleService console, AuthenticationService authenticationService, ApplicationService applicationService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.applicationService = applicationService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");

		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
		Account account = applicationService.getMyBalance(currentUser.getToken());
		System.out.println("Current Balance: " + account.getBalance());
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		Transfer[] transferList = applicationService.getMyTransfers(currentUser.getToken());
		System.out.println(transferListHeader);

		//TODO need to be able to see account from
		for (Transfer transfer : transferList) {
			Long toAccount = transfer.getAccount_to();
			String username = applicationService.findUsernameByAccountId(toAccount, currentUser.getToken()).getUsername();
			System.out.println(transfer.getTransfer_id() + "\t\t\t" + username + "\t\t\t$" + transfer.getAmount());
		}

	}

	private void viewPendingRequests() {
		// TODO pending transfers currently show all transfers, not just pending
		Transfer[] transferList = applicationService.getMyPendingTransfers(currentUser.getToken());
		System.out.println(transferListHeader);

		// TODO need to be able to see account FROM as well
		for (Transfer transfer : transferList) {
			Long toAccount = transfer.getAccount_to();
			String username = applicationService.findUsernameByAccountId(toAccount, currentUser.getToken()).getUsername();
			System.out.println(transfer.getTransfer_id() + "\t\t\t" + username + "\t\t\t$" + transfer.getAmount());
		}
	}

	private void sendBucks() {
		User[] userList = applicationService.getAllUsers(currentUser.getToken());
		for (User user : userList) {
			System.out.println("Username: " + user.getUsername() + "(ID: " + user.getId() + ")");
		}

		String toUser = console.getUserInput("Enter the ID of the user you want to send TE bucks to");
		String amount = console.getUserInput("Enter the amount to send");

		Transfer transfer = new Transfer();

		transfer.setAccount_to(Long.valueOf(toUser));
		transfer.setAmount(new BigDecimal(amount));

		Transfer newTransfer = applicationService.sendTransfer(transfer, currentUser.getToken());

		// TODO: Check DAO mapping of new transfer when transfer sent
		if (newTransfer.getTransfer_status_id()!= null) {
		System.out.println("New Transfer Created With Status Code" + newTransfer.getTransfer_status_id());
		}
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		User[]	userList = applicationService.getAllUsers(currentUser.getToken());
		for (User user : userList) {
			System.out.println("Username: " + user.getUsername() + "(ID: " + user.getId() + ")");
		}

		String fromUser = console.getUserInput("Enter the ID of the user you want to request TE bucks from");
		String amount = console.getUserInput("Enter the amount to send");

		Transfer transfer = new Transfer();

		transfer.setAccount_from(Long.valueOf(fromUser));
		transfer.setAmount(new BigDecimal(amount));

		Transfer newTransfer = applicationService.requestTransfer(transfer, currentUser.getToken());

		System.out.println("Transfer requested. Returning to main menu.");
	}

	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
		while (!isRegistered) //will keep looping until user is registered
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				authenticationService.register(credentials);
				isRegistered = true;
				System.out.println("Registration successful. You can now login.");
			} catch(AuthenticationServiceException e) {
				System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
			}
		}
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}

	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}