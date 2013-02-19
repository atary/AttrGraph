/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package attrgraph;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author itü
 */
public class DBOperations {
    private Connection conn;

    
    public void connectDB() throws SQLException, ClassNotFoundException{
        Properties connectionProps = new Properties();
        
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        
        connectionProps.put("user", "ovatman");
        connectionProps.put("password", "tolga");

        conn = DriverManager.getConnection("jdbc:derby://localhost:1527/allMembers",
                   connectionProps);
    
        System.out.println("Connected to database");
        
    }
    
    public void closeCon() throws SQLException{
        conn.close();
    }
    
    public void createAppsDB() throws SQLException{
        PreparedStatement insertapp;
        
        String insertString="CREATE TABLE apps"
                + "("
                + "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                + "name VARCHAR(40) NOT NULL,"
                + "CONSTRAINT primary_key PRIMARY KEY (id)"
                + ")";
        
        insertapp=conn.prepareStatement(insertString);
        
        insertapp.executeUpdate();
        
        System.out.println("App Database created");
    }

    public void createClassesDB() throws SQLException{
        PreparedStatement insertapp;
        
        String insertString="CREATE TABLE CLASSES"
                + "("
                + "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                + "appid INTEGER REFERENCES APPS (id),"
                + "package VARCHAR(200) NOT NULL,"
                + "name VARCHAR(200) NOT NULL,"
                + "CONSTRAINT clss_primary_key PRIMARY KEY (id)"
                + ")";
        
        insertapp=conn.prepareStatement(insertString);
        
        insertapp.executeUpdate();
        
        System.out.println("Class Database created");
    }
        
    void insertApp(String javaParser) throws SQLException {
        PreparedStatement insertapp;
        
        String insertString="INSERT INTO APPS(NAME) VALUES('"+ javaParser +"')";
        
        insertapp=conn.prepareStatement(insertString);
        
        insertapp.executeUpdate();
        
        //System.out.println("App inserted");
    }

    void insertClass(String appName, String[] np) throws SQLException {
        PreparedStatement insertclass;
        
        String insertString="INSERT INTO CLASSES(APPID,PACKAGE,NAME) VALUES("
                + "((select ID from APPS where name = '"
                + appName
                + "')),?,?)";
        
        insertclass=conn.prepareStatement(insertString);
        insertclass.setString(1,np[0]);
        insertclass.setString(2,np[1]);
        
        insertclass.executeUpdate();
        
        //System.out.println("Class inserted");
    }

    void createMethodsDB() throws SQLException {
        PreparedStatement insertapp;
        
        String insertString="CREATE TABLE METHODS"
                + "("
                + "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                + "classid INTEGER REFERENCES CLASSES (id),"
                + "name VARCHAR(200) NOT NULL,"
                + "CONSTRAINT mthd_primary_key PRIMARY KEY (id)"
                + ")";
        
        insertapp=conn.prepareStatement(insertString);
        
        insertapp.executeUpdate();
        
        System.out.println("Method Database created");
    }

    void createParametersDB() throws SQLException {
        PreparedStatement insertapp;
        
        String insertString="CREATE TABLE PARAMETERS"
                + "("
                + "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                + "methodid INTEGER REFERENCES METHODS (id),"
                + "name VARCHAR(200) NOT NULL,"
                + "CONSTRAINT prmtr_primary_key PRIMARY KEY (id)"
                + ")";
        
        insertapp=conn.prepareStatement(insertString);
        
        insertapp.executeUpdate();
        
        System.out.println("Method Parameter Database created");
    }

    long insertMethod(String[] split, String mname) throws SQLException {
        PreparedStatement insertapp;
        
        long key=-1;
        
        String insertString="INSERT INTO METHODS(CLASSID,NAME) VALUES("
                + "((select ID from CLASSES where package = '"
                + split[0]
                +"' AND name='"
                + split[1]
                + "')),'"
                + mname
                + "')";
        
        insertapp=conn.prepareStatement(insertString, Statement.RETURN_GENERATED_KEYS);
        ResultSet generatedKeys;
        
        insertapp.executeUpdate();
        
        generatedKeys = insertapp.getGeneratedKeys();
        if (generatedKeys.next()) {
           key=generatedKeys.getLong(1);
        }
        
        //System.out.println("Method inserted");
        
        return key;
    }

    void insertParameter(long key,String[] np) throws SQLException {
        PreparedStatement insertclass;
        
        String insertString="INSERT INTO PARAMETERS(METHODID,NAME) VALUES("
                + "("
                + key
                + "),?)";

        insertclass=conn.prepareStatement(insertString);

        for(int i=2;i<np.length;i++){
            insertclass.setString(1,np[i]);
            insertclass.executeUpdate();
        }
        
        //System.out.println("Parameter inserted");
    }

    void createAttributesDB() throws SQLException {
                PreparedStatement insertapp;
        
        String insertString="CREATE TABLE ATTRIBUTES"
                + "("
                + "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                + "classid INTEGER REFERENCES METHODS (id),"
                + "datatype VARCHAR(200) NOT NULL,"
                + "name VARCHAR(200) NOT NULL,"
                + "CONSTRAINT atttr_primary_key PRIMARY KEY (id)"
                + ")";
        
        insertapp=conn.prepareStatement(insertString);
        
        insertapp.executeUpdate();
        
        System.out.println("Attributes Database created");
    }

    void insertAttribute(String[] np) throws SQLException {
        PreparedStatement insertclass;
        String[] className=np[0].split("\\.");
        
        String insertString="INSERT INTO ATTRIBUTES(CLASSID,DATATYPE,NAME) VALUES("
                + "((select ID from CLASSES where package = '"
                + className[0]
                +"' AND name='"
                + className[1]
                + "')),?,?)";
        
        insertclass=conn.prepareStatement(insertString);
        
        String[] s;
        for(int i=1;i<np.length;i++){
            s=np[i].split(" ");
            insertclass.setString(1,s[0]);
            insertclass.setString(2,s[1]);
            insertclass.executeUpdate();
        }
        
        //System.out.println("Attribute inserted");
    }

    void createMethodCallsDB() throws SQLException {
        PreparedStatement insertapp;
        
        String insertString="CREATE TABLE METHODCALLS"
                + "("
                + "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                + "methodid INTEGER REFERENCES METHODS (id),"
                + "name VARCHAR(200) NOT NULL,"
                + "CONSTRAINT mthdcll_primary_key PRIMARY KEY (id)"
                + ")";
        
        insertapp=conn.prepareStatement(insertString);
        
        insertapp.executeUpdate();
        
        System.out.println("Method Call Database created");
    }

    void createMethodCallParametersDB() throws SQLException {
        PreparedStatement insertapp;
        
        String insertString="CREATE TABLE METHODCALLPARAMETERS"
                + "("
                + "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                + "methodcallid INTEGER REFERENCES METHODCALLS (id),"
                + "name VARCHAR(200) NOT NULL,"
                + "CONSTRAINT mthdcllprmtr_primary_key PRIMARY KEY (id)"
                + ")";
        
        insertapp=conn.prepareStatement(insertString);
        
        insertapp.executeUpdate();
        
        System.out.println("Method Call Parameter Database created");
    }

    long insertMethodCall(String[] className, String method, String name) throws SQLException {
        
        Statement insertclass = conn.createStatement();
        
        long key=-1;
                
        ResultSet rs=insertclass.executeQuery("SELECT * FROM CLASSES WHERE PACKAGE='"+className[0]+"' AND NAME='"+className[1]+"'");
        rs.next();
        int classID=rs.getInt("ID");
        
        rs=insertclass.executeQuery("SELECT * FROM METHODS WHERE CLASSID="+classID+" AND NAME='"+method+"'");
        rs.next();
        int methodID=rs.getInt("ID");
        
        PreparedStatement insertapp;
        
        String insertString="INSERT INTO METHODCALLS(METHODID,NAME) VALUES("
                + methodID
                + ",'"
                + name
                + "')";
        
        insertapp=conn.prepareStatement(insertString, Statement.RETURN_GENERATED_KEYS);
        ResultSet generatedKeys;
        
        insertapp.executeUpdate();
        
        generatedKeys = insertapp.getGeneratedKeys();
        if (generatedKeys.next()) {
           key=generatedKeys.getLong(1);
        }
        
        
        //System.out.println("Method inserted");
        
        return key;
    }

    void insertMethodCallParameter(long id, String[] np) throws SQLException {
        PreparedStatement insertclass;
        
        String insertString="INSERT INTO METHODCALLPARAMETERS(METHODCALLID,NAME) VALUES("
                + "("
                + id
                + "),?)";

        insertclass=conn.prepareStatement(insertString);

        int j=0;
        try{
        for(int i=3;i<np.length;i++){
            insertclass.setString(1,np[i]);
            insertclass.executeUpdate();
        }
        }
        catch(SQLIntegrityConstraintViolationException e){
            j++;
        }
        
        //System.out.println("Parameter inserted");
    }
}
