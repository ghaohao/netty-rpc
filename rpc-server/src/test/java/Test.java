/**
 * Created by hao.g on 18/4/19.
 */
public class Test {
    public static void main(String[] args) {
        for(int i = 0;i < 80;i++){
            System.out.println(i + " " +  ((i & -i ) == i));
        }
    }
}
