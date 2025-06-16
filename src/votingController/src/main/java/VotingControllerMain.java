import Contract.*;
import java.util.Scanner;

public class VotingControllerMain {
    private static boolean isVotingActive = false;
    private static Thread votingThread = null;

    public static void main(String[] args) {
        int status = 0;
        java.util.List<String> extraArgs = new java.util.ArrayList<String>();

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "config.pub", extraArgs)) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (votingThread != null) {
                    votingThread.interrupt();
                }
                communicator.destroy();
            }));

            status = run(communicator, extraArgs.toArray(new String[extraArgs.size()]));
        }
        System.exit(status);
    }

    private static int run(com.zeroc.Ice.Communicator communicator, String[] args) {
        String topicName = "ControlCommandTopic";

        com.zeroc.IceStorm.TopicManagerPrx manager = com.zeroc.IceStorm.TopicManagerPrx.checkedCast(
                communicator.propertyToProxy("TopicManager.Proxy"));
        if (manager == null) {
            System.err.println("invalid proxy");
            return 1;
        }

        com.zeroc.IceStorm.TopicPrx topic;
        try {
            topic = manager.retrieve(topicName);
        } catch (com.zeroc.IceStorm.NoSuchTopic e) {
            try {
                topic = manager.create(topicName);
            } catch (com.zeroc.IceStorm.TopicExists ex) {
                System.err.println("temporary failure, try again.");
                return 1;
            }
        }

        com.zeroc.Ice.ObjectPrx publisher = topic.getPublisher();
        ControlCommandPrx controlCommand = ControlCommandPrx.uncheckedCast(publisher);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Voting Controller Menu:");
        System.out.println("1. Start Voting");
        System.out.println("2. End Voting");
        System.out.println("3. Exit");

        while (true) {
            System.out.print("\nEnter your choice (1-3): ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    if (!isVotingActive) {
                        isVotingActive = true;
                        controlCommand.receiveCommand("START_VOTATION");
                        System.out.println("[SENT] START_VOTATION");

                        votingThread = new Thread(() -> {
                            while (isVotingActive) {
                                try {
                                    Thread.sleep(20000); // 20 seconds
                                    if (isVotingActive) {
                                        controlCommand.receiveCommand("SEND_VOTE");
                                        System.out.println("[SENT] SEND_VOTE");
                                    }
                                } catch (InterruptedException e) {
                                    break;
                                }
                            }
                        });
                        votingThread.start();
                    } else {
                        System.out.println("Voting is already active!");
                    }
                    break;

                case 2:
                    if (isVotingActive) {
                        isVotingActive = false;
                        if (votingThread != null) {
                            votingThread.interrupt();
                        }
                        controlCommand.receiveCommand("END_VOTATION");
                        System.out.println("[SENT] END_VOTATION");
                        System.out.println("Shutting down...");
                        return 0;
                    } else {
                        System.out.println("Voting is not active!");
                    }
                    break;

                case 3:
                    if (isVotingActive) {
                        isVotingActive = false;
                        if (votingThread != null) {
                            votingThread.interrupt();
                        }
                        controlCommand.receiveCommand("END_VOTATION");
                        System.out.println("[SENT] END_VOTATION");
                    }
                    return 0;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}