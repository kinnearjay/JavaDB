package com.dbapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.scene.control.Tab;
import net.proteanit.sql.DbUtils;

/**
 * Created by Jorda on 03/10/2018.
 */
public class App {


    private JPanel panelMain;
    private JTabbedPane paneSwitch;
    private JPanel removeDB;
    private JPanel addDB;
    private JButton addBtn;
    private JPanel editDB;
    private JPanel viewDB;
    private JButton removeBtn;
    private JButton editBtn;
    private JTable viewTbl;
    private JTextField titleTxt;
    private JLabel lblTitle;
    private JLabel lblSeason;
    private JLabel lblRating;
    private JComboBox seasonCB;
    private JComboBox ratingCB;
    private JLabel remlblTitle;
    private JTextField remTxt;
    private JTextField editTitleTxt;
    private JComboBox seasonEditCB;
    private JComboBox ratingEditCB;
    private JButton viewBtn;
    private JTextField keyTxt;
    private JButton searchBtn;
    private JLabel rmlblTitle;
    private JLabel rmLblSeason;
    private JLabel rmLblRating;
    private JLabel rmResults;
    private JTextField editRatingTxt;
    private JTextField editSeasonTxt;

    public App() {
        // add show to database
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection conn = null;
                    conn = DriverManager.getConnection("jdbc:mysql://localhost/jarvadb", "root", "");
                    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO shows (Title, Season, Rating) VALUES(?,?,?)");
                    if(titleTxt.getText().isEmpty()){
                        JOptionPane.showMessageDialog(null, "Please enter title of the show");

                    }else {
                        pstmt.setString(1, titleTxt.getText());
                        pstmt.setInt(2, seasonCB.getSelectedIndex() + 1);
                        pstmt.setInt(3, ratingCB.getSelectedIndex());
                        pstmt.execute();
                        titleTxt.getText();
                        System.out.println("Database is connected!");
                        conn.close();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid season");
                    System.out.println("there was an error: " + ex);
                }
            }
        });
        // remove show from database
        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection conn = null;
                    conn = DriverManager.getConnection("jdbc:mysql://localhost/jarvadb", "root", "");
                    PreparedStatement pstmtrm = conn.prepareStatement("DELETE FROM shows WHERE KeyID = ?");
                    pstmtrm.setInt(1, Integer.parseInt(remTxt.getText()));
                    pstmtrm.execute();

                    System.out.println("Database is connected!");
                    conn.close();
                } catch (Exception ex) {
                    System.out.println("there was an error: " + ex);
                }
            }
        });
        // update  show in database
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection conn = null;
                    conn = DriverManager.getConnection("jdbc:mysql://localhost/jarvadb", "root", "");
                    PreparedStatement pstmted = conn.prepareStatement("UPDATE shows  SET Title = ? , " + "Rating =? ," + "Season = ? WHERE KeyID = ?" );
                    pstmted.setString(1,editTitleTxt.getText());
                    pstmted.setInt(2, ratingEditCB.getSelectedIndex()+1);
                    pstmted.setInt(3, seasonEditCB.getSelectedIndex());
                    pstmted.setInt(4,Integer.parseInt(keyTxt.getText()));
                    pstmted.execute();
                    System.out.println("Database is connected!");
                    conn.close();
                } catch (Exception ex) {
                    System.out.println("there was an error: " + ex);
                }
            }
        });
        paneSwitch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if(paneSwitch.getTitleAt(paneSwitch.getSelectedIndex()).equals("view")){
                    try {

                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection conn = null;
                        conn = DriverManager.getConnection("jdbc:mysql://localhost/jarvadb", "root", "");
                        String header[] ={"Title", "Rating", "season", "Key"};
                        for(int i=0;i<viewTbl.getColumnCount();i++)
                        {
                            TableColumn column1 = viewTbl.getTableHeader().getColumnModel().getColumn(i);
                            column1.setHeaderValue(header[i]);
                        }
                        PreparedStatement pstmtrs = conn.prepareStatement("SELECT * FROM shows" );
                        ResultSet rs = pstmtrs.executeQuery();
                        viewTbl.setModel(DbUtils.resultSetToTableModel(rs));





                        System.out.println("Database is connected!");
                        conn.close();


                    } catch (Exception ex) {
                        System.out.println("there was an error: " + ex);
                    }
                }




            }
        });

        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection conn = null;
                    conn = DriverManager.getConnection("jdbc:mysql://localhost/jarvadb", "root", "");
                    PreparedStatement pstmtrms = conn.prepareStatement("SELECT * FROM shows WHERE KeyID =?");
                    pstmtrms.setInt(1,Integer.parseInt(remTxt.getText()));
                    ResultSet rss = pstmtrms.executeQuery();
                    while(rss.next()){
                        String title = rss.getString("Title");
                        String season = rss.getString("Season");
                        String rating = rss.getString("Rating");
                        rmlblTitle.setText("Title: " + title);
                        rmLblSeason.setText(" Season: " +season);
                        rmLblRating.setText(" rating: "+rating+" /10");
                    }







                    System.out.println("Database is connected!");
                    conn.close();


                } catch (Exception ex) {
                    System.out.println("there was an error: " + ex);
                }
            }
        });
    }

    public static void main(String argv[]){
        JFrame frame = new JFrame("app");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);





    }


}
