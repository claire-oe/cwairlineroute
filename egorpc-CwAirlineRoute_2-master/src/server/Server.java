package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

        private static final HashSet<ClientThreadHandler> CLIENT_HANDLER_THREADS = new HashSet<>();

        public static void main (String[]args){
            Server server = new Server();
            //broadcasts a message every 2 seconds
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (CLIENT_HANDLER_THREADS.size() != 0) {
                        System.out.println("Server: attempting to broadcast a message...");
                        try {
                            clientBroadcasting();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 0, 2000);
            server.connectToClient();
        }

        /**
         * Method iterates through every thread/client and sends a message using the sendBroadcast()method
         * */
        public static void clientBroadcasting () throws IOException {
            for (ClientThreadHandler ClientThread : CLIENT_HANDLER_THREADS) {
                ClientThread.sendBroadcast();
            }
        }

        public static void removeThread (ClientThreadHandler threadToRemove){
            CLIENT_HANDLER_THREADS.remove(threadToRemove);
        }

        public void connectToClient () {
            System.out.println("Server: Server starting.");

            try (ServerSocket serverSocket = new ServerSocket(2000)) {

                while (true) {
                    System.out.println("Server: Waiting for connecting client...");
                    try {
                        // Listen for a connection to this socket and accept it
                        Socket socket = serverSocket.accept();

                        // Instantiate a new CWClientHandlerThread
                        ClientThreadHandler clientThreadHandler = new ClientThreadHandler(socket);

                        // Instantiate a new thread
                        Thread connThread = new Thread(clientThreadHandler);
                        // Execute this thread
                        connThread.start();

                        CLIENT_HANDLER_THREADS.add(clientThreadHandler);

                    } catch (IOException ex) {
                        System.out.println("Server: Could not start connection to a client.");
                    }
                }
            } catch (IOException ex) {

                // Log error details
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);

                // Shut down server in the case of an I/O error
                System.out.println("Server: Server shut down");
            }
        }


}
