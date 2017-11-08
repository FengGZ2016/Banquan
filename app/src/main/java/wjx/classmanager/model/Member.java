package wjx.classmanager.model;

/**
 * Created by wjx on 2017/10/12.
 */

public class Member {
    private String username;
    private boolean section=true;

    public Member(String username){
        this.username=username;
    }

    public String getUsername() {
        return username;
    }

    public boolean isSection() {
        return section;
    }

    public void setSection(boolean section) {
        this.section = section;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char getFirstLetter(){
        return username.charAt(0);
    }

    public String getFirstLetterString(){
        return String.valueOf(getFirstLetter()).toUpperCase();
    }
}
