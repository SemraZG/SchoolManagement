package com.project.payload.messages;

public class SuccessMessages {

    private SuccessMessages(){//singleton scope-->public olan constructor i private yaptik, bu classdan nesne uretemiyoruz bu sekilde, cunku bu classin objesine ihtiyacim yok baska bir dev gelip obje uretmesin diye private yaptik

    }
    //baska classlarda injeksion da yapmiyorum cunku xzaten nesnesin eihtiyacim yok degiskenleri static yaptim class ismiyle direk ulasabiliyorum zaten.
    public static final String PASSWORD_CHANGED_RESPONSE_MESSAGE ="Password Successfully Changed";
    public static final String USER_CREATED="User is saved successfully";
    public static final String USER_FOUND="User is found successfully";
    public static final String USER_DELETE="User is deleted successfully";
    public static final String USER_UPDATE_MESSAGE = "User is Updated Successfully";
    public static final String USER_UPDATE = "Your information has been updated successfully ";
    public static final String TEACHER_SAVED = "Teacher is saved successfully ";
    public static final String TEACHER_UPDATE = "Teacher has been updated successfully ";
    public static final String ADVISOR_TEACHER_SAVE = "Advisor Teacher is saved successfully ";
    public static final String ADVISOR_TEACHER_DELETE = "Advisor Teacher is deleted successfully ";
    public static final String STUDENT_SAVED = "Student is saved successfully ";
    public static final String STUDENT_UPDATE = "Student is updated successfully ";



}
