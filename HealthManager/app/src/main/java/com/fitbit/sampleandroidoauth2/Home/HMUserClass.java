package com.fitbit.sampleandroidoauth2.Home;
import android.content.Context;
import android.util.Base64;
/**
 * Created by niraj on 04-08-17.
 */

public class HMUserClass{
    private int userID = 0;
    private String userEmail = "";
    private String userPassword = "";
    private static String userGender = "";
    private static String userTarget = "";
    private static int userWeight = 0;
    private static int userHeight = 0;
    private static int userAge = 0;
    private static int userDesiredWeight = 0;
    private static int noOfWeeks = 0;
    private double goal=0;

    public HMUserClass(int userID, String userEmail, String userPassword, int userWeight, int userHeight,String userTarget,
                       int userAge, String userGender,int userDesiredWeight,int noOfWeeks,double goal) {
        this.userID = userID;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userGender = userGender;
        this.userWeight = userWeight;
        this.userHeight = userHeight;
        this.userTarget=userTarget;
        this.userAge = userAge;
        this.userDesiredWeight=userDesiredWeight;
        this.noOfWeeks=noOfWeeks;
        this.goal=goal;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }


    public String getUserPassword() {
        return userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public static int getuserDesiredWeight() {
        return userDesiredWeight;
    }

    public static void setuserDesiredWeight(int userDesiredWeight) {
        HMUserClass.userDesiredWeight = userDesiredWeight;
    }

    public static String setUserTarget(String userTarget) {
        return userGender;
    }

    public static String getUserTarget() {
        return userTarget;
    }

    public static String getUserGender() {
        return userGender;
    }

    public static void setUserGender(String userGender) {
        HMUserClass.userGender = userGender;
    }

    public static int getnoOfWeeks() {
        return noOfWeeks;
    }

    public static void setnoOfWeeks(int noOfWeeks) {
        HMUserClass.noOfWeeks = noOfWeeks;
    }


    public HMUserClass getLoggedInUser(){
        return this;
    }

    /**
     *
     * @param userPassword
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = this.getEncryptedPassword(userPassword);
    }

    /**
     *
     * @return
     */
    public int getUserWeight() {
        return userWeight;
    }

    /**
     *
     * @param userWeight
     */
    public void setUserWeight(int userWeight) {
        this.userWeight = userWeight;
    }

    /**
     *
     * @return
     */
    public int getUserHeight() {
        return userHeight;
    }

    /**
     *
     * @param userHeight
     */
    public void setUserHeight(int userHeight) {
        this.userHeight = userHeight;
    }

    /**
     *
     * @return
     */
    public int getUserAge() {
        return userAge;
    }

    /**
     *
     * @param userAge
     */
    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    /**
     *
     * @return
     */
    public int getBMR(){

        return 0;
    }

    public double getGoal(){
        return goal;
    }

    /**
     *
     * @param password
     * @return
     */
    public static String getEncryptedPassword(String password){
        return Base64.encodeToString(password.getBytes(),Base64.DEFAULT);
    }

    /**
     *
     * @param password
     * @return
     */
    public boolean checkPassword(String password){
       String p1=this.getEncryptedPassword(password);
        String p2=this.getUserPassword();
        if(this.getEncryptedPassword(password).equals(this.getUserPassword())) return true;
        return false;
    }

    /**
     *
     * @param ID
     * @param context
     * @return
     */
    public static HMUserClass getUserFromID(int ID,Context context){
        HMDatabaseHandler dbInstance = new HMDatabaseHandler(context);
        HMUserClass User = dbInstance.getUserFromID(ID);
        return User;
    }

    /**
     *
     * @param email
     * @param context
     * @return
     */
    public static HMUserClass getUserFromEmail(String email,Context context){
        HMDatabaseHandler dbInstance = new HMDatabaseHandler(context);
        HMUserClass User = dbInstance.getUserFromEmail(email);
        return User;
    }

}
