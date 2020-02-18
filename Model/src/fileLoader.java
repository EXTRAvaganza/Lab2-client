/*
import Exceptions.duplicateDirector;
import Exceptions.duplicateObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;

public class fileLoader implements model {
    String dataBase = "C:\\Users\\EXTRAVAGANZA\\Desktop\\DB\\";
    public String createDepartment(String name,String id_dir) throws IOException, ClassNotFoundException {
        String emptyId = emptyId("1");
        Department temp = new Department();
        if(findEmployee(id_dir).getOtdel()==0 && findEmployee(id_dir)!=null)
        {
            temp.setDirector(id_dir);
            temp.setName(name);
        }
        else
            return "Сотрудник ID "+id_dir+" не находится в отделе переводов(СТОК) или его не существует";
        try {
            checkExistsDepartment(temp);
            changeEmployee(temp.getDirector(),"отдел",emptyId);
            FileOutputStream outputStream = new FileOutputStream(dataBase+"1_"+emptyId);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(temp);
            objectOutputStream.close();

        }
        catch (duplicateObject ex)
        {
           return "Отдел с указанным именем уже существует.\nВыберите другое имя.";
        }
        catch (duplicateDirector ex)
        {
            return "Заданный сотрудник уже возглавляет другой отдел";
        }
        return reportDeparment(temp);
    }
    private Employee findEmployee(String id) throws IOException, ClassNotFoundException {
        if(new File(dataBase+"0_"+id).exists())
        {
            FileInputStream fileInputStream = new FileInputStream(dataBase+"0_"+id);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Employee read = (Employee) objectInputStream.readObject();
            fileInputStream.close();
            objectInputStream.close();
            return read;
        }
        else
            return null;
    }
    private void checkExistsDepartment(Department temp) throws IOException, duplicateDirector, ClassNotFoundException, duplicateObject {
        File file = new File(dataBase);
        String[] test = file.list();
        ArrayList<String[]> stro4ki = new ArrayList<String[]>();
        for (int i = 0; i < test.length; i++)
            stro4ki.add(test[i].split("_"));
        ArrayList<String[]> res = new ArrayList<String[]>();
        for (int i = 0; i < stro4ki.size(); i++)
        {
            if(stro4ki.get(i)[0].equals("1"))
                res.add(stro4ki.get(i));
        }
        for(int i=0;i<res.size();i++) {
            FileInputStream fileInputStream = new FileInputStream(dataBase + res.get(i)[0] + "_" + res.get(i)[1]);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Department read = (Department) objectInputStream.readObject();
            if (read.getName().equals(temp.getName())) {
                throw new duplicateObject();
            }
            if(read.getDirector().equals(temp.getDirector()))
                throw new duplicateDirector();
        }
    }
    private  String emptyId(String k) {
        File file = new File(dataBase);
        String[] test = file.list();
        ArrayList<String[]> stro4ki = new ArrayList<String[]>();
        for (int i = 0; i < test.length; i++)
            stro4ki.add(test[i].split("_"));
        ArrayList<String[]> res = new ArrayList<String[]>();
        for (int i = 0; i < stro4ki.size(); i++)
        {
            if(stro4ki.get(i)[0].equals(k))
                res.add(stro4ki.get(i));
        }
        boolean flag = false;
        for(int i=0;i<1000;i++)
        {
            flag = false;
            for(int j=0;j<res.size();j++)
            {
                if(Integer.parseInt(res.get(j)[1])==i)
                {
                    flag = true;
                }
            }
            if(!flag)
            {
                return Integer.toString(i);
            }
        }
        return "";
    }
    public  void createEmployee(String firstName,String lastName, String middleName,String depId,String phone,String salary) throws IOException {
        Employee temp = new Employee();
        temp.setFirstName(firstName);
        temp.setLastName(lastName);
        temp.setMiddleName(middleName);
        temp.setOtdel(Integer.parseInt(depId));
        temp.setPhone(phone);
        temp.setSalary(Integer.parseInt(salary));
        try {
            checkExistsEmployee(temp);
            FileOutputStream outputStream = new FileOutputStream(dataBase+"0_"+emptyId("0"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(temp);
            objectOutputStream.close();
        }
        catch (duplicateObject ex)
        {
            duplicateObject.message("Сотрудник с указанными параметрами уже существует.");
        }
        catch(ClassNotFoundException ignored){}
    }
    private  void checkExistsEmployee(Employee temp) throws duplicateObject, IOException, ClassNotFoundException {
        File file = new File(dataBase);
        String[] test = file.list();
        ArrayList<String[]> stro4ki = new ArrayList<String[]>();
        for (int i = 0; i < test.length; i++)
            stro4ki.add(test[i].split("_"));
        ArrayList<String[]> res = new ArrayList<String[]>();
        for (int i = 0; i < stro4ki.size(); i++)
        {
            if(stro4ki.get(i)[0].equals("0"))
                res.add(stro4ki.get(i));
        }
        for(int i=0;i<res.size();i++)
        {
            FileInputStream fileInputStream = new FileInputStream(dataBase+res.get(i)[0]+"_"+res.get(i)[1]);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Employee read = (Employee) objectInputStream.readObject();
            if(read.equals(temp)) throw new duplicateObject();
            fileInputStream.close();
            objectInputStream.close();
        }
    }
    private  String reportEmployee(Employee ob) {
        String kek =
                "Сотрудник: " +
                        ob.getLastName() + " " +
                        ob.getFirstName() + " " +
                        ob.getMiddleName() + " " + "\n" +
                        "Отдел № " +
                        ob.getOtdel() + "\n" +
                        "Телефон: " +
                        ob.getPhone() +
                        "\nРазмер зарплаты: " +
                        ob.getSalary();
        return kek;
    }
    public  String showEmployee(String id) throws IOException, ClassNotFoundException {
        if(new File(dataBase+"0_"+id).exists())
        {
            FileInputStream fileInputStream = new FileInputStream(dataBase+"0_"+id);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Employee read = (Employee) objectInputStream.readObject();
            fileInputStream.close();
            objectInputStream.close();
            return "ID сотрудника:"+ id+"\n"+reportEmployee(read);
        }
        else
           return "Сотрудник с указанным ID не существует";
    }
    public  String showEmployees() throws IOException, ClassNotFoundException, TransformerException, ParserConfigurationException {
        String resultat = "";
        File file = new File(dataBase);
        String[] test = file.list();
        ArrayList<Employee> array = new ArrayList<Employee>();
        ArrayList<String[]> stro4ki = new ArrayList<String[]>();
        for (int i = 0; i < test.length; i++)
            stro4ki.add(test[i].split("_"));
        ArrayList<String[]> res = new ArrayList<String[]>();
        for (int i = 0; i < stro4ki.size(); i++)
        {
            if(stro4ki.get(i)[0].equals("0"))
                res.add(stro4ki.get(i));
        }
        boolean flag = false;
        for(int i=0;i<res.size();i++)
        {
            FileInputStream fileInputStream = new FileInputStream(dataBase+res.get(i)[0]+"_"+res.get(i)[1]);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Employee read = (Employee) objectInputStream.readObject();
            array.add(read);
            new xmlGenerator().getEmployee(array);
            resultat+="ID сотрудника:"+ res.get(i)[1] +"\n"+reportEmployee(read);
            fileInputStream.close();
            objectInputStream.close();
            flag = true;
        }
        if(!flag) return"Ни одного сотрудника не обнаружено";
        return resultat;
    }
    public String showDepartment(String id) throws IOException, ClassNotFoundException {
        if(new File(dataBase+"1_"+id).exists())
        {
            FileInputStream fileInputStream = new FileInputStream(dataBase+"1_"+id);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Department read = (Department) objectInputStream.readObject();
            fileInputStream.close();
            objectInputStream.close();
            return "ID отдела:"+ id + reportDeparment(read);
        }
        else
           return "Отдел с указанным ID не существует";
    }
    public String showDepartments() throws IOException, ClassNotFoundException {
        String resultat="";
        File file = new File(dataBase);
        String[] test = file.list();
        ArrayList<String[]> stro4ki = new ArrayList<String[]>();
        for (int i = 0; i < test.length; i++)
            stro4ki.add(test[i].split("_"));
        ArrayList<String[]> res = new ArrayList<String[]>();
        for (int i = 0; i < stro4ki.size(); i++)
        {
            if(stro4ki.get(i)[0].equals("1"))
                res.add(stro4ki.get(i));
        }
        boolean flag = false;
        for(int i=0;i<res.size();i++)
        {
            FileInputStream fileInputStream = new FileInputStream(dataBase+res.get(i)[0]+"_"+res.get(i)[1]);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Department read = (Department) objectInputStream.readObject();
            resultat += "ID отдела:"+ res.get(i)[1] + "\n" +reportDeparment(read);
            fileInputStream.close();
            objectInputStream.close();
            flag = true;
        }
        if(!flag) return "Ни одного отдела не обнаружено";
        return resultat;
    }
    private  String reportDeparment(Department temp) {
        String kek =
                "Отдел:" + temp.getName()+ "\n"+
                        "ID начальника:" + temp.getDirector();
        return kek;
    }
    public  String searchDepartament(String attr,String value) throws IOException, ClassNotFoundException {
        boolean flag = false;
        String resultat = "";
        File file = new File(dataBase);
        String[] test = file.list();
        ArrayList<String[]> stro4ki = new ArrayList<String[]>();
        for (int i = 0; i < test.length; i++)
            stro4ki.add(test[i].split("_"));
        ArrayList<String[]> res = new ArrayList<String[]>();
        for (int i = 0; i < stro4ki.size(); i++)
        {
            if(stro4ki.get(i)[0].equals("1"))
                res.add(stro4ki.get(i));
        }
        for(int i=0;i<res.size();i++)
        {
            FileInputStream fileInputStream = new FileInputStream(dataBase+res.get(i)[0]+"_"+res.get(i)[1]);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Department read = (Department) objectInputStream.readObject();
            switch (attr)
            {
                case("имя"):
                {
                    if(read.getName().equals(value))
                    {

                        resultat += "ID отдела:"+ res.get(i)[1] +reportDeparment(read);
                        flag = true;
                    }
                    break;
                }
                case("директор"):
                {
                    if(value.equals(read.getDirector()))
                    {
                        resultat += "ID отдела:"+ res.get(i)[1] +reportDeparment(read);
                        flag = true;
                    }
                    break;
                }
                default:
                {
                    i=res.size();
                    resultat ="Указанный атрибут: "+attr+" не существует"+"\nВоспользуйтесь справкой -h";
                    break;
                }
            }
        }
        if(!flag) return "Отделы с указанным значением атрибута \""+attr+"\" = "+value+" не существуют";
        return resultat;
    }
    public  String deleteDepartment(String id) throws IOException, ClassNotFoundException {

        FileInputStream fileInputStream = new FileInputStream(dataBase+"1_"+id);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Department read = (Department) objectInputStream.readObject();
        fileInputStream.close();
        objectInputStream.close();
        File file = new File(dataBase+"1_"+id);
        if(file.delete()) {
            return "ID отдела:" + id + reportDeparment(read) + "Успешно удалён";
        }
        else {
            return "Отдел с таким ID не существует";
        }
    }
    public  String deleteEmployee(String id) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(dataBase+"0_"+id);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Employee read = (Employee) objectInputStream.readObject();
        fileInputStream.close();
        objectInputStream.close();
        File file = new File(dataBase+"0_"+id);
        if(file.delete()) {
            return "ID сотрудника:" + id +"" + "\n" + reportEmployee(read) + "\nУспешно удалён";
        }
        else {
            return "Сотрудник с таким ID не существует";
        }
    }
    public  String searchEmployee(String attr,String value) throws IOException, ClassNotFoundException {
        String resultat = "";
        boolean flag = false;
        File file = new File(dataBase);
        String[] test = file.list();
        ArrayList<String[]> stro4ki = new ArrayList<String[]>();
        for (int i = 0; i < test.length; i++)
            stro4ki.add(test[i].split("_"));
        ArrayList<String[]> res = new ArrayList<String[]>();
        for (int i = 0; i < stro4ki.size(); i++)
        {
            if(stro4ki.get(i)[0].equals("0"))
                res.add(stro4ki.get(i));
        }
        for(int i=0;i<res.size();i++)
        {
            FileInputStream fileInputStream = new FileInputStream(dataBase+res.get(i)[0]+"_"+res.get(i)[1]);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Employee read = (Employee) objectInputStream.readObject();
            switch (attr)
            {
                case("имя"):
                {
                    if(read.getFirstName().equals(value))
                    {
                        resultat+="ID сотрудника:"+ res.get(i)[1]+"\n" + reportEmployee(read);
                        flag = true;
                    }
                    break;
                }
                case("фамилия"):
                {
                    if(read.getLastName().equals(value))
                    {
                        resultat+="ID сотрудника:"+ res.get(i)[1]+"\n" + reportEmployee(read);
                        flag = true;
                    }
                    break;
                }
                case("отчество"):
                {
                    if(read.getMiddleName().equals(value))
                    {
                        resultat+="ID сотрудника:"+ res.get(i)[1]+"\n" + reportEmployee(read);
                        flag = true;
                    }
                    break;
                }
                case("телефон"):
                {
                    if(read.getPhone().equals(value))
                    {
                        resultat+="ID сотрудника:"+ res.get(i)[1]+"\n" + reportEmployee(read);
                        flag = true;
                    }
                    break;
                }
                case("отдел"):
                {
                    if(read.getOtdel() == Integer.parseInt(value))
                    {
                        resultat+="ID сотрудника:"+ res.get(i)[1]+"\n" + reportEmployee(read);
                        flag = true;
                    }
                    break;
                }
                case("зарплата"):
                {
                    if(read.getSalary() == Integer.parseInt(value))
                    {
                        resultat+="ID сотрудника:"+ res.get(i)[1]+"\n" + reportEmployee(read);
                        flag = true;
                    }
                    break;
                }
                default:
                {
                    i=res.size();
                    return "Указанный атрибут: "+attr+" не существует"+"\nВоспользуйтесь справкой -h";
                }
            }
            fileInputStream.close();
            objectInputStream.close();
        }
        if(!flag) return "Сотрудников с указанным значением атрибута \""+attr+"\" = "+value+" не существует";
        return resultat;
    }
    public  void changeDepartment(String id, String attr,String value) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(dataBase+"1_"+id);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Department read = (Department) objectInputStream.readObject();
        fileInputStream.close();
        objectInputStream.close();
        switch (attr) {
            case ("имя"): {
                read.setName(value);
                break;
            }
            case ("директор"): {
                read.setDirector(value);
                break;
            }
        }
        FileOutputStream outputStream = new FileOutputStream(dataBase+"1_"+id);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(read);
        objectOutputStream.close();
    }
    public  void changeEmployee(String id,String attr,String value) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(dataBase+"0_"+id);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Employee read = (Employee) objectInputStream.readObject();
        fileInputStream.close();
        objectInputStream.close();
        switch (attr) {
            case ("имя"): {
                read.setFirstName(value);
                break;
            }
            case ("фамилия"): {
                read.setLastName(value);
                break;
            }
            case("отчество"):
            {
                read.setMiddleName(value);
                break;
            }
            case("отдел"):
            {
                read.setOtdel(Integer.parseInt(value));
                break;
            }
            case("зарплата"):
            {
                read.setSalary(Integer.parseInt(value));
                break;
            }
            case("телефон"):
            {
                read.setPhone(value);
                break;
            }
        }
        FileOutputStream outputStream = new FileOutputStream(dataBase+"0_"+id);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(read);
        objectOutputStream.close();
    }
}
*/
