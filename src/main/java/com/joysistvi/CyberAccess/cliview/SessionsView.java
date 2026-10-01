package com.joysistvi.CyberAccess.cliview;

import com.joysistvi.CyberAccess.controller.ComputersController;
import com.joysistvi.CyberAccess.controller.RatesController;
import com.joysistvi.CyberAccess.controller.SessionsController;
import com.joysistvi.CyberAccess.controller.UsersController;
import com.joysistvi.CyberAccess.model.Computers;
import com.joysistvi.CyberAccess.model.Rates;
import com.joysistvi.CyberAccess.model.Sessions;
import com.joysistvi.CyberAccess.model.Users;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class SessionsView {

    private final SessionsController sessionsController;
    private final ComputersController computersController;
    private final RatesController ratesController;
    private final UsersController usersController;
    private final Scanner scanner;

    public SessionsView(Scanner scanner) {

        this.scanner = scanner;

        this.sessionsController =
                new SessionsController();

        this.computersController =
                new ComputersController();

        this.ratesController =
                new RatesController();

        this.usersController =
                new UsersController();
    }

    public List<Sessions> getAllSessions()
            throws SQLException {

        return sessionsController.getAll();
    }


    // admin menu

    public void showMenu() {

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("================================");
            System.out.println("        SESSION MANAGEMENT");
            System.out.println("================================");
            System.out.println("1. Start Session");
            System.out.println("2. View Sessions");
            System.out.println("3. Edit Session");
            System.out.println("4. Remove Session");
            System.out.println("0. Return");
            System.out.println("================================");
            System.out.print("Select: ");

            String choice =
                    scanner.nextLine().trim();

            switch (choice) {

                case "1" ->
                        startSessionForAdmin();

                case "2" ->
                        viewSessions();

                case "3" ->
                        editSession();

                case "4" ->
                        removeSession();

                case "0" ->
                        running = false;

                default ->
                        System.out.println(
                                "Invalid choice."
                        );
            }
        }
    }

    private void startSessionForAdmin() {

        try {

            List<Users> users =
                    usersController.getAll();

            users.removeIf(user ->
                    !Users.STATUS_ACTIVE.equalsIgnoreCase(
                            user.getStatus()
                    )
            );

            if (users.isEmpty()) {

                System.out.println();
                System.out.println(
                        "No active users available."
                );

                pause();
                return;
            }

            System.out.println();
            System.out.println("================================");
            System.out.println("          SELECT USER");
            System.out.println("================================");

            for (int i = 0; i < users.size(); i++) {

                Users user =
                        users.get(i);

                System.out.printf(
                        "%d. %-25s %s%n",
                        i + 1,
                        user.getFullName(),
                        user.getUsername()
                );
            }

            System.out.println(
                    "================================"
            );

            int userChoice =
                    readChoice(
                            "Select User: ",
                            users.size()
                    );

            Users selectedUser =
                    users.get(userChoice - 1);

            startSession(selectedUser);

        } catch (SQLException e) {

            System.out.println();
            System.out.println(
                    "Database error: " +
                            e.getMessage()
            );

            pause();
        }
    }


    public void startUserSession(
            Users loggedInUser) {

        if (loggedInUser == null) {

            System.out.println(
                    "No logged-in user."
            );

            return;
        }

        startSession(loggedInUser);
    }


    private void startSession(
            Users selectedUser) {

        try {

            List<Sessions> existingSessions =
                    sessionsController.getAll();

            for (Sessions existingSession :
                    existingSessions) {

                if (existingSession.getUserId() ==
                        selectedUser.getId() &&
                        "Ongoing".equalsIgnoreCase(
                                existingSession.getStatus()
                        )) {

                    System.out.println();
                    System.out.println(
                            "This user already has an ongoing session."
                    );

                    System.out.println(
                            "Session ID: " +
                                    existingSession.getId()
                    );

                    System.out.println(
                            "Computer ID: " +
                                    existingSession.getComputerId()
                    );

                    pause();

                    return;
                }
            }

            List<Computers> availableComputers =
                    computersController.getByStatus(
                            Computers.STATUS_AVAILABLE
                    );

            if (availableComputers.isEmpty()) {

                System.out.println();
                System.out.println(
                        "No computers are currently available."
                );

                pause();

                return;
            }

            System.out.println();
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "       AVAILABLE COMPUTERS"
            );
            System.out.println(
                    "================================"
            );

            for (int i = 0;
                 i < availableComputers.size();
                 i++) {

                Computers computer =
                        availableComputers.get(i);

                System.out.printf(
                        "%d. %-15s %s%n",
                        i + 1,
                        computer.getComputerName(),
                        computer.getStatus()
                );
            }

            System.out.println(
                    "================================"
            );

            int computerChoice =
                    readChoice(
                            "Select Computer: ",
                            availableComputers.size()
                    );

            Computers selectedComputer =
                    availableComputers.get(
                            computerChoice - 1
                    );

            List<Rates> rates =
                    ratesController.getByType("Computer");

            rates.removeIf(rate ->
                    !Rates.STATUS_ACTIVE.equalsIgnoreCase(
                            rate.getStatus()
                    )
            );

            if (rates.isEmpty()) {

                System.out.println();
                System.out.println(
                        "No active computer rates available."
                );

                pause();

                return;
            }

            System.out.println();
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "         AVAILABLE RATES"
            );
            System.out.println(
                    "================================"
            );

            for (int i = 0;
                 i < rates.size();
                 i++) {

                Rates rate =
                        rates.get(i);

                System.out.printf(
                        "%d. %-28s ₱%.2f %s%n",
                        i + 1,
                        rate.getRateName(),
                        rate.getPrice(),
                        rate.getUnit()
                );
            }

            System.out.println(
                    "================================"
            );

            int rateChoice =
                    readChoice(
                            "Select Rate: ",
                            rates.size()
                    );

            Rates selectedRate =
                    rates.get(
                            rateChoice - 1
                    );

            int durationMinutes =
                    getDurationMinutes(
                            selectedRate
                    );

            if (durationMinutes <= 0) {

                System.out.println();
                System.out.println(
                        "Unable to determine session duration."
                );

                pause();

                return;
            }

            Sessions session =
                    new Sessions(
                            selectedUser.getId(),
                            selectedComputer.getId(),
                            selectedRate.getId(),
                            durationMinutes
                    );

            sessionsController.add(session);

            // computer in use
            computersController.changeStatus(
                    selectedComputer.getId(),
                    Computers.STATUS_IN_USE
            );

            System.out.println();
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "        SESSION STARTED"
            );
            System.out.println(
                    "================================"
            );

            System.out.println(
                    "User: " +
                            selectedUser.getFullName()
            );

            System.out.println(
                    "Computer: " +
                            selectedComputer.getComputerName()
            );

            System.out.println(
                    "Rate: " +
                            selectedRate.getRateName()
            );

            System.out.printf(
                    "Price: ₱%.2f%n",
                    selectedRate.getPrice()
            );

            System.out.println(
                    "Duration: " +
                            durationMinutes +
                            " minutes"
            );

            System.out.println(
                    "Session ID: " +
                            session.getId()
            );

            System.out.println(
                    "================================"
            );


            startCountdown(
                    session,
                    selectedComputer
            );

        } catch (SQLException e) {

            System.out.println();
            System.out.println(
                    "Database error: " +
                            e.getMessage()
            );

            pause();
        }
    }


    private int getDurationMinutes(
            Rates rate) {

        String rateName =
                rate.getRateName();

        if (rateName == null) {
            return 0;
        }

        String name =
                rateName.toLowerCase();

        if (name.contains("30 minute")) {
            return 30;
        }

        if (name.contains("1 hour")) {
            return 60;
        }

        if (name.contains("2 hour")) {
            return 120;
        }

        if (name.contains("3 hour")) {
            return 180;
        }

        if (name.contains("5 hour")) {
            return 300;
        }

        if (name.contains("10 hour")) {
            return 600;
        }

        // Current Computer rates
        if (rate.getUnit() != null &&
                rate.getUnit().equalsIgnoreCase(
                        "per hour"
                )) {

            return 60;
        }

        return 0;
    }

    // COUNTDOWN

    private void startCountdown(
            Sessions session,
            Computers selectedComputer) {

        Integer durationSeconds =
                session.getDurationSeconds();

        if (durationSeconds == null ||
                durationSeconds <= 0) {

            System.out.println(
                    "Invalid session duration."
            );

            return;
        }

        AtomicBoolean logoutRequested =
                new AtomicBoolean(false);

        AtomicBoolean paused =
                new AtomicBoolean(false);

        AtomicBoolean inputFinished =
                new AtomicBoolean(false);


        Thread inputThread =
                new Thread(() -> {

                    while (!inputFinished.get()) {

                        try {

                            String input =
                                    scanner.nextLine()
                                            .trim();

                            if (!input.equalsIgnoreCase("L")) {
                                continue;
                            }

                            // Pause timer
                            paused.set(true);

                            System.out.println();
                            System.out.println();
                            System.out.println(
                                    "================================"
                            );
                            System.out.println(
                                    "       LOGOUT SESSION?"
                            );
                            System.out.println(
                                    "================================"
                            );

                            System.out.print(
                                    "Are you sure you want to logout? (Y/N): "
                            );

                            String confirmation =
                                    scanner.nextLine()
                                            .trim();

                            // --------------------------
                            // YES
                            // --------------------------

                            if (confirmation.equalsIgnoreCase("Y")) {

                                logoutRequested.set(true);
                                inputFinished.set(true);
                            }


                            else if (
                                    confirmation.equalsIgnoreCase("N")
                            ) {

                                paused.set(false);

                                System.out.println();
                                System.out.println(
                                        "Logout cancelled."
                                );
                                System.out.println(
                                        "Session resumed."
                                );
                                System.out.println();
                            }

                            else {

                                paused.set(false);

                                System.out.println();
                                System.out.println(
                                        "Invalid choice."
                                );
                                System.out.println(
                                        "Session resumed."
                                );
                                System.out.println();
                            }

                        } catch (Exception e) {

                            inputFinished.set(true);
                        }
                    }

                });

        inputThread.setDaemon(true);
        inputThread.start();

        int remainingSeconds =
                durationSeconds;

        System.out.println();
        System.out.println(
                "================================"
        );
        System.out.println(
                "          TIME REMAINING"
        );
        System.out.println(
                "================================"
        );
        System.out.println(
                "Type L + ENTER to logout."
        );
        System.out.println();

        try {

            while (remainingSeconds > 0 &&
                    !logoutRequested.get()) {

                if (paused.get()) {

                    Thread.sleep(100);

                    continue;
                }

                int minutes =
                        remainingSeconds / 60;

                int seconds =
                        remainingSeconds % 60;

                System.out.printf(
                        "\rTime Remaining: %02d:%02d   ",
                        minutes,
                        seconds
                );

                System.out.flush();

                Thread.sleep(1000);


                if (!paused.get() &&
                        !logoutRequested.get()) {

                    remainingSeconds--;
                }
            }

            if (logoutRequested.get()) {

                inputFinished.set(true);

                session.setStatus(
                        "Completed"
                );

                session.setEndTime(
                        LocalDateTime.now()
                );

                session.setDurationSeconds(
                        remainingSeconds
                );

                sessionsController.update(
                        session
                );

                computersController.changeStatus(
                        selectedComputer.getId(),
                        Computers.STATUS_AVAILABLE
                );

                System.out.println();
                System.out.println();
                System.out.println(
                        "================================"
                );
                System.out.println(
                        "        SESSION LOGGED OUT"
                );
                System.out.println(
                        "================================"
                );

                System.out.println(
                        "Computer: " +
                                selectedComputer.getComputerName()
                );

                int timeUsed =
                        durationSeconds -
                                remainingSeconds;

                System.out.println(
                        "Time Used: " +
                                formatTime(timeUsed)
                );

                System.out.println(
                        "Computer is now AVAILABLE."
                );

                System.out.println(
                        "================================"
                );

                return;
            }


            inputFinished.set(true);

            session.setStatus(
                    "Completed"
            );

            session.setEndTime(
                    LocalDateTime.now()
            );

            session.setDurationSeconds(0);

            sessionsController.update(
                    session
            );

            computersController.changeStatus(
                    selectedComputer.getId(),
                    Computers.STATUS_AVAILABLE
            );

            System.out.printf(
                    "\rTime Remaining: 00:00   "
            );

            System.out.println();
            System.out.println();

            System.out.println(
                    "================================"
            );
            System.out.println(
                    "        SESSION COMPLETED"
            );
            System.out.println(
                    "================================"
            );

            System.out.println(
                    "Computer: " +
                            selectedComputer.getComputerName()
            );

            System.out.println(
                    "Computer is now AVAILABLE."
            );

            System.out.println(
                    "================================"
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            inputFinished.set(true);

            System.out.println();
            System.out.println(
                    "Countdown interrupted."
            );

        } catch (SQLException e) {

            inputFinished.set(true);

            System.out.println();
            System.out.println(
                    "Database error: " +
                            e.getMessage()
            );
        }
    }

    // VIEW SESSIONS

    private void viewSessions() {

        try {

            List<Sessions> sessions =
                    sessionsController.getAll();

            if (sessions.isEmpty()) {

                System.out.println();
                System.out.println(
                        "No sessions found."
                );

                pause();
                return;
            }

            System.out.println();
            System.out.println(
                    "================================================================================"
            );
            System.out.println(
                    "                              ALL SESSIONS"
            );
            System.out.println(
                    "================================================================================"
            );

            System.out.printf(
                    "%-5s %-25s %-15s %-25s %-12s%n",
                    "ID",
                    "User",
                    "Computer",
                    "Rate",
                    "Status"
            );

            System.out.println(
                    "--------------------------------------------------------------------------------"
            );

            for (Sessions session : sessions) {

                // USER NAME
                String userName =
                        "Unknown User";

                Users user =
                        usersController.getById(
                                session.getUserId()
                        );

                if (user != null) {

                    userName =
                            user.getFullName();
                }

                // COMPUTER NAME
                String computerName =
                        "PC-" +
                                String.format(
                                        "%02d",
                                        session.getComputerId()
                                );

                Computers computer =
                        computersController.getById(
                                session.getComputerId()
                        );

                if (computer != null) {

                    computerName =
                            computer.getComputerName();
                }

                // RATE NAME
                String rateName =
                        "Unknown Rate";

                if (session.getRateId() != null) {

                    Rates rate =
                            ratesController.getById(
                                    session.getRateId()
                            );

                    if (rate != null) {

                        rateName =
                                rate.getRateName();
                    }
                }

                System.out.printf(
                        "%-5d %-25s %-15s %-25s %-12s%n",
                        session.getId(),
                        userName,
                        computerName,
                        rateName,
                        session.getStatus()
                );
            }

            System.out.println(
                    "================================================================================"
            );

            pause();

        } catch (SQLException e) {

            System.out.println();
            System.out.println(
                    "Database error: " +
                            e.getMessage()
            );

            pause();
        }
    }


    private void editSession() {

        try {

            System.out.println();
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "          EDIT SESSION"
            );
            System.out.println(
                    "================================"
            );

            System.out.print(
                    "Enter Session ID: "
            );

            String input =
                    scanner.nextLine().trim();

            int sessionId;

            try {

                sessionId =
                        Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid Session ID."
                );

                pause();
                return;
            }

            Sessions session =
                    sessionsController.getById(
                            sessionId
                    );

            if (session == null) {

                System.out.println(
                        "Session not found."
                );

                pause();
                return;
            }

            int oldComputerId =
                    session.getComputerId();

            System.out.println();
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "       CURRENT INFORMATION"
            );
            System.out.println(
                    "================================"
            );

            Users currentUser =
                    usersController.getById(
                            session.getUserId()
                    );

            Computers currentComputer =
                    computersController.getById(
                            session.getComputerId()
                    );

            Rates currentRate = null;

            if (session.getRateId() != null) {

                currentRate =
                        ratesController.getById(
                                session.getRateId()
                        );
            }

            System.out.println(
                    "Session ID: " +
                            session.getId()
            );

            System.out.println(
                    "User: " +
                            (currentUser == null
                                    ? "Unknown User"
                                    : currentUser.getFullName())
            );

            System.out.println(
                    "Computer: " +
                            (currentComputer == null
                                    ? "Unknown Computer"
                                    : currentComputer.getComputerName())
            );

            System.out.println(
                    "Rate: " +
                            (currentRate == null
                                    ? "Unknown Rate"
                                    : currentRate.getRateName())
            );

            System.out.println(
                    "Duration: " +
                            (session.getDurationMinutes() == null
                                    ? "-"
                                    : session.getDurationMinutes()
                                    + " minutes")
            );

            System.out.println(
                    "Status: " +
                            session.getStatus()
            );

            System.out.println(
                    "================================"
            );


            System.out.print(
                    "New User ID [" +
                            session.getUserId() +
                            "]: "
            );

            String userInput =
                    scanner.nextLine().trim();

            if (!userInput.isEmpty()) {

                try {

                    int newUserId =
                            Integer.parseInt(
                                    userInput
                            );

                    Users newUser =
                            usersController.getById(
                                    newUserId
                            );

                    if (newUser == null) {

                        System.out.println(
                                "User not found."
                        );

                        pause();
                        return;
                    }

                    if (!Users.STATUS_ACTIVE.equalsIgnoreCase(
                            newUser.getStatus()
                    )) {

                        System.out.println(
                                "Selected user is inactive."
                        );

                        pause();
                        return;
                    }

                    session.setUserId(
                            newUserId
                    );

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Invalid User ID."
                    );

                    pause();
                    return;
                }
            }


            System.out.print(
                    "New Computer ID [" +
                            session.getComputerId() +
                            "]: "
            );

            String computerInput =
                    scanner.nextLine().trim();

            if (!computerInput.isEmpty()) {

                try {

                    int newComputerId =
                            Integer.parseInt(
                                    computerInput
                            );

                    Computers newComputer =
                            computersController.getById(
                                    newComputerId
                            );

                    if (newComputer == null) {

                        System.out.println(
                                "Computer not found."
                        );

                        pause();
                        return;
                    }

                    if (newComputerId != oldComputerId &&
                            !Computers.STATUS_AVAILABLE.equalsIgnoreCase(
                                    newComputer.getStatus()
                            )) {

                        System.out.println(
                                "Selected computer is not available."
                        );

                        pause();
                        return;
                    }

                    session.setComputerId(
                            newComputerId
                    );

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Invalid Computer ID."
                    );

                    pause();
                    return;
                }
            }


            System.out.print(
                    "New Rate ID [" +
                            (session.getRateId() == null
                                    ? "-"
                                    : session.getRateId()) +
                            "]: "
            );

            String rateInput =
                    scanner.nextLine().trim();

            if (!rateInput.isEmpty()) {

                try {

                    int newRateId =
                            Integer.parseInt(
                                    rateInput
                            );

                    Rates newRate =
                            ratesController.getById(
                                    newRateId
                            );

                    if (newRate == null) {

                        System.out.println(
                                "Rate not found."
                        );

                        pause();
                        return;
                    }

                    if (!Rates.STATUS_ACTIVE.equalsIgnoreCase(
                            newRate.getStatus()
                    )) {

                        System.out.println(
                                "Selected rate is inactive."
                        );

                        pause();
                        return;
                    }

                    session.setRateId(
                            newRateId
                    );

                    int newDuration =
                            getDurationMinutes(
                                    newRate
                            );

                    if (newDuration > 0) {

                        session.setDurationMinutes(
                                newDuration
                        );

                        session.setDurationSeconds(
                                newDuration * 60
                        );
                    }

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Invalid Rate ID."
                    );

                    pause();
                    return;
                }
            }

            // duration
            int currentDuration =
                    session.getDurationMinutes() == null
                            ? 0
                            : session.getDurationMinutes();

            System.out.print(
                    "New Duration [" +
                            currentDuration +
                            " minutes, Enter to keep]: "
            );

            String durationInput =
                    scanner.nextLine().trim();

            if (!durationInput.isEmpty()) {

                try {

                    int duration =
                            Integer.parseInt(
                                    durationInput
                            );

                    if (duration <= 0) {

                        System.out.println(
                                "Duration must be greater than 0."
                        );

                        pause();
                        return;
                    }

                    session.setDurationMinutes(
                            duration
                    );

                    session.setDurationSeconds(
                            duration * 60
                    );

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Invalid duration."
                    );

                    pause();
                    return;
                }
            }


            System.out.print(
                    "New Status [" +
                            session.getStatus() +
                            "] (Ongoing/Completed): "
            );

            String statusInput =
                    scanner.nextLine().trim();

            if (!statusInput.isEmpty()) {

                if (statusInput.equalsIgnoreCase(
                        "Ongoing"
                )) {

                    session.setStatus(
                            "Ongoing"
                    );

                } else if (
                        statusInput.equalsIgnoreCase(
                                "Completed"
                        )
                ) {

                    session.setStatus(
                            "Completed"
                    );

                } else {

                    System.out.println(
                            "Invalid status."
                    );

                    pause();
                    return;
                }
            }

            System.out.println();
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "          NEW INFORMATION"
            );
            System.out.println(
                    "================================"
            );

            Users newUser =
                    usersController.getById(
                            session.getUserId()
                    );

            Computers newComputer =
                    computersController.getById(
                            session.getComputerId()
                    );

            Rates newRate = null;

            if (session.getRateId() != null) {

                newRate =
                        ratesController.getById(
                                session.getRateId()
                        );
            }

            System.out.println(
                    "Session ID: " +
                            session.getId()
            );

            System.out.println(
                    "User: " +
                            (newUser == null
                                    ? "Unknown User"
                                    : newUser.getFullName())
            );

            System.out.println(
                    "Computer: " +
                            (newComputer == null
                                    ? "Unknown Computer"
                                    : newComputer.getComputerName())
            );

            System.out.println(
                    "Rate: " +
                            (newRate == null
                                    ? "Unknown Rate"
                                    : newRate.getRateName())
            );

            System.out.println(
                    "Duration: " +
                            session.getDurationMinutes() +
                            " minutes"
            );

            System.out.println(
                    "Status: " +
                            session.getStatus()
            );

            System.out.println(
                    "================================"
            );

            System.out.print(
                    "Save changes? (Y/N): "
            );

            String confirmation =
                    scanner.nextLine().trim();

            if (!confirmation.equalsIgnoreCase("Y")) {

                System.out.println(
                        "Edit cancelled."
                );

                pause();
                return;
            }

            // COMPUTER STATUS

            if (oldComputerId !=
                    session.getComputerId()) {

                computersController.changeStatus(
                        oldComputerId,
                        Computers.STATUS_AVAILABLE
                );
            }

            if ("Ongoing".equalsIgnoreCase(
                    session.getStatus()
            )) {

                computersController.changeStatus(
                        session.getComputerId(),
                        Computers.STATUS_IN_USE
                );

            } else {

                computersController.changeStatus(
                        session.getComputerId(),
                        Computers.STATUS_AVAILABLE
                );

                if (session.getEndTime() == null) {

                    session.setEndTime(
                            LocalDateTime.now()
                    );
                }

                session.setDurationSeconds(0);
            }

            sessionsController.update(
                    session
            );

            System.out.println();
            System.out.println(
                    "Session edited successfully."
            );

        } catch (SQLException e) {

            System.out.println();
            System.out.println(
                    "Database error: " +
                            e.getMessage()
            );
        }

        pause();
    }

    // REMOVE SESSION

    private void removeSession() {

        try {

            System.out.println();
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "          REMOVE SESSION"
            );
            System.out.println(
                    "================================"
            );

            System.out.print(
                    "Enter Session ID: "
            );

            String input =
                    scanner.nextLine().trim();

            int sessionId;

            try {

                sessionId =
                        Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid Session ID."
                );

                pause();
                return;
            }

            Sessions session =
                    sessionsController.getById(
                            sessionId
                    );

            if (session == null) {

                System.out.println(
                        "Session not found."
                );

                pause();
                return;
            }

            Users user =
                    usersController.getById(
                            session.getUserId()
                    );

            Computers computer =
                    computersController.getById(
                            session.getComputerId()
                    );

            Rates rate = null;

            if (session.getRateId() != null) {

                rate =
                        ratesController.getById(
                                session.getRateId()
                        );
            }


            // SHOW INFORMATION

            System.out.println();
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "       SESSION INFORMATION"
            );
            System.out.println(
                    "================================"
            );

            System.out.println(
                    "Session ID: " +
                            session.getId()
            );

            System.out.println(
                    "User: " +
                            (user == null
                                    ? "Unknown User"
                                    : user.getFullName())
            );

            System.out.println(
                    "Computer: " +
                            (computer == null
                                    ? "Unknown Computer"
                                    : computer.getComputerName())
            );

            System.out.println(
                    "Rate: " +
                            (rate == null
                                    ? "Unknown Rate"
                                    : rate.getRateName())
            );

            System.out.println(
                    "Status: " +
                            session.getStatus()
            );

            System.out.println(
                    "================================"
            );

            System.out.print(
                    "Are you sure you want to " +
                            "remove this session? (Y/N): "
            );

            String confirmation =
                    scanner.nextLine().trim();

            if (!confirmation.equalsIgnoreCase("Y")) {

                System.out.println(
                        "Remove cancelled."
                );

                pause();
                return;
            }

            // Free computer if session is ongoing
            if ("Ongoing".equalsIgnoreCase(
                    session.getStatus()
            )) {

                computersController.changeStatus(
                        session.getComputerId(),
                        Computers.STATUS_AVAILABLE
                );
            }

            boolean removed =
                    sessionsController.remove(
                            sessionId
                    );

            System.out.println();

            if (removed) {

                System.out.println(
                        "Session removed successfully."
                );

            } else {

                System.out.println(
                        "Session could not be removed."
                );
            }

        } catch (SQLException e) {

            System.out.println();
            System.out.println(
                    "Database error: " +
                            e.getMessage()
            );
        }

        pause();
    }


    private int readChoice(
            String message,
            int max) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            try {

                int choice =
                        Integer.parseInt(input);

                if (choice >= 1 &&
                        choice <= max) {

                    return choice;
                }

            } catch (NumberFormatException ignored) {
            }

            System.out.println(
                    "Invalid choice. Please try again."
            );
        }
    }

    private String formatTime(
            int totalSeconds) {

        if (totalSeconds < 0) {
            totalSeconds = 0;
        }

        int minutes =
                totalSeconds / 60;

        int seconds =
                totalSeconds % 60;

        return String.format(
                "%02d:%02d",
                minutes,
                seconds
        );
    }


    private void pause() {

        System.out.print(
                "\nPress Enter to continue..."
        );

        scanner.nextLine();
    }
}