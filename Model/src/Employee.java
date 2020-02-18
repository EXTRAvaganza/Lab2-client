import java.io.Serializable;

public class Employee implements Serializable {
    private String firstName;
    private String middleName;
    private String lastName;
    private int otdel;
    private String phone;
    private int salary;
    public void setFirstName(String name)
    {
        firstName = name;
    }
    public void setMiddleName(String name)
    {
        middleName = name;
    }
    public void setLastName(String name)
    {
        lastName = name;
    }
    public void setOtdel(int ex)
    {
        otdel = ex;
    }
    public void setPhone(String ex)
    {
        phone = ex;
    }
    public void setSalary(int zp)
    {
        salary = zp;
    }
    public String getFirstName() {return firstName;}
    public String getMiddleName() {return middleName;}
    public String getLastName() {return lastName;}
    public int getOtdel() {return otdel;}
    public String getPhone() {return phone;}
    public int getSalary() {return salary;}
    public boolean equals(Employee temp)
    {
       if(firstName.equals(temp.getFirstName()))
            if(lastName.equals(temp.getLastName()))
                 if(middleName.equals(temp.getMiddleName()))
                     if(this.otdel == temp.getOtdel())
                        if(this.phone.equals(temp.getPhone()))
                         if(this.salary == temp.getSalary())
                             return true;
        return false;
    }
}
