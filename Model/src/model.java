import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;

public interface model {
    public Department createDepartment(String name,String id_dir) throws IOException, ClassNotFoundException;
    public  Employee createEmployee(String firstName,String lastName, String middleName,String depId,String phone,String salary) throws IOException;
    public  Employee showEmployee(String id) throws IOException, ClassNotFoundException;
    public ArrayList<Employee> showEmployees() throws IOException, ClassNotFoundException, TransformerException, ParserConfigurationException;
    public Department showDepartment(String id) throws IOException, ClassNotFoundException;
    public ArrayList<Department> showDepartments() throws IOException, ClassNotFoundException;
    public  ArrayList<Department> searchDepartament(String attr,String value) throws IOException, ClassNotFoundException;
    public  String deleteDepartment(String id) throws IOException, ClassNotFoundException;
    public  String deleteEmployee(String id) throws IOException, ClassNotFoundException;
    public  ArrayList<Employee> searchEmployee(String attr,String value) throws IOException, ClassNotFoundException;
    public  void changeDepartment(String id, String attr,String value) throws IOException, ClassNotFoundException;
    public  void changeEmployee(String id,String attr,String value) throws IOException, ClassNotFoundException;
}
