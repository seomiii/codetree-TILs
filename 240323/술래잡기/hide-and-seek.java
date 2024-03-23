// 9:21 - 

// nxn , 정중앙에 서있다
// m명의 도망자가 있다. 
// 좌우 : 항상 오른쪽을 보고 시작 | 상하 : 항상 아래쪽을 보고 시작 
// h개의 나무
// 도망자와 나무는 겹쳐질 수 있음
// k 번 반복
// 도망자가 움직일 때 현재 술래와의 거리가 3이하인 도망자만 움직임
// 술래는 달팽이 모양으로 움직임
//

import java.io.*;
import java.util.*;

class Node{
    int r;
    int c;
    int dir;

    public Node(int r, int c, int dir){
        this.r = r;
        this.c = c;
        this.dir = dir;
    }
}

public class Main {
    static int N,M,H,K;
    static int[][] graph;
    static int[][] trees;

    static Deque<Node> domangs = new ArrayDeque<>();

    static int result; 
    static Node sul;
    public static void main(String[] args) throws Exception {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        N = Integer.parseInt(st.nextToken()); // graph
        M = Integer.parseInt(st.nextToken()); // 도망자
        H = Integer.parseInt(st.nextToken()); // 나무개수
        K = Integer.parseInt(st.nextToken()); // k번 반복

        // 술래 : -1, 도망자 : 1 (수로 셈), 방향 
        graph = new int[N+1][N+1];
        trees = new int[N+1][N+1];
        // graph[N/2+1][N/2+1] = -1; // 술래
        sul = new Node(N/2+1, N/2+1, 0); // 술래

        for (int i=0; i<M; i++){ // 도망자 // 상 하 좌 우 0 1 2 3 
            st = new StringTokenizer(br.readLine(), " ");
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int dir = Integer.parseInt(st.nextToken());
            if (dir == 1){
                dir = 3;
            } else {
                dir = 1;
            }

            graph[r][c] += 1; // 도망자 그래프에 기록
            domangs.add(new Node(r,c,dir)); // 큐에 넣어 놓기 
        }

        for (int i=0; i<H; i++){ // 나무 기록
            st = new StringTokenizer(br.readLine(), " ");
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());

            trees[r][c] = -2;
        }

        
            

        for (int i=1; i<=K; i++){ // k번 반복
            // 1. 도망자 도망치기
            Domang();
            // 2. 술래 움직이기 
            // System.out.println(sul.r+" "+sul.c+" "+sul.dir+ " ddd");
            Sul();

            // 3. 시야 내에 있는 도망자 잡기 - 방향을 기준으로 3칸
            // System.out.println(sul.r+" "+sul.c);
            // for (int k=1; k<=N; k++){
            //     System.out.println(Arrays.toString(graph[k]));
            // }
            // System.out.println();

            int nr = sul.r;
            int nc = sul.c;
            int ccatch = 0;
            // System.out.println("tree "+trees[sul.r][sul.c]);
            if (graph[nr][nc] > 0 && trees[nr][nc] == 0){
                ccatch+= graph[nr][nc];
                graph[nr][nc] = 0; // 잡으면 사라짐.
            }
    
            for (int j=0; j<2; j++){
            nr += suldr[sul.dir];
            nc += suldc[sul.dir];
            
            if (nr<=0 || nr>N || nc<=0 || nc>N) break;
            // System.out.println(nr +" "+ nc +" "+graph[nr][nc] +" " + trees[nr][nc]);
            if (graph[nr][nc] > 0 && trees[nr][nc] == 0){
                // System.out.println(nr+" "+ nc+" " +graph[nr][nc]+"cathc");
                ccatch+= graph[nr][nc];
                graph[nr][nc] = 0; // 잡으면 사라짐.
                }
            }
                
            // System.out.println(result+" "+ccatch+" "+i);
            result += ccatch * i;

        }
        
        System.out.println(result);

    } // end of main

    static Deque<Node> newdomangs = new ArrayDeque<>();
    static int[] dr = {-1,1,0,0};
    static int[] dc = {0,0,-1,1};
    static boolean flag = false;

    static int move = 0;
    static int moveMax = 1;
    static int crash = 0;
    static int crashMax = 2;

    static int[] suldr = {-1,0,1,0}; // 상 우 하 좌
    static int[] suldc ={0,1,0,-1};

    public static void Sul(){
        if (!flag){ // 정방향
            if (moveMax == N-1) crashMax = 3;
            if (move == moveMax){ // 방향 바꿔주기
                move = 0;
                // sul.dir = (sul.dir+1)%4;

                crash++; // 충돌
                if (crash == crashMax){
                    moveMax++;
                    crash = 0;
                }
            } 
        
            sulmove(); // 한칸 이동
            move++;
            if (move == moveMax){
                sul.dir = (sul.dir+1)%4;
            }

            if (sul.r == 1 && sul.c == 1){
                flag = true; // 역방향으로 전환
                move = 0;
                crash = 0;
                sul.dir = 2;
            }
            // System.out.println(sul.r +" "+ sul.c + " "+ sul.dir);
        } // end of 정방향
        
        else { // 역방향
            // System.out.println(move+" "+moveMax + " "+crash +" "+ crashMax);
 
            if (moveMax == N-2) crashMax = 2;
            if (move == moveMax){
                move = 0;
                // sul.dir = (sul.dir+3)%4;

                crash++; // 충돌
                if (crash == crashMax){
                    moveMax--;
                    crash = 0;
                }
            }
            sulmove(); // 한칸 이동
            move++;
            if (move == moveMax){
                sul.dir = (sul.dir+3)%4;
            }
            // System.out.println(sul.r +" "+ sul.c + " "+ sul.dir);

            if (sul.r == (N+1)/2 && sul.c == (N+1)/2){ // 정방향으로 전환
                flag = false;
                move = 0;
                crash = 0;
                sul.dir = 0;
            }
        }
    }

    public static void sulmove(){
        int nr = sul.r + suldr[sul.dir];
        int nc = sul.c + suldc[sul.dir];

        // graph[nr][nc] = -1;
        // graph[sul.r][sul.c] = 0;

        sul.r = nr;
        sul.c = nc;
    }

    public static void Domang(){
        while(!domangs.isEmpty()){
            Node dom = domangs.poll();
            if(graph[dom.r][dom.c] == 0) continue;
            
            //System.out.println("domang : "+dom.r+" "+dom.c+" "+dom.dir+" "+graph[dom.r][dom.c]);
            // 술래와의 거리가 3 이하인가?
            int distance = Math.abs(dom.r-sul.r) + Math.abs(dom.c-sul.c);
            if (distance <= 3){
                // 바라보고 있는 방향으로 움직이면 격자를 벗어나는가?
                int nr = dom.r + dr[dom.dir];
                int nc = dom.c + dc[dom.dir];

                if (nr>N || nr<=0 || nc<=0 || nc>N){ // 벗어나면
                    // 방향 반대로 틀기 
                    dom.dir = changeDir(dom.dir);
                    nr = dom.r + dr[dom.dir];
                    nc = dom.c + dc[dom.dir];
                }

                // 술래가 없다면
               // System.out.println("?? : "+nr+" "+sul.r+" "+nc+" "+sul.c+" "+dom.dir);
                if (nr != sul.r || nc != sul.c){ 
                        graph[dom.r][dom.c] -=1;
                        graph[nr][nc] +=1; // 이동
                        newdomangs.add(new Node(nr,nc,dom.dir));
                        // System.out.println("newnewe domang : "+nr+" "+nc+" "+dom.dir);
                }else {
                    newdomangs.add(dom);
                }

               // System.out.println("new domang : "+nr+" "+nc+" "+dom.dir);

            }else {
                newdomangs.add(dom);
            }
        }

        domangs = newdomangs;
        newdomangs = new ArrayDeque<>(); // 초기화
    }

    public static int changeDir(int dir){
        if (dir == 0) return 1;
        if (dir == 1) return 0;
        if (dir == 2) return 3;
        else return 2;
    }

}