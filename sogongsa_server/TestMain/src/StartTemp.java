import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.*;

public class StartTemp {

	public static void main(String[] args) {
		new Server(3333).Start();
	}
}