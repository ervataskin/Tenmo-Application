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

	// If I had another day, maybe two, I could probably get this program working much more like a grown up.
	// As it is, it functions on the database side, mostly without exploding??
	// Current mood: The garbage will do.

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
		boolean stay = true;
		while (stay) {
			Transfer[] transferList = applicationService.getMyTransfers(currentUser.getToken());
			System.out.println(transferListHeader);

			//TODO need to be able to see account from
			for (Transfer transfer : transferList) {
				Long toAccount = transfer.getAccount_to();
				String username = applicationService.findUsernameByAccountId(toAccount, currentUser.getToken()).getUsername();
				System.out.println(transfer.getTransfer_id() + "\t\t\t" + username + "\t\t\t$" + transfer.getAmount());
			}

			String transferDetail = console.getUserInput("\nEnter a transfer ID to view details, or enter 0 to return to the main menu");
			if (transferDetail.equals("0")){
				stay = false;
			} else {
				transferDetail(transferList, Long.valueOf(transferDetail));
			}
		}
	}

	private void viewPendingRequests() {
		// TODO pending transfers currently show all transfers, not just pending
		boolean stay = true;
		while (stay) {
			Transfer[] transferList = applicationService.getMyPendingTransfers(currentUser.getToken());
			System.out.println(transferListHeader);

		// TODO need to be able to see account FROM as well
		// TODO make things look less stupid when they print out
			for (Transfer transfer : transferList) {
				Long toAccount = transfer.getAccount_to();
				String username = applicationService.findUsernameByAccountId(toAccount, currentUser.getToken()).getUsername();
				System.out.println(transfer.getTransfer_id() + "\t\t\t" + username + "\t\t\t\t\t\t $" + transfer.getAmount());
			}

			String transferDetail = console.getUserInput("\nEnter a transaction ID to approve or reject, or enter 0 to return to the main menu");
			if (transferDetail.equals("0")){
				mainMenu();
			} else {
				updateStatus(Long.valueOf(transferDetail));
			}
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

	private void transferDetail(Transfer[] transferList, Long transferId) {
			for (Transfer transfer : transferList) {
				if (transfer.getTransfer_id().equals(transferId)) {
					String transferStatus = "no status";
					boolean displayPending = false;
					String toUsername = applicationService.findUsernameByAccountId(transfer.getAccount_to(), currentUser.getToken()).getUsername();
					String fromUsername = applicationService.findUsernameByAccountId(transfer.getAccount_from(), currentUser.getToken()).getUsername();

					if (transfer.getTransfer_status_id() == 1) {
						transferStatus = "Pending";
						displayPending = true;
					} else if (transfer.getTransfer_status_id() == 2) {
						transferStatus = "Approved";
					} else if (transfer.getTransfer_status_id() == 3) {
						transferStatus = "Rejected";
					}

					System.out.println("----------------------------");
					System.out.println("Transfer " + transfer.getTransfer_id() + ": $" + transfer.getAmount() + " sent to " + toUsername + " from " + fromUsername + ". (" + transferStatus + ")");
					System.out.println("----------------------------\n");

					String exit = console.getUserInput("Enter anything to exit.");
					if (exit != null) {
						mainMenu();
				}
			}
		}
	}

	private void updateStatus(Long transferDetail) {
		// TODO Still says transfers approved through the app are pending even when they're not pending in the database.
		boolean stay = true;
		while (stay) {
			String updateStatus = console.getUserInput("Would you like to (A)pprove or (R)eject this transaction?");
			Transfer thisTransfer = applicationService.getTransferById(Long.valueOf(transferDetail), currentUser.getToken());
			if (updateStatus.equalsIgnoreCase("A")) {
				applicationService.approveTransfer(thisTransfer, currentUser.getToken());
			} else if (updateStatus.equalsIgnoreCase("R")) {
				applicationService.rejectTransfer(thisTransfer, currentUser.getToken());
			} else {
				stay = false;
			}
		}
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