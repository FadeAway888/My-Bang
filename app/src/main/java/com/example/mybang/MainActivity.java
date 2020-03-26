package com.example.mybang;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    //private List<Fruit> fruitList = new ArrayList<>();
    public List<Card> cardList;
    public RecyclerView recyclerView;
    public String[] equip0Str;
    public String[] equip1Str;
    public String[] equip2Str;
    public String[] equip3Str;
    TextView textView_loca0;
    TextView textView_identify0;
    TextView textView_loca1;
    TextView textView_identify1;
    TextView textView_loca2;
    TextView textView_identify2;
    TextView textView_loca3;
    TextView textView_identify3;
    List<TextView> textViewList_loca;
    List<TextView> textViewList_identify;
    List<Normal> normals;
    List<Normal> normals_id = new ArrayList<>();
    int alertDialog_index;
    int attack_index;
    boolean overBang = false;
    boolean endPlayACard = false;
    Normal attackNormal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //锁定竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_fiirst);
        Toast.makeText(this,"开始游戏",Toast.LENGTH_SHORT).show();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view); // 先获取recyclerView实例
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); //LinearLayoutManager是线性布局，可以实现和ListView布局类似效果
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置布局的排列方向横向排列
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL)); //分割线
        startGame();
    }

    public void startGame(){
        initiGame();
        // 从警长开始循环 （按 normals_id 的顺序
        Normal mNormal;
        for(;;){
            for(int i = 0;i<4;i++){ //四个人轮流回合
                overBang = false; //初始化该回合没用过bang
                mNormal = normals_id.get(i); //mNormal是当前回合的normal
                // 对mNormal进行摸牌 出牌 弃牌
                //摸牌（两张）
                cardList = mNormal.getCard(cardList);
                cardList = mNormal.getCard(cardList);
                if(mNormal.location == 0){ //如果是自己
                    showLoca0Cards(mNormal.handCardList); //抽完牌后更新玩家手牌显示
                    //Toast.makeText(MainActivity.this,"你的出牌阶段",Toast.LENGTH_SHORT).show();
                    // 出牌阶段
                    playAHand(mNormal);
                    showLoca0Cards(mNormal.handCardList); //出完一回合牌后更新玩家手牌显示
                    //弃牌阶段
                    //TODO
                }
                else{   //如果电脑
                    //TODO 出牌、弃牌
                }
                break;//DEBUG
            }
            break;//DEBUG
        }
    }

    // 出牌方法
    public void playAHand(final Normal mNormal){
        while (mNormal.handCardList.size()>0){ //只要有手牌，就循环出牌
            Toast.makeText(MainActivity.this,"你的出牌阶段",Toast.LENGTH_SHORT).show();
            endPlayACard = false;
            alertDialog_index = 0;
            final int optionNum = mNormal.handCardList.size()+1; //选项数量是手牌数量加一
            String[] strOption = new String[optionNum];
            for(int i = 0;i<strOption.length-1;i++){ //选项顺序与手牌顺序一致
                strOption[i] = mNormal.handCardList.get(i).getName();
            }
            strOption[strOption.length-1] = "结束出牌";

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("请选择要出的牌");
            builder.setSingleChoiceItems(strOption, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog_index = which;
                }
            });

            Log.e("test", "创建Alertialog实例之前 1");

            AlertDialog dialog = builder.create();

            Log.e("test", "刚创建完Alertialog实例之后 2");

            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Log.e("test", "点击确认之后里面执行的 6");


                    if(alertDialog_index>=0 && alertDialog_index<optionNum-1){//选的是牌
                        //Toast.makeText(MainActivity.this,"你选择了"+mNormal.handCardList.get(alertDialog_index).getName(),Toast.LENGTH_SHORT).show();

                        Log.e("选的牌型是",mNormal.handCardList.get(alertDialog_index).getName());

                        if(!mNormal.haveShotgun && overBang && mNormal.handCardList.get(alertDialog_index).getKey()==1){ //没有霰弹枪还重复选bang
                            dialog.dismiss();
                            Log.e("点击确定","重复bang了");
                        }
                        if(mNormal.handCardList.get(alertDialog_index).getKey()==2){ //主动选避开
                            dialog.dismiss();
                            Log.e("点击确定","主动出避开了");
                        }
                        if(mNormal.handCardList.get(alertDialog_index).getKey()==3 && mNormal.getLife()==4){ //满血还补血
                            dialog.dismiss();
                            Log.e("点击确定","满血补血了");
                        }


                        if(mNormal.handCardList.get(alertDialog_index).getKey()==1){//选择出bang
                            if(mNormal.attackDistance == 1){

                                Log.e("test", "准备进入selectNormal（）方法 7");

                                String[] str = new String[2];
                                str[0] = "Local1";
                                str[1] = "Local3";
                                selectNormal(mNormal,str,2,alertDialog_index);

                                Log.e("test", "执行完selectNormal（）方法回来 8");

                            }
                            if(mNormal.attackDistance > 1){
                                String[] str = new String[3];
                                str[0] = "Local1";
                                str[1] = "Local2";
                                str[2] = "Local3";
                                selectNormal(mNormal,str,3,alertDialog_index);
                            }
                        }
                        if(mNormal.handCardList.get(alertDialog_index).getKey()==4){//选择出burglar
                            String[] str = new String[3];
                            str[0] = "Local1";
                            str[1] = "Local2";
                            str[2] = "Local3";
                            selectNormal(mNormal,str,3,alertDialog_index);
                        }
                        if(mNormal.handCardList.get(alertDialog_index).getKey()==5){//选择出duel
                            String[] str = new String[3];
                            str[0] = "Local1";
                            str[1] = "Local2";
                            str[2] = "Local3";
                            selectNormal(mNormal,str,3,alertDialog_index);
                        }
                        if(mNormal.handCardList.get(alertDialog_index).getKey()==6){//选择出panic
                            String[] str = new String[3];
                            str[0] = "Local1";
                            str[1] = "Local2";
                            str[2] = "Local3";
                            selectNormal(mNormal,str,3,alertDialog_index);
                        }

                        //useCard(mNormal,alertDialog_index); //***************
                    }else{// 选的是结束出牌
                        endPlayACard = true;
                    }
                    showLoca0Cards(mNormal.handCardList);
                    dialog.dismiss();

                }
            });

            Log.e("test", "dialog.show()前面3");

            dialog.show();

            Log.e("test", "dialog.show()后面 4");

            if(endPlayACard) break;

            showLoca0Cards(mNormal.handCardList);
            // TEST
            showEquip1(normals.get(1).handCardList);
            showEquip2(normals.get(2).handCardList);
            showEquip3(normals.get(3).handCardList);

            Log.e("test", "执行return的上一句 5");

            return; //DEBUG

            
        }
    }


    public void selectNormal(final Normal mNormal, String[] str, final int strLen, final int cardid){ //这里的cardid只是在手牌中的序号

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择您要选择作用的对象");
        builder.setSingleChoiceItems(str, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                attack_index = which;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(strLen == 2){
                    if(attack_index == 0)
                        attackNormal = normals.get(1);
                    if(attack_index == 1)
                        attackNormal = normals.get(3);
                }
                if(strLen == 3){
                    if(attack_index == 0)
                        attackNormal = normals.get(1);
                    if(attack_index == 1)
                        attackNormal = normals.get(2);
                    if(attack_index == 2)
                        attackNormal = normals.get(3);
                }
                useCard(mNormal,attackNormal,cardid);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void useCard(Normal mNormal,Normal attackNormal,int which){ //真正用牌

        switch (mNormal.handCardList.get(which).getKey()){
            case 1:
                //String[] str;
                mNormal.bang(attackNormal);
                break;
            case 3:
                mNormal.beer();
                break;
            case 4: //窃贼
                String[] str = new String[3];
                str[0] = "local1";
                str[1] = "Local2";
                str[2] = "Local3";
                mNormal.burglar(attackNormal);
                break;
            case 5:
                str = new String[3];
                str[0] = "local1";
                str[1] = "Local2";
                str[2] = "Local3";
                mNormal.duel(attackNormal);
                break;
            case 6:
                str = new String[3];
                str[0] = "local1";
                str[1] = "Local2";
                str[2] = "Local3";
                mNormal.panic(attackNormal);
                break;
            case 7:
                mNormal.indians();
                break;
            case 8: //杂货店，暂时每人摸一张牌
                for(Normal normal:normals){
                    cardList = normal.getCard(cardList);
                }
                break;
            case 9:
                mNormal.gatling();
                break;
            case 10: //小马车，获取两张
                cardList = mNormal.getCard(cardList);
                cardList = mNormal.getCard(cardList);
                for(int i = 0;i<mNormal.handCardList.size();i++){
                    if(mNormal.handCardList.get(i).getKey()==10){
                        mNormal.handCardList.remove(i);
                        mNormal.handCardList = mNormal.sortHandCardList(mNormal.handCardList);
                        break;
                    }
                }
                break;
            case 11:
                cardList = mNormal.getCard(cardList);
                cardList = mNormal.getCard(cardList);
                cardList = mNormal.getCard(cardList);
                for(int i = 0;i<mNormal.handCardList.size();i++){
                    if(mNormal.handCardList.get(i).getKey()==11){
                        mNormal.handCardList.remove(i);
                        mNormal.handCardList = mNormal.sortHandCardList(mNormal.handCardList);
                        break;
                    }
                }
                break;
            case 12:
                mNormal.saloon();
                for(int i = 0;i<4;i++){
                    if(normals.get(i).getLife()<4 && normals.get(i).getLife()>0){
                        normals.get(i).life++;
                        normals.get(i).setTextView_life();
                    }
                }
            case 13: //霰弹枪
                mNormal.shotgun();
                switch (mNormal.location){ //更新装备区
                    case 0:
                        showEquip0(mNormal.equipList);
                        break;
                    case 1:
                        showEquip1(mNormal.equipList);
                        break;
                    case 2:
                        showEquip2(mNormal.equipList);
                        break;
                    case 3:
                        showEquip3(mNormal.equipList);
                        break;
                }
                break;
            case 14: //步枪
                mNormal.rifle();
                switch (mNormal.location){ //更新装备区
                    case 0:
                        showEquip0(mNormal.equipList);
                        break;
                    case 1:
                        showEquip1(mNormal.equipList);
                        break;
                    case 2:
                        showEquip2(mNormal.equipList);
                        break;
                    case 3:
                        showEquip3(mNormal.equipList);
                        break;
                }
                break;
            case 15: //左轮枪
                mNormal.revolver();
                switch (mNormal.location){ //更新装备区
                    case 0:
                        showEquip0(mNormal.equipList);
                        break;
                    case 1:
                        showEquip1(mNormal.equipList);
                        break;
                    case 2:
                        showEquip2(mNormal.equipList);
                        break;
                    case 3:
                        showEquip3(mNormal.equipList);
                        break;
                }
                break;
            case 16: //卡宾枪
                mNormal.carbine();
                switch (mNormal.location){ //更新装备区
                    case 0:
                        showEquip0(mNormal.equipList);
                        break;
                    case 1:
                        showEquip1(mNormal.equipList);
                        break;
                    case 2:
                        showEquip2(mNormal.equipList);
                        break;
                    case 3:
                        showEquip3(mNormal.equipList);
                        break;
                }
                break;
            case 17: //狙击枪
                mNormal.snipingRifle();
                switch (mNormal.location){ //更新装备区
                    case 0:
                        showEquip0(mNormal.equipList);
                        break;
                    case 1:
                        showEquip1(mNormal.equipList);
                        break;
                    case 2:
                        showEquip2(mNormal.equipList);
                        break;
                    case 3:
                        showEquip3(mNormal.equipList);
                        break;
                }
                break;
            case 18://酒桶
                mNormal.barrel();
                switch (mNormal.location){ //更新装备区
                    case 0:
                        showEquip0(mNormal.equipList);
                        break;
                    case 1:
                        showEquip1(mNormal.equipList);
                        break;
                    case 2:
                        showEquip2(mNormal.equipList);
                        break;
                    case 3:
                        showEquip3(mNormal.equipList);
                        break;
                }
                break;
            case 19:
                mNormal.jail();
                break;
            case 20:
                mNormal.dynamite();
                break;
            case 21:
                mNormal.scope();
                switch (mNormal.location){ //更新装备区
                    case 0:
                        showEquip0(mNormal.equipList);
                        break;
                    case 1:
                        showEquip1(mNormal.equipList);
                        break;
                    case 2:
                        showEquip2(mNormal.equipList);
                        break;
                    case 3:
                        showEquip3(mNormal.equipList);
                        break;
                }
                break;
            case 22:
                mNormal.mustang();
                switch (mNormal.location){ //更新装备区
                    case 0:
                        showEquip0(mNormal.equipList);
                        break;
                    case 1:
                        showEquip1(mNormal.equipList);
                        break;
                    case 2:
                        showEquip2(mNormal.equipList);
                        break;
                    case 3:
                        showEquip3(mNormal.equipList);
                        break;
                }
                break;



        }
    }



    public void initiGame(){

        //分别把位置和身份的布局textview放进list
        textView_loca0 = findViewById(R.id.textView_loca0);
        textView_identify0 = findViewById(R.id.textView_identify0);
        textView_loca1 = findViewById(R.id.textView_loca1);
        textView_identify1 = findViewById(R.id.textView_identify1);
        textView_loca2 = findViewById(R.id.textView_loca2);
        textView_identify2 = findViewById(R.id.textView_identify2);
        textView_loca3 = findViewById(R.id.textView_loca3);
        textView_identify3 = findViewById(R.id.textView_identify3);
        textViewList_loca = new ArrayList<>();
        textViewList_identify = new ArrayList<>();
        textViewList_loca.add(textView_loca0);
        textViewList_loca.add(textView_loca1);
        textViewList_loca.add(textView_loca2);
        textViewList_loca.add(textView_loca3);
        textViewList_identify.add(textView_identify0);
        textViewList_identify.add(textView_identify1);
        textViewList_identify.add(textView_identify2);
        textViewList_identify.add(textView_identify3);


        //创建4个对象（location）
        normals = new ArrayList<>();
        for(int i = 0;i<4;i++){
            normals.add(new Normal(i,MainActivity.this,recyclerView));
        }

        //随机给四个对象赋予身份 暂时让自己是警长
        List<Integer> identify_random = new ArrayList();
        identify_random.add(1);
        identify_random.add(3);
        identify_random.add(3);
        identify_random.add(4);
        Collections.shuffle(identify_random);

        // DEBUG
        normals.get(0).setIdentity(1);
        normals.get(1).setIdentity(3);
        normals.get(2).setIdentity(3);
        normals.get(3).setIdentity(4);
        // DEBUG

        for(int i = 0;i<4;i++){
            // normals.get(i).setIdentity(identify_random.get(i)); //这句话把 1 3 3 4 随机给四个人
            switch (normals.get(i).getIdentify()){
                case 1:
                    textViewList_identify.get(i).append("警长");
                    break;
                case 2:
                    textViewList_identify.get(i).append("副警长");
                    break;
                case 3:
                    textViewList_identify.get(i).append("歹徒");
                    break;
                case 4:
                    textViewList_identify.get(i).append("叛徒");
                    break;
                default:
                    break;
            }
        }
        // 根据谁是警长为 normals_id 这个集合赋值，代表出牌次序
        for(int i = 0;i<4;i++){
            if(normals.get(i).getIdentify() == 1){
                switch (i){
                    case 0: //自己是警长
                        normals_id = normals;
                        break;
                    case 1:
                        normals_id.add(normals.get(1));
                        normals_id.add(normals.get(2));
                        normals_id.add(normals.get(3));
                        normals_id.add(normals.get(0));
                        break;
                    case 2:
                        normals_id.add(normals.get(2));
                        normals_id.add(normals.get(3));
                        normals_id.add(normals.get(0));
                        normals_id.add(normals.get(1));
                        break;
                    case 3:
                        normals_id.add(normals.get(3));
                        normals_id.add(normals.get(0));
                        normals_id.add(normals.get(1));
                        normals_id.add(normals.get(2));
                        break;
                }
            }
        }

        // 给四个normal联系上 listView_life
        normals.get(0).textView_life = findViewById(R.id.textView_life0);
        normals.get(1).textView_life = findViewById(R.id.textView_life1);
        normals.get(2).textView_life = findViewById(R.id.textView_life2);
        normals.get(3).textView_life = findViewById(R.id.textView_life3);
        // 初始化显示血量 往后改变血量只需改变life然后调用 setTextView_life（）函数
        for(int i = 0;i<4;i++){
            normals.get(i).setTextView_life();
        }

        // 初始化抽牌堆
        // 先创造牌放入list
        cardList = createCards();
        //洗牌
        Collections.shuffle(cardList);
        Collections.shuffle(cardList);
        // 四个角色抽血量张牌（利用 normal 的抽牌函数）
        for(int i = 0;i<4;i++){
            for(int j = 0;j<normals.get(i).life;j++){
                cardList = normals.get(i).getCard(cardList);  //派牌时会自动为手牌排序
            }
        }
        // 显示自己的手牌
        showLoca0Cards(normals.get(0).handCardList);

        // TEST
        showEquip0(normals.get(0).handCardList);
        showEquip1(normals.get(1).handCardList);
        showEquip2(normals.get(2).handCardList);
        showEquip3(normals.get(3).handCardList);

    }



    // 布局上显示（更新）玩家自己的手牌方法 输入参数： 自己的 handCardList
    public void showLoca0Cards(List<Card> cardList){
        // *尝试不靠参数？* recycleView的更新问题？

        CardAdapter adapter = new CardAdapter(cardList);
        recyclerView.setAdapter(adapter);
    }

    //更新装备区方法
    void showEquip0(List<Card> cardList){
        List<String> ls = new ArrayList<>();
        for(Card card:cardList){
            ls.add(card.getName());
        }
        equip0Str = ls.toArray(new String[ls.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this,R.layout.equip_item,equip0Str);
        ListView lv_eq0 = (ListView) findViewById(R.id.listView_equip0);
        lv_eq0.setAdapter(adapter);
    }
    void showEquip1(List<Card> cardList){
        List<String> ls = new ArrayList<>();
        for(Card card:cardList){
            ls.add(card.getName());
        }
        equip1Str = ls.toArray(new String[ls.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this,R.layout.equip_item,equip1Str);
        ListView lv_eq1 = (ListView) findViewById(R.id.listView_equip1);
        lv_eq1.setAdapter(adapter);
    }
    void showEquip2(List<Card> cardList){
        List<String> ls = new ArrayList<>();
        for(Card card:cardList){
            ls.add(card.getName());
        }
        equip2Str = ls.toArray(new String[ls.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this,R.layout.equip_item,equip2Str);
        ListView lv_eq2 = (ListView) findViewById(R.id.listView_equip2);
        lv_eq2.setAdapter(adapter);
    }
    void showEquip3(List<Card> cardList){
        List<String> ls = new ArrayList<>();
        for(Card card:cardList){
            ls.add(card.getName());
        }
        equip3Str = ls.toArray(new String[ls.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this,R.layout.equip_item,equip3Str);
        ListView lv_eq3 = (ListView) findViewById(R.id.listView_equip3);
        lv_eq3.setAdapter(adapter);
    }


    //初始牌堆方法
    public List<Card> createCards(){
        List<Card> resList = new ArrayList<>();
        for(int i = 0;i<25;i++){
            resList.add(new Card("砰！","a2_1" ,1));
        }
        for(int i = 0;i<12;i++){
            resList.add(new Card("避开！","a2_2",2));
        }
        for(int i = 0;i<6;i++){
            resList.add(new Card("啤酒","a2_3",3));
        }
        for(int i = 0;i<4;i++){
            resList.add(new Card("窃贼","a2_4",4));
        }
        for(int i = 0;i<3;i++){
            resList.add(new Card("决斗","a2_5",5));
        }
        for(int i = 0;i<4;i++){
            resList.add(new Card("恐慌","a2_6",6));
        }
        for(int i = 0;i<2;i++){
            resList.add(new Card("印第安人入侵","a2_7",7));
        }
        for(int i = 0;i<2;i++){
            resList.add(new Card("杂货店","a2_8",8));
        }
        for(int i = 0;i<2;i++){
            resList.add(new Card("小马车","a2_10",10));
        }
        for(int i = 0;i<2;i++){
            resList.add(new Card("霰弹枪","a1_1",13));
        }
        for(int i = 0;i<3;i++){
            resList.add(new Card("步枪","a1_2",14));
        }
        for(int i = 0;i<2;i++){
            resList.add(new Card("酒桶","a1_6",18));
        }
        for(int i = 0;i<3;i++){
            resList.add(new Card("监狱","a1_7",19));
        }
        for(int i = 0;i<2;i++){
            resList.add(new Card("野马","a1_10",22));
        }
        resList.add(new Card("加特林机枪","a2_9",9));
        resList.add(new Card("威尔士马车","a2_11",11));
        resList.add(new Card("酒吧沙龙","a2_12",12));
        resList.add(new Card("左轮枪","a1_3",15));
        resList.add(new Card("卡宾枪","a1_4",16));
        resList.add(new Card("狙击枪","a1_5",17));
        resList.add(new Card("炸药","a1_8",20));
        resList.add(new Card("瞄准镜","a1_9",21));
        return resList;
    }
}

