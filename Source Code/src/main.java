public class main {
    public static void main(String[] args) {
        PIM pim = new PIM();
        System.out.println("Welcome to the Personal Information Management System!");
        int cmd = 0;
        while(cmd != -1){
            cmd = pim.home();
        }
    }
}
