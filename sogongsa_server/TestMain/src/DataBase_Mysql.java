import java.sql.*;
import java.util.*;

public class DataBase_Mysql {
   private Connection conn = null;
   private PreparedStatement pstmt = null;
   private ResultSet rs = null;


   public DataBase_Mysql(String db, String id, String pw) {
      String mysql = "jdbc:mysql://localhost:3306/" + db;

      try {
         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(mysql, id, pw);
         System.out.println("mysql 연결 성공");

      } catch (Exception e) {
         System.out.println("생성자 오류");
         e.printStackTrace();
      }
   }

   // 그룹 아이디, 그룹 순서 찾기
   public int getGroupId() {
      String query = "select max(groupId) from groupInfo";

      int ret = -1;

      try {
         pstmt = conn.prepareStatement(query);
         rs = pstmt.executeQuery();

         if (rs.next()) {
            int id = rs.getInt(1) + 1;
            ret = id;
         }

      } catch (Exception e) {
         e.printStackTrace();
      }

      return ret;
   }

   // 모임 아이디 찾기
   public int getMeetingId() {
      String query = "select max(meetingId) from meetingInfo";

      int id = 0;

      try {
         pstmt = conn.prepareStatement(query);
         rs = pstmt.executeQuery();

         if (rs.next()) {
            id = rs.getInt(1) + 1;

            System.out.println(id);
         }

      } catch (Exception e) {
         e.printStackTrace();
      }

      return id;
   }

   // 리뷰 아이디 찾기, 순서 찾기
   public String getReviewId() {
      String query = "select max(reviewId), max(reviewGrpNo) from reviewInfo";

      String ret = "";

      try {
         pstmt = conn.prepareStatement(query);
         rs = pstmt.executeQuery();

         if (rs.next()) {
            ret += (rs.getInt(1) + 1) + "&";
            ret += (rs.getInt(2) + 1);
            System.out.println(ret);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return ret;
   }

   public int getGroupCnt(String str) {
      String query = "select max(groupImage) from groupInfo where groupImage like '" + str + "'";

         int cnt = 0;
         try {
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();

            while (rs.next()) {
              System.out.println(2222222);
               String tmp = rs.getString(1);
               if(tmp==null)
                  break;
               System.out.println(tmp);
               tmp=tmp.substring(11);
               System.out.println(tmp);
               tmp=tmp.split(".jpeg")[0];
               System.out.println(tmp);
               if (cnt < Integer.parseInt(tmp))
                  cnt = Integer.parseInt(tmp);
            }
         } catch (Exception e) {
            e.printStackTrace();
         }

         System.out.println(cnt);
         return ++cnt;
   }

   // 사용자 이미지의 순서 찾기
   public int getImageCnt(String str) {
      String query = "select max(userImage) from userInfo where userImage like '" + str + "'";

      int cnt = 0;
      try {
         pstmt = conn.prepareStatement(query);
         rs = pstmt.executeQuery();

         while (rs.next()) {
            System.out.println(2222222);
            String tmp = rs.getString(1);
            if (tmp == null)
               break;
            System.out.println(tmp);
            tmp = tmp.substring(15);
            System.out.println(tmp);
            tmp = tmp.split(".jpeg")[0];
            System.out.println(tmp);
            if (cnt < Integer.parseInt(tmp))
               cnt = Integer.parseInt(tmp);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      System.out.println(cnt);
      return ++cnt;
   }

   // 리뷰 이미지의 순서 찾기
   public int getReviewCnt(String str) {
      String query = "select max(reviewImage) from reviewInfo where reviewImage like '" + str + "'";

      int cnt = 0;
      try {
         pstmt = conn.prepareStatement(query);
         rs = pstmt.executeQuery();

         while (rs.next()) {
            System.out.println(2222222);
            String tmp = rs.getString(1);
            if (tmp == null)
               break;
            System.out.println(tmp);
            tmp = tmp.substring(13);
            System.out.println(tmp);
            tmp = tmp.split(".jpg")[0];
            System.out.println(tmp);
            if (cnt < Integer.parseInt(tmp))
               cnt = Integer.parseInt(tmp);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      System.out.println(cnt);
      return ++cnt;
   }

   // 그룹 신청
   public int getGroupHeader(int gId) {
      String query = "select groupHeader from groupInfo where groupId=" + gId;
      int header = -1;
      try {
         pstmt = conn.prepareStatement(query);
         rs = pstmt.executeQuery();

         if (rs.next())
            header = rs.getInt(1);

      } catch (Exception e) {
         e.printStackTrace();
      }
      return header;
   }

   public int getMeetingWriter(int mId) {
      String query = "select meetingUserId from meetingInfo where meetingId=" + mId;
      int writer = -1;
      try {
         pstmt = conn.prepareStatement(query);
         rs = pstmt.executeQuery();

         if (rs.next())
            writer = rs.getInt(1);

      } catch (Exception e) {
         e.printStackTrace();
      }
      return writer;
   }

   // 회원가입 - 회원 정보 받기
   public boolean signUp(int id, String pw, String name, String major, int sex, int age, String phone, String image,
         String comment, int hobby) {
      String idCheck = "select userId from userInfo";
      String query = "insert into userInfo values(?,?,?,?,?,?,?,?,?,?)";

      try {
         pstmt = conn.prepareStatement(idCheck);
         rs = pstmt.executeQuery();

         while (rs.next()) {
            if (id == rs.getInt(1))
               return false;
         }
         {
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.setString(2, pw);
            pstmt.setString(3, name);
            pstmt.setString(4, major);
            pstmt.setInt(5, sex);
            pstmt.setInt(6, age);
            pstmt.setString(7, phone);
            pstmt.setString(8, image);
            pstmt.setString(9, comment);
            pstmt.setInt(10, hobby);
            pstmt.executeUpdate();

            System.out.println("회원가입 성공");
            return true;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return false;
   }

   // 로그인
   public boolean login(int id, String pw) {
      String query = "select userPw from userInfo where userId=" + id;

      String dbPw = "";

      try {
         pstmt = conn.prepareStatement(query);
         rs = pstmt.executeQuery();

         if (rs.next())
            dbPw = rs.getString(1);

      } catch (Exception e) {
         e.printStackTrace();
      }

      if (pw.equals(dbPw))
         return true;
      else
         return false;
   }

   // 그룹 만들기
   public void makeGroup(String name, String content, int pNum, int header, int category, String image, int seq,
         int uId) {
      int id = getGroupId();
      String query = "insert into groupInfo values(?,?,?,?,?,?,?,?)";
      String qry = "insert into userGroup values(?,?)";

      try {
         pstmt = conn.prepareStatement(query);
         pstmt.setInt(1, id);
         pstmt.setString(2, name);
         pstmt.setString(3, content);
         pstmt.setInt(4, pNum);
         pstmt.setInt(5, header);
         pstmt.setInt(6, category);
         pstmt.setString(7, image);
         pstmt.setInt(8, seq);
         pstmt.executeUpdate();

         pstmt = conn.prepareStatement(qry);
         pstmt.setInt(1, uId);
         pstmt.setInt(2, id);
         pstmt.executeUpdate();

         System.out.println("그룹 만들기 성공");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // 모임 만들기 - 안드로이드에서 uId, gId, uName을 받음
   public void makeMeeting(int gId, int uId, String title, String content, String date, int cost, int pCnt,
         String location) {
      int id = getMeetingId();
      String query = "insert into meetingInfo values(?,?,?,?,?,?,?,?)";
      String qry = "insert into userMeeting values(?,?)";
      String qy = "insert into groupMeeting values(?,?)";

      try {
         pstmt = conn.prepareStatement(query);
         pstmt.setInt(1, id);
         pstmt.setInt(2, uId);
         pstmt.setString(3, title);
         pstmt.setString(4, content);
         pstmt.setString(5, date);
         pstmt.setInt(6, cost);
         pstmt.setInt(7, pCnt);
         pstmt.setString(8, location);
         pstmt.executeUpdate();

         pstmt = conn.prepareStatement(qry);
         pstmt.setInt(1, uId);
         pstmt.setInt(2, id);
         pstmt.executeUpdate();

         pstmt = conn.prepareStatement(qy);
         pstmt.setInt(1, gId);
         pstmt.setInt(2, id);
         pstmt.executeUpdate();

         System.out.println("모임 만들기 성공");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // 후기 작성하기
   public void makeReview(int uId, int gId, String content, String date, String image) {
      String data = getReviewId();
      int id = Integer.parseInt(data.split("&")[0]);
      int grpNo = Integer.parseInt(data.split("&")[1]);
      int level = 0;
      int seq = 0;

      System.out.println(image);

      String query = "insert into reviewInfo values(?,?,?,?,?,?,?,?)";
      String qry = "insert into groupReview values(?,?)";

      try {
         pstmt = conn.prepareStatement(query);
         pstmt.setInt(1, id);
         pstmt.setInt(2, uId);
         pstmt.setString(3, content);
         pstmt.setString(4, date);
         pstmt.setString(5, image);
         pstmt.setInt(6, grpNo);
         pstmt.setInt(7, level);
         pstmt.setInt(8, seq);
         pstmt.executeUpdate();

         pstmt = conn.prepareStatement(qry);
         pstmt.setInt(1, gId);
         pstmt.setInt(2, id);
         pstmt.executeUpdate();

         System.out.println("후기 작성 성공");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // 답변 작성하기
   public void makeReply(int uId, int gId, String content, String date) {
      String data = getReviewId();
      int id = Integer.parseInt(data.split("&")[0]);
      String subQ = "select reviewGrpNo, reviewLevel, reviewSeq from reviewInfo where reviewId=" + gId;
      // System.out.println(id);
      String query = "insert into reviewInfo values(?,?,?,?,?,?,?,?)";
      String qry = "insert into groupReview values(?,?)";

      try {
         pstmt = conn.prepareStatement(subQ);
         rs = pstmt.executeQuery();

         if(rs.next()==false)
        	 return;
         
         int grpNo = rs.getInt(1);
         int level = rs.getInt(2) + 1;
         int seq = rs.getInt(3) + 1;

         pstmt = conn.prepareStatement(query);
         pstmt.setInt(1, id);
         pstmt.setInt(2, uId);
         pstmt.setString(3, content);
         pstmt.setString(4, date);
         pstmt.setString(5, "null");
         pstmt.setInt(6, grpNo);
         pstmt.setInt(7, level);
         pstmt.setInt(8, seq);
         pstmt.executeUpdate();

         pstmt = conn.prepareStatement(qry);
         pstmt.setInt(1, gId);
         pstmt.setInt(2, id);
         pstmt.executeUpdate();

         System.out.println("댓글 작성 성공");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // 메인 페이지 - 사용자의 그룹을 출력...
   public ArrayList<HashMap> myGroup(int uId) {
      String query = "select groupName, groupImage, groupCategory, groupId from groupInfo "
            + "where groupId in(select groupId from userGroup where userId=" + uId + ")";

      ArrayList<HashMap> ret = new ArrayList<HashMap>();

      try {
         pstmt = conn.prepareStatement(query);
         rs = pstmt.executeQuery();
         while (rs.next()) {
            String name = rs.getString(1);
            String img = rs.getString(2);
            String category = rs.getString(3);
            int id = rs.getInt(4);

            HashMap hashmap = new HashMap();
            hashmap.put("name", name);
            hashmap.put("img", img);
            hashmap.put("category", category);
            hashmap.put("gId", Integer.toString(id));

            ret.add(hashmap);

         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return ret;
   }

   
   // 일정 페이지 - 나의 모임들을 모두 클라이언트가 출력
   public ArrayList myMeeting(int uId) {
      String query = "select meetingDate from meetingInfo where meetingId in (select meetingId from userMeeting where userId="
            + uId + ")";

      ArrayList ret = new ArrayList();

      try {
         pstmt = conn.prepareStatement(query);
         rs = pstmt.executeQuery();

         while (rs.next()) {
            ret.add(rs.getString(1));
         }

      } catch (Exception e) {
         e.printStackTrace();
      }

      return ret;
   }

   // 그룹 메인 페이지
   // 클라이언트에서 그룹 아이디 받음(gId를 갖고 있다가 이벤트가 발생한 gId 전달)
   // 그룹 이미지, 그룹 내용, 그룹 제목, 그룹 카테고리
   public HashMap selectGroup(int gId, int uId) {// ?
      String query = "select groupImage, groupContent, groupName, groupCategory from groupInfo where groupId=" + gId;
      String query0 = "select groupId from userGroup where userId=" + uId;
      HashMap ret = new HashMap();

      String attr = "no";
      try {
         pstmt = conn.prepareStatement(query0);
         rs = pstmt.executeQuery();

         while (rs.next()) {
            if (gId == rs.getInt(1)) {
               attr = "ok";
               break;
            }
         }

         pstmt = conn.prepareStatement(query);
         rs = pstmt.executeQuery();

         if (rs.next()) {
            String img = rs.getString(1);
            String content = rs.getString(2);
            String name = rs.getString(3);
            int category = rs.getInt(4);

            String tmp = name + "&" + Integer.toString(category) + "&" + content + "@" + img;
            System.out.println(tmp);

            ret.put("img", img);
            ret.put("content", content);
            ret.put("name", name);
            ret.put("category", Integer.toString(category));
            ret.put("attr", attr);
         }

      } catch (Exception e) {
         e.printStackTrace();
      }

      return ret;
   }

   // 모임날짜, 모임 제목, 모임 내용, 모임 위치
   public ArrayList<HashMap> getMeeting(int gId) {
      String query = "select meetingId, meetingDate, meetingTitle, meetingLocation, meetingPersonCnt, meetingContent, meetingCost from meetingInfo "
            + "where meetingId in (select meetingId from groupMeeting where groupId=" + gId + ")";

      ArrayList<HashMap> ret = null;

      try {
         pstmt = conn.prepareStatement(query);
         rs = pstmt.executeQuery();

         ret = new ArrayList<HashMap>();

         while (rs.next()) {
            int id = rs.getInt(1);
            String date = rs.getString(2);
            String title = rs.getString(3);
            String location = rs.getString(4);
            int pCnt = rs.getInt(5);
            String content = rs.getString(6);
            int cost=rs.getInt(7);

            String data = Integer.toString(id) + "&" + date + "&" + title + "&" + location + "&"
                  + Integer.toString(pCnt) + "&" + content;
            System.out.println(data);
            HashMap tmp = new HashMap();
            tmp.put("id", Integer.toString(id));
            tmp.put("date", date);
            tmp.put("title", title);
            tmp.put("location", location);
            tmp.put("personCnt", Integer.toString(pCnt));
            tmp.put("content", content);
            tmp.put("cost", Integer.toString(cost));
            ret.add(tmp);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return ret;
   }


   // 그룹 신청 승인
   public void approveGroup(int gId, int uId, int flag) {
      String query0 = "update waitUser set flag=" + flag + " where userId=" + uId + " and flag=-1";
      String query = "insert into userGroup values(?,?)";
      String query1="update groupInfo set groupPersonNum=groupPersonNum+1";

      try {
         pstmt = conn.prepareStatement(query0);
         pstmt.executeUpdate();
         if (flag == 0) {
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, uId);
            pstmt.setInt(2, gId);
            pstmt.executeUpdate();
            pstmt=conn.prepareStatement(query1);
            pstmt.executeUpdate();
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   // 모임 신청 승인
   public void approveMeeting(int mId, int uId, int flag) {
      String query0 = "update waitUser set flag=" + flag + " where userId=" + uId + " and flag=-1";
      String query = "insert into userGroup values(?,?)";

      try {
         pstmt = conn.prepareStatement(query0);
         pstmt.executeUpdate();
         if (flag == 0) {
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, uId);
            pstmt.setInt(2, mId);
            pstmt.executeUpdate();
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // 그룹 후기 페이지
   // 그룹 아이디를 받음
   // 사용자의 이미지, 사용자 이름, 후기 이미지, 후기 내용, 후기 제목, 후기 날짜
   public ArrayList<HashMap> getReview(int gId) {
      String query2="select reviewUserId, reviewId, reviewImage, reviewContent, reviewDate from reviewInfo "
            +"where reviewId in (select reviewId from groupReview where groupId="+gId+")" 
            +"and reviewLevel=0 order by reviewId desc";
      //후기정보를 그룹아이디를 통해서 조인하여 모두 정렬하여 추출(이광 교수님 게시판 참고) 
      
      String query3="select userImage, userName from userInfo where userId=?";//사용자의 정보를 아디를 갖고 추출
      
      System.out.println(query2);
      
      ArrayList<HashMap> ret = null;

      try {
         pstmt = conn.prepareStatement(query2);
         rs = pstmt.executeQuery();

         ret = new ArrayList<HashMap>();
         
         
         while(rs.next()){
            int uid=rs.getInt(1);
            int rid=rs.getInt(2);
            String rImg=rs.getString(3);
            String rCont=rs.getString(4);
            String date=rs.getString(5);
            
            PreparedStatement pstmt2=conn.prepareStatement(query3);
            pstmt2.setInt(1, uid);
            ResultSet rs2=pstmt2.executeQuery();
            
            String uImg;
            String uName;
            if(rs2.next())
            {
            	uImg=rs2.getString(1);
            	uName=rs2.getString(2);
            }
            else
            {
            	uImg="";
            	uName="";
            }
            
            
            
            HashMap tmp = new HashMap();
            tmp.put("uImg", uImg);
            tmp.put("uName", uName);
            tmp.put("rId", Integer.toString(rid));
            tmp.put("rImg", rImg);
            tmp.put("rCont", rCont);
            tmp.put("rDate", date);
            ret.add(tmp);
         }

      } catch (Exception e) {
         e.printStackTrace();
      }
      System.out.println(ret.size());
      return ret;
   }

   // 댓글 페이지
   public ArrayList<HashMap> getReply(int gId, int rId) {
      //
	  String query0 = "select reviewGrpNo from reviewInfo where reviewId=" + rId;
      String query2="select reviewUserId, reviewContent, reviewDate from reviewInfo "
            +"where "
            +"reviewGrpNo=? and reviewLevel>0 order by reviewGrpNo desc, reviewSeq asc";
      String query3="select userImage, userName from userInfo where userId=?";

      ArrayList<HashMap> ret = new ArrayList<HashMap>();
      int grpNo = -1;
      try {
         pstmt = conn.prepareStatement(query0);
         rs = pstmt.executeQuery();
         if (rs.next())
            grpNo = rs.getInt(1);
         System.out.println(grpNo);
         pstmt = conn.prepareStatement(query2);
         pstmt.setInt(1, grpNo);
         rs = pstmt.executeQuery();

         while(rs.next()){
            int uid=rs.getInt(1);
            String rCont = rs.getString(2);
            String rDate = rs.getString(3);
            
            PreparedStatement pstmt2=conn.prepareStatement(query3);
            pstmt2.setInt(1, uid);
            ResultSet rs2=pstmt2.executeQuery();
            rs2.next();
            
            String uImg=rs2.getString(1);
            String uName=rs2.getString(2);
            
            HashMap tmp = new HashMap();
            tmp.put("uImg", uImg);
            tmp.put("uName", uName);
            tmp.put("rCont", rCont);
            tmp.put("rDate", rDate);
            ret.add(tmp);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return ret;
   }

   // 그룹 검색 페이지
   // seq 최신부터 뽑는다
   // gId, gCategory, gName, gPNum, uName
   public ArrayList<HashMap> searchGroup(int category, String val) {
      String query = "";

      if (category != 0 && val != "") {
         query = "select g.groupId, g.groupName, g.groupCategory, g.groupPersonNum, u.userName from groupInfo g, userInfo u "
               + "where g.groupHeader=u.userId and g.groupName=%" + val + "% and g.groupCategory=" + category
               + "order by groupId desc";
      } else if (category != 0) {
         query = "select g.groupId, g.groupName, g.groupCategory, g.groupPersonNum, u.userName from groupInfo g, userInfo u "
               + "where g.groupHeader=u.userId and g.groupCategory=" + category + "order by groupId desc";
      } else if (val != "") {
         query = "select g.groupId, g.groupName, g.groupCategory, g.groupPersonNum, u.userName from groupInfo g, userInfo u "
               + "where g.groupHeader=u.userId and g.groupName=%" + val + "% " + "order by groupId desc";
      } else {
         query = "select g.groupId, g.groupName, g.groupCategory, g.groupPersonNum, u.userName from groupInfo g, userInfo u "
               + "where g.groupHeader=u.userId order by groupId desc";
      }

      ArrayList<HashMap> ret = null;

      try {
         pstmt = conn.prepareStatement(query);
         rs = pstmt.executeQuery();

         ret = new ArrayList<HashMap>();
         while (rs.next()) {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            int cate = rs.getInt(3);
            int pNum = rs.getInt(4);
            String uName = rs.getString(5);

            String data = Integer.toString(id) + "&" + name + "&" + Integer.toString(cate) + "&"
                  + Integer.toString(pNum) + "&" + uName;

            HashMap tmp = new HashMap();
            tmp.put("id", Integer.toString(id));
            tmp.put("name", name);
            tmp.put("category", Integer.toString(cate));
            tmp.put("personNum", Integer.toString(pNum));
            tmp.put("uName", uName);
            ret.add(tmp);
         }

      } catch (Exception e) {
         e.printStackTrace();
      }

      return ret;
   }

   // 프로필 변경
   public void changeProfile(int id, String name, String img, String comment, int age) {
      String query = "update userInfo set userName='" + name + "', userImage='"+ img + "', userComment='" + comment;
         query += "', userAge=" + age + " where userId=" + id;

      try {
         pstmt = conn.prepareStatement(query);
         pstmt.executeUpdate();

         System.out.println("프로필 변경 성공");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // 비밀번호 변경
   public void changePass(int id, String pw, int hobby) {
      String query = "update userInfo set userPw=" + pw + ", userHobby=" + hobby;
      query += " where userId=" + id;
      try {
         pstmt = conn.prepareStatement(query);
         pstmt.executeUpdate();

         System.out.println("비밀번호 변경 성공");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // 네비게이션 바 출력
   // 프로필 이미지, 이름, 취미, 인삿말, 나이, 성별
   public HashMap getUserInfo(int id) {
      String query = "select userImage, userName, userHobby, userComment, userAge, userSex from userInfo where userId="
            + id;

      HashMap ret = new HashMap();

      try {
         pstmt = conn.prepareStatement(query);
         rs = pstmt.executeQuery();

         if (rs.next()) {
            String img = rs.getString(1);
            String name = rs.getString(2);
            int hobby = rs.getInt(3);
            String comment = rs.getString(4);
            int age = rs.getInt(5);
            int sex = rs.getInt(6);

            // 이름, 취미, 나이, 성별, 코멘트, 이미지
            

            ret.put("img", img);
            ret.put("name", name);
            ret.put("hobby", Integer.toString(hobby));
            ret.put("age", Integer.toString(age));
            ret.put("sex", Integer.toString(sex));
            ret.put("comment", comment);
         }

         System.out.println("회원정보 출력 성공");
      } catch (Exception e) {
         e.printStackTrace();
      }

      return ret;
   }

   // 대기 사용자를 삽입함
   public void waitUser(int uId, int gId, int mId) {
      String query = "insert into waitUser values(?,?,?,?,?)";
      String query2 = "";
      if (gId != -1) {// 그룹 신청자
         query2 = "select groupHeader from groupInfo where groupId=" + gId;
      } else {// 모임 신청자
         query2 = "select meetingUserId from meetingInfo where meetingId=" + mId;
      }

      int head = -1;
      try {
         pstmt = conn.prepareStatement(query2);
         rs = pstmt.executeQuery();

         if (rs.next()) {
            head = rs.getInt(1);
         }
         pstmt = conn.prepareStatement(query);
         pstmt.setInt(1, uId);
         pstmt.setInt(2, gId);
         pstmt.setInt(3, mId);
         pstmt.setInt(4, head);
         pstmt.setInt(5, -1);
         pstmt.executeUpdate();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // 사용자에게 들어온 알림 정보를 검색
   // 작성자에게 노티
   public ArrayList<HashMap> getNoti(int uId) {
      String query = "select w.groupId, w.meetingId, w.userId, u.userImage, u.userName  from waitUser w, userInfo u where w.head="
            + uId + " and u.userId=w.userId and w.flag=-1";
      String query1 = "select w.flag, w.meetingId, w.groupId, u.userImage, u.userName from waitUser w, userInfo u where w.userId="
            + uId + " and u.userId=w.head";
      ArrayList<HashMap> data = new ArrayList<HashMap>();
      int len = 0;
      try {
         pstmt = conn.prepareStatement(query);
         rs = pstmt.executeQuery();
         while (rs.next()) {
            int gId = rs.getInt(1);
            int mId = rs.getInt(2);
            int id = rs.getInt(3);
            String img = rs.getString(4);
            String name = rs.getString(5);
            String sub = "";
            String sw = "";
            if (gId != -1) {
               sub = "select groupName from groupInfo where groupId=" + gId;
               sw = "0";
            } else {
               sub = "select meetingtitle from meetingInfo where meetingId=" + mId;
               sw = "1";
            }
            PreparedStatement pstmt2 = conn.prepareStatement(sub);
            ResultSet rs2 = pstmt2.executeQuery();
            rs2.next();
            String title = rs2.getString(1);

            HashMap ret = new HashMap();
            ret.put("uId", Integer.toString(id));
            ret.put("gId", Integer.toString(gId));
            ret.put("mId", Integer.toString(mId));
            ret.put("img", img);
            ret.put("text", name + "님이 " + title + "에 신청하였습니다.");
            ret.put("switch", sw);
            System.out.println(ret);
            data.add(ret);
            len++;
         }

         pstmt = conn.prepareStatement(query1);
         rs = pstmt.executeQuery();
         while (rs.next()) {
            int flag = rs.getInt(1);
            int mId = rs.getInt(2);
            int gId = rs.getInt(3);
            String img = rs.getString(4);
            String name = rs.getString(5);

            String tmp = "";
            if (flag == 0) {
               tmp = "승인 하였습니다.";
            } else if (flag == 1) {
               tmp = "거절 하였습니다.";
            } else {
               tmp = "심사 중입니다.";
            }

            String sw = "";
            String id = "";

            if (mId == -1) {
               sw = "0";
               id = Integer.toString(gId);
            } else {
               sw = "1";
               id = Integer.toString(mId);
            }

            HashMap ret = new HashMap();
            ret.put("img", img);
            ret.put("text", name + "님이 가입을 " + tmp);
            ret.put("switch", sw);
            ret.put("id", id);
            data.add(ret);
            System.out.println(ret);
         }
         HashMap temp = new HashMap();
         temp.put("img", "profile\\default.jpg");
         temp.put("size", len);
         data.add(temp);

      } catch (Exception e) {
         e.printStackTrace();
      }

      return data;
   }

   public void deleteWait(int uId, int mId, int gId) {
      String query = "delete from waitUser where userId=" + uId;
      if (mId == -1)
         query += " and groupId=" + gId;
      else
         query += " and meetingId=" + mId;
      try {
         System.out.println(query);
         pstmt = conn.prepareStatement(query);
         pstmt.executeUpdate();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

}