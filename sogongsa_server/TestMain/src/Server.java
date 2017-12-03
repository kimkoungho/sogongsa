import java.net.*;
import java.util.*;

public class Server extends Thread {

	Client client = null;
	public static ArrayList<Client> list = new ArrayList<Client>();
	ServerSocket serverSok = null;
	Socket clientSok = null;
	int port = 0;
	boolean service = false;

	DataBase_Mysql db;
	String dbName = "sogongsa";
	String dbId = "sogongsauser";
	String dbPw = "sogongsapass";

	public Server(int port) {
		this.port = port;

		db = new DataBase_Mysql(dbName, dbId, dbPw);
	}

	public void run() {
		try {
			serverSok = new ServerSocket(port);

			while (true) {
				System.out.println("\nClient접속이 대기중입니다.");
				clientSok = serverSok.accept();
				System.out.println(clientSok.getInetAddress() + "의 주소가 연결되었습니다. ");
				if (clientSok.isConnected()) {

					Client client = new Client(clientSok, db);
					list.add(client);
					client.index=list.size()-1;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void Start() {
		this.start();
		service = true;
	}

	public void Stop() {
		if (service) {
			try {
				serverSok.close();
				this.interrupt();

				for (int i=0; i<list.size(); i++) {
					Client cc=list.get(i);
					cc.Close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			service = false;
		}
	}
}