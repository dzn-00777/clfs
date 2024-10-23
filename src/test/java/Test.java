import java.util.*;

/**
 * @Date 2024/10/17 18:01
 * @Author DZN
 * @Desc Test
 */
public class Test {

    public static void main(String[] args) {
/*        List<String> list = new ArrayList<>();
        list.add("apple");
        list.add("banana");
        list.add("apple");
        list.add("orange");
        list.add("banana");
        list.add("grape");
        list.add("grape");
//        deleteStr(list);
        Map<String, Integer> wordCount = findWordCount(list);
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }*/

/*        Map<String, Integer> map = new HashMap<>();
        map.put("John", 35);
        map.put("Bob", 40);
        map.put("Alice", 30);
        map.put("Tom", 45);
        map.put("Jerry", 50);
        findMaxForThree(map);*/

/*        List<String> list = new ArrayList<>();
        list.add("123");
        list.add("abc");
        list.add("456");
        list.add("def");
        list.add("789");
        list.add("ghi");
        splitList(list);*/

/*        Map<String, List<Integer>> map = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(85);
        list.add(90);
        list.add(95);
        map.put("John", list);
        list = new ArrayList<>();
        list.add(80);
        list.add(85);
        list.add(90);
        map.put("Bob", list);
        list = new ArrayList<>();
        list.add(95);
        list.add(100);
        list.add(105);
        map.put("Alice", list);
        countAvg(map);*/

        ArrayList<String> list = new ArrayList<>();
        list.add("John Doe,john@doe.com");
        list.add("Bob Smith,bob@smith.com");
        list.add("Alice Johnson,alice@johnson.com");
        splitMail(list);
    }

    // 删除所有重复字符串，并保证顺序不变
    public static void deleteStr(List<String> list) {
        LinkedHashSet<String> set = new LinkedHashSet<>(list);
        for (String s : set) {
            System.out.println(s);
        }
    }

    // 找出map中value最大的三个元素
    public static void findMaxForThree(Map<String, Integer> map) {
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(4);
        // 遍历map
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (list.size() < 3) {
                list.add(entry);
                list.sort(new MyComparator());
            } else {
                sortList(list, entry);
            }
        }
        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(entry.getKey());
        }
    }

    public static void sortList(ArrayList<Map.Entry<String, Integer>> list, Map.Entry<String, Integer> entry) {
        list.add(entry);
        list.sort(new MyComparator());
        list.remove(3);
    }

    static class MyComparator implements Comparator<Map.Entry<String, Integer>> {

        @Override
        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
            if (o1.getValue() < o2.getValue()) {
                return 1;
            }else if (o1.getValue().equals(o2.getValue())) {
                return 0;
            }else {
                return -1;
            }
        }
    }

    public static Map<String, Integer> findWordCount(List<String> list) {
        Map<String, Integer> map = new HashMap<>();
        for (String s : list) {
            Integer i = map.getOrDefault(s, 0);
            map.put(s, i + 1);
        }
        return map;
    }

    public static void splitList(List<String> list) {
        ArrayList<String> word = new ArrayList<>();
        ArrayList<String> num = new ArrayList<>();

        // 默认每个s要么全是数字 要么全是字母
        for (String s : list) {
            char c = s.charAt(0);
            if (c >= '0' && c <= '9') {
                num.add(s);
            } else {
                word.add(s);
            }
        }

        for (String s : num) {
            System.out.print(s + " ");
        }
        System.out.println();
        for (String s : word) {
            System.out.print(s + " ");
        }
    }

    public static void countAvg(Map<String, List<Integer>> map) {
        HashMap<String, Float> res = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<Integer> value = entry.getValue();
            float sum = 0;
            for (Integer i : value) {
                sum += i;
            }
            float avg = sum / value.size();
            res.put(key, avg);
        }

        for (Map.Entry<String, Float> entry : res.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    public static void splitMail(List<String> list) {
        HashMap<String, String > map = new HashMap<>();
        for (String s : list) {
            String[] split = s.split(",");
            map.put(split[0], split[1]);
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}
