package common.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;



public class RandomPassWord {
    private Pattern pattern;
    private Matcher matcher;

    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})";

    public RandomPassWord() {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    /**
     * Validate password with regular expression
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public boolean validate(final String password) {

        matcher = pattern.matcher(password);
        return matcher.matches();

    }

    private static final char[] symbols;

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch)
            tmp.append(ch);
        for (char ch = 'a'; ch <= 'z'; ++ch)
            tmp.append(ch);
        for (char ch = 'A'; ch <= 'Z'; ++ch)
            tmp.append(ch);
        symbols = tmp.toString().toCharArray();
    }

    private final Random random = new Random();

    private char[] buf = null;

    public RandomPassWord(int length) {
        if (length < 1)
            throw new IllegalArgumentException("length < 1: " + length);
        buf = new char[length];
    }

    public RandomPassWord(int minLength, int maxLength) {
        //        if (maxLength <= minLength)
        //            throw new IllegalArgumentException("maxLength nho hon minLength");
        //        buf = new char[random.nextInt(maxLength - minLength + 1) + minLength]; // Tu 8 den 20 ky tu
        iResultLength = random.nextInt(maxLength - minLength + 1) + minLength;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }
    int minLength, maxLength;
    private int iResultLength;


    public String nextString1() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    public String nextString() {
        String sResult = null;
        while (!check(sResult)) {
            sResult = RandomStringUtils.randomAscii(iResultLength);
        }
        return sResult;
    }

    public boolean check(String input) {
        if (input == null)
            return false;
        // Check do dai
        if (!(input.length() >= this.minLength && input.length() <= this.maxLength))
            return false;
        char[] temp = input.toCharArray();
        boolean bUpper6590 = false, bLower97to122 = false, bNumber4857 = false, bSpecchar3347 = false, bSpecchar5864 =
            false, bSpecchar9196 = false, bSpeccharle122 = false;
        for (char c : temp) {


            int icode = (int)c;
            if (icode == 32) // Khoang trang
                return false;
            if (icode == 44) // Dau '
                return false;
            if (!bUpper6590) {
                bUpper6590 = icode >= 65 && icode <= 90;
                if (bUpper6590) {
                    if (bLower97to122 && bNumber4857 && bUpper6590 &&
                        (bSpecchar3347 || bSpecchar5864 || bSpecchar9196 || bSpeccharle122))
                        return true;
                    continue;
                }
            }


            if (!bLower97to122) {
                bLower97to122 = icode >= 97 && icode <= 122;
                if (bLower97to122) {
                    if (bLower97to122 && bNumber4857 && bUpper6590 &&
                        (bSpecchar3347 || bSpecchar5864 || bSpecchar9196 || bSpeccharle122))
                        return true;
                    continue;
                }
            }


            if (!bNumber4857) {
                bNumber4857 = icode >= 48 && icode <= 57;
                if (bNumber4857) {
                    if (bLower97to122 && bNumber4857 && bUpper6590 &&
                        (bSpecchar3347 || bSpecchar5864 || bSpecchar9196 || bSpeccharle122))
                        return true;
                    continue;
                }
            }


            if (!bSpecchar3347) {
                bSpecchar3347 = icode >= 33 && icode <= 47;
                if (bSpecchar3347) {
                    if (bLower97to122 && bNumber4857 && bUpper6590 &&
                        (bSpecchar3347 || bSpecchar5864 || bSpecchar9196 || bSpeccharle122))
                        return true;
                    continue;
                }
            }


            if (!bSpecchar5864) {
                bSpecchar5864 = icode >= 58 && icode <= 64;
                if (bSpecchar5864) {
                    if (bLower97to122 && bNumber4857 && bUpper6590 &&
                        (bSpecchar3347 || bSpecchar5864 || bSpecchar9196 || bSpeccharle122))
                        return true;
                    continue;
                }
            }


            if (!bSpecchar9196) {
                bSpecchar9196 = icode >= 91 && icode <= 96;
                if (bSpecchar9196) {
                    if (bLower97to122 && bNumber4857 && bUpper6590 &&
                        (bSpecchar3347 || bSpecchar5864 || bSpecchar9196 || bSpeccharle122))
                        return true;
                    continue;
                }
            }


            if (!bSpeccharle122) {
                bSpeccharle122 = icode >= 122;
                if (bSpeccharle122) {
                    if (bLower97to122 && bNumber4857 && bUpper6590 &&
                        (bSpecchar3347 || bSpecchar5864 || bSpecchar9196 || bSpeccharle122))
                        return true;
                    continue;
                }

            }


        }
        return false;
    }
}
