public class GUIMain {
    public static void main(String[] args) 
    {
        GUI3 v3 = new GUI3();
        Controller2 c = new Controller2(v3);
        v3.addController(c);
    }
}
