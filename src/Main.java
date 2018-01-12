import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Environment env = new Environment();
        env.parseFile("input/sbr1.txt");

        Engine engine = new Engine(env);
        engine.run();
    }


}
