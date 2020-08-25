import java.util.*;
import java.text.DecimalFormat;
public class TestQlearning {

	public static void main(String[] args) {
        //获取vnf list的请求
        List<Character> list = new ArrayList<>();
        list.add('A');
        list.add('B');
        list.add('C');
        list.add('E');
        list.add('D');
        ServiceChainRequest sfcRequest = new ServiceChainRequest(list);
        List<Character> sfcList =  sfcRequest.getSfc();
        //获取vnf的信息
        Map<Integer,Character> vnfs = new HashMap<>();
        vnfs.put(1,'A');
        vnfs.put(2,'D');
        vnfs.put(3,'B');
        vnfs.put(4,'C');
        vnfs.put(5,'B');
        vnfs.put(6,'E');
        vnfs.put(7,'A');
        vnfs.put(8,'C');
        vnfs.put(9,'A');
        vnfs.put(10,'B');
        VnfManager vnfManager = new VnfManager();
        vnfManager.setVnfs(vnfs);
        long start,end;
        start = System.currentTimeMillis();
        //获取Rtable
        double[][] rtable = getRtable(sfcList,vnfManager);
        //获取qtable
        double[][] qtable = getQtable(rtable);
        end = System.currentTimeMillis();
        System.out.println("start time:" + start+ "; end time:" + end+ "; Run Time:" + (end - start) + "(ms)");
        

    }
    public static double[][] getRtable(List<Character> sfcList,VnfManager vnfManager){
        List<Integer> instanceList = new ArrayList();
        Map<Integer,Character> vnfs = vnfManager.getVnfs();
        Set<Map.Entry<Integer,Character>> vnfSet = vnfs.entrySet();

        for(int i=0;i<sfcList.size();i++){
            char type = sfcList.get(i);
            Iterator<Map.Entry<Integer,Character>> iterator = vnfSet.iterator();
            while(iterator.hasNext()){
                Map.Entry<Integer,Character> vnf = iterator.next();
                if(type==vnf.getValue()){
                    instanceList.add(vnf.getKey());
                }
            }

        }
        //打印可用实例列表 耶 此步测试成功
        Iterator<Integer> instances = instanceList.iterator();
        while(instances.hasNext()){
            System.out.print(instances.next()+" ");            
        }
        System.out.println();
        //开始计算r值
        int length = instanceList.size();
        double[][] rtable = new double[length][length];
        for(int i=0;i<length;i++){
            for(int j=0;j<length;j++){
                int a = instanceList.get(i);
                int b = instanceList.get(j);
                rtable[i][j]=getRvalue(a,b,vnfs,sfcList);
                System.out.print(rtable[i][j]+" ");//测试r值输出正常
            }
            System.out.println();
        }
        return rtable;
    }
    public static double[][] getQtable(double[][] rtable){
    	int s=0,a=0;      
        double a1= 0.5;
        double r= 0.34;
        double e =0.5;
        int length = rtable[0].length;
        double[][] qtable = new double[length][length];

        for(int i=1;i<100;i++){
            while(true){
                if(Math.random()<e){//如果产生的0-1之间的随机数<e，随机探索一个动作
                    a=(int)(Math.random()*length);
                    if(rtable[s][a]==-1) continue;
                }else{
                    a=findMaxAction(s,qtable);//利用一个最大q值的动作
                }
                qtable[s][a]=(1-a1)*qtable[s][a]+a1*(rtable[s][a]+r*findMax(a,qtable));
                s=a;//状态更新
                if(a==length-1) break;//如果到达终点退出本次while循环，开始下一次训练
            }
            s=0;//一次训练完成后，重新从起点开始
            e=e-0.001;

        }
        DecimalFormat df = new DecimalFormat("0.00");
        //打印qtable
        for(int i=0;i<length;i++){
            for(int j=0;j<length;j++){
                System.out.print(df.format(qtable[i][j])+" ");
            }
            System.out.println();
        }
        return qtable;
    }
    
    public static double getRvalue(int a,int b,Map<Integer,Character> vnfs,List<Character>  sfcList){
        Character typea = vnfs.get(a);
        Character typeb = vnfs.get(b);
        double rvalue=10;
        if(typea==typeb) return -1;
        if((sfcList.indexOf(typeb)-sfcList.indexOf(typea))!=1) return -1;
        return rvalue;

    }
  //找出在状态s下，具有最大的q值
    public static double findMax(int s,double[][] qtable){
        double max=0;
        for(int i=0;i<qtable[0].length;i++){
            max=Math.max(max,qtable[s][i]);
        }
        return max;
    }
    //找到在状态s下，最大q值对应的动作
    public static int findMaxAction(int s,double[][] qtable){
        int a=0;
        double max=0;
        for(int i=0;i<qtable[0].length;i++){
            if(qtable[s][i]>max){
                a=i;
                max=qtable[s][i];
            }
        }
        return a;
    }
	
}
