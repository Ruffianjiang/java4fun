package com.dawn.java.fun.banner;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

/**
 * @author Ruffianjiang
 * @version V1.0
 * @Title: MyBanner
 * @Package java4fun
 * @date 2019/5/3 13:41
 */
public class MyBanner implements Banner {

    private static final String[] BANNER = {"",
            "  .   ____          _            __ _ _",
            " /\\\\ / ___'_ __ _ _(_)_ __  __ _ \\ \\ \\ \\",
            "( ( )\\___ | '_ | '_| | '_ \\/ _` | \\ \\ \\ \\",
            " \\\\/  ___)| |_)| | | | | || (_| |  ) ) ) )",
            "  '  |____| .__|_| |_|_| |_\\__, | / / / /",
            " =========|_|==============|___/=/_/_/_/"};

    // https://www.bootschool.net/ascii
    private static final String[] BANNER2 = {"                        .__                   ___.                      __   \n" +
            "  ____________  _______ |__|  ____     ____   \\_ |__    ____    ____  _/  |_ \n" +
            " /  ___/\\____ \\ \\_  __ \\|  | /    \\   / ___\\   | __ \\  /  _ \\  /  _ \\ \\   __\\\n" +
            " \\___ \\ |  |_> > |  | \\/|  ||   |  \\ / /_/  >  | \\_\\ \\(  <_> )(  <_> ) |  |  \n" +
            "/____  >|   __/  |__|   |__||___|  / \\___  /   |___  / \\____/  \\____/  |__|  \n" +
            "     \\/ |__|                     \\/ /_____/        \\/                      "};

    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream printStream) {

        for (String line : BANNER2) {
            printStream.println(line);
        }

        printStream.println();

    }
}
