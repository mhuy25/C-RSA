/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package De5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Acer
 */
public class ThreadSocket extends Thread {

    protected Socket socket = null;
//    protected ServerSocket server = null;
    BufferedWriter out = null;
    BufferedReader in = null;
    public final String FILENAME = "MSSV.txt";
    int chiKhoahoc = 150;
    int chiXaHoi = 132;
    //    connect database 
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sinhvien";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "";

//    private void Init_Connect(int port) {
//        try {
//            server = new ServerSocket(port);
//            System.out.println("Server started");
//            System.out.println("Waiting for a client ...");
//            socket = server.accept();
//            System.out.println("Client accepted");
//        } catch (IOException e) {
//            System.err.println(e);
//        }
//    }
//    private void Close_Connect(BufferedReader in, BufferedWriter out, Socket socket, ServerSocket server) {
//        try {
//            System.out.println("Closing connection");
//            in.close();
//            out.close();
//            socket.close();
//            server.close();
//        } catch (IOException ex) {
//            System.err.println(ex);
//        }
//    }
    public void Sent(BufferedWriter out) {
        try {
            out.newLine();
            out.flush();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public static void pushDatabase(String mssv, String hoten, String khoahoc, String nganh, String khoa, double diem4, double diem10) {
        try {
            com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) getConnection(DB_URL, USER_NAME, PASSWORD);

//            String sql = "select * from sinhvien ";
            String query = " insert into sinhvien (mssv, hoten, khoahoc, nganh , khoa , diem4, diem10)"
                    + " values (?, ?, ? , ? , ?, ?, ?)";
            java.sql.Statement st = conn.createStatement();
//            ResultSet rs = st.executeQuery(sql);
            PreparedStatement data = conn.prepareStatement(query);
            data.setString(1, mssv);
            data.setString(2, hoten);
            data.setString(3, khoahoc);
            data.setString(4, nganh);
            data.setString(5, khoa);
            data.setDouble(6, diem4);
            data.setDouble(7, diem10);
            data.executeUpdate();
            conn.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    public void getPositionStudentAll(String mssv, BufferedWriter out) {
        try {
            String data = "";
            com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) getConnection(DB_URL, USER_NAME, PASSWORD);

            String query = "SELECT Count(*) as position	FROM sinhvien WHERE diem4 >= (SELECT diem4 from sinhvien WHERE mssv=\"" + mssv + "\") order by diem4 DESC ";
            java.sql.Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                data = rs.getString(1);
            }
            out.write(data);
            Sent(out);
            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void getStudentAllLower(String mssv, BufferedWriter out) {
        try {
            String data = "";
            com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) getConnection(DB_URL, USER_NAME, PASSWORD);

            String query = "SELECT hoten, diem4	FROM sinhvien WHERE diem4 < (SELECT diem4 from sinhvien WHERE mssv=\"" + mssv + "\") order by diem4 DESC limit 10";
            java.sql.Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                data += rs.getString("hoten") + ":" + rs.getString("diem4") + ";";
            }
            out.write(data);
            Sent(out);
            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void getStudentAllHigher(String mssv, BufferedWriter out) {
        try {
            String data = "";
            com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) getConnection(DB_URL, USER_NAME, PASSWORD);

            String query = "SELECT hoten, diem4	FROM sinhvien WHERE diem4 > (SELECT diem4 from sinhvien WHERE mssv=\"" + mssv + "\") order by diem4 ASC limit 10";
            java.sql.Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                data += rs.getString("hoten") + ":" + rs.getString("diem4") + ";";
            }
            out.write(data);
            Sent(out);
            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    // khóa
    
    public void getPositionWithFaculty(String mssv, BufferedWriter out) {
        try {
            String data = "";
            com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) getConnection(DB_URL, USER_NAME, PASSWORD);

            String query = "SELECT count(*) as position	FROM sinhvien WHERE "
                    + "diem4 >= (SELECT diem4 from sinhvien WHERE mssv=\"" + mssv + "\") "
                    + "AND "
                    + "khoa = ( SELECT khoa FROM sinhvien WHERE mssv=\"" + mssv + "\" )"
                    + "order by diem4 DESC";
            java.sql.Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                data = rs.getString(1);
            }
            out.write(data);
            Sent(out);
            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    public void getStudentLowerWithFaculty(String mssv, BufferedWriter out) {
        try {
            String data = "";
            com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) getConnection(DB_URL, USER_NAME, PASSWORD);

            String query = "SELECT hoten, diem4	FROM sinhvien WHERE "
                    + "diem4 < (SELECT diem4 from sinhvien WHERE mssv=\"" + mssv + "\") "
                    + "AND "
                    + "khoa = ( SELECT khoa FROM sinhvien WHERE mssv=\"" + mssv + "\" )"
                    + "order by diem4 DESC limit 10";
            java.sql.Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                data += rs.getString("hoten") + ":" + rs.getString("diem4") + ";";
            }
            out.write(data);
            Sent(out);
            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void getStudentHigherWithFaculty(String mssv, BufferedWriter out) {
        try {
            String data = "";
            com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) getConnection(DB_URL, USER_NAME, PASSWORD);
            String query = "SELECT hoten, diem4	FROM sinhvien WHERE "
                    + "diem4 > (SELECT diem4 from sinhvien WHERE mssv=\"" + mssv + "\") "
                    + "AND "
                    + "khoa = ( SELECT khoa FROM sinhvien WHERE mssv=\"" + mssv + "\" )"
                    + "order by diem4 ASC limit 10";
            java.sql.Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                data += rs.getString("hoten") + ":" + rs.getString("diem4") + ";";
            }
            out.write(data);
            Sent(out);
            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    // ngành
    public void getPositionWithMajors(String mssv, BufferedWriter out) {
        try {
            String data = "";
            com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) getConnection(DB_URL, USER_NAME, PASSWORD);

            String query = "SELECT count(*) as position	FROM sinhvien WHERE "
                    + "diem4 >= (SELECT diem4 from sinhvien WHERE mssv=\"" + mssv + "\") "
                    + "AND "
                    + "nganh = ( SELECT nganh FROM sinhvien WHERE mssv=\"" + mssv + "\" )"
                    + "order by diem4 DESC ";
            java.sql.Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                data = rs.getString(1);
            }
            out.write(data);
            Sent(out);
            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    public void getStudentLowerWithMajors(String mssv, BufferedWriter out) {
        try {
            String data = "";
            com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) getConnection(DB_URL, USER_NAME, PASSWORD);

            String query = "SELECT hoten, diem4	FROM sinhvien WHERE "
                    + "diem4 < (SELECT diem4 from sinhvien WHERE mssv=\"" + mssv + "\") "
                    + "AND "
                    + "nganh = ( SELECT nganh FROM sinhvien WHERE mssv=\"" + mssv + "\" )"
                    + "order by diem4 DESC limit 10";
            java.sql.Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                data += rs.getString("hoten") + ":" + rs.getString("diem4") + ";";
            }
            out.write(data);
            Sent(out);
            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void getStudentHigherWithMajors(String mssv, BufferedWriter out) {
        try {
            String data = "";
            com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) getConnection(DB_URL, USER_NAME, PASSWORD);
            String query = "SELECT hoten, diem4	FROM sinhvien WHERE "
                    + "diem4 > (SELECT diem4 from sinhvien WHERE mssv=\"" + mssv + "\") "
                    + "AND "
                    + "nganh = ( SELECT nganh FROM sinhvien WHERE mssv=\"" + mssv + "\" )"
                    + "order by diem4 ASC limit 10";
            java.sql.Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                data += rs.getString("hoten") + ":" + rs.getString("diem4") + ";";
            }
            out.write(data);
            Sent(out);
            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    //khóa học
    public void getPositionWithCourse(String mssv, BufferedWriter out) {
        try {
            String data = "";
            try (com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) getConnection(DB_URL, USER_NAME, PASSWORD)) {
                String query = "SELECT count(*) as position FROM sinhvien WHERE "
                        + "diem4 >= (SELECT diem4 from sinhvien WHERE mssv=\"" + mssv + "\") "
                        + "AND "
                        + "khoahoc = ( SELECT khoahoc FROM sinhvien WHERE mssv=\"" + mssv + "\" )"
                        + "order by diem4 DESC  ";
                java.sql.Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    data = rs.getString(1);
                }
                out.write(data);
                Sent(out);
            }
        } catch (SQLException | IOException e) {
            System.err.println(e);
        }
    }
    
    public void getStudentLowerWithCourse(String mssv, BufferedWriter out) {
        try {
            String data = "";
            try (com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) getConnection(DB_URL, USER_NAME, PASSWORD)) {
                String query = "SELECT hoten, diem4	FROM sinhvien WHERE "
                        + "diem4 < (SELECT diem4 from sinhvien WHERE mssv=\"" + mssv + "\") "
                        + "AND "
                        + "khoahoc = ( SELECT khoahoc FROM sinhvien WHERE mssv=\"" + mssv + "\" )"
                        + "order by diem4 DESC limit 10";
                java.sql.Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    data += rs.getString("hoten") + ":" + rs.getString("diem4") + ";";
                }
                out.write(data);
                Sent(out);
            }
        } catch (SQLException | IOException e) {
            System.err.println(e);
        }
    }

    public void getStudentHigherWithCourse(String mssv, BufferedWriter out) {
        try {
            String data = "";
            com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) getConnection(DB_URL, USER_NAME, PASSWORD);
            String query = "SELECT hoten, diem4	FROM sinhvien WHERE "
                    + "diem4 > (SELECT diem4 from sinhvien WHERE mssv=\"" + mssv + "\") "
                    + "AND "
                    + "khoahoc = ( SELECT khoahoc FROM sinhvien WHERE mssv=\"" + mssv + "\" )"
                    + "order by diem4 ASC limit 10";
            java.sql.Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                data += rs.getString("hoten") + ":" + rs.getString("diem4") + ";";
            }
            out.write(data);
            Sent(out);
            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void getStudentWithCondition(String mssv, String condition, BufferedWriter out) throws IOException {
        if (checkMaSoSinhVien(mssv, out).equals("404")) {
            out.write("404");
            Sent(out);
        } else {
            out.write("200");
            Sent(out);
            switch (condition) {
                case "Khóa": {
                    getPositionWithCourse(mssv, out);
                    getStudentLowerWithCourse(mssv, out);
                    getStudentHigherWithCourse(mssv, out);
                }
                ;
                break;
                case "Ngành": {
                    getPositionWithMajors(mssv, out);
                    getStudentLowerWithMajors(mssv, out);
                    getStudentHigherWithMajors(mssv, out);
                }
                ;
                break;
                case "Khoa": {
                    getPositionWithFaculty(mssv, out);
                    getStudentLowerWithFaculty(mssv, out);
                    getStudentHigherWithFaculty(mssv, out);
                }
                ;
                break;
                case "all": {
                    getPositionStudentAll(mssv, out);
                    getStudentAllLower(mssv, out);
                    getStudentAllHigher(mssv, out);
                }
                ;
                break;
            }
        }
    }

    public static Connection getConnection(String dbURL, String userName, String password) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(dbURL, userName, password);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("connect failure!");
        }
        return conn;
    }

    private String readFile() {
        BufferedReader br = null;
        StringBuilder resultStringBuilder = new StringBuilder();
        try {

            br = new BufferedReader(new FileReader(FILENAME));
            String textInALine;
            while ((textInALine = br.readLine()) != null) {
                resultStringBuilder.append(textInALine).append("\n");
            }
            br.close();
        } catch (IOException e) {
            System.err.println(e);
        }
        return resultStringBuilder.toString();
    }

    private String checkMaSoSinhVien(String maSv, BufferedWriter out) throws IOException {
        String urlLogin = "http://thongtindaotao.sgu.edu.vn/default.aspx?page=nhapmasv&flag=XemDiemThi";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36";
        Response response = Jsoup.connect(urlLogin)
                .method(Method.GET)
                .execute();

        Document loginPage = response.parse();
        response = Jsoup.connect(urlLogin)
                .data("__EVENTTARGET", "")
                .data("__EVENTARGUMENT", "")
                .data("__VIEWSTATE", loginPage.getElementById("__VIEWSTATE").val())
                .data("__VIEWSTATEGENERATOR", loginPage.getElementById("__VIEWSTATEGENERATOR").val())
                .data("ctl00$ContentPlaceHolder1$ctl00$txtMaSV", maSv)
                .data("ctl00$ContentPlaceHolder1$ctl00$btnOK", "OK")
                .userAgent(userAgent)
                .timeout(0)
                .cookies(response.cookies())
                .method(Method.POST)
                .execute();
        loginPage = response.parse();
        Element mssv = loginPage.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblMaSinhVien");
        if (mssv == null || mssv.text().equals("")) {
            return "404";
        } else {
            return "200";
        }
    }

    //get point push database
    private void getPoint(String maSv, BufferedReader in, BufferedWriter out) {
        try {
            String urlLogin = "http://thongtindaotao.sgu.edu.vn/default.aspx?page=nhapmasv&flag=XemDiemThi";
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36";
            Response response = Jsoup.connect(urlLogin)
                    .method(Method.GET)
                    .execute();

            Document loginPage = response.parse();
            response = Jsoup.connect(urlLogin)
                    .data("__EVENTTARGET", "")
                    .data("__EVENTARGUMENT", "")
                    .data("__VIEWSTATE", loginPage.getElementById("__VIEWSTATE").val())
                    .data("__VIEWSTATEGENERATOR", loginPage.getElementById("__VIEWSTATEGENERATOR").val())
                    .data("ctl00$ContentPlaceHolder1$ctl00$txtMaSV", maSv)
                    .data("ctl00$ContentPlaceHolder1$ctl00$btnOK", "OK")
                    .userAgent(userAgent)
                    .timeout(0)
                    .cookies(response.cookies())
                    .method(Method.POST)
                    .execute();
            loginPage = response.parse();
            Element mssv = loginPage.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblMaSinhVien");
            if (mssv == null || mssv.text().equals("")) {
//                System.out.println("Mã không tồn tại");
//                out.write("Mã không tồn tại");
//                Sent(out);
            } else {
                String url1 = "http://thongtindaotao.sgu.edu.vn/Default.aspx?page=xemdiemthi&id=" + maSv + " ";
                response = Jsoup.connect(url1)
                        .method(Method.GET)
                        .ignoreHttpErrors(true)
                        .execute();
                response = Jsoup.connect(url1)
                        .data("__EVENTTARGET", "")
                        .data("__EVENTARGUMENT", "")
                        .data("__VIEWSTATE", loginPage.getElementById("__VIEWSTATE").val())
                        .data("__VIEWSTATEGENERATOR", loginPage.getElementById("__VIEWSTATEGENERATOR").val())
                        .data("ctl00$ContentPlaceHolder1$ctl00$lnkChangeview2", "")
                        .userAgent(userAgent)
                        .timeout(0)
                        .cookies(response.cookies())
                        .method(Method.POST)
                        .execute();
                Document doc = response.parse();

                Integer getSize = doc.getElementsByClass("row-diemTK").size();

                String hoTen = loginPage.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblTenSinhVien").text();
                String khoahoc = loginPage.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblKhoaHoc").text();
                String nganh = loginPage.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lbNganh").text();
                String khoa = loginPage.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblKhoa").text();
                String point4 = doc.getElementsByClass("row-diemTK").get(getSize - 3).getElementsByClass("Label").get(1).text();
                String point10 = doc.getElementsByClass("row-diemTK").get(getSize - 4).getElementsByClass("Label").get(1).html();
                Double diem4 = Double.parseDouble(point4);
                Double diem10 = Double.parseDouble(point10);
                pushDatabase(mssv.text(), hoTen, khoahoc, nganh, khoa, diem4, diem10);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void getResult(BufferedReader in, BufferedWriter out, String mssv) {
        try {
            String urlLogin = "http://thongtindaotao.sgu.edu.vn/default.aspx?page=nhapmasv&flag=XemDiemThi";
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36";
            Response response = Jsoup.connect(urlLogin)
                    .method(Method.GET)
                    .execute();

            Document loginPage = response.parse();
            String url1 = "http://thongtindaotao.sgu.edu.vn/Default.aspx?page=xemdiemthi&id=" + mssv;
            response = Jsoup.connect(url1)
                    .method(org.jsoup.Connection.Method.GET)
                    .execute();
            loginPage = response.parse();

            response = Jsoup.connect(url1)
                    .data("__EVENTTARGET", "")
                    .data("__EVENTARGUMENT", "")
                    .data("__VIEWSTATE", loginPage.getElementById("__VIEWSTATE").val())
                    .data("__VIEWSTATEGENERATOR", loginPage.getElementById("__VIEWSTATEGENERATOR").val())
                    .data("ctl00$ContentPlaceHolder1$ctl00$lnkChangeview2", "")
                    .userAgent(userAgent)
                    .timeout(0)
                    .cookies(response.cookies())
                    .method(org.jsoup.Connection.Method.POST)
                    .execute();
            HashMap<String, String> map = new HashMap<String, String>();
            Document point = response.parse();
            Elements row = point.getElementsByClass("row-diem");
            String[] stt = null;
            for (Element element : row) {
                List<Element> temp = element.getElementsByClass("Label");
                String ten = temp.get(2).text();
                String diem = temp.get(9).text();
                if (!ten.equals("Đủ điều kiện học Tiếng Anh I (866101)")) {
                    map.put(ten, diem);
                }
            }
            String dataSendClient = "";
            Object[] keys = map.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                if (!map.get(keys[i]).equals("")) {
                    dataSendClient += keys[i] + ": " + map.get(keys[i]) + ";";
                }
            }
            out.write(dataSendClient);
            Sent(out);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void getInfor(BufferedReader in, BufferedWriter out, String mssv) {
        try {
            String urlLogin = "http://thongtindaotao.sgu.edu.vn/default.aspx?page=nhapmasv&flag=XemDiemThi";
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36";
            Response response = Jsoup.connect(urlLogin)
                    .method(Method.GET)
                    .execute();

            Document loginPage = response.parse();
            response = Jsoup.connect(urlLogin)
                    .data("__EVENTTARGET", "")
                    .data("__EVENTARGUMENT", "")
                    .data("__VIEWSTATE", loginPage.getElementById("__VIEWSTATE").val())
                    .data("__VIEWSTATEGENERATOR", loginPage.getElementById("__VIEWSTATEGENERATOR").val())
                    .data("ctl00$ContentPlaceHolder1$ctl00$txtMaSV", mssv)
                    .data("ctl00$ContentPlaceHolder1$ctl00$btnOK", "OK")
                    .userAgent(userAgent)
                    .timeout(0)
                    .cookies(response.cookies())
                    .method(Method.POST)
                    .execute();
            loginPage = response.parse();
            Element masosv = loginPage.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblMaSinhVien");
            if (masosv == null || masosv.text().equals("")) {
                System.out.println("Mã không tồn tại");
                out.write("Mã không tồn tại");
                Sent(out);
            } else {
                String hoTen = loginPage.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblTenSinhVien").text();
                String phai = loginPage.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblPhai").text();
                String noiSinh = loginPage.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblNoiSinh").text();
                String nganh = loginPage.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lbNganh").text();
                String lop = loginPage.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblLop").text();
                String khoa = loginPage.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblKhoa").text();
                String dataSendClient = hoTen + ";" + phai + ";" + noiSinh + ";"
                        + nganh + ";" + lop + ";" + khoa + ";";
                out.write(dataSendClient);
                Sent(out);
            }
            // Lấy điểm học kỳ bất kỳ
        } catch (IOException e) {
            System.err.println(e);
        }

    }

    public void GetStatistic(BufferedReader in, BufferedWriter out, String mssv) {
        //phần của Dương Vĩ Hiền 
        //lấy url trang xem điểm
        try {

            String urlLogin = "http://thongtindaotao.sgu.edu.vn/default.aspx?page=nhapmasv&flag=XemDiemThi";
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36";
            Response response = Jsoup.connect(urlLogin)
                    .method(Method.GET)
                    .execute();
            Document loginPage = response.parse();

            response = Jsoup.connect(urlLogin)
                    .data("__EVENTTARGET", "")
                    .data("__EVENTARGUMENT", "")
                    .data("__VIEWSTATE", loginPage.getElementById("__VIEWSTATE").val())
                    .data("__VIEWSTATEGENERATOR", loginPage.getElementById("__VIEWSTATEGENERATOR").val())
                    .data("ctl00$ContentPlaceHolder1$ctl00$txtMaSV", mssv)
                    .data("ctl00$ContentPlaceHolder1$ctl00$btnOK", "OK")
                    .userAgent(userAgent)
                    .timeout(0)
                    .cookies(response.cookies())
                    .method(Method.POST)
                    .execute();
            loginPage = response.parse();

            String url1 = "http://thongtindaotao.sgu.edu.vn/Default.aspx?page=xemdiemthi&id=" + mssv;
//            String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36";
            response = Jsoup.connect(url1)
                    .method(Method.GET)
                    .execute();
            Document login = response.parse();
            //xem điểm học kỳ 2 2019
            response = Jsoup.connect(url1)
                    .data("__EVENTTARGET", "")
                    .data("__EVENTARGUMENT", "")
                    .data("__VIEWSTATE", login.getElementById("__VIEWSTATE").val())
                    .data("__VIEWSTATEGENERATOR", login.getElementById("__VIEWSTATEGENERATOR").val())
                    .data("ctl00$ContentPlaceHolder1$ctl00$lnkChangeview2", "")
                    .userAgent(userAgent)
                    .timeout(0)
                    .cookies(response.cookies())
                    .method(Method.POST)
                    .execute();
            // 
            Document MarkPage = response.parse();
            Integer getSize = MarkPage.getElementsByClass("row-diemTK").size();

            String tbhe10 = MarkPage.getElementsByClass("row-diemTK").get(getSize - 6).getElementsByClass("Label").get(1).text();
            String tbhe4 = MarkPage.getElementsByClass("row-diemTK").get(getSize - 5).getElementsByClass("Label").get(1).html();
            String tbtichluy = MarkPage.getElementsByClass("row-diemTK").get(getSize - 4).getElementsByClass("Label").get(1).text();
            String tbtichluy4 = MarkPage.getElementsByClass("row-diemTK").get(getSize - 3).getElementsByClass("Label").get(1).text();
            String tinchitichluy = MarkPage.getElementsByClass("row-diemTK").get(getSize - 1).getElementsByClass("Label").get(1).text();

            String khoa = loginPage.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblKhoa").text();
            Elements rowDiem = loginPage.getElementsByClass("row-diem");
            int tongchidanghoc = 0;
            for (Element childRow : rowDiem) {
                tongchidanghoc += Integer.parseInt(childRow.getElementsByClass("Label").get(3).text());
            }
            int tinchiconlai = 0;
            if (khoa.equals("Công nghệ thông tin") || khoa.equals("Điện tử viễn thông") || khoa.equals("Khoa học môi trường")) {
                tinchiconlai = chiKhoahoc - tongchidanghoc - Integer.parseInt(tinchitichluy);
            } else {
                tinchiconlai = chiXaHoi - tongchidanghoc - Integer.parseInt(tinchitichluy);
            }

            String dataSendClient = "";
            dataSendClient = tbhe10 + ";" + tbhe4 + ";" + tbtichluy + ";"
                    + tbtichluy4 + ";" + tinchitichluy + ";" + tongchidanghoc + ";" + tinchiconlai;
            out.write(dataSendClient);
            Sent(out);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    //lấy số môn học    
    public void Count(BufferedReader in, BufferedWriter out, String mssv) {
        try {
            String urlLogin = "http://thongtindaotao.sgu.edu.vn/default.aspx?page=nhapmasv&flag=XemDiemThi";
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36";
            Response response = Jsoup.connect(urlLogin)
                    .method(Method.GET)
                    .execute();

            Document loginPage = response.parse();
            String url1 = "http://thongtindaotao.sgu.edu.vn/Default.aspx?page=xemdiemthi&id=" + mssv;
            response = Jsoup.connect(url1)
                    .method(Method.GET)
                    .execute();
            loginPage = response.parse();
            response = Jsoup.connect(url1)
                    .data("__EVENTTARGET", "")
                    .data("__EVENTARGUMENT", "")
                    .data("__VIEWSTATE", loginPage.getElementById("__VIEWSTATE").val())
                    .data("__VIEWSTATEGENERATOR", loginPage.getElementById("__VIEWSTATEGENERATOR").val())
                    .data("ctl00$ContentPlaceHolder1$ctl00$lnkChangeview2", "")
                    .userAgent(userAgent)
                    .timeout(0)
                    .cookies(response.cookies())
                    .method(Method.POST)
                    .execute();
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            Document doc = response.parse();
            Elements row = doc.getElementsByClass("row-diem");
            String[] mamh = null;
            for (Element element : row) {
                String temp = element.getElementsByClass("Label").text();
                mamh = temp.split(" ");
                if (!mamh[1].equals("KSTA60")) {
                    if (!map.containsKey(mamh[1])) {
                        map.put(mamh[1], 1);
                    } else {
                        map.put(mamh[1], map.get(mamh[1] + 1));
                    }
                }
            }
            int somh = map.size();
            String dataSendClient = Integer.toString(somh);
            out.write(dataSendClient);
            Sent(out);

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public ThreadSocket(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
//            Init_Connect(port);
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String dataClient = "";

            while (true) {

                dataClient = in.readLine();
                String[] data = dataClient.split("#");

                if (data[0].equals("button1")) {
                    if (checkMaSoSinhVien(data[1], out).equals("404")) {
                        out.write("404");
                        Sent(out);
                    } else {
                        out.write("200");
                        Sent(out);
                        getInfor(in, out, data[1]);
                        GetStatistic(in, out, data[1]);
                        Count(in, out, data[1]);
                        getResult(in, out, data[1]);
                    }
                } else {
                    String[] str = data[1].split(";");
                    getStudentWithCondition(str[0], str[1], out);
                }
            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }

//    public static void main(String[] args) throws IOException {
//
//        Server server = new Server(4000);
//    }
}
