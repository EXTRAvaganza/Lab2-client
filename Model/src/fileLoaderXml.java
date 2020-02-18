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
import java.util.Objects;

public class fileLoaderXml implements model {
    private String dataBase = "C:\\Users\\Рафаэль\\Desktop\\";
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
    public Department createDepartment(String name, String id_dir) throws IOException, ClassNotFoundException
    {
        String emptyId = emptyId("1");
        System.out.println(emptyId);
        Department temp = new Department();
        temp.setDirector(id_dir);
        temp.setName(name);
        try {
            checkExistsDepartment(temp);
            //changeEmployee(temp.getDirector(),"отдел",emptyId);
            FileOutputStream outputStream = new FileOutputStream(dataBase+"1_"+emptyId);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(temp);
            objectOutputStream.close();

        }
        catch (duplicateObject | duplicateDirector ex)
        {
            return null;
        }
        return temp;
    }
    public  Employee createEmployee(String firstName,String lastName, String middleName,String depId,String phone,String salary) throws IOException
    {
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
        return temp;
    }
    public  Employee showEmployee(String id) throws IOException, ClassNotFoundException
    {
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
    public  ArrayList<Employee>  showEmployees() throws IOException, ClassNotFoundException, TransformerException, ParserConfigurationException
    {
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
            fileInputStream.close();
            objectInputStream.close();
            flag = true;
        }
        if(!flag) return null;
        return array;
    }
    public Department showDepartment(String id) throws IOException, ClassNotFoundException
    {
        if(new File(dataBase+"1_"+id).exists())
        {
            FileInputStream fileInputStream = new FileInputStream(dataBase+"1_"+id);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Department read = (Department) objectInputStream.readObject();
            fileInputStream.close();
            objectInputStream.close();
            return read;
        }
        else
            return null;
    }
    public ArrayList<Department> showDepartments() throws IOException, ClassNotFoundException
    {
        ArrayList<Department> array = new ArrayList<Department>();
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
            System.out.println(dataBase+res.get(i)[0]+"_"+res.get(i)[1]);
            Department read = (Department) objectInputStream.readObject();
           array.add(read);
            fileInputStream.close();
            objectInputStream.close();
            flag = true;
        }
        if(!flag) return null;
        return array;
    }
    public  ArrayList<Department> searchDepartament(String attr,String value) throws IOException, ClassNotFoundException
    {
        ArrayList<Department> array = new ArrayList<Department>();
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

                        array.add(read);
                        flag = true;
                    }
                    break;
                }
                case("директор"):
                {
                    if(value.equals(read.getDirector()))
                    {
                        array.add(read);
                        flag = true;
                    }
                    break;
                }
                default:
                {
                    i=res.size();
                    break;
                }
            }
        }
        if(!flag) return null;
        return array;
    }
    public  String deleteDepartment(String id) throws IOException, ClassNotFoundException {

        FileInputStream fileInputStream = new FileInputStream(dataBase+"1_"+id);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Department read = (Department) objectInputStream.readObject();
        fileInputStream.close();
        objectInputStream.close();
        File file = new File(dataBase+"1_"+id);
        if(file.delete()) {
            return "ID отдела:" + id + "Успешно удалён";
        }
        else {
            return "Отдел с таким ID не существует";
        }
    }
    public  String deleteEmployee(String id) throws IOException, ClassNotFoundException
    {
        FileInputStream fileInputStream = new FileInputStream(dataBase+"0_"+id);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Employee read = (Employee) objectInputStream.readObject();
        fileInputStream.close();
        objectInputStream.close();
        File file = new File(dataBase+"0_"+id);
        if(file.delete()) {
            return "ID сотрудника:" + id  + "успешно удалён";
        }
        else {
            return "Сотрудник с таким ID не существует";
        }
    }
    public  ArrayList<Employee> searchEmployee(String attr,String value) throws IOException, ClassNotFoundException
    {
        ArrayList<Employee> array = new ArrayList<Employee>();
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
                        array.add(read);
                        flag = true;
                    }
                    break;
                }
                case("фамилия"):
                {
                    if(read.getLastName().equals(value))
                    {
                        array.add(read);
                        flag = true;
                    }
                    break;
                }
                case("отчество"):
                {
                    if(read.getMiddleName().equals(value))
                    {
                        array.add(read);
                        flag = true;
                    }
                    break;
                }
                case("телефон"):
                {
                    if(read.getPhone().equals(value))
                    {
                        array.add(read);
                        flag = true;
                    }
                    break;
                }
                case("отдел"):
                {
                    if(read.getOtdel() == Integer.parseInt(value))
                    {
                        array.add(read);
                        flag = true;
                    }
                    break;
                }
                case("зарплата"):
                {
                    if(read.getSalary() == Integer.parseInt(value))
                    {
                        array.add(read);
                        flag = true;
                    }
                    break;
                }
                default:
                {
                    i=res.size();
                    return null;
                }
            }
            fileInputStream.close();
            objectInputStream.close();
        }
        if(!flag) return null;
        return array;
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
