import java.util.*;
import java.io.*;

public class Main {
    static int N,M,P,C,D;
    static int[] Rloc = new int[2]; // 루돌프위치
    static int[] scores; // 산타 점수

    static class Node{
        int r;
        int c;
        int kijul; // 정상일 순간
        int num; // 번호
        boolean flag;

        Node(int r, int c, int kijul, int num, boolean flag){
            this.r = r;
            this.c = c;
            this.kijul = kijul;
            this.num = num;
            this.flag = flag;
        }
    }

    static Node[][] graph;
    static Map<Integer, Node> santas = new HashMap<>();

    static List<Integer> alive = new ArrayList<>();
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        N = Integer.parseInt(st.nextToken()); //게임판
        M = Integer.parseInt(st.nextToken()); // 게임턴
        P = Integer.parseInt(st.nextToken()); // 산타 수
        C = Integer.parseInt(st.nextToken()); // 루돌프점수
        D = Integer.parseInt(st.nextToken()); // 산타 점수

        scores = new int[P+1]; // 점수
        graph = new Node[N+1][N+1]; // 그래프

        st = new StringTokenizer(br.readLine());
        Rloc[0] = Integer.parseInt(st.nextToken());
        Rloc[1] = Integer.parseInt(st.nextToken());

        for (int i=1; i<=P; i++){
            st = new StringTokenizer(br.readLine());
            int num = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());

            Node santa = new Node(r,c,1,num, true);
            graph[r][c] = santa;
            santas.put(num,santa);
            alive.add(num);
        }

        for (int game=1; game<=M; game++){
            if (santas.size() == 0){
                game = M+100;
                continue;
            }

            // 1. 루돌프의 움직임
            // 1-1. 가장 가까운 산타 찾기
            //System.out.println("rrrr "+ Rloc[0] +" "+Rloc[1]);
            Node closeSanta = findSanta();
            // 산타 방향으로 이동
            int dir = findRDir(Rloc[0], Rloc[1], closeSanta.r,closeSanta.c);

            // System.out.println("close santa dir "+dir);
            int Rnr = Rloc[0] + Rdr[dir];
            int Rnc = Rloc[1] + Rdc[dir];
            Rloc[0] = Rnr; // 루돌프 움직이기
            Rloc[1] = Rnc;

            // 루돌프가 움직여서 충돌이 일어난 경우
            if (graph[Rnr][Rnc] != null){
                Node crashSanta = graph[Rnr][Rnc];
                //System.out.println("루돌프가 움직여서 충돌 , 위치: "+ Rnr+" "+Rnc+" 충돌한 산타: "+ crashSanta.num);
                scores[crashSanta.num] += C;
                crash(dir, C, crashSanta);
            }

            // 2. 산타들의 움직임
            for (Map.Entry<Integer, Node> entry : santas.entrySet()){
            // for (int i=0; i<santas.size(); i++){
               // System.out.println("iii "+i+" "+alive.size());
                Node moveSanta = santas.get(entry.getKey());
                if (!moveSanta.flag) continue;
                //System.out.println("SANTA "+ moveSanta.num +" "+moveSanta.kijul+" "+game );
                if (moveSanta.kijul > game) continue;

                int santadir = findSDir(moveSanta.r,moveSanta.c); // 가까워지는 방향
                //System.out.println(moveSanta.num + " 산타가 갈 방향 "+santadir+" "+ moveSanta.r +" "+ moveSanta.c);

                if (santadir == -1) continue;

                int santanr = moveSanta.r + Sdr[santadir];
                int santanc = moveSanta.c + Sdc[santadir];

                if (santanr<=0 || santanr>N || santanc<=0 || santanc>N) continue;
                if (graph[santanr][santanc] != null) continue;
                // 루돌프로부터 가까워질 수 있는 방법 없다면 ? 움직이지 않음.

                if (santanr== Rloc[0] && santanc == Rloc[1]){ // 루돌프와 충돌났을 경우
                    scores[moveSanta.num] += D; // 점수 얻기
                    int ddir = changeDir(santadir);
                    ///////// 충돌 났을 경우
                    graph[moveSanta.r][moveSanta.c] = null;
                    graph[santanr][santanc] = moveSanta;
                    moveSanta.r = santanr;
                    moveSanta.c = santanc;
                    crash(ddir, D, moveSanta);
                } else{
                    graph[moveSanta.r][moveSanta.c] = null;
                    graph[santanr][santanc] = moveSanta;
                    moveSanta.r = santanr;
                    moveSanta.c = santanc;
                }

                moveSanta.kijul+=1;
                //System.out.println(moveSanta.num + " 번 산타 이동 후 "+ moveSanta.r +" "+moveSanta.c);
               // System.out.println("iii "+i+" "+alive.size());
            } // end of each santa

            // 1점씩 추가 부여
            for (int i=0; i<alive.size(); i++){
                int num = alive.get(i);
                scores[num] += 1;
                Node s = santas.get(num);
                //s.kijul+=1;
            }

            //printalive();
            //System.out.println(Arrays.toString(scores));
            //System.out.println();

        } // end of game

        // for (int i=1; i<=P; i++){
        //     System.out.println(scores[i]);
        // }
        //System.out.println(Arrays.toString(scores));
        for (int i=1; i<=P; i++){
            sb.append(scores[i]+" ");
        }
        System.out.println(sb.toString());

    }

    static void printalive(){
        System.out.println("alive");
        for (int i=0; i<alive.size(); i++){
            System.out.print(alive.get(i));
        }
        System.out.println();
    }

    // 가장 가까운 산타 찾기
    // 상하좌우 대각선
    // 상우하좌
    static int[] Rdr = {-1,0,1,0,-1,-1,1,1};
    static int[] Rdc = {0,1,0,-1,1,-1,1,-1};
    static int[] Sdr = {-1,0,1,0}; // 상 우 하 좌
    static int[] Sdc = {0,1,0,-1}; // 0  1 2 3

    static int changeDir(int d){
        if (d == 0) return 2;
        if (d == 1) return 3;
        if (d == 2) return 0;
        else return 1;
    }

    static void crash(int dir, int kan, Node santa){ // 어떤 방향으로 몇 칸 밀려나는지
        //System.out.println("crash!! santa : "+ santa.num );

        int r = santa.r + (Rdr[dir]*kan);
        int c = santa.c + (Rdc[dir]*kan);
        //System.out.println("kan "+kan+" "+r+" "+c);

        if (r<=0 || r>N || c<=0 || c>N) {
            //System.out.println(santa.num +" 번 산타가 벗어남 ");
            int santanum = santa.num;
            graph[santa.r][santa.c] = null; // 그래프에서 삭제
            santa.flag = false;
            alive.remove((Integer)santanum);
            return;
        }

        if (graph[r][c] != null){ // 밀려난 칸에 다른 산타가 있는 경우 -> 상호작용
            sang(dir, santa, graph[r][c]);
        } else{
            //System.out.println("santa 이동 "+ santa.r+" "+santa.c +" 에서 삭제");
            graph[santa.r][santa.c] = null;
            santa.r = r;
            santa.c = c;
            // graph[santa.r][santa.c] = null;
            graph[r][c] = santa;
            //System.out.println("santa "+r+" "+c+" 로 이동");
        }

        santa.kijul += 2;
        //System.out.println("crash : kijul+1 "+ santa.kijul);
    }

    static void sang(int dir, Node curSanta, Node nextSanta){
        curSanta.r = nextSanta.r;
        curSanta.c = nextSanta.c;
        graph[curSanta.r][curSanta.c] = null;
        graph[nextSanta.r][nextSanta.c] = curSanta;

        int nr = nextSanta.r + Rdr[dir];
        int nc = nextSanta.c + Rdc[dir];

        if (nr <=0 || nr>N || nc<=0 || nc>N){ // 벗어나면
            nextSanta.flag = false;
            alive.remove((Integer)nextSanta.num);
        }

        if (graph[nr][nc] != null){ // 누가 또 있으면
            sang(dir, nextSanta, graph[nr][nc]);
        }else{ // 없으면
            graph[nr][nc] = nextSanta;
            nextSanta.r = nr;
            nextSanta.c = nc;
        }
    }

    static Node findSanta(){ // 가장 가까운 산타 찾기
        double dMin = Double.MAX_VALUE;
        Node minSanta = santas.get(alive.get(0));

        for (int i=0; i<alive.size(); i++){
            int curnum = alive.get(i);
            Node santa = santas.get(curnum);

            double d = Math.pow(Rloc[0] - santa.r, 2) + Math.pow(Rloc[1] - santa.c,2);
            // System.out.println(Rloc[0]+" "+ Rloc[1] +" " +santa.r+" "+ santa.c+" "+santa.num + " dddd "+d );
            if (dMin > d){
                dMin = d;
                minSanta = santa;
            }else if (dMin == d){ // 같을 때는
                if (minSanta.r < santa.r){
                    minSanta = santa;
                } else if (minSanta.r == santa.r && minSanta.c < santa.c){
                    minSanta = santa;
                }
            }
        }
        return minSanta;
    } // end of findsanta

    // 루돌프가 갈 방향찾아주기 - 산타가 가장 가까울
    static int findRDir(int r, int c, int sr, int sc){
        int min = Integer.MAX_VALUE;
        int dir = 0;
        for (int i=0; i<8; i++){
            int nr = r+Rdr[i];
            int nc = c+Rdc[i];

            if (nr <=0 || nr>N || nc<=0 || nc>N) continue;

            int diff = (int)(Math.pow(nr - sr,2) + Math.pow(nc-sc,2));
            if (diff < min){
                dir = i;
                min = diff;
            }
        }
        return dir;
    }

    static int findSDir(int r, int c){
        int min = Integer.MAX_VALUE;
        int dir = 0;
        for (int i=0; i<4; i++){
            int nr = r+Sdr[i];
            int nc = c+Sdc[i];

            if (nr <=0 || nr>N || nc<=0 || nc>N) continue;

            int diff = (int)(Math.pow(nr - Rloc[0],2) + Math.pow(nc-Rloc[1],2));
            if (diff < min && graph[nr][nc] == null){
                dir = i;
                min = diff;
            }
        }

        if ((int)(Math.pow(r - Rloc[0],2) + Math.pow(c-Rloc[1],2)) < min){
            return -1;
        }

        return dir;
    }


}