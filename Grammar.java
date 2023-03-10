import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Grammar {
    HashMap<String, List<String>> P;
    String V[];
    String S;

    public Grammar(HashMap<String, List<String>> P, String V[], String S) {
        this.P = P;
        this.V = V;
        this.S = S;
    }

    public boolean CYK(String string) {
        HashSet<String> triangularTable[][] = new HashSet[string.length() + 1][string.length() + 1];
        int n = string.length();
        for (int i = 1; i <= n; i++) {
            for (String A : V) {
                if (pExists(A, String.valueOf(string.charAt(i - 1)))) {
                    if (triangularTable[i][i] == null) {
                        triangularTable[i][i] = new HashSet<>();
                    }
                    triangularTable[i][i].add(A);
                }
            }
        }
        for (int s = 1; s <= n - 1; s++) {
            for (int i = 1; i <= n - s; i++) {
                if (triangularTable[i][i + s] == null) {
                    triangularTable[i][i + s] = new HashSet<>();
                }
                for (int k = i; k <= i + s - 1; k++) {
                    for (String B : triangularTable[i][k]) {
                        for (String C : triangularTable[k + 1][i + s]) {
                            for (String A : V) {
                                if (pExists(A, B + C)) {
                                    triangularTable[i][i + s].add(A);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (triangularTable[1][n].contains(S)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean CYK2(String string) {
        HashSet<String> triangularTable[][] = new HashSet[string.length() + 1][string.length() + 1];
        HashMap<String, ArrayList> Y[][] = new HashMap[string.length() + 1][string.length() + 1];
        int n = string.length();
        for (int i = 1; i <= n; i++) {
            for (String A : V) {
                if (pExists(A, String.valueOf(string.charAt(i - 1)))) {
                    if (triangularTable[i][i] == null) {
                        triangularTable[i][i] = new HashSet<>();
                        Y[i][i] = new HashMap<>();
                    }
                    triangularTable[i][i].add(A);
                    Y[i][i].put(A, new ArrayList());
                }
            }
        }
        for (int s = 1; s <= n - 1; s++) {
            for (int i = 1; i <= n - s; i++) {
                if (triangularTable[i][i + s] == null) {
                    triangularTable[i][i + s] = new HashSet<>();
                    Y[i][i + s] = new HashMap<>();
                }
                for (int k = i; k <= i + s - 1; k++) {
                    for (String B : triangularTable[i][k]) {
                        for (String C : triangularTable[k + 1][i + s]) {
                            for (String A : V) {
                                if (pExists(A, B + C)) {
                                    triangularTable[i][i + s].add(A);
                                    ArrayList<String> value = new ArrayList<>();
                                    value.add(String.valueOf(k));
                                    value.add(B);
                                    value.add(C);
                                    Y[i][i + s].put(A, value);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (triangularTable[1][n].contains(S)) {
            if (Y[1][n].get(S) != null) {
                Derive(Y[1][n].get(S), 1, n, string, Y);
            }
            return true;
        } else {
            System.out.println("No Output!");
            return false;
        }
    }

    public boolean pExists(String A, String stringI) {
        if (P.containsKey(A)) {
            if (P.get(A).contains(stringI)) {
                return true;
            }
            return false;
        }
        return false;
    }

    public void Derive(ArrayList<String> val, int i, int j, String string, HashMap<String, ArrayList> Y[][]) {
        if (i == j) {
            System.out.println(S + "=>" + string.charAt(i - 1));
        } else {
            System.out.println(S + "=>" + val.get(1) + val.get(2));
            Derive(Find(val.get(1), i, Integer.parseInt(val.get(0)), Y), i, Integer.parseInt(val.get(0)), string, Y);
            Derive(Find(val.get(2), Integer.parseInt(val.get(0)) + 1, j, Y), Integer.parseInt(val.get(0)) + 1, j, string, Y);
        }
    }

    public ArrayList<String> Find(String A, int i, int j, HashMap<String, ArrayList> Y[][]) {
        if (Y[i][j].get(A) != null) {
            return Y[i][j].get(A);
        }
        return new ArrayList<>();
    }

    public static void printMessage(boolean res, String test) {
        if (res)
            System.out.println("The grammar specified above, accepts the input string:" + test);
        else
            System.out.println("The grammar specified above, doesn't accept the input string:" + test);
    }

    public static void test(HashMap<String, List<String>> P) {
        for (String key : P.keySet()) {
            System.out.print(key + "=>");
            int n = P.get(key).size();
            for (int i = 0; i < n; i++) {
                System.out.print(P.get(key).get(i));
                if (i != n - 1)
                    System.out.print("|");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        //first grammar test
        List<String> firstP1 = new ArrayList<>();
        firstP1.add("AB");
        firstP1.add("BC");
        List<String> secondP1 = new ArrayList<>();
        secondP1.add("BA");
        secondP1.add("a");
        List<String> thirdP1 = new ArrayList<>();
        thirdP1.add("CC");
        thirdP1.add("b");
        List<String> forthP1 = new ArrayList<>();
        forthP1.add("AB");
        forthP1.add("a");
        HashMap<String, List<String>> P1 = new HashMap<String, List<String>>();
        P1.put("S", firstP1);
        P1.put("A", secondP1);
        P1.put("B", thirdP1);
        P1.put("C", forthP1);

        String V1[] = {"S", "A", "B", "C"};
        String S1 = "S";
        String string1 = "abb";
        String string2 = "ababa";
        String string3 = "baaba";
        Grammar grammar1 = new Grammar(P1, V1, S1);
        test(grammar1.P);
        System.out.println("\u001B[94m" + "Part two of project:" + "\u001B[30m");
        printMessage(grammar1.CYK(string1), string1);
        printMessage(grammar1.CYK(string2), string2);
        printMessage(grammar1.CYK(string3), string3);
        System.out.println("\u001B[94m" + "Part three of project:" + "\u001B[30m");
        System.out.println("Input string:" + string1);
        grammar1.CYK2(string1);
        System.out.println("----------------------------------------------------------------");
        System.out.println("Input string:" + string2);
        grammar1.CYK2(string2);
        System.out.println("----------------------------------------------------------------");
        System.out.println("Input string:" + string3);
        grammar1.CYK2(string3);
        //second grammar test
        ArrayList<String> firstP2 = new ArrayList<>();
        firstP2.add("ET");
        firstP2.add("EQ");
        firstP2.add("EI");
        firstP2.add("(");
        firstP2.add("x");
        firstP2.add("y");
        firstP2.add("z");
        firstP2.add("EF");
        firstP2.add("EP");
        ArrayList<String> secondP2 = new ArrayList<>();
        secondP2.add("NE");
        ArrayList<String> thirdP2 = new ArrayList<>();
        thirdP2.add("BE");
        ArrayList<String> forthP2 = new ArrayList<>();
        forthP2.add("EC");
        ArrayList<String> fifthP2 = new ArrayList<>();
        fifthP2.add("+");
        ArrayList<String> sixthP2 = new ArrayList<>();
        sixthP2.add("*");
        ArrayList<String> seventhP2 = new ArrayList<>();
        seventhP2.add(")");
        ArrayList<String> eighthP2 = new ArrayList<>();
        eighthP2.add("-");
        ArrayList<String> ninthP2 = new ArrayList<>();
        ninthP2.add("/");
        ArrayList<String> tenthP2 = new ArrayList<>();
        tenthP2.add("RE");
        ArrayList<String> eleventhP2 = new ArrayList<>();
        eleventhP2.add("DE");

        HashMap<String, List<String>> P2 = new HashMap<String, List<String>>();
        P2.put("E", firstP2);
        P2.put("T", secondP2);
        P2.put("Q", thirdP2);
        P2.put("I", forthP2);
        P2.put("N", fifthP2);
        P2.put("B", sixthP2);
        P2.put("C", seventhP2);
        P2.put("R", eighthP2);
        P2.put("D", ninthP2);
        P2.put("F", tenthP2);
        P2.put("P", eleventhP2);

        String V2[] = P2.keySet().toArray(new String[0]);
        String S2 = "E";
        String stringTest1 = "(x-(y+z)*(x-y))+z";
        String stringTest2 = "x+x*y(z*)";
        String stringTest3 = "(z+x*y";
        Grammar grammar = new Grammar(P2, V2, S2);
        System.out.println("----------------------------------------------------------------");
        System.out.println("----------------------------------------------------------------");
        System.out.println("\u001B[94m" + "Part two of project:" + "\u001B[30m");
        test(grammar.P);
        printMessage(grammar.CYK(stringTest1), stringTest1);
        printMessage(grammar.CYK(stringTest2), stringTest2);
        printMessage(grammar.CYK(stringTest3), stringTest3);
        System.out.println("\u001B[94m" + "Part three of project:" + "\u001B[30m");
        System.out.println("Input string:" + stringTest1);
        grammar.CYK2(stringTest1);
        System.out.println("----------------------------------------------------------------");
        System.out.println("Input string:" + stringTest2);
        grammar.CYK2(stringTest2);
        System.out.println("----------------------------------------------------------------");
        System.out.println("Input string:" + stringTest3);
        grammar.CYK2(stringTest3);
    }
}
