import java.io.Serializable;

public class Department implements Serializable {
    private String name;
    private String director;

    public void setName(String name) {
        this.name = name;
    }
    public void setDirector(String director)
    {
        this.director = director;
    }
    public String getName(){return name;}
    public String getDirector(){return director;}
}
