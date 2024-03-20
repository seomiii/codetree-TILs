// 8 : 54 - 
// n개의 식당
// 팀장, 팀원
// 한 가게 당 팀장 : 1명만 무조건 , 팀원 여러명
// 담당한 가게만 검사
// 필요한 검사자 수의 최솟값 

// 식당 수 n,
// 각 식당의 고객 수
// 팀장 최대 고객수 , 팀원 최대 고객수

// 10 ^ 6 ,
import java.util.*;
import java.io.*; 
public class Main {
    static int N; // 식당 수
    static int[] customers; // 각 식당 고객 수 
    static int[] maxCustomer = new int[2]; // 팀장,팀원
    static int result;
    public static void main(String[] args) throws Exception {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        customers = new int[N];

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        for (int i=0; i<N; i++){
            customers[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine(), " ");
        for (int i=0; i<2; i++){
            maxCustomer[i] = Integer.parseInt(st.nextToken());
        }

        int leader = maxCustomer[0];
        int member = maxCustomer[1];

        for (int i=0; i<N; i++){
            int maintain = customers[i] - leader;
            if (maintain > 0){
                result += (1+ maintain / member);
            
                if (maintain%member > 0){ // 나머지가 있으면
                    result += 1;
                }
            } else {
                result += 1;
            }
            
        }

        System.out.println(result);

    }
}