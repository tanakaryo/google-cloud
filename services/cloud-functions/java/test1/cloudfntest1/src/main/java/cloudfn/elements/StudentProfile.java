package cloudfn.elements;

import lombok.Data;

@Data
public class StudentProfile {
    
    public StudentProfile() {
    }

    private String name;
    private String age;
    private String sex;
    private String address;
    private String school;
}
