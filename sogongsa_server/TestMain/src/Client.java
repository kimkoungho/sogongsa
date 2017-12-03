import java.io.*;
import java.net.Socket;
import java.util.*;
import java.nio.file.*;

public class Client extends Thread implements Serializable {

	Socket socket = null;// 서버의 소켓
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;

	public String clientId = null;
	int index = -1;

	public String TAG = "q*%u*%i*%t";// 완료 되었다는 태그

	DataBase_Mysql db = null;

	public Client(Socket socket, DataBase_Mysql db) {
		this.socket = socket;
		this.db = db;
		if (socket.isConnected()) {
			try {
				oos = new ObjectOutputStream(socket.getOutputStream());
				this.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void send(Client client, Object msg) {
		try {
			oos.writeObject(msg);
			oos.flush();

			System.out.println(msg.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			System.out.println("시작");
			InputStream in = socket.getInputStream();
			ois = new ObjectInputStream(in);

			while (socket.isConnected()) {
				HashMap clientData = (HashMap) ois.readObject();
				System.out.println(clientData.toString());

				String cmd = (String) clientData.get("cmd");
				System.out.println("명령 : " + cmd);

				// 회원 가입 페이지
				// id, pw, name, major, sex, age, phone, hobby
				if (cmd.equals("signup")) {
					String id = (String) clientData.get("userId");
					String pw = (String) clientData.get("userPw");
					String name = (String) clientData.get("userName");
					String major = (String) clientData.get("userMajor");
					int sex = Integer.parseInt((String) clientData.get("userSex"));
					int age = Integer.parseInt((String) clientData.get("userAge"));
					String phone = (String) clientData.get("userPhone");
					String image = (String) clientData.get("userImage");
					String comment = (String) clientData.get("userComment");
					int hobby = Integer.parseInt((String) clientData.get("userHobby"));

					System.out.println(id + " " + pw + " " + name);

					boolean flag = db.signUp(Integer.parseInt(id), pw, name, major, sex, age, phone,
							"profile\\default.jpg", "안녕하세요.", hobby);

					if (flag)
						send(this, TAG);
					else
						send(this, "00");
				}

				// 로긴 페이지
				if (cmd.equals("login")) {

					String id = (String) clientData.get("id");
					String pw = (String) clientData.get("pw");

					System.out.println(id + "  " + pw);
					clientId = id;

					if (db.login(Integer.parseInt(id), pw))
						send(this, TAG);// 로그인 성공
					else
						send(this, "abc");
				}

				// 메인 페이지
				if (cmd.equals("main")) {
					String id = (String) clientData.get("id");
					System.out.println(id);

					// 사용자 정보
					HashMap userInfo = db.getUserInfo(Integer.parseInt(id));
					if (userInfo.get("img").equals("profile\\default.jpg")) {
						Path path = Paths.get("profile\\default.jpg");
						byte[] img = Files.readAllBytes(path);
						userInfo.put("img", img);
					} else {
						Path path = Paths.get((String) userInfo.get("img"));
						byte[] img = Files.readAllBytes(path);
						userInfo.put("img", img);
					}

					// 사용자 그룹
					ArrayList<HashMap> mygroup = db.myGroup(Integer.parseInt(id));

					for (HashMap item : mygroup) {
						if (item.get("img").equals("profile\\default.jpg")) {
							Path path = Paths.get("profile\\default.jpg");
							byte[] img = Files.readAllBytes(path);
							userInfo.put("img", img);
						} else {
							Path path1 = Paths.get((String) item.get("img"));
							System.out.println((String) item.get("img"));
							byte[] img1 = Files.readAllBytes(path1);
							item.put("img", img1);
						}
					}

					// 모든 그룹
					ArrayList<HashMap> groupAll = db.searchGroup(0, "");

					// 사용자 일정
					ArrayList userMeet = db.myMeeting(Integer.parseInt(id));

					// 사용자 노티
					ArrayList<HashMap> userNoti = db.getNoti(Integer.parseInt(id));
					for (HashMap item : userNoti) {
						System.out.println((String) item.get("img"));
						if (item.get("img").equals("profile\\default.jpg")) {
							Path path = Paths.get("profile\\default.jpg");
							byte[] img = Files.readAllBytes(path);
							item.put("img", img);
						} else {
							Path path = Paths.get((String) item.get("img"));
							byte[] img = Files.readAllBytes(path);
							item.put("img", img);
						}
					}

					HashMap data = new HashMap();
					data.put("userInfo", userInfo);
					data.put("myGroup", mygroup);
					data.put("groupAll", groupAll);
					data.put("userMeet", userMeet);
					data.put("userNoti", userNoti);

					if (userNoti == null)
						System.out.println("1111111");
					System.out.println(userNoti);

					send(this, data);
				}

				// 그룹 페이지
				if (cmd.equals("group")) {
					String id = (String) clientData.get("id");
					String uId = (String) clientData.get("uId");

					HashMap group = db.selectGroup(Integer.parseInt(id), Integer.parseInt(uId));
					Path path = Paths.get((String) group.get("img"));
					byte[] img = Files.readAllBytes(path);
					group.put("img", img);

					ArrayList<HashMap> meeting = db.getMeeting(Integer.parseInt(id));

					// 후기 - ?
					ArrayList<HashMap> review = db.getReview(Integer.parseInt(id));
					if (review != null) {
						for (HashMap hashmap : review) {
							String uImg = (String) hashmap.get("uImg");
							Path path1 = Paths.get(uImg);
							byte[] img1 = Files.readAllBytes(path1);
							hashmap.put("uImg", img1);

							String rImg = (String) hashmap.get("rImg");
							System.out.println(rImg);
							Path path2 = Paths.get(rImg);
							byte[] img2 = Files.readAllBytes(path2);
							hashmap.put("rImg", img2);
						}
					}

					HashMap data = new HashMap();
					data.put("group", group);
					data.put("meeting", meeting);
					data.put("review", review);

					send(this, data);
				}

				// 검색 페이지
				if (cmd.equals("search")) {
					String category = (String) clientData.get("category");
					String val = (String) clientData.get("text");

					ArrayList<HashMap> groupAll = new ArrayList<HashMap>();
					HashMap group = new HashMap();

					group.put("id", "1");
					group.put("name", "축구팸");
					group.put("category", "3");
					group.put("personNum", "30");
					group.put("uName", "김경호");
					groupAll.add(group);

					ArrayList<HashMap> data = db.searchGroup(Integer.parseInt(category), val);

					send(this, groupAll);
				}

				// 프로필 변경 페이지
				if (cmd.equals("profile")) {
					String id = (String) clientData.get("id");
					byte[] profile = (byte[]) clientData.get("profile");
					String name = (String) clientData.get("name");
					String age = (String) clientData.get("age");
					String content = (String) clientData.get("content");
					String url = null;

					if (profile.length > 0) {
						int cnt = db.getImageCnt("profile\\profile%");
						url = "profile" + cnt + ".jpg";
						File userProfile = new File(url);
						FileOutputStream fout = new FileOutputStream(userProfile);
						fout.write(profile, 0, profile.length);
					}

					db.changeProfile(Integer.parseInt(id), name, url, content, Integer.parseInt(age));
					send(this, TAG);
				}

				// 회원정보 변경 페이지
				if (cmd.equals("pass")) {
					String id = (String) clientData.get("id");
					String password = (String) clientData.get("pw");
					String hobby = (String) clientData.get("hobby");

					db.changePass(Integer.parseInt(id), password, Integer.parseInt(hobby));
					send(this, TAG);
				}

				// 그룹 만들기 페이지
				if (cmd.equals("createGroup")) {
					String url = null;
					String name = (String) clientData.get("name");
					String content = (String) clientData.get("content");
					String header = (String) clientData.get("uId");
					String category = (String) clientData.get("category");

					if (clientData.get("image") != null) {
						byte[] image = (byte[]) clientData.get("image");

						int i = db.getGroupCnt("group\\group%");
						url = "group" + i + ".jpg";
						System.out.println(url);
						File userProfile = new File(url);
						FileOutputStream fout = new FileOutputStream(userProfile);
						fout.write(image, 0, image.length);
					}

					db.makeGroup(name, content, 1, Integer.parseInt(header), Integer.parseInt(category), url, 0,
							Integer.parseInt(header));

					System.out.println(clientData.toString());

					send(this, TAG);
				}

				// 모임 만들기
				if (cmd.equals("createMeeting")) {
					int id = db.getMeetingId();

					String gId = (String) clientData.get("gId");
					String uId = (String) clientData.get("uId");
					String title = (String) clientData.get("title");
					String date = (String) clientData.get("date");
					String location = (String) clientData.get("location");
					String cost = (String) clientData.get("cost");
					String personNum = (String) clientData.get("personNum");
					String content = (String) clientData.get("content");

					db.makeMeeting(Integer.parseInt(gId), Integer.parseInt(uId), title, content, date,
							Integer.parseInt(cost), Integer.parseInt(personNum), location);

					System.out.println(clientData.toString());

					send(this, TAG);
				}

				// 후기 작성하기 페이지
				if (cmd.equals("writeReview")) {
					String uId = (String) clientData.get("uId");
					String gId = (String) clientData.get("gId");
					String content = (String) clientData.get("content");
					String date = (String) clientData.get("date");
					String image = "";

					if (clientData.get("img") != null) {
						byte[] img = (byte[]) clientData.get("img");

						int cnt = db.getReviewCnt("review%");

						image = "review\\review" + cnt + ".jpg";
						File userProfile = new File(image);
						FileOutputStream fout = new FileOutputStream(userProfile);
						fout.write(img, 0, img.length);
					}

					db.makeReview(Integer.parseInt(uId), Integer.parseInt(gId), content, date, image);

					send(this, TAG);
				}

				// 댓글 작성
				if (cmd.equals("writeReply")) {
					String uId = (String) clientData.get("uId");
					String rId = (String) clientData.get("rId");
					String content = (String) clientData.get("content");
					String date = (String) clientData.get("date");

					db.makeReply(Integer.parseInt(uId), Integer.parseInt(rId), content, date);

					send(this, TAG);
				}

				if (cmd.equals("reply")) {
					int gId = Integer.parseInt((String) clientData.get("gId"));
					int rId = Integer.parseInt((String) clientData.get("rId"));
					ArrayList<HashMap> data = db.getReply(gId, rId);
					System.out.println(gId);

					for (HashMap item : data) {
						String url = (String) item.get("uImg");
						Path path = Paths.get(url);
						byte[] uImg = Files.readAllBytes(path);
						item.put("uImg", uImg);
					}

					send(this, data);
				}

				// 그룹 신청
				if (cmd.equals("applyGroup")) {
					// 그룹 작성자의 아이디를 검색함
					int gId = Integer.parseInt((String) clientData.get("gId"));
					int uId = Integer.parseInt((String) clientData.get("uId"));

					db.waitUser(uId, gId, -1);

					int header = db.getGroupHeader(gId);
					HashMap groupInfo = db.selectGroup(gId, uId);

					send(this, TAG);

				}

				// 그룹 신청 승인
				// 대기 사용자에서 해당 사용자를 승인한다
				if (cmd.equals("groupApprove")) {
					int flag = Integer.parseInt((String) clientData.get("flag"));
					int gId = Integer.parseInt((String) clientData.get("id"));
					int uId = Integer.parseInt((String) clientData.get("uId"));

					db.approveGroup(gId, uId, flag);

					send(this, TAG);

				}
				// 모임 신청
				if (cmd.equals("applyMeeting")) {
					int mId = Integer.parseInt((String) clientData.get("mId"));
					int uId = Integer.parseInt((String) clientData.get("uId"));
					db.waitUser(uId, -1, mId);
					int writer = db.getMeetingWriter(mId);
					send(this, TAG);

				}

				// 모임 신청 승인
				if (cmd.equals("meetingApprove")) {
					int flag = Integer.parseInt((String) clientData.get("flag"));
					int mId = Integer.parseInt((String) clientData.get("id"));
					int uId = Integer.parseInt((String) clientData.get("uId"));

					db.approveMeeting(mId, uId, flag);

					send(this, TAG);
				}

				if (cmd.equals("deleteWait")) {
					int uId = Integer.parseInt((String) clientData.get("userId"));
					int id = Integer.parseInt((String) clientData.get("id"));
					int flag = Integer.parseInt((String) clientData.get("sw"));

					System.out.println(clientData);
					if (flag == 0)
						db.deleteWait(uId, -1, id);
					else
						db.deleteWait(uId, id, -1);
					send(this, TAG);
				}

				clientData.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Close();
		}
	}

	public void Close() {
		if (socket.isConnected()) {
			try {
				this.interrupt();
				oos.close();
				ois.close();
				socket.close();
				if (index != -1)
					Server.list.remove(index);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}